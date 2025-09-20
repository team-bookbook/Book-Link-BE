package com.bookbook.booklink.library_service.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryLockEvent {
    private String key;
}
