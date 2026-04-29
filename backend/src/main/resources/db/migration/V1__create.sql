CREATE TABLE IF NOT EXISTS mood (
    id UUID PRIMARY KEY,
    mood_value INTEGER NOT NULL,
    note VARCHAR(255),
    mood_date DATE NOT NULL,
    aiAdvice VARCHAR(255)
);