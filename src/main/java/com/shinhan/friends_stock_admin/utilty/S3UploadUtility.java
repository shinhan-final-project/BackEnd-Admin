package com.shinhan.friends_stock_admin.utilty;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class S3UploadUtility {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public void upload(String key, String content) {

        try {
            byte[] jsonData = content.getBytes();

            InputStream inputStream = new ByteArrayInputStream(jsonData);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, jsonData.length));

            System.out.println("uploaded");
        } catch (S3Exception e) {
            e.printStackTrace();
        }
    }
}
