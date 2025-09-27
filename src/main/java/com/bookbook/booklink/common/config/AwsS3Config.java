package com.bookbook.booklink.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsS3Config {

    // todo : 논의 결과에 따라 s3 client 삭제 ( s3 등록 로직을  프론트에서 할지 백엔드에서 할지 )
    /**
     * S3Client를 빈으로 생성합니다.
     *
     * <p>
     * 서버에서 직접 S3 API를 호출할 때 사용합니다.
     * 예: 파일 업로드, 다운로드, 삭제 등
     * DefaultCredentialsProvider를 사용하여 환경변수, AWS CLI 프로파일,
     * 또는 IAM 역할에서 자격증명을 자동으로 가져옵니다.
     * </p>
     *
     * @return S3Client 인스턴스
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전 예시
                .credentialsProvider(DefaultCredentialsProvider.create()) // 환경변수에서 자동으로 key 찾아서 사용
                .build();
    }

    /**
     * S3Presigner를 빈으로 생성합니다.
     *
     * <p>
     * 클라이언트가 직접 S3에 파일을 업로드하거나 다운로드할 수 있는
     * presigned URL을 생성할 때 사용합니다.
     * DefaultCredentialsProvider를 사용하여 환경변수, AWS CLI 프로파일,
     * 또는 IAM 역할에서 자격증명을 자동으로 가져옵니다.
     * </p>
     *
     * @return S3Presigner 인스턴스
     */
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create()) // 환경변수에서 자동으로 key 찾아서 사용
                .build();
    }
}
