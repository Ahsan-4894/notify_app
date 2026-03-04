package com.fitnessmicroservice.notify.service;

import com.fitnessmicroservice.notify.dto.request.CreateNoteRequestDto;
import com.fitnessmicroservice.notify.dto.request.UpdateNoteRequestDto;
import com.fitnessmicroservice.notify.dto.response.NoteResponseDto;
import com.fitnessmicroservice.notify.entity.Note;
import com.fitnessmicroservice.notify.entity.User;
import com.fitnessmicroservice.notify.entity.UserPrincipal;
import com.fitnessmicroservice.notify.exception.custom.NoteNotFoundException;
import com.fitnessmicroservice.notify.exception.custom.UserNotFoundException;
import com.fitnessmicroservice.notify.repository.NoteRepo;
import com.fitnessmicroservice.notify.repository.UserRepo;
import com.fitnessmicroservice.notify.util.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepo noteRepo;
    private final AuthContext authContext;
    private final UserService userService;

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
        UserPrincipal userPrincipal = authContext.getCurrentLoggedInUser();
        String userId = userPrincipal.getUser().getId();
        String role = userPrincipal.getUser().getRole();

        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));

//        If the incoming user is admin, show note to him
        if(role.equals("ROLE_ADMIN")) {
            return mapToResponseDto(note);
        }

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

    public List<NoteResponseDto> getAllNotesOfAUser() {
        String userId = authContext.getIdOfCurrentLoggedInUser();
//        What if this user has no notes?
        List<Note> notes = noteRepo.findByUserId(userId);
        if(notes.isEmpty())
            throw new NoteNotFoundException("Notes not found for this user: " + userId);

        return notes.stream().map(this::mapToResponseDto).toList();
    }
}
