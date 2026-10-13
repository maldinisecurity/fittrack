-- MySQL 8.0.16+; import into a dedicated assessment database.
CREATE DATABASE IF NOT EXISTS fittrack CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE fittrack;
CREATE TABLE IF NOT EXISTS members (
 id INT PRIMARY KEY CHECK (id > 0),
 name VARCHAR(200) NOT NULL,
 email VARCHAR(254) NOT NULL UNIQUE,
 age INT NOT NULL CHECK (age > 0)
);
CREATE TABLE IF NOT EXISTS workouts (
 id INT PRIMARY KEY CHECK (id > 0),
 member_id INT NOT NULL,
 activity_type VARCHAR(200) NOT NULL,
 duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
 workout_date DATE NOT NULL,
 CONSTRAINT fk_workout_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE RESTRICT,
 INDEX idx_workout_member_date (member_id,workout_date)
);
CREATE TABLE IF NOT EXISTS challenges (
 id INT PRIMARY KEY CHECK (id > 0),
 member_id INT NOT NULL,
 name VARCHAR(200) NOT NULL,
 type VARCHAR(20) NOT NULL CHECK (type IN ('MINUTES','SESSIONS')),
 target INT NOT NULL CHECK (target > 0),
 start_date DATE NOT NULL,
 end_date DATE NOT NULL,
 CHECK (end_date >= start_date),
 CONSTRAINT fk_challenge_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE RESTRICT
);
-- Seed IDs are reserved for the demonstration. Re-importing does not overwrite records.
INSERT IGNORE INTO members VALUES (1,'Alex Johnson','alex@example.com',25);
INSERT IGNORE INTO workouts VALUES (1,1,'Running',30,'2026-10-01'),(2,1,'Cycling',45,'2026-10-02');
INSERT IGNORE INTO challenges VALUES (1,1,'October minutes','MINUTES',150,'2026-10-01','2026-10-31'),(2,1,'October sessions','SESSIONS',3,'2026-10-01','2026-10-31');
