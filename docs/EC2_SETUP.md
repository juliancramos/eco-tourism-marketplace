# EC2 Setup

AWS EC2 Ubuntu instance configuration for marketplace deployment.

## SSH Access

Set key permissions:
```bash
chmod 400 jenkins-key.pem
```

Connect to instance:
```bash
ssh -i jenkins-key.pem ubuntu@<ec2-ip>
```

## System Dependencies

Update system:
```bash
sudo apt-get update
sudo apt-get install -y software-properties-common
sudo add-apt-repository universe
sudo apt-get update
```

Install Docker and Docker Compose:
```bash
sudo apt-get install -y docker.io docker-compose
```

Verify installation:
```bash
docker --version
docker-compose --version
```

Add user to Docker group:
```bash
sudo usermod -aG docker ubuntu
```

Logout and reconnect for group changes to take effect:
```bash
exit
ssh -i jenkins-key.pem ubuntu@<ec2-ip>
```

## Jenkins Installation

Create persistent volume:
```bash
docker volume create jenkins_home
```

Run Jenkins container:
```bash
docker run -d \
  --name jenkins \
  -p 9090:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

Verify container is running:
```bash
docker ps
```

Set Docker socket permissions:
```bash
sudo chmod 666 /var/run/docker.sock
ls -l /var/run/docker.sock
```

## Jenkins Configuration

Access UI at `http://<ec2-ip>:9090`

Get initial admin password:
```bash
docker exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Install build tools inside Jenkins container:
```bash
docker exec -u root -it jenkins bash
apt-get update
apt-get install -y maven docker.io docker-compose
mvn -v
docker --version
docker-compose --version
exit
```
