--
-- MassVaxx Database Schema Definition for PostgreSQL.
--

SET client_encoding TO 'UTF8';

\set ON_ERROR_STOP ON

--DROP SCHEMA massvaxx CASCADE;
CREATE SCHEMA massvaxx;

SET search_path = massvaxx, pg_catalog;

ALTER SCHEMA massvaxx OWNER TO massvaxxadmin;
--GRANT USAGE ON SCHEMA massvaxx TO massvaxx_role;

CREATE TABLE massvaxx.users (
	user_id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	username varchar(256) NOT NULL UNIQUE,
	password varchar(1024) NOT NULL,
	last_changed date NOT NULL DEFAULT('1970-01-01'),
	role varchar(256) NOT NULL DEFAULT('CLIENT')
);

ALTER TABLE massvaxx.users OWNER TO massvaxxadmin;
GRANT ALL ON  massvaxx.users TO massvaxxadmin;
REVOKE ALL ON massvaxx.users FROM PUBLIC;
--GRANT SELECT ON massvaxx.users TO massvaxx_readonly_role;
--GRANT SELECT, INSERT, UPDATE, DELETE ON massvaxx.users TO massvaxx_role;

CREATE TABLE massvaxx.patient_ids (
	patient_id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    assigner varchar(256) NOT NULL,
	start date NOT NULL DEFAULT NOW()
);

ALTER TABLE massvaxx.patient_ids OWNER TO massvaxxadmin;
GRANT ALL ON  massvaxx.patient_ids TO massvaxxadmin;
REVOKE ALL ON massvaxx.patient_ids FROM PUBLIC;
--GRANT SELECT ON massvaxx.patient_ids TO massvaxx_readonly_role;
--GRANT SELECT, INSERT, UPDATE, DELETE ON massvaxx.patient_ids TO massvaxx_role;

CREATE TABLE massvaxx.qr_codes (
    qr_code_id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    patient_id bigint NOT NULL,
    qr_code varchar(256) UNIQUE
);

ALTER TABLE massvaxx.qr_codes ADD CONSTRAINT qr_code_patient_id_fkey FOREIGN KEY(patient_id) REFERENCES massvaxx.patient_ids(patient_id);

ALTER TABLE massvaxx.qr_codes OWNER TO massvaxxadmin;
GRANT ALL ON  massvaxx.qr_codes TO massvaxxadmin;
REVOKE ALL ON massvaxx.qr_codes FROM PUBLIC;
--GRANT SELECT ON massvaxx.qr_codes TO massvaxx_readonly_role;
--GRANT SELECT, INSERT, UPDATE, DELETE ON massvaxx.qr_codes TO massvaxx_role;
