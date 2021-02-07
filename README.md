# MassVaccinationSystem

For more information about this project, visit [MassVaxx](https://www.massvaxx.com). The following sections provide multiple methods to setup and run the system.

# How to run

## Method 1: Using Docker Compose 

See [README.md](sandbox/README.md) in sandbox directory for further instructions.

## Method 2: Using only Node.js (Docker not required)
### Prerequisites:
- [Node.js](https://nodejs,org) is installed.
- MassVaccinationSystem project source code downloaded.

### Steps:
1. Create .env file in <MassVaccinationSystem>/broker directory with the contents:

        DEVELOPMENT=1
        FHIR_URL_BASE=http://fhir-dstu2-nprogram.azurewebsites.net/

2. Run the following commands:

        cd <MassVaccinationSystem>/PatientRegistrationApp
        npm install
        npm run devbuild
        cd <MassVaccinationSystem>/PointOfDispensingApp
        npm install
        npm run devbuild
        cd <MassVaccinationSystem>/broker
        npm install
        node server.js

### Notes:
- You'll have a web server running on port 3000 with the following URLs available:  
    http://localhost:3000/Registration is PatientRegistrationApp  
    http://localhost:3000/POD is PointOfDispensingApp
- You can access broker endpoints directly two ways, e.g.:  
    http://localhost:3000/healthcheck (for test env only)  
    http://localhost:3000/broker/healthcheck (for proxied front-end connections)
- To access from other workstations, make sure your firewall allows connections over port 3000 and replace localhost with your IP address or FQDN.
- The front-end should embed broker endpoints as "/broker/<endpoint>" WITHOUT "http://localhost:3000" to allow for the webserver to be at a different host/port/https!
- The .env sets the FHIR server to a public test server. You can change it to another. Here are some potentials, though many are down: https://wiki.hl7.org/index.php?title=Publicly_Available_FHIR_Servers_for_testing

## Method 3: Using Docker Compose and Nodejs
### Prerequisites:
- [Node.js](https://nodejs,org) is installed.
- [Docker Compose](https://docs.docker.com/compose/install/) and any dependencies are installed.
- MassVaccinationSystem project source code downloaded.

### Setup
1. In /, copy **env.template** to a file named **.env** (note the dot in .env filename)
and update the FHIR_URL_BASE variable to `FHIR_URL_BASE=http://localhost:8080/hapi-fhir-jpaserver/fhir`
3. In /data/hapi/, copy **hapi.properties.template** to a file named **hapi.properties**.
4. (Optional) Edit .env and /data/hapi/hapi.properties file to change default database passwords (_BROKER_DB_PASSWORD_ 
and _HAPI_DB_PASSWORD_). Make sure the passwords match in .env and hapi.properties.

### How to run
Perform each of the following in a separate terminal:
1. Navigate to / and run `docker-compose -f docker-compose-dev.yml up --build`
2. Navigate to /broker and run `npm install`.  Once dependencies are installed, run `npm run devstart`
3. Navigate to /PatientRegistrationApp and run `npm install`.  Once dependencies are installed, run `npm run serve`
4. Navigate to /PointOfDispensingApp and run `npm install`.  Once dependencies are installed, run `npm run serve`

### How to view
1. The Patient Registration App can be viewed at http://localhost:8081
2. The Point of Dispensing App can be viewed at http://localhost:8082
3. HTTP requests from the Vue servers will automatically be redirected to broker (running on port 3000) for you
4. Any changes made to the source files for Broker, PatientRegistration, or PointOfDispensing will immediately take effect!  Changes made to the Vue apps will cause the browser to automatically refresh with the updated code/content

# Troubleshooting
### 1. Database connectivity issues
If you encounter issues connecting to the database after following the instructions above (e.g. server address not found, invalid database credentials), try performing the steps below.  Note that these commands will delete all existing data from the database.

1. Stop the docker containers and broker
2. Run `docker-compose down -v` or `docker-compose -f docker-compose-dev.yml down -v` (depending on how you've been running docker) from the root project directory
3. Restart the docker containers and broker

If you are still having connectivity issues, try the following steps
1. Stop the docker containers and broker
2. Run `docker-compose down -v` or `docker-compose -f docker-compose-dev.yml down -v` (depending on how you've been running docker) from the root project directory
3. Run `docker container prune -f`
4. Run `docker volume prune -f`
5. Run `docker network prune -f`
6. Restart the docker containers and broker


### 2. Broker errors related to patientIds or QR Codes
It's likely that something prevented the broker database from starting that handles patient Ids and QR Codes.  Perform the steps in issue 1 "Database connectivity issues" above.

