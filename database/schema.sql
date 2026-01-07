-- Create database
CREATE DATABASE IF NOT EXISTS railway_db;
USE railway_db;

-- Create trains table
CREATE TABLE trains (
    train_id INT PRIMARY KEY AUTO_INCREMENT,
    train_name VARCHAR(100) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    fare DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create tickets table
CREATE TABLE tickets (
    ticket_id INT PRIMARY KEY AUTO_INCREMENT,
    train_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_email VARCHAR(100) NOT NULL,
    passenger_phone VARCHAR(15) NOT NULL,
    seat_number INT NOT NULL,
    fare DECIMAL(10,2) NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    status ENUM('BOOKED', 'CANCELLED') DEFAULT 'BOOKED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (train_id) REFERENCES trains(train_id),
    UNIQUE KEY unique_seat_per_train (train_id, seat_number, status)
);

-- Insert sample train data for Tamil Nadu districts
INSERT INTO trains (train_name, source, destination, departure_time, arrival_time, total_seats, available_seats, fare) VALUES
('Chennai Express', 'Chennai', 'Coimbatore', '06:00:00', '12:30:00', 120, 120, 285.50),
('Madurai Mail', 'Chennai', 'Madurai', '22:30:00', '06:45:00', 100, 100, 320.00),
('Nilgiri Express', 'Chennai', 'Tirunelveli', '20:45:00', '08:15:00', 110, 110, 425.75),
('Kovai Express', 'Chennai', 'Coimbatore', '14:20:00', '20:45:00', 95, 95, 295.25),
('Pearl City Express', 'Chennai', 'Tuticorin', '16:15:00', '04:30:00', 105, 105, 380.00),
('Rockfort Express', 'Chennai', 'Tiruchirappalli', '17:50:00', '23:20:00', 90, 90, 245.50),
('Vaigai Express', 'Chennai', 'Madurai', '13:40:00', '21:55:00', 85, 85, 315.75),
('Pandian Express', 'Chennai', 'Tirunelveli', '21:45:00', '09:30:00', 100, 100, 435.25),
('Cheran Express', 'Chennai', 'Salem', '23:15:00', '05:45:00', 80, 80, 195.00),
('Cauvery Express', 'Chennai', 'Thanjavur', '06:30:00', '13:15:00', 95, 95, 225.50),
('Nellai Express', 'Coimbatore', 'Tirunelveli', '05:45:00', '12:30:00', 90, 90, 185.75),
('Kongu Express', 'Coimbatore', 'Salem', '07:20:00', '10:45:00', 75, 75, 125.25),
('Pothigai Express', 'Madurai', 'Tirunelveli', '14:30:00', '17:15:00', 85, 85, 95.50),
('Kumari Express', 'Madurai', 'Kanyakumari', '06:15:00', '11:45:00', 100, 100, 165.00),
('Cholan Express', 'Tiruchirappalli', 'Thanjavur', '08:45:00', '10:30:00', 70, 70, 85.25),
('Kaveri Express', 'Tiruchirappalli', 'Salem', '15:20:00', '18:45:00', 80, 80, 145.75),
('Sethu Express', 'Madurai', 'Rameswaram', '05:30:00', '09:15:00', 90, 90, 135.50),
('Yercaud Express', 'Salem', 'Erode', '12:15:00', '14:30:00', 65, 65, 75.25),
('Dharmapuri Express', 'Salem', 'Dharmapuri', '16:45:00', '18:20:00', 60, 60, 65.00),
('Nilgiris Express', 'Coimbatore', 'Ooty', '06:45:00', '09:30:00', 55, 55, 95.75),
('Kodai Express', 'Madurai', 'Kodaikanal', '07:30:00', '11:15:00', 70, 70, 125.50),
('Periyar Express', 'Madurai', 'Theni', '13:20:00', '15:45:00', 65, 65, 85.25),
('Tamirabarani Express', 'Tirunelveli', 'Tenkasi', '09:15:00', '10:30:00', 60, 60, 45.50),
('Courtallam Express', 'Tirunelveli', 'Courtallam', '14:45:00', '16:15:00', 55, 55, 55.75);

-- Create indexes for better performance
CREATE INDEX idx_trains_route ON trains(source, destination);
CREATE INDEX idx_tickets_passenger ON tickets(passenger_email);
CREATE INDEX idx_tickets_train ON tickets(train_id);
CREATE INDEX idx_tickets_status ON tickets(status);