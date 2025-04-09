-- Drop the table if it already exists (optional)
DROP TABLE IF EXISTS payment_transactions;

-- Enable the uuid-ossp extension (run once in your database)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the payment_transactions table
CREATE TABLE payment_transactions (
    transaction_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    payer_name       VARCHAR(100) NOT NULL,
    payer_bank       VARCHAR(100) NOT NULL,
    payer_country    VARCHAR(100) NOT NULL,
    payer_account    VARCHAR(50) NOT NULL,

    payee_name       VARCHAR(100) NOT NULL,
    payee_bank       VARCHAR(100) NOT NULL,
    payee_country    VARCHAR(100) NOT NULL,
    payee_account    VARCHAR(50) NOT NULL,

    payment_instruction TEXT,
    currency         VARCHAR(10) NOT NULL,
    amount           DECIMAL(15,2) NOT NULL,
    execution_date   DATE NOT NULL,
    created_timetamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    status          VARCHAR(20)
);

-- Insert sample data
INSERT INTO payment_transactions (payer_name, payer_bank, payer_country, payer_account,
                                  payee_name, payee_bank, payee_country, payee_account,
                                  payment_instruction, currency, amount, execution_date, status)
VALUES
('John Doe', 'Bank A', 'USA', '123456789',
 'Alice Smith', 'Bank B', 'UK', '987654321',
 'Payment for services', 'USD', 500.00, '2024-03-29', 'FAILED'),

('Michael Brown', 'Bank C', 'Canada', '555444333',
 'David Johnson', 'Bank D', 'Germany', '999888777',
 'Invoice Payment', 'EUR', 1200.50, '2024-03-30', '');
