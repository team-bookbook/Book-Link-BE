package com.bookbook.booklink.library_service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.bookbook.booklink.library_service.model.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, UUID> {

}
    