output "instance_public_ip" {
  description = "Public IP of the UI test EC2 runner (EIP — stable across stop/start)"
  value       = aws_eip.uitest.public_ip
}
