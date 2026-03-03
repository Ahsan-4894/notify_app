package com.fitnessmicroservice.notify.controller;

import com.fitnessmicroservice.notify.dto.request.CreateNoteRequestDto;
import com.fitnessmicroservice.notify.dto.request.UpdateNoteRequestDto;
import com.fitnessmicroservice.notify.dto.response.NoteResponseDto;
import com.fitnessmicroservice.notify.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@Valid @RequestBody CreateNoteRequestDto requestDto) {
        NoteResponseDto response = noteService.createNote(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable String noteId) {
        NoteResponseDto response = noteService.getNoteById(noteId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponseDto> updateNote(
            @PathVariable String noteId,
            @Valid @RequestBody UpdateNoteRequestDto requestDto) {
        NoteResponseDto response = noteService.updateNote(noteId, requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Boolean> deleteNote(@PathVariable String noteId) {
        noteService.deleteNote(noteId);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.NO_CONTENT);
    }
}
