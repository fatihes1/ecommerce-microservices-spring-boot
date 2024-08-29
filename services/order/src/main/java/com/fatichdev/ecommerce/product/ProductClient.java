package com.fatichdev.ecommerce.product;

import com.fatichdev.ecommerce.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl;

    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requests) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<List<PurchaseRequest>> entity = new HttpEntity<>(requests, headers);

        ParameterizedTypeReference<List<PurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<List<PurchaseResponse>> response = restTemplate.exchange(
                productUrl + "/purchase", HttpMethod.POST, entity, responseType
        );

        if (response.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while purchasing products: " + response.getStatusCode());
        }


        return response.getBody();
    }
}
