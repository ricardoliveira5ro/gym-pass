##### Login into EC2 instance

```
ssh -i credentials.pem username[ubuntu]@PUBLIC_IP
```

##### Java & Maven

```
sudo apt update
sudo apt install -y openjdk-21-jdk
sudo apt install -y maven
```

##### Docker & Docker-Compose

```
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

##### Enable & Start Docker

```
sudo systemctl enable docker
sudo systemctl start docker
sudo systemctl status docker
```

##### Verify

```
sudo docker --version
sudo docker compose version
```