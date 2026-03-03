package com.fitnessmicroservice.notify.service;

import com.fitnessmicroservice.notify.dto.response.UserDashboardDto;
import com.fitnessmicroservice.notify.repository.NoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final NoteRepo noteRepo;

    public List<UserDashboardDto> getDashboard() {
        List<Object[]> results = noteRepo.getUsersWithNotesCount();
        return results.stream()
                .map(result -> UserDashboardDto.builder()
                        .userId((String) result[0])
                        .notesCount(((Number) result[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }
}

