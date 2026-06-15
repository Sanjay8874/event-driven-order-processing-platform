# Design Decisions

## Modular Monolith First

A modular monolith was selected for the portfolio implementation because it is easier to run, test, and review while preserving service boundaries. The design can be split into independent services later because modules communicate through explicit domain events.

## PostgreSQL for Core Transactions

Orders, inventory, payments, and shipments require relational consistency and strong query support. PostgreSQL provides constraints, indexes, JSONB audit payload support, and a familiar operational model.

## JWT and Role-Based Access

JWT access tokens keep the API stateless. Refresh tokens are generated separately so production deployments can store/revoke refresh sessions in Redis or PostgreSQL.

## Spring Events as Local SNS/SQS Adapter

Spring events make the event flow demonstrable without requiring AWS infrastructure. In production, `EventPublisher` becomes the adapter that writes to SNS, and listener classes become SQS consumers or Lambda handlers.

## Flyway Migrations

Flyway provides deterministic schema evolution and makes GitHub reviewers confident the database can be recreated from source.
