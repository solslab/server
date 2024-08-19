package kr.sols.domain.suggestion.controller;

import kr.sols.auth.annotation.RoleUser;
import kr.sols.domain.suggestion.dto.SuggestionCreatedRes;
import kr.sols.domain.suggestion.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggestion")
public class SuggestionController {
    private final SuggestionService suggestionService;

//    @RoleUser
//    @PostMapping("/{positionId}")
//    public ResponseEntity<SuggestionCreatedRes> createSuggestion(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @PathVariable UUID positionId
//            ){
//
//    }


}
