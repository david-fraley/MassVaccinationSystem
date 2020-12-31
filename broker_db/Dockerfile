# Container definition for MassVaxx database to be used for testing 
# by Broker back-end server.
#
# For production, user/passwords should be moveed to secrets file as
# described in reference below.
#
# Reference: https://hub.docker.com/_/postgres/
#
FROM postgres
EXPOSE 5432

# Set POSTGRES_PASSWORD using -e option for docker run.
#
ENV POSTGRES_USER massvaxxadmin
ENV POSTGRES_DB massvaxx

# Set up Broker roles and schema.
#
COPY . /var/local/massvaxx
COPY massvaxx_db_init.sh /docker-entrypoint-initdb.d/massvaxx_db_init.sh
RUN chmod +x /docker-entrypoint-initdb.d/massvaxx_db_init.sh