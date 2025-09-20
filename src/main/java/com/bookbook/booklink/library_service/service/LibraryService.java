package com.bookbook.booklink.library_service.service;

import com.bookbook.booklink.library_service.model.Library;
import com.bookbook.booklink.library_service.model.dto.request.LibraryRegDto;
import com.bookbook.booklink.library_service.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    /**
     * 새로운 Library 등록하는 메서드
     *
     * @param libraryRegDto Library 생성에 필요한 정보가 담긴 dto
     */
    @Transactional
    public UUID registerLibrary(LibraryRegDto libraryRegDto, UUID traceId, UUID userId) {
        log.info("[LibraryService] [traceId = {}, userId = {}] register library initiate name={}", traceId, userId, libraryRegDto.getName());

        // 새로운 library 생성
        Library newLibrary = Library.toEntity(libraryRegDto);

        // DB에 저장
        Library savedLibrary = save(newLibrary);

        log.info("[LibraryService] [traceId = {}, userId = {}] register library success name={}", traceId, userId, savedLibrary.getName());

        return savedLibrary.getId();
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
    