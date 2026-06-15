# Logging and Retry Strategy

## Logging

Every HTTP request receives an `X-Correlation-ID`. The value is added to SLF4J MDC and returned in the response header. Logs include event type, order ID, event ID, and payment/shipment references where safe.

## Retry

Payment processing uses Spring Retry with exponential backoff. After retry exhaustion, the recovery method marks the order as `FAILED` and logs DLQ routing. In AWS, this maps to SQS redrive policies with a dedicated payment DLQ.

## Monitoring

Recommended CloudWatch metrics:

- Orders created per minute
- Inventory reservation failures
- Payment retry count
- DLQ message count
- Notification latency
- API 4xx and 5xx rates
