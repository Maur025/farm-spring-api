UPDATE devices
SET device_number_long = CAST(TRIM(device_number) as BIGINT)
WHERE device_number_long IS NULL;