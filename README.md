# MassVaccinationSystem

<<<<<<< HEAD
For more information about this project, visit [MassVaxx](https://www.massvaxx.com).

# How to run

## Using Docker Compose 

See [README.md](sandbox/README.md) in sandbox directory.

## Using only Node.js (Docker not required)
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

## Using Docker Compose (deprecated)
### Prerequisites:
- [Node.js](https://nodejs,org) is installed.
- [Docker Compose](https://docs.docker.com/compose/install/) and any dependencies are installed.
- MassVaccinationSystem project source code downloaded.

### Steps:
1. Navigate to /PatientRegistrationApp and /PointOfDispensingApp and run `npm install` followed by `npm run devbuild` for both. Wait for build to finish
2. Navigate to / and run `docker-compose up --build`

### Notes:
- The PatientRegistrationApp is served at http://localhost:3000/Registration.
- The PointOfDispensingApp is at http://localhost:3000/POD.
- You can access broker endpoints directly two ways, e.g.:  
    http://localhost:3000/healthcheck (for test env only)  
    http://localhost:3000/broker/healthcheck (for proxied front-end connections)
- To access from other workstations, make sure your firewall allows connections over port 3000 and replace localhost with your IP address or FQDN.
- The front-end should embed broker endpoints as "/broker/<endpoint>" WITHOUT "http://localhost:3000" to allow for the webserver to be at a different host/port/https!
=======
## Setup
Add or update /broker/.env with the following line: `FHIR_URL_BASE=http://localhost:8080/hapi-fhir-jpaserver/fhir`

## How to run
Perform each of the following in a separate terminal:
1. Navigate to / and run `docker-compose -f docker-compose-dev.yml up --build`
2. Navigate to /broker and run `npm install`.  Once dependencies are installed, run `npm run devstart`
3. Navigate to /PatientRegistrationApp and run `npm install`.  Once dependencies are installed, run `npm run serve`
4. Navigate to /PointOfDispensingApp and run `npm install`.  Once dependencies are installed, run `npm run serve`

## How to view
1. The Patient Registration App can be viewed at http://localhost:8081
2. The Point of Dispensing App can be viewed at http://localhost:8082
3. HTTP requests from the Vue servers will automatically be redirected to broker (running on port 3000) for you
4. Any changes made to the source files for Broker, PatientRegistration, or PointOfDispensing will immediately take effect!  Changes made to the Vue apps will cause the browser to automatically refresh with the updated code/content
>>>>>>> Development
