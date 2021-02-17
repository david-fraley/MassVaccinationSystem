#!/bin/bash
set -e

# Change massvaxxadmin user in schema definition to match actual postgres superuser
#
sed -e s\/massvaxxadmin\/${POSTGRES_USER}\/g /var/local/massvaxx/massvaxx_schema.sql > /tmp/massvaxx_schema_local.sql

# Install schema
#
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -f /tmp/massvaxx_schema_local.sql