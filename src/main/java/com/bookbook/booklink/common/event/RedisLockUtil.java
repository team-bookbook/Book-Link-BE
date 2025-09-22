package com.bookbook.booklink.common.event;

import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisLockUtil {

    private final StringRedisTemplate redisTemplate;

    /**
     * 주어진 key에 대해 Redis 분산락을 획득한다.
     * 실패 시 CustomException(DUPLICATE_REQUEST) 발생
     *
     * @param key redis lock key
     */
    public void acquireLockOrThrow(String key) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "LOCK", 1, TimeUnit.MINUTES);

        if (Boolean.FALSE.equals(success)) {
            throw new CustomException(ErrorCode.DUPLICATE_REQUEST);
        }
    }
}
