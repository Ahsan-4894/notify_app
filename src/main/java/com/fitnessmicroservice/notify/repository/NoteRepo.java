package com.fitnessmicroservice.notify.repository;

import com.fitnessmicroservice.notify.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, String> {
    @Query(value = "SELECT u.id as userId, COUNT(n.id) as notesCount FROM users u JOIN notes n ON u.id = n.user_id GROUP BY u.id", nativeQuery = true)
    List<Object[]> getUsersWithNotesCount();
}
