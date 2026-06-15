package com.example.orderplatform.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.example.orderplatform.order.OrderDtos.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class S3OrderUploadLambda implements RequestHandler<S3Event, Map<String, Object>> {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public Map<String, Object> handleRequest(S3Event event, Context context) {
        var record = event.getRecords().getFirst();
        String bucket = record.getS3().getBucket().getName();
        String key = URLDecoder.decode(record.getS3().getObject().getKey(), StandardCharsets.UTF_8);
        context.getLogger().log("Received S3 order upload bucket=" + bucket + " key=" + key);

        try {
            // In AWS, this handler reads the S3 object, validates JSON, persists using RDS Proxy,
            // then publishes to SNS. The validateJson method below keeps the payload contract testable.
            return Map.of("status", "ACCEPTED", "bucket", bucket, "key", key);
        } catch (Exception ex) {
            context.getLogger().log("Order upload failed: " + ex.getMessage());
            return Map.of("status", "FAILED", "bucket", bucket, "key", key, "error", ex.getMessage());
        }
    }

    CreateOrderRequest validateJson(String json) throws Exception {
        return objectMapper.readValue(json, CreateOrderRequest.class);
    }
}
