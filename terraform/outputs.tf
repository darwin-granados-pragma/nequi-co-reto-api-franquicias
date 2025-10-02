output "app_server_public_ip" {
  description = "Public IP address of the EC2 instance to application access."
  value       = aws_instance.app_server.public_ip
}

