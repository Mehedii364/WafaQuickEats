<?php
require_once __DIR__ . '/../../config/cors.php';
require_once __DIR__ . '/../../config/database.php';

$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

if (!$data || empty($data['orderId']) || empty($data['newStatus'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Missing order ID or status']);
    exit;
}

$db = Database::getConnection();
$orderId = $data['orderId'];
$newStatus = $data['newStatus'];
$actorId = $data['actorId'] ?? 'SYSTEM';
$actorRole = $data['actorRole'] ?? 'RIDER';
$otp = $data['otp'] ?? null;

// If marking as DELIVERED, verify OTP
if ($newStatus === 'DELIVERED') {
    $checkStmt = $db->prepare("SELECT otp_code FROM orders WHERE order_id = ? LIMIT 1");
    $checkStmt->execute([$orderId]);
    $order = $checkStmt->fetch();

    if ($order && !empty($order['otp_code']) && $order['otp_code'] !== $otp) {
        http_response_code(422);
        echo json_encode([
            'success' => false,
            'message' => 'Invalid OTP verification code'
        ]);
        exit;
    }
}

$stmt = $db->prepare("UPDATE orders SET status = ?, updated_at = NOW() WHERE order_id = ?");
$stmt->execute([$newStatus, $orderId]);

// Record in audit log
$audit = $db->prepare("INSERT INTO audit_logs (actor_id, action, entity_name, entity_id, details) VALUES (?, ?, 'ORDER', ?, ?)");
$audit->execute([$actorId, "STATUS_CHANGE_TO_$newStatus", $orderId, "Changed by $actorRole"]);

echo json_encode([
    'success' => true,
    'message' => "Order $orderId status updated to $newStatus successfully",
    'data' => [
        'orderId' => $orderId,
        'status' => $newStatus
    ]
]);
