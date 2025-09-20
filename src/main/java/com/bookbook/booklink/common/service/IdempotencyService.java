package com.bookbook.booklink.common.service;

import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.library_service.event.LibraryLockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Redis를 이용한 멱등성 체크
     *
     * @param prefix  key prefix (ex: "library:register", "library:update")
     * @param traceId 클라이언트에서 전달받은 traceId
     * @param ttl     Lock 유지 시간 (분 단위)
     * @throws CustomException 중복 요청일 경우
     */
    public void checkIdempotency(String prefix, String traceId, long ttl) {
        String key = prefix + ":" + traceId;
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "LOCK", ttl, TimeUnit.MINUTES);
        if (Boolean.FALSE.equals(success)) {
            throw new CustomException(ErrorCode.DUPLICATE_REQUEST);
        }

        // DB 실패 시 롤백을 위해 이벤트 발행
        eventPublisher.publishEvent(LibraryLockEvent.builder().key(key).build());
    }
}
