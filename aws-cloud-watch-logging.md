##### IAM Role attached to EC2 instance (permissions)

- AmazonSSMManagedInstanceCore
- CloudWatchAgentServerPolicy

##### Install Cloud Watch Agent

```
sudo wget -O amazon-cloudwatch-agent.deb https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i amazon-cloudwatch-agent.deb
```

##### Configuration File

```
{
  "agent": {
    "metrics_collection_interval": 60,
    "run_as_user": "root"
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/var/lib/docker/containers/*/*.log",
            "log_group_name": "gym-pass-prod",
            "log_stream_name": "{instance_id}"
          }
        ]
      }
    }
  },
  "metrics": {
    "namespace": "GymPass-Prod",
    "metrics_collected": {
      "cpu": {
        "measurement": ["cpu_usage_idle","cpu_usage_user","cpu_usage_system"],
        "totalcpu": true
      },
      "mem": {
        "measurement": ["mem_used_percent"]
      },
      "disk": {
        "measurement": ["used_percent"],
        "resources": ["*"]
      }
    }
  }
}
```

##### Start the Agent

```

```