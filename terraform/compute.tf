resource "aws_instance" "app_server" {
  ami           = data.aws_ami.amazon_linux_2023.id
  instance_type = "t3.small"
  vpc_security_group_ids = [aws_security_group.web_sg.id]
  key_name      = aws_key_pair.app_key_pair.key_name
  subnet_id     = aws_subnet.public_a.id

  user_data = <<-EOF
              set -e
              exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1

              yum update -y
              yum install -y java-17-amazon-corretto-devel git

              sudo -u ec2-user git clone "${var.git_repo_url}" /home/ec2-user/app

              sudo -u ec2-user bash -c '
                cd /home/ec2-user/app
                chmod +x ./gradlew

                export FRANCHISE_PROFILE_ACTIVE="prod"
                export CORS_ALLOWED_ORIGINS="*"
                export FRANCHISE_R2DBC_HOST="${aws_db_instance.app_db.address}"
                export FRANCHISE_R2DBC_PORT="${aws_db_instance.app_db.port}"
                export FRANCHISE_R2DBC_DATABASE="${var.db_name}"
                export FRANCHISE_R2DBC_SCHEMA="public"
                export FRANCHISE_R2DBC_USERNAME="${var.db_username}"
                export FRANCHISE_R2DBC_PASSWORD="${var.db_password}"

                ./gradlew :app-service:bootRun > /home/ec2-user/app/app.log 2>&1 &
              '
              EOF

  tags = {
    Name = "Servidor-App-Franquicias"
  }
}