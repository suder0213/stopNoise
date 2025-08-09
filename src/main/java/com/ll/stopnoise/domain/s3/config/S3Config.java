package com.ll.stopnoise.domain.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        // application.yml에서 가져온 accessKey와 secretKey를 사용하여 자격 증명 객체를 생성
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                // 생성한 자격 증명 객체를 S3Client 빌더에 직접 전달
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}