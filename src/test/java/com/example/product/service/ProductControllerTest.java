package com.example.product.service;
import com.example.product.auth.model.JwtRequest;
import com.example.product.auth.model.JwtResponse;
import com.example.product.dto.ProductRequest;
import com.example.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken());
    }

    private String getToken() {
        String url = "http://localhost:" + port + "/authenticate";
        JwtRequest request = new JwtRequest();
        request.setUsername("admin");
        request.setPassword("admin");

        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(url, request, JwtResponse.class);

        JwtResponse body = response.getBody();
        System.out.println(body.getToken());
        return body.getToken();
    }

    @Test
    public void testCreateProductWithToken() {
        String url = "http://localhost:" + port + "/api/v1/products";
        ProductRequest product = new ProductRequest();
        product.setName("Product 123");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(55.0));

        HttpEntity<ProductRequest> entity = new HttpEntity<>(product, headers);
        ResponseEntity<Product> response = restTemplate.postForEntity(url, entity, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Product 123");
    }


}
