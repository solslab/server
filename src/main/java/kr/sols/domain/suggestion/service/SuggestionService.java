package kr.sols.domain.suggestion.service;

import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.position.Exception.PositionException;
import kr.sols.domain.position.entity.Position;
import kr.sols.domain.position.repository.PositionRepository;
import kr.sols.domain.suggestion.dto.SuggestionCreatedReq;
import kr.sols.domain.suggestion.dto.SuggestionCreatedRes;
import kr.sols.domain.suggestion.entity.Status;
import kr.sols.domain.suggestion.entity.Suggestion;
import kr.sols.domain.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static kr.sols.exception.ErrorCode.MEMBER_NOT_FOUND;
import static kr.sols.exception.ErrorCode.POSITION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final PositionRepository positionRepository;
    private final MemberRepository memberRepository;
    private final SuggestionRepository suggestionRepository;

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
}
