package com.bookbook.booklink.chat.single.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.chat.single.service.OneToOneChatsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onetoonechats")
public class OneToOneChatsController {
    private final OneToOneChatsService oneToOneChatsService;
}
    