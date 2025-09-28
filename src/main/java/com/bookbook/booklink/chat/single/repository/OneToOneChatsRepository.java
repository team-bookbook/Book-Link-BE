package com.bookbook.booklink.chat.single.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.bookbook.booklink.chat.single.model.OneToOneChats;

@Repository
public interface OneToOneChatsRepository extends JpaRepository<OneToOneChats, UUID> {

}
    