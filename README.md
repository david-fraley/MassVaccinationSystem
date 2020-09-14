# MassVaccinationSystem

# How to run
1. Install docker compose
2. Navigate to MassVaccinationSystem directory with the docker-compose.yml file
3. `docker-compose up`
4. The backend will run on localhost:3000 and the frontend will run on localhost:8080
5. The fhir server will run on `http://localhost:8090/hapi-fhir-jpaserver/`;
   Access the database with `docker exec -it docker-container-name bash`;
   Login to database with `psql -U username database-name`;
   `\l` list databases, `\c database` connect to database

Source for frontend app / backend app and future source (maybe) for
authentication: https://itnext.io/how-to-use-firebase-auth-with-a-custom-node-backend-99a106376c8a?source=friends_link&sk=ab8373c4ce9c869728e97b6ac4e160d2

Source for vueapp: https://medium.com/quick-code/how-to-create-a-simple-vue-js-app-in-5-minutes-f74fb04adc01

Source for Dockerization of vueapp https://vuejs.org/v2/cookbook/dockerize-vuejs-app.html

Source for HAPI FHIR https://github.com/hapifhir/hapi-fhir-jpaserver-starter