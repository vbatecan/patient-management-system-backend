CREATE TYPE role AS ENUM ('GUEST', 'PATIENT', 'DOCTOR', 'ADMIN');


CREATE TABLE user_account
(
    id         SERIAL,
    username   VARCHAR UNIQUE NOT NULL,
    password   VARCHAR        NOT NULL,
    role       role           NOT NULL DEFAULT 'GUEST',
    email      VARCHAR UNIQUE NOT NULL,
    created_at TIMESTAMP               DEFAULT (now()),
    updated_at TIMESTAMP               DEFAULT (now())
);

CREATE TABLE patient
(
    id                SERIAL,
    user_account_id   INT,
    first_name        VARCHAR NOT NULL,
    last_name         VARCHAR NOT NULL,
    date_of_birth     DATE    NOT NULL,
    gender            VARCHAR,
    contact_number    VARCHAR,
    email             VARCHAR,
    address           TEXT,
    emergency_contact VARCHAR,
    created_at        TIMESTAMP DEFAULT (now()),
    updated_at        TIMESTAMP DEFAULT (now())
);

CREATE TABLE doctor
(
    id              SERIAL,
    user_account_id INT,
    first_name      VARCHAR NOT NULL,
    last_name       VARCHAR NOT NULL,
    specialty       VARCHAR,
    contact_number  VARCHAR,
    email           VARCHAR,
    created_at      TIMESTAMP DEFAULT (now()),
    updated_at      TIMESTAMP DEFAULT (now())
);

CREATE TABLE appointment
(
    id               SERIAL,
    patient_id       INT,
    doctor_id        INT,
    appointment_date TIMESTAMP NOT NULL,
    reason           text,
    status           VARCHAR   DEFAULT ('SCHEDULED'),
    created_at       TIMESTAMP DEFAULT (now()),
    updated_at       TIMESTAMP DEFAULT (now())
);

CREATE TABLE medical_record
(
    id          SERIAL,
    patient_id  INT,
    record_date TIMESTAMP NOT NULL,
    description TEXT,
    file_path   VARCHAR NULL,
    created_at  TIMESTAMP DEFAULT (now()),
    updated_at  TIMESTAMP DEFAULT (now())
);

CREATE TABLE prescription
(
    id             SERIAL,
    appointment_id INT,
    medication     VARCHAR NOT NULL,
    dosage         VARCHAR,
    instructions   text,
    created_at     TIMESTAMP DEFAULT (now()),
    updated_at     TIMESTAMP DEFAULT (now())
);

CREATE TABLE billing
(
    id             SERIAL,
    patient_id     INT     NOT NULL,
    appointment_id INT,
    amount         DECIMAL NOT NULL,
    status         VARCHAR   DEFAULT ('PENDING'),
    billing_date   TIMESTAMP DEFAULT (now()),
    details        TEXT    NULL,
    created_at     TIMESTAMP DEFAULT (now()),
    updated_at     TIMESTAMP DEFAULT (now())
);

ALTER TABLE patient
    ADD FOREIGN KEY (user_account_id) REFERENCES user_account (id);

ALTER TABLE doctor
    ADD FOREIGN KEY (user_account_id) REFERENCES user_account (id);

ALTER TABLE appointment
    ADD FOREIGN KEY (patient_id) REFERENCES patient (id);

ALTER TABLE appointment
    ADD FOREIGN KEY (doctor_id) REFERENCES doctor (id);

ALTER TABLE medical_record
    ADD FOREIGN KEY (patient_id) REFERENCES patient (id);

ALTER TABLE prescription
    ADD FOREIGN KEY (appointment_id) REFERENCES appointment (id);

ALTER TABLE billing
    ADD FOREIGN KEY (patient_id) REFERENCES patient (id);

ALTER TABLE billing
    ADD FOREIGN KEY (appointment_id) REFERENCES appointment (id);
