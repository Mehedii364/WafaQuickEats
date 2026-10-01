<?php
require_once __DIR__ . '/../../config/cors.php';
require_once __DIR__ . '/../../config/database.php';

$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

if (!$data || empty($data['orderId']) || empty($data['locations'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid batch GPS payload']);
    exit;
}

$db = Database::getConnection();
$orderId = $data['orderId'];
$riderId = $data['riderId'] ?? 'WQE-RDR-ACTIVE';
$locations = $data['locations'];

$stmt = $db->prepare("INSERT INTO gps_locations (order_id, rider_id, latitude, longitude, accuracy, speed, bearing, recorded_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

$inserted = 0;
foreach ($locations as $loc) {
    $stmt->execute([
        $orderId,
        $riderId,
        $loc['latitude'],
        $loc['longitude'],
        $loc['accuracy'] ?? 5.0,
        $loc['speed'] ?? 0.0,
        $loc['bearing'] ?? 0.0,
        $loc['timestamp'] ?? (time() * 1000)
    ]);
    $inserted++;
}

echo json_encode([
    'success' => true,
    'message' => "Successfully synchronized $inserted GPS tracking records",
    'data' => [
        'orderId' => $orderId,
        'records_synced' => $inserted
    ]
]);
