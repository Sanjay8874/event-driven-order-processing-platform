# Scaling Strategy

Scale API nodes horizontally behind API Gateway or an application load balancer. Move event listeners to independent SQS consumers when throughput grows. Partition queues by event type and use DLQs per queue. Use RDS read replicas for reporting workloads, connection pooling through RDS Proxy, and DynamoDB for high-volume audit streams.

Inventory reservation should use optimistic locking or database-level conditional updates when concurrent order volume increases. Payment and notification consumers should be idempotent using `eventId` and `orderId` keys.
