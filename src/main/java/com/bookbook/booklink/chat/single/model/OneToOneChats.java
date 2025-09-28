package com.bookbook.booklink.chat.single.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import jakarta.persistence.Column;
import org.hibernate.annotations.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneToOneChats {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;
}
    