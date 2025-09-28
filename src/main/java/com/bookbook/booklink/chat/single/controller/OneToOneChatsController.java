package com.bookbook.booklink.chat.single.controller;

import com.bookbook.booklink.chat.chat_mutual.model.dto.request.MessageReqDto;
import com.bookbook.booklink.chat.chat_mutual.model.dto.response.MessageResDto;
import com.bookbook.booklink.chat.single.controller.docs.SingleChatApiDocs;
import com.bookbook.booklink.chat.single.model.dto.request.SingleRoomReqDto;
import com.bookbook.booklink.chat.single.model.dto.response.SingleRoomResDto;
import com.bookbook.booklink.chat.single.service.OneToOneChatsService;
import com.bookbook.booklink.common.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OneToOneChatsController implements SingleChatApiDocs {

    private final OneToOneChatsService oneToOneChatsService;

    @Override
    public ResponseEntity<BaseResponse<SingleRoomResDto>> createOrGetRoom(
            @RequestBody SingleRoomReqDto reqDto
    ) {
        SingleRoomResDto response =
                oneToOneChatsService.getOrCreateChatRoom(reqDto);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Override
    public ResponseEntity<BaseResponse<MessageResDto>> sendMessage(
            @RequestBody MessageReqDto dto
    ) {
        MessageResDto reponse = oneToOneChatsService.saveChatMessages(dto);
        return ResponseEntity.ok(BaseResponse.success(reponse));
    }

    @Override
    public ResponseEntity<BaseResponse<List<MessageResDto>>> getMessages(
            @PathVariable UUID chatId
    ) {
        List<MessageResDto> response = oneToOneChatsService.getChatMessages(chatId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
    