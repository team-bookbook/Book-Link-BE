package com.bookbook.booklink.chat.single.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.chat.single.repository.OneToOneChatsRepository;

@Service
@RequiredArgsConstructor
public class OneToOneChatsService {
    private final OneToOneChatsRepository oneToOneChatsRepository;
}
    