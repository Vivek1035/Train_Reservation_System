-- Train Reservation System - PostgreSQL Database Setup
-- Run this script to create the database and initial data

-- Create database (run this separately as postgres user)
-- CREATE DATABASE train_reservation_db;

-- Connect to the database
\c train_reservation_db;

-- The tables will be auto-created by Hibernate with ddl-auto=update
-- This script contains sample data for testing

-- ============================================
-- SAMPLE DATA
-- ============================================

-- Insert sample stations
INSERT INTO stations (station_code, station_name, city, state, pincode, created_at) VALUES
('NDLS', 'New Delhi Railway Station', 'New Delhi', 'Delhi', '110001', NOW()),
('BCT', 'Mumbai Central', 'Mumbai', 'Maharashtra', '400008', NOW()),
('MAS', 'Chennai Central', 'Chennai', 'Tamil Nadu', '600003', NOW()),
('HWH', 'Howrah Junction', 'Howrah', 'West Bengal', '711101', NOW()),
('BLR', 'Bangalore City', 'Bangalore', 'Karnataka', '560001', NOW()),
('SBC', 'Bangalore City Junction', 'Bangalore', 'Karnataka', '560023', NOW()),
('PUNE', 'Pune Junction', 'Pune', 'Maharashtra', '411001', NOW()),
('ADI', 'Ahmedabad Junction', 'Ahmedabad', 'Gujarat', '380002', NOW()),
('JP', 'Jaipur Junction', 'Jaipur', 'Rajasthan', '302006', NOW()),
('LKO', 'Lucknow', 'Lucknow', 'Uttar Pradesh', '226001', NOW())
ON CONFLICT (station_code) DO NOTHING;

-- Insert sample users
INSERT INTO users (first_name, last_name, email, password, phone_number, address, role, active, created_at, updated_at) VALUES
('Admin', 'User', 'admin@trainres.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', '9999999999', 'Admin Address', 'ADMIN', true, NOW(), NOW()),
('John', 'Doe', 'john.doe@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', '9876543210', '123 Main St, Delhi', 'USER', true, NOW(), NOW()),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', '9876543211', '456 Park Ave, Mumbai', 'USER', true, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Note: Password is 'password123' hashed with BCrypt

-- Insert sample trains
INSERT INTO trains (train_number, train_name, source_station_id, destination_station_id, departure_time, arrival_time, total_seats, available_seats, base_fare, active, operating_days, created_at, updated_at)
SELECT 
    '12301', 
    'Rajdhani Express', 
    s1.id, 
    s2.id, 
    '16:55:00', 
    '09:25:00', 
    1000, 
    1000, 
    1500.00, 
    true, 
    'MON,TUE,WED,THU,FRI,SAT,SUN',
    NOW(),
    NOW()
FROM stations s1, stations s2
WHERE s1.station_code = 'NDLS' AND s2.station_code = 'BCT'
ON CONFLICT (train_number) DO NOTHING;

INSERT INTO trains (train_number, train_name, source_station_id, destination_station_id, departure_time, arrival_time, total_seats, available_seats, base_fare, active, operating_days, created_at, updated_at)
SELECT 
    '12302', 
    'Shatabdi Express', 
    s1.id, 
    s2.id, 
    '06:00:00', 
    '11:00:00', 
    500, 
    500, 
    800.00, 
    true, 
    'MON,TUE,WED,THU,FRI',
    NOW(),
    NOW()
FROM stations s1, stations s2
WHERE s1.station_code = 'NDLS' AND s2.station_code = 'JP'
ON CONFLICT (train_number) DO NOTHING;

INSERT INTO trains (train_number, train_name, source_station_id, destination_station_id, departure_time, arrival_time, total_seats, available_seats, base_fare, active, operating_days, created_at, updated_at)
SELECT 
    '12303', 
    'Duronto Express', 
    s1.id, 
    s2.id, 
    '20:30:00', 
    '08:00:00', 
    800, 
    800, 
    1200.00, 
    true, 
    'MON,WED,FRI,SUN',
    NOW(),
    NOW()
FROM stations s1, stations s2
WHERE s1.station_code = 'BCT' AND s2.station_code = 'MAS'
ON CONFLICT (train_number) DO NOTHING;

-- Insert sample coaches (for train 12301 - Rajdhani Express)
INSERT INTO coaches (train_id, coach_number, coach_type, total_seats, available_seats, fare_multiplier, created_at)
SELECT 
    t.id,
    'A1',
    'AC_1A',
    20,
    20,
    3.0,
    NOW()
FROM trains t
WHERE t.train_number = '12301'
ON CONFLICT DO NOTHING;

INSERT INTO coaches (train_id, coach_number, coach_type, total_seats, available_seats, fare_multiplier, created_at)
SELECT 
    t.id,
    'A2',
    'AC_2A',
    50,
    50,
    2.0,
    NOW()
FROM trains t
WHERE t.train_number = '12301'
ON CONFLICT DO NOTHING;

INSERT INTO coaches (train_id, coach_number, coach_type, total_seats, available_seats, fare_multiplier, created_at)
SELECT 
    t.id,
    'A3',
    'AC_3A',
    70,
    70,
    1.5,
    NOW()
FROM trains t
WHERE t.train_number = '12301'
ON CONFLICT DO NOTHING;

-- Insert sample seats (for coach A1)
INSERT INTO seats (coach_id, seat_number, available, quota_type, is_window_seat, is_aisle_seat, is_lower_berth, is_upper_berth, created_at, updated_at)
SELECT 
    c.id,
    generate_series::text,
    true,
    'GENERAL',
    (generate_series % 4 = 1 OR generate_series % 4 = 0),
    (generate_series % 4 = 2),
    (generate_series % 3 = 1),
    (generate_series % 3 = 0),
    NOW(),
    NOW()
FROM coaches c, generate_series(1, 20)
WHERE c.coach_number = 'A1'
ON CONFLICT DO NOTHING;

-- Verify data
SELECT 'Stations' as table_name, COUNT(*) as count FROM stations
UNION ALL
SELECT 'Users', COUNT(*) FROM users
UNION ALL
SELECT 'Trains', COUNT(*) FROM trains
UNION ALL
SELECT 'Coaches', COUNT(*) FROM coaches
UNION ALL
SELECT 'Seats', COUNT(*) FROM seats;

-- Display sample data
SELECT * FROM stations LIMIT 5;
SELECT train_number, train_name, base_fare FROM trains;
