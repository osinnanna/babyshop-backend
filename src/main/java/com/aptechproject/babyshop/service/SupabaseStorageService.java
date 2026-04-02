package com.aptechproject.babyshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aptechproject.babyshop.constant.AppConstants;

import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final String BUCKET_NAME = "product-images";

    public String uploadImage(byte[] imageBytes) {
        // 1. generate a random, unique filename to remove overlapping names
        // 2. Supabase API endpoint URL to our bucket
        // 3. Set up the HTTP headers (This proves to Supabase that we are the admin)
        // 4. Package the headers and the image bytes together
        // 5. Send the POST request
        // 6. If successful, construct and return the public URL so the client(Flutter) can access / display it


        // 1
        String fileName = UUID.randomUUID().toString() + ".webp";
        // 2
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;

        // 3
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.parseMediaType("image/webp"));

        // 4
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(imageBytes, headers);
        
        // 5
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 6
            return supabaseUrl + "/storage/v1/object/public/" + BUCKET_NAME + "/" + fileName;
        } else {
            throw new RuntimeException(AppConstants.ERROR_IMAGE_UPLOAD);
        }
    }
}