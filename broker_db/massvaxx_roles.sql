--
-- Massvaxx Database Role and User Definitions for PostgreSQL.
--

SET client_encoding TO 'UTF8';

\set ON_ERROR_STOP ON

SET search_path = pg_catalog;

CREATE ROLE massvaxx_role with nosuperuser nocreatedb nocreaterole inherit noreplication; 
CREATE ROLE massvaxx_readonly_role with nosuperuser nocreatedb nocreaterole inherit nologin noreplication; 
CREATE ROLE massvaxxuser with nosuperuser nocreatedb nocreaterole inherit login password '#ChangeMeASAP!' in role massvaxx_role noreplication;
