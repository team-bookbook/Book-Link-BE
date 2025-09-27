package com.bookbook.booklink.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전 예시
                .credentialsProvider(DefaultCredentialsProvider.create()) // 환경변수에서 자동으로 key 찾아서 사용
                .build();
    }
}
