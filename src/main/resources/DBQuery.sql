CREATE DATABASE userservice;
CREATE USER userserviceuser IDENTIFIED BY "password";
GRANT ALL PRIVILEGES ON userservice.* TO userserviceuser;