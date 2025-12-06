-- Database setup script for AgriFarm
CREATE DATABASE IF NOT EXISTS agrifarm;
USE agrifarm;

CREATE TABLE IF NOT EXISTS farmers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    age INT,
    phone VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS fields (
    id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT,
    location VARCHAR(100),
    size DOUBLE,
    soilType VARCHAR(50),
    status VARCHAR(50),
    FOREIGN KEY (owner_id) REFERENCES farmers(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS plants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    field_id INT,
    name VARCHAR(100),
    type VARCHAR(50),
    growthStage VARCHAR(50),
    estimatedHarvestDate DATE,
    FOREIGN KEY (field_id) REFERENCES fields(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS irrigation_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fieldId INT,
    waterVolume DOUBLE,
    timestamp DATETIME,
    FOREIGN KEY (fieldId) REFERENCES fields(id) ON DELETE CASCADE
);
