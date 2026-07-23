variable "project_name" {
  description = "Project name used as a resource prefix and tag"
  type        = string
}

variable "ssh_public_key" {
  description = "SSH public key for EC2 key pair (injected by platform)"
  type        = string
}
