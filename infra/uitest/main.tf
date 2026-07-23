data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # Canonical

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_key_pair" "uitest" {
  key_name   = "${var.project_name}-uitest-key"
  public_key = var.ssh_public_key

  tags = {
    Project   = var.project_name
    ManagedBy = "udap"
    Purpose   = "ui-test-runner"
  }
}

resource "aws_security_group" "uitest" {
  name        = "${var.project_name}-uitest-sg"
  description = "Security group for Android UI test EC2 runner"

  ingress {
    description = "SSH from CI"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound (SDK downloads, Maestro)"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Project   = var.project_name
    ManagedBy = "udap"
    Purpose   = "ui-test-runner"
  }
}

resource "aws_instance" "uitest" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = "t3.large"
  key_name               = aws_key_pair.uitest.key_name
  vpc_security_group_ids = [aws_security_group.uitest.id]

  root_block_device {
    volume_size = 30
    volume_type = "gp3"
  }

  tags = {
    Project   = var.project_name
    ManagedBy = "udap"
    Purpose   = "ui-test-runner"
    Name      = "${var.project_name}-uitest-runner"
  }
}

resource "aws_eip" "uitest" {
  instance = aws_instance.uitest.id
  domain   = "vpc"

  tags = {
    Project   = var.project_name
    ManagedBy = "udap"
    Purpose   = "ui-test-runner"
  }
}
