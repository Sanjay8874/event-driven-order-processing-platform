# AWS Cost Estimation

Small portfolio/demo environment assumptions:

| Service | Assumption | Estimated Monthly Cost |
| --- | --- | --- |
| API Gateway | 1M REST requests | $3-$5 |
| Lambda | 1M order upload validations | <$2 |
| S3 | Low-volume JSON uploads | <$1 |
| SNS/SQS | 1M events/messages | <$2 |
| RDS PostgreSQL | db.t4g.micro dev instance | $15-$25 |
| CloudWatch | Logs and metrics | $3-$10 |
| Secrets Manager | 2-4 secrets | $1-$2 |

Expected demo total: roughly $25-$50/month depending on region, log retention, and RDS uptime. Turning off RDS outside demo hours can reduce cost significantly.
