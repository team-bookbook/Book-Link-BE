package com.bookbook.booklink.library_service.controller;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.book_service.service.LibraryBookService;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.library_service.controller.docs.LibraryApiDocs;
import com.bookbook.booklink.library_service.model.dto.request.LibraryRegDto;
import com.bookbook.booklink.library_service.model.dto.request.LibraryUpdateDto;
import com.bookbook.booklink.library_service.model.dto.response.LibraryDetailDto;
import com.bookbook.booklink.library_service.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class LibraryController implements LibraryApiDocs {
    private final LibraryService libraryService;
    private final LibraryBookService libraryBookService;

    @Override
    public ResponseEntity<BaseResponse<UUID>> registerLibrary(
            @Valid @RequestBody LibraryRegDto libraryRegDto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {
        UUID userId = member.getId();

        log.info("[LibraryController] [traceId = {}, userId = {}] register library request received, name={}",
                traceId, userId, libraryRegDto.getName());

        UUID savedLibraryId = libraryService.registerLibrary(libraryRegDto, traceId, member);

        log.info("[LibraryController] [traceId = {}, userId = {}] register library response success, libraryId={}",
                traceId, userId, savedLibraryId);
        return ResponseEntity.ok()
                .body(BaseResponse.success(savedLibraryId));
    }

    @Override
    public ResponseEntity<BaseResponse<UUID>> updateLibrary(
            @Valid @RequestBody LibraryUpdateDto libraryUpdateDto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {
        UUID userId = member.getId();
        log.info("[LibraryController] [traceId = {}, userId = {}] update library request received, libraryId={}",
                traceId, userId, libraryUpdateDto.getLibraryId());

        UUID updatedLibraryId = libraryService.updateLibrary(libraryUpdateDto, traceId, userId);

        log.info("[LibraryController] [traceId = {}, userId = {}] update library response success, libraryId={}",
                traceId, userId, updatedLibraryId);
        return ResponseEntity.ok()
                .body(BaseResponse.success(updatedLibraryId));
    }


    @Override
    public ResponseEntity<BaseResponse<Boolean>> deleteLibrary(
            @PathVariable @NotNull(message = "수정할 도서관의 ID는 필수입니다.") UUID id,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {
        UUID userId = member.getId();
        log.info("[LibraryController] [traceId = {}, userId = {}] delete library request received, libraryId={}",
                traceId, userId, id);

        libraryService.deleteLibrary(id, traceId, userId);

        log.info("[LibraryController] [traceId = {}, userId = {}] delete library response success, libraryId={}",
                traceId, userId, id);

        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }

    @Override
    public ResponseEntity<BaseResponse<LibraryDetailDto>> getLibrary(
            @PathVariable @NotNull(message = "조회할 도서관의 ID는 필수입니다.") UUID id
    ) {
        List<LibraryBook> top5List = libraryBookService.findTop5Books(id);
        LibraryDetailDto libraryDetailDto = libraryService.getLibrary(id, top5List);

        return ResponseEntity.ok()
                .body(BaseResponse.success(libraryDetailDto));
    }

    @Override
    public ResponseEntity<BaseResponse<List<LibraryDetailDto>>> getLibraries(
            @RequestParam Double lat,
            @RequestParam Double lng
    ) {

        List<LibraryDetailDto> result = libraryService.getLibraries(lat, lng);

        return ResponseEntity.ok()
                .body(BaseResponse.success(result));
    }
}
