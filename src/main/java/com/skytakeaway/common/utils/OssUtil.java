package com.skytakeaway.common.utils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class OssUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(byte[] bytes, String objectName){
        try {
            // Initialize the MinIO client
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKeyId, accessKeySecret)
                    .build();

            // Create a ByteArrayInputStream from the byte array
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

            // Upload the object
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, bytes.length, -1)  // -1 indicates unknown stream size
                            .build()
            );
        } catch (MinioException e) {
            log.error("Error occurred during file upload: ", e);
        } catch (Exception e) {
            log.error("Unexpected error during file upload: ", e);
        }

        // Log success and return the object's URL
        String objectUrl = endpoint + "/" + bucketName + "/" + objectName;
        log.info("File uploaded successfully to: {}", objectUrl);
        return objectUrl;
    }
}
