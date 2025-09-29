CREATE TABLE franchise (
  id_franchise VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  CONSTRAINT name_unique_constraint UNIQUE (name)
);
