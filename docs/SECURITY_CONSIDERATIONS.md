# Security Considerations

- Store JWT secrets and database credentials in AWS Secrets Manager.
- Use short-lived access tokens and persist refresh tokens for revocation.
- Enforce role checks with `@PreAuthorize`.
- Hash passwords with BCrypt.
- Validate every request DTO with Jakarta Validation.
- Use TLS everywhere and private subnets for RDS.
- Emit correlation IDs, but never log passwords, tokens, or payment secrets.
- Use least-privilege IAM roles for Lambda, S3, SNS, SQS, and RDS access.
