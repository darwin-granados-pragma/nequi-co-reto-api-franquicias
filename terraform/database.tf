resource "aws_db_parameter_group" "app_db_params" {
  name   = "app-db-params"
  family = "postgres16"

  parameter {
    name  = "rds.force_ssl"
    value = "0"
  }

  tags = {
    Name = "App DB Parameter Group"
  }
}


resource "aws_db_instance" "app_db" {
  allocated_storage      = 10
  engine                 = "postgres"
  engine_version         = "16.9"
  instance_class         = "db.t3.micro"
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  skip_final_snapshot    = true
  publicly_accessible    = false
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  db_subnet_group_name = aws_db_subnet_group.default.name
  parameter_group_name = aws_db_parameter_group.app_db_params.name
}

