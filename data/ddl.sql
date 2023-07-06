CREATE TABLE IF NOT EXISTS CARD_HOLDER(
    id uuid PRIMARY KEY,
    client_id uuid NOT NULL unique,
    bank_account_id uuid,
    credit_limit DECIMAL(10, 2) NOT NULL,
    withdrawal_limit DECIMAL(10, 2) NOT NULL,
    status varchar(10) NOT NULL,
    credit_analysis_id uuid NOT NULL,
    created_at timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS BANK_ACCOUNT(
        id uuid PRIMARY KEY,
        agency varchar(10) NOT NULL,
        bank_code varchar(10) NOT NULL,
        account_number varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS CREDIT_CARD(
    id uuid PRIMARY KEY,
    card_holder_id uuid NOT NULL,
    credit_limit DECIMAL(10, 2) NOT NULL,
    card_number varchar(18) NOT NULL,
    cvv varchar(3) NOT NULL,
    due_date date NOT NULL,
    created_at timestamp NOT NULL
);

alter table CARD_HOLDER ADD constraint fk_bank_account_id foreign key (bank_account_id) references BANK_ACCOUNT(id);
alter table CREDIT_CARD ADD constraint fk_card_holder_id foreign key (card_holder_id) references CARD_HOLDER(id);
