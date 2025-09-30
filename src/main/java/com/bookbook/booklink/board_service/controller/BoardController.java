package com.bookbook.booklink.board_service.controller;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.BoardCategory;
import com.bookbook.booklink.board_service.model.dto.request.BoardCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.BoardUpdateDto;
import com.bookbook.booklink.board_service.model.dto.response.BoardDetailDto;
import com.bookbook.booklink.board_service.model.dto.response.BoardListDto;
import com.bookbook.booklink.board_service.service.BoardService;
import com.bookbook.booklink.common.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> createBoard(
            @RequestBody BoardCreateDto boardCreateDto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {

        UUID newBoardId = boardService.createBoard(boardCreateDto, member, traceId);

        return ResponseEntity.ok()
                .body(BaseResponse.success(newBoardId));
    }

    @PutMapping
    public ResponseEntity<BaseResponse<UUID>> updateBoard(
            @RequestBody BoardUpdateDto boardUpdateDto,
            @RequestHeader("Trace-Id") String traceId,
            @AuthenticationPrincipal(expression = "member") Member member
    ) {

        UUID updateBoardId = boardService.updateBoard(boardUpdateDto, member, traceId);

        return ResponseEntity.ok()
                .body(BaseResponse.success(updateBoardId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> deleteBoard(
            @PathVariable UUID id
    ) {
        boardService.deleteBoard(id);

        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<BoardListDto>>> getBoards(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BoardCategory category
    ) {
        List<BoardListDto> dtoList = boardService.getBoards(title, category);

        return ResponseEntity.ok()
                .body(BaseResponse.success(dtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BoardDetailDto>> getBoard(
            @PathVariable UUID id
    ) {
        BoardDetailDto dto = boardService.getBoard(id);

        return ResponseEntity.ok()
                .body(BaseResponse.success(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> likeBoard(
            @PathVariable UUID id
    ) {
        boardService.likeBoard(id);

        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }
}
    