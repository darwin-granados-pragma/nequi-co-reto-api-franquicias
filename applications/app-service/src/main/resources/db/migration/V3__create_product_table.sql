CREATE TABLE product (
  id_product VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  stock INT NOT NULL,
  id_branch VARCHAR(50) NOT NULL,
  FOREIGN KEY (id_branch) REFERENCES branch(id_branch)
);
