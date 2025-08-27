#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER dbusr WITH PASSWORD 'W4lFRuS0wosPePhL6Otr';
	CREATE DATABASE farm_db;
	GRANT ALL PRIVILEGES ON DATABASE farm_db TO dbusr;
	\c farm_db;
	CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
EOSQL
