package com.bookbook.booklink.library_service.service;

import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.library_service.model.Library;
import com.bookbook.booklink.library_service.model.dto.request.LibraryRegDto;
import com.bookbook.booklink.library_service.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final StringRedisTemplate redisTemplate;
    private final LibraryRepository libraryRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 새로운 Library 등록하는 메서드
     *
     * @param libraryRegDto Library 생성에 필요한 정보가 담긴 dto
     */
    @Transactional
    public UUID registerLibrary(LibraryRegDto libraryRegDto, String traceId, UUID userId) {
        log.info("[LibraryService] [traceId = {}, userId = {}] register library initiate name={}", traceId, userId, libraryRegDto.getName());

        // 멱등성 체크
        String key = "library:register:" + traceId;
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "LOCK", 1, TimeUnit.MINUTES);

        if (Boolean.FALSE.equals(success)) {
            throw new CustomException(ErrorCode.DUPLICATE_REQUEST);
        }

        // DB 저장 실패시 Redis 롤백
        eventPublisher.publishEvent(LockEvent.builder().key(key).build());

        // 저장할 객체 생성
        Library newLibrary = Library.toEntity(libraryRegDto);

        // DB에 저장
        Library savedLibrary = save(newLibrary);
        UUID savedLibraryId = savedLibrary.getId();

        log.info("[LibraryService] [traceId = {}, userId = {}] register library success name={}", traceId, userId, savedLibrary.getName());

        return savedLibraryId;
    }

    /**
     * Library 엔티티를 db에 저장하는 메서드
     *
     * @param library Library entity
     */
    public Library save(Library library) {
        return libraryRepository.save(library);
    }
}
    