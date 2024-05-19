-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE influencer (id SERIAL, first_name VARCHAR, last_name VARCHAR, instagram_id VARCHAR, api_key VARCHAR, PRIMARY KEY (id))