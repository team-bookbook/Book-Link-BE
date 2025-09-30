package com.bookbook.booklink.chat_service.single.controller.docs;

import com.bookbook.booklink.chat_service.chat_mutual.model.dto.request.MessageReqDto;
import com.bookbook.booklink.chat_service.chat_mutual.model.dto.response.MessageResDto;
import com.bookbook.booklink.chat_service.single.model.dto.request.SingleRoomReqDto;
import com.bookbook.booklink.chat_service.single.model.dto.response.SingleRoomResDto;
import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "1:1 채팅 API", description = "1:1 채팅방 및 메시지 관련 API 명세")
@RequestMapping("/api/singleChats")
public interface SingleChatApiDocs {

    @Operation(
            summary = "1:1 채팅방 생성 or 가져오기",
            description = "두 사용자 ID를 받아 기존 채팅방이 있으면 반환하고, 없으면 새로 생성합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PostMapping("/rooms")
    public ResponseEntity<BaseResponse<SingleRoomResDto>> createOrGetRoom(
            @RequestBody SingleRoomReqDto dto
    );

    @Operation(
            summary = "메시지 보내기",
            description = "특정 채팅방에 메시지를 전송합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @PostMapping("/sendMessage")
    public ResponseEntity<BaseResponse<MessageResDto>> sendMessage(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MessageReqDto dto);

    @Operation(
            summary = "채팅방 메시지 조회",
            description = "채팅방의 모든 메시지를 시간순으로 조회합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})
    @GetMapping("/rooms/{chatId}/messages")
    public ResponseEntity<BaseResponse<List<MessageResDto>>> getMessages(
            @PathVariable UUID chatId);
}
