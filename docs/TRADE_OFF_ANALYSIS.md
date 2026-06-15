# Trade-Off Analysis

| Decision | Benefit | Trade-Off |
| --- | --- | --- |
| Modular monolith | Simple local execution and cohesive code review | Less independent scaling than separate microservices |
| Spring events locally | No cloud dependency during development | Requires SNS/SQS adapter for production |
| PostgreSQL audit table | Easy joins and local demos | DynamoDB may be better for very high-volume immutable audit events |
| Mock payment gateway | Safe portfolio demo | Real gateway integration requires PCI-aware design |
| JWT stateless auth | Scales horizontally | Token revocation needs refresh-token persistence or blacklist |
