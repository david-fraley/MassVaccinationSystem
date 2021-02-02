# MassVaccinationSystem

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
