<?php
require_once __DIR__ . '/../config/cors.php';

echo json_encode([
    'success' => true,
    'message' => 'Wafa QuickEats REST API is online ⚡',
    'data' => [
        'app' => 'Wafa QuickEats',
        'brand' => 'Wafa Zone by Mehedi364',
        'developer' => 'Md. Mehedi Hasan',
        'status' => 'operational',
        'php_version' => PHP_VERSION,
        'timestamp' => time()
    ]
]);
