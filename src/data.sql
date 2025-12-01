CREATE TABLE study (
    id_study INT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    hours_per_week DECIMAL(4,1) NOT NULL,
    start_date DATE,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO study (id_study, title, description, hours_per_week, start_date, is_completed)
VALUES (1, 'Introduction à la programmation', 'Cours de base sur la programmation informatique', 10.0, '2023-01-15', FALSE);

SELECT * FROM study;

SELECT id_study, title, hours_per_week
FROM study
WHERE is_completed = FALSE
ORDER BY start_date;

SELECT SUM(hours_per_week) as total_hours_per_week FROM study;

SELECT
    is_completed,
    COUNT(*) as nombre_etudes
FROM study
GROUP BY is_completed;

SELECT * FROM study
WHERE title LIKE '%Java%'
   OR description LIKE '%programmation%';

UPDATE study
SET hours_per_week = 16.0,
    description = 'Programmation Java avancée'
WHERE id_study = 3;
