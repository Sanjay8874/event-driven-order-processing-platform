package com.example.orderplatform;

import com.example.orderplatform.auth.JwtService;
import com.example.orderplatform.auth.RoleEntity;
import com.example.orderplatform.auth.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    @Test
    void createsAndParsesAccessToken() {
        JwtService service = new JwtService("test", "test-secret-test-secret-test-secret-test-secret", 30, 7);
        UserEntity user = new UserEntity("user@example.com", "hash", "User", Set.of(new RoleEntity("CUSTOMER")));

        var claims = service.parse(service.accessToken(user));

        assertThat(claims.getSubject()).isEqualTo("user@example.com");
        assertThat(service.roles(claims)).contains("CUSTOMER");
    }
}
