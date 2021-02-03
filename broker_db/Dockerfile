# Container definition for MassVaxx database to be used for testing 
# by Broker back-end server.
#
# For production, user/passwords should be moved to secrets file as
# described in reference below.
#
# Reference: https://hub.docker.com/_/postgres/
#
FROM postgres
EXPOSE 5432

# Set POSTGRES_DB, POSTGRES_USER, and POSTGRES_PASSWORD using -e option for docker run.
# Refer to base postgres container for more about these variables.
#

# Set up Broker schema.
#
COPY . /var/local/massvaxx
COPY massvaxx_db_init.sh /docker-entrypoint-initdb.d/massvaxx_db_init.sh
RUN chmod +x /docker-entrypoint-initdb.d/massvaxx_db_init.sh

