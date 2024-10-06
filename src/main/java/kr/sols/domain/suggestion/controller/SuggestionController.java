package kr.sols.domain.suggestion.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.domain.suggestion.dto.*;
import kr.sols.domain.suggestion.entity.Status;
import kr.sols.domain.suggestion.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggestion")
public class SuggestionController {
    private final SuggestionService suggestionService;

    @RoleUser
    @PostMapping("/{positionId}")
    public ResponseEntity<SuggestionCreatedRes> createSuggestion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID positionId,
            @RequestBody SuggestionCreatedReq request) {
        SuggestionCreatedRes sug = suggestionService.createSuggestion(userDetails.getUsername(), positionId, request);
        return ResponseEntity.ok(sug);
    }

    @RoleAdmin
    @GetMapping
    public ResponseEntity<SuggestionPageDto> getAllSuggestion(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {
        SuggestionPageDto suggestionPage = suggestionService.getAllSuggestion(pageNum, pageSize);
        return ResponseEntity.ok(suggestionPage);
    }

    @RoleAdmin
    @GetMapping("/{suggestionId}")
    public ResponseEntity<SuggestionDto> getSuggestion(
            @PathVariable UUID suggestionId
    ) {
        return ResponseEntity.ok(suggestionService.getSuggestion(suggestionId));
    }

    @RoleAdmin
    @PutMapping("/{suggestionId}")
    public ResponseEntity<SuggestionCreatedRes> updateSuggestionStatus(
            @PathVariable UUID suggestionId,
            @RequestParam(value = "status") Status updatedStatus
    ) {
        return ResponseEntity.ok(suggestionService.updateSuggestionStatus(suggestionId, updatedStatus));
    }
}
