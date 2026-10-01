-- ============================================================================
-- WAFA QUICKEATS ⚡ DATABASE SCHEMA (MySQL 8.x / MariaDB / InfinityFree PDO)
-- Brand: Wafa Zone by Mehedi364
-- Developer: Md. Mehedi Hasan / Mehedi364
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS api_tests;
DROP TABLE IF EXISTS ai_keys;
DROP TABLE IF EXISTS ai_providers;
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS verifications;
DROP TABLE IF EXISTS support_messages;
DROP TABLE IF EXISTS support_tickets;
DROP TABLE IF EXISTS delivery_incidents;
DROP TABLE IF EXISTS rider_notes;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS saved_locations;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS offers;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS points_ledger;
DROP TABLE IF EXISTS points_accounts;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS refunds;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS delivery_zones;
DROP TABLE IF EXISTS delivery_routes;
DROP TABLE IF EXISTS gps_locations;
DROP TABLE IF EXISTS deliveries;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS food_addons;
DROP TABLE IF EXISTS food_variants;
DROP TABLE IF EXISTS food_photos;
DROP TABLE IF EXISTS foods;
DROP TABLE IF EXISTS food_categories;
DROP TABLE IF EXISTS restaurant_photos;
DROP TABLE IF EXISTS restaurant_hours;
DROP TABLE IF EXISTS restaurant_documents;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS rider_documents;
DROP TABLE IF EXISTS riders;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Users Table
CREATE TABLE users (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('CUSTOMER', 'RIDER', 'RESTAURANT', 'ADMIN', 'SUPER_ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    status ENUM('ACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION') NOT NULL DEFAULT 'ACTIVE',
    avatar_url VARCHAR(255) DEFAULT '',
    language VARCHAR(5) DEFAULT 'bn',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_role (role),
    INDEX idx_user_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Customers Table
CREATE TABLE customers (
    customer_id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    default_address TEXT,
    default_lat DECIMAL(10, 8),
    default_lng DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Riders Table
CREATE TABLE riders (
    rider_id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    vehicle_type ENUM('MOTORCYCLE', 'BICYCLE', 'SCOOTER') DEFAULT 'MOTORCYCLE',
    is_online TINYINT(1) DEFAULT 0,
    current_lat DECIMAL(10, 8),
    current_lng DECIMAL(11, 8),
    total_deliveries INT DEFAULT 0,
    rating DECIMAL(3, 2) DEFAULT 5.00,
    verification_status ENUM('PENDING', 'UNDER_REVIEW', 'VERIFIED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Restaurants Table
CREATE TABLE restaurants (
    restaurant_id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    name VARCHAR(120) NOT NULL,
    name_bn VARCHAR(150),
    cuisine VARCHAR(100),
    phone VARCHAR(20),
    address TEXT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    logo_url VARCHAR(255),
    cover_url VARCHAR(255),
    delivery_time_minutes INT DEFAULT 25,
    delivery_fee_bdt DECIMAL(8, 2) DEFAULT 40.00,
    rating DECIMAL(3, 2) DEFAULT 4.80,
    is_open TINYINT(1) DEFAULT 1,
    verification_status ENUM('PENDING', 'UNDER_REVIEW', 'VERIFIED', 'REJECTED') DEFAULT 'VERIFIED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Food Categories Table
CREATE TABLE food_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    name_bn VARCHAR(80),
    icon_url VARCHAR(255),
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Foods Table
CREATE TABLE foods (
    id VARCHAR(32) PRIMARY KEY,
    restaurant_id VARCHAR(32) NOT NULL,
    name VARCHAR(120) NOT NULL,
    name_bn VARCHAR(150),
    description TEXT,
    category_id INT,
    price_bdt DECIMAL(8, 2) NOT NULL,
    discount_price_bdt DECIMAL(8, 2),
    image_url VARCHAR(255),
    is_available TINYINT(1) DEFAULT 1,
    prep_time_minutes INT DEFAULT 15,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    INDEX idx_food_restaurant (restaurant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Food Variants Table
CREATE TABLE food_variants (
    id VARCHAR(32) PRIMARY KEY,
    food_id VARCHAR(32) NOT NULL,
    name VARCHAR(60) NOT NULL,
    extra_price_bdt DECIMAL(8, 2) DEFAULT 0.00,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Food Addons Table
CREATE TABLE food_addons (
    id VARCHAR(32) PRIMARY KEY,
    food_id VARCHAR(32) NOT NULL,
    name VARCHAR(60) NOT NULL,
    price_bdt DECIMAL(8, 2) NOT NULL,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Orders Table
CREATE TABLE orders (
    order_id VARCHAR(32) PRIMARY KEY,
    customer_id VARCHAR(32) NOT NULL,
    restaurant_id VARCHAR(32) NOT NULL,
    rider_id VARCHAR(32),
    subtotal_bdt DECIMAL(10, 2) NOT NULL,
    delivery_fee_bdt DECIMAL(8, 2) NOT NULL,
    discount_bdt DECIMAL(8, 2) DEFAULT 0.00,
    vat_tax_bdt DECIMAL(8, 2) DEFAULT 0.00,
    total_bdt DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'PREPARING', 'READY_FOR_PICKUP', 'RIDER_ASSIGNED', 'PICKED_UP', 'ON_THE_WAY', 'NEAR_CUSTOMER', 'ARRIVED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    delivery_address TEXT NOT NULL,
    customer_lat DECIMAL(10, 8),
    customer_lng DECIMAL(11, 8),
    delivery_instructions TEXT,
    payment_method VARCHAR(40) DEFAULT 'Cash on Delivery',
    payment_status ENUM('PENDING', 'PAID', 'REFUNDED') DEFAULT 'PENDING',
    otp_code VARCHAR(8) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_customer (customer_id),
    INDEX idx_order_rider (rider_id),
    INDEX idx_order_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Order Items Table
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(32) NOT NULL,
    food_id VARCHAR(32) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price_bdt DECIMAL(8, 2) NOT NULL,
    total_price_bdt DECIMAL(10, 2) NOT NULL,
    variant_info VARCHAR(100),
    addons_info TEXT,
    special_instructions TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. GPS Locations (Rider Live Fleet Telemetry)
CREATE TABLE gps_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(32) NOT NULL,
    rider_id VARCHAR(32) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    accuracy FLOAT DEFAULT 5.0,
    speed FLOAT DEFAULT 0.0,
    bearing FLOAT DEFAULT 0.0,
    recorded_at BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_gps_order (order_id),
    INDEX idx_gps_rider (rider_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. Rider Notes Table
CREATE TABLE rider_notes (
    id VARCHAR(32) PRIMARY KEY,
    rider_id VARCHAR(32) NOT NULL,
    order_id VARCHAR(32) NOT NULL,
    title VARCHAR(120) NOT NULL,
    details TEXT NOT NULL,
    category VARCHAR(40) DEFAULT 'Road Issue',
    priority ENUM('NORMAL', 'IMPORTANT') DEFAULT 'NORMAL',
    privacy ENUM('ADMIN_ONLY', 'CUSTOMER_VISIBLE', 'RIDER_VISIBLE') DEFAULT 'CUSTOMER_VISIBLE',
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_notes_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. Delivery Incidents Table
CREATE TABLE delivery_incidents (
    id VARCHAR(32) PRIMARY KEY,
    rider_id VARCHAR(32) NOT NULL,
    order_id VARCHAR(32) NOT NULL,
    incident_type VARCHAR(60) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('OPEN', 'IN_REVIEW', 'RESOLVED') DEFAULT 'OPEN',
    resolution TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. Invoices Table
CREATE TABLE invoices (
    invoice_number VARCHAR(40) PRIMARY KEY,
    order_id VARCHAR(32) UNIQUE NOT NULL,
    customer_name VARCHAR(120) NOT NULL,
    restaurant_name VARCHAR(120) NOT NULL,
    subtotal_bdt DECIMAL(10, 2) NOT NULL,
    delivery_fee_bdt DECIMAL(8, 2) NOT NULL,
    vat_tax_bdt DECIMAL(8, 2) NOT NULL,
    total_bdt DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(40) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    snapshot_json TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 15. Points Ledger Table
CREATE TABLE points_ledger (
    id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    amount INT NOT NULL,
    reason VARCHAR(150) NOT NULL,
    order_id VARCHAR(32),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_points_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 16. Support Tickets Table
CREATE TABLE support_tickets (
    id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    order_id VARCHAR(32),
    category VARCHAR(60) NOT NULL,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    status ENUM('OPEN', 'IN_REVIEW', 'RESOLVED', 'CLOSED') DEFAULT 'OPEN',
    admin_reply TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 17. AI Providers Table
CREATE TABLE ai_providers (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    category ENUM('FREE', 'PREMIUM') DEFAULT 'FREE',
    endpoint VARCHAR(255) NOT NULL,
    model VARCHAR(80) NOT NULL,
    api_key_encrypted TEXT NOT NULL,
    is_active TINYINT(1) DEFAULT 0,
    is_healthy TINYINT(1) DEFAULT 1,
    latency_ms INT DEFAULT 180,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 18. Audit Logs Table
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    actor_id VARCHAR(32) NOT NULL,
    action VARCHAR(100) NOT NULL,
    entity_name VARCHAR(60),
    entity_id VARCHAR(32),
    details TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_actor (actor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- INITIAL SEED DATA FOR WAFA QUICKEATS ⚡
-- ============================================================================

INSERT INTO users (id, name, email, phone, password_hash, role) VALUES
('WQE-ADM-001', 'Md. Mehedi Hasan', 'admin@wafazone.com', '+8801700000000', '$2y$10$7qYk7L/z9gK.SampleHashedPasswordMehedi364', 'SUPER_ADMIN'),
('WQE-CUS-742918', 'Mehedi Hasan', 'mehedi364@wafazone.com', '+8801712345678', '$2y$10$7qYk7L/z9gK.SampleHashedPasswordMehedi364', 'CUSTOMER'),
('WQE-RDR-5542', 'Farhan Ahmed (Rider ⚡)', 'farhan.rider@wafazone.com', '+8801819998877', '$2y$10$7qYk7L/z9gK.SampleHashedPasswordMehedi364', 'RIDER'),
('WQE-USR-RST-1011', 'Manager (Wafa Burger House)', 'kitchen@wafaburger.com', '+8801711223344', '$2y$10$7qYk7L/z9gK.SampleHashedPasswordMehedi364', 'RESTAURANT');

INSERT INTO restaurants (restaurant_id, user_id, name, name_bn, cuisine, phone, address, latitude, longitude, delivery_time_minutes, delivery_fee_bdt, rating) VALUES
('WQE-RST-1011', 'WQE-USR-RST-1011', 'Wafa Burger House ⚡', 'ওয়াফা বার্গার হাউস ⚡', 'Burgers, Fast Food, Shakes', '+8801711223344', 'Plot 24, Road 11, Banani, Dhaka', 23.79370000, 90.40660000, 20, 40.00, 4.90);

INSERT INTO foods (id, restaurant_id, name, name_bn, description, price_bdt, discount_price_bdt, image_url, prep_time_minutes) VALUES
('WQE-FD-1', 'WQE-RST-1011', 'Wafa Double Blast Beef Burger ⚡', 'ওয়াফা ডাবল ব্লাস্ট বিফ বার্গার ⚡', 'Juicy grilled double beef patties, melted cheddar cheese, secret Wafa sauce.', 360.00, 310.00, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600', 15),
('WQE-FD-2', 'WQE-RST-1011', 'Crispy Thunder Chicken Burger', 'ক্রিস্পি থান্ডার চিকেন বার্গার', 'Super crunchy fried breast fillet with spicy garlic mayo and fresh lettuce.', 280.00, 250.00, 'https://images.unsplash.com/photo-1521305916504-4a1121188589?w=600', 12);

INSERT INTO ai_providers (id, name, category, endpoint, model, api_key_encrypted, is_active) VALUES
('ai-openrouter', 'OpenRouter AI (Configured)', 'FREE', 'https://openrouter.ai/api/v1', 'meta-llama/llama-3.2-3b-instruct:free', 'sk-or-v1-••••••••82f9', 1),
('ai-gemini', 'Google Gemini 2.0 Flash', 'FREE', 'https://generativelanguage.googleapis.com/v1beta', 'gemini-2.0-flash', 'AIzaSy••••••••812q', 0);
