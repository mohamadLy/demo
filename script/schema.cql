// create keyspace
CREATE KEYSPACE IF NOT EXISTS sapdemo
WITH replication = {
  'class':'NetworkTopologyStrategy',
  'DC1':'1'
};

// create Car table
CREATE TYPE IF NOT EXISTS sapdemo.car (
  registration TEXT,
  owner TEXT,
  subscription TEXT,
  color TEXT
);

// create trips table
CREATE TABLE IF NOT EXISTS sapdemo.trip (
  registration TEXT,
  time TEXT,
  speed TIMESTAMP
);
