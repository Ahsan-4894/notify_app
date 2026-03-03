package com.fitnessmicroservice.notify.service;

import com.fitnessmicroservice.notify.dto.request.CreateNoteRequestDto;
import com.fitnessmicroservice.notify.dto.request.UpdateNoteRequestDto;
import com.fitnessmicroservice.notify.dto.response.NoteResponseDto;
import com.fitnessmicroservice.notify.entity.Note;
import com.fitnessmicroservice.notify.entity.User;
import com.fitnessmicroservice.notify.exception.custom.NoteNotFoundException;
import com.fitnessmicroservice.notify.exception.custom.UserNotFoundException;
import com.fitnessmicroservice.notify.repository.NoteRepo;
import com.fitnessmicroservice.notify.repository.UserRepo;
import com.fitnessmicroservice.notify.util.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepo noteRepo;
    private final UserRepo userRepo;
    private final AuthContext authContext;
    private final UserService userService;;

    public NoteResponseDto createNote(CreateNoteRequestDto requestDto) {
        String userId = authContext.getIdOfCurrentLoggedInUser();
        User user = userService.getUserById(userId);

        Note note = Note.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        Note savedNote = noteRepo.save(note);
        return mapToResponseDto(savedNote);
    }

    public NoteResponseDto getNoteById(String noteId) {
        String userId = authContext.getIdOfCurrentLoggedInUser();

        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));

        if (note.getUser() == null || !note.getUser().getId().equals(userId)) {
            throw new NoteNotFoundException("Note not found with id: " + noteId);
        }

        return mapToResponseDto(note);
    }

    public NoteResponseDto updateNote(String noteId, UpdateNoteRequestDto requestDto) {
        String userId = authContext.getIdOfCurrentLoggedInUser();

        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));

        // Verify that the note belongs to the current user
        if (note.getUser() == null || !note.getUser().getId().equals(userId)) {
            throw new NoteNotFoundException("Note not found with id: " + noteId);
        }

        note.setContent(requestDto.getContent());
        Note updatedNote = noteRepo.save(note);
        return mapToResponseDto(updatedNote);
    }

    public void deleteNote(String noteId) {
        String userId = authContext.getIdOfCurrentLoggedInUser();

        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));

        // Verify that the note belongs to the current user
        if (note.getUser() == null || !note.getUser().getId().equals(userId)) {
            throw new NoteNotFoundException("Note not found with id: " + noteId);
        }

        noteRepo.deleteById(noteId);
    }

    private NoteResponseDto mapToResponseDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
