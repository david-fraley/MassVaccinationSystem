The files in this directory have been auto-generated from the massvaxx database schema using sequelize-auto (https://github.com/sequelize/sequelize-auto).

To re-generate files (assuming on host with PostgreSQL database with massvaxx schema):

npm install -g pg sequalize sequalize-auto
sequelize-auto -h localhost -d massvaxx -u massvaxxadmin -x [password] -p 5432  --dialect postgres -o [directory_for_files]

To create a test container running with massvaxx schema for the above:

cd broker_db
docker build -t massvaxx/broker_db:latest .
docker run -d --rm=true --name broker_db -e POSTGRES_PASSWORD=[password] massvaxx/broker_db
