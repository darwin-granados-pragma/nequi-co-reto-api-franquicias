CREATE TABLE branch (
  id_branch VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  id_franchise VARCHAR(50) NOT NULL,
  CONSTRAINT branch_name_unique_constraint UNIQUE (name),
  FOREIGN KEY (id_franchise) REFERENCES franchise(id_franchise)
);
