variable "db_name" {
  description = "Name of the data base."
  type        = string
}

variable "db_username" {
  description = "Username of the data base."
  type        = string
}

variable "db_password" {
  description = "Password of the data base."
  type        = string
  sensitive   = true
}

variable "git_repo_url" {
  description = "Url of the github spring project."
  type        = string
}

variable "ec2_key_name" {
  description = "Name of the key pair for ec2."
  type        = string
}

variable "allowed_access_ip" {
  description = "IP allowed to access to EC2 instance."
  type        = string
  default     = "0.0.0.0/0"
}