DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk6prqdy5cyao7x9te7pyatc1md'
    ) THEN
        ALTER TABLE meter_reading
        RENAME CONSTRAINT fk6prqdy5cyao7x9te7pyatc1md
        TO fk_meter_reading_customer;
    END IF;
END $$;