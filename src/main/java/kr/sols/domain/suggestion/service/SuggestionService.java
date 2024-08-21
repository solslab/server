package kr.sols.domain.suggestion.service;

import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.company.service.CompanyService;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.member.service.MemberService;
import kr.sols.domain.position.Exception.PositionException;
import kr.sols.domain.position.entity.Position;
import kr.sols.domain.position.repository.PositionRepository;
import kr.sols.domain.suggestion.dto.SuggestionCreatedReq;
import kr.sols.domain.suggestion.dto.SuggestionCreatedRes;
import kr.sols.domain.suggestion.dto.SuggestionDto;
import kr.sols.domain.suggestion.dto.SuggestionListDto;
import kr.sols.domain.suggestion.entity.Status;
import kr.sols.domain.suggestion.entity.Suggestion;
import kr.sols.domain.suggestion.exception.SuggestionException;
import kr.sols.domain.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final PositionRepository positionRepository;
    private final MemberRepository memberRepository;
    private final SuggestionRepository suggestionRepository;
    private final MemberService memberService;
    private final CompanyService companyService;

    @Transactional
    public SuggestionCreatedRes createSuggestion(String memberKey, UUID positionId, SuggestionCreatedReq request) {
        Member member = memberRepository.findByMemberKey(memberKey).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Position position = positionRepository.findById(positionId).orElseThrow(() -> new PositionException(POSITION_NOT_FOUND));

        // 생성
        Suggestion suggestion = Suggestion.builder()
                .position(position)
                .member(member)
                .suggestionContent(request.getSuggestionContent())
                .suggestionStatus(Status.NOT_STARTED)
                .build();

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        return new SuggestionCreatedRes(savedSuggestion.getId());
    }

    @Transactional(readOnly = true)
    public List<SuggestionListDto> getAllSuggestion() {
        List<Suggestion> suggestions = suggestionRepository.findAllWithMembersPositionsAndCompanies();
        return suggestions.stream()
                .map(suggestion -> {
                    String memberName = suggestion.getMember().getName();
                    String companyName = suggestion.getPosition().getCompany().getCompanyName();

                    return SuggestionListDto.fromEntity(suggestion, companyName, memberName);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SuggestionDto getSuggestion(UUID suggestionId) {
        Suggestion suggestion = suggestionRepository.findByIdWithMemberPositionAndCompany(suggestionId).orElseThrow(()-> new SuggestionException(SUGGESTION_NOT_FOUND));
        String memberName = suggestion.getMember().getName();
        String memberEmail = suggestion.getMember().getEmail();
        String companyName = suggestion.getPosition().getCompany().getCompanyName();
        String positionName = suggestion.getPosition().getPositionName();
        return SuggestionDto.fromEntity(suggestion, companyName, positionName, memberName, memberEmail);
    }

    @Transactional
    public SuggestionCreatedRes updateSuggestionStatus(UUID suggestionId, Status updatedStatus) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId).orElseThrow(() -> new SuggestionException(SUGGESTION_NOT_FOUND));
        suggestion.updateStatus(updatedStatus);
        return new SuggestionCreatedRes(suggestionId);
    }
}
