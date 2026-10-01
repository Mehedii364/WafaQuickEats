<?php
/**
 * Wafa QuickEats ⚡ Database Connection (PDO)
 * InfinityFree & cPanel MySQL Compatible
 */

class Database {
    // Configurable via environment variables or default InfinityFree / Local settings
    private static $host = 'sql100.infinityfree.com'; // Replace with your InfinityFree DB host
    private static $db_name = 'if0_38000000_wafa_quickeats';
    private static $username = 'if0_38000000';
    private static $password = 'YourDbPasswordHere';
    private static $conn = null;

    public static function getConnection() {
        if (self::$conn === null) {
            $host = getenv('DB_HOST') ?: self::$host;
            $db   = getenv('DB_NAME') ?: self::$db_name;
            $user = getenv('DB_USER') ?: self::$username;
            $pass = getenv('DB_PASS') ?: self::$password;

            $dsn = "mysql:host=$host;dbname=$db;charset=utf8mb4";
            $options = [
                PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES   => false,
            ];

            try {
                self::$conn = new PDO($dsn, $user, $pass, $options);
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode([
                    'success' => false,
                    'message' => 'Database connection failed: ' . $e->getMessage()
                ]);
                exit;
            }
        }
        return self::$conn;
    }
}
