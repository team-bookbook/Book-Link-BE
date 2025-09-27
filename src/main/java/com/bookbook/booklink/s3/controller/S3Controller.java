package com.bookbook.booklink.s3.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.s3.controller.docs.S3ApiDocs;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class S3Controller implements S3ApiDocs {

    private final S3PresignedUrlService s3PresignedUrlService;

    @Value("${s3.url-duration}")
    private long urlDuration;

    @Value("${s3.bucket-name}")
    private String bucketName;

    private final String ROOT = "library-book-images/";

    @Override
    public ResponseEntity<BaseResponse<String>> getPresignedUrl(
            @RequestParam @NotNull(message = "저장할 이미지 이름은 필수입니다.") String fileName,
            @RequestHeader("Trace-Id") String traceId
    ) {
        // 5분 동안 유효
        URL url = s3PresignedUrlService.generatePresignedUrl(
                bucketName,
                ROOT + fileName,
                Duration.ofMillis(urlDuration)
        );

        return ResponseEntity.ok()
                .body(BaseResponse.success(url.toString()));
    }
}
