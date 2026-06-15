# Architecture Document

This project is a modular Spring Boot 3 application that models an event-driven e-commerce order platform. The local runtime uses Spring application events so the code is runnable without AWS credentials. The deployment design maps those same event boundaries to SNS topics, SQS queues, Lambda handlers, and DLQs.

## Runtime Flow

1. A customer creates an order through the REST API or uploads a JSON order file to S3.
2. The API validates the payload, stores the order, and publishes `ORDER_CREATED`.
3. Inventory reserves stock asynchronously and publishes `INVENTORY_RESERVED`.
4. Payment processes a mock gateway transaction with exponential retry and failure recovery.
5. Shipment creates a tracking number after payment succeeds.
6. Notification publishes email/SMS events.
7. Audit records every domain event with event ID, order ID, type, timestamp, and JSON payload.

## Clean Architecture Boundaries

Controllers handle HTTP concerns only. Services own business rules. Repositories isolate persistence. Events are immutable records that act as integration contracts. Security is centralized through JWT filters and method-level role checks.

## AWS Mapping

| Local Component | AWS Production Equivalent |
| --- | --- |
| Spring EventPublisher | SNS topic |
| Async EventListener | SQS consumer or Lambda |
| Retry + Recover | SQS redrive policy and DLQ |
| PostgreSQL | Amazon RDS PostgreSQL |
| Audit table | DynamoDB or immutable RDS audit table |
| Correlation ID logs | CloudWatch Logs Insights |

## Reliability

The platform uses idempotent identifiers, transactional writes, retry with exponential backoff, and DLQ routing for failed payment processing. In production, each SQS message should include `eventId`, `orderId`, `eventType`, `correlationId`, and schema version.
