CREATE TABLE tx_loan(
    id VARCHAR(10) PRIMARY KEY NOT NULL,
    user_id INT4 NOT NULL,
    role_id VARCHAR(5) NOT NULL,
    loan_amount NUMERIC(10,2) NOT NULL,
    loan_term INTEGER NOT NULL,
    interest_rate NUMERIC(4,2) NOT NULL,
    total_loan NUMERIC(12,2) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    start_date TIMESTAMP
)WITH(
    OIDS = FALSE
);