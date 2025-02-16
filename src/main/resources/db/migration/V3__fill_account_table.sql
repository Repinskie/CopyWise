SET schema 'accounts';

INSERT INTO accounts (account_number, account_balance, created_at, user_id)
VALUES
    (5000011320065945, 2576.90, NOW() - INTERVAL '1 day' * floor(random() * 365), 1),
    (5000019854392243, 43.00, NOW() - INTERVAL '1 day' * floor(random() * 365), 2),
    (5000010945223639, 0.00, NOW() - INTERVAL '1 day' * floor(random() * 365), 3);

