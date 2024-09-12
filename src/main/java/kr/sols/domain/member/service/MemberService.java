package kr.sols.domain.member.service;
import static kr.sols.exception.ErrorCode.MEMBER_NOT_FOUND;

import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.dto.MemberDto;
import kr.sols.domain.member.dto.MemberEditRequest;
import kr.sols.domain.member.dto.MemberListDto;
import kr.sols.domain.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    @Value("${KAKAO_ADMIN}") // 카카오 Admin Key를 application-prod.properties 또는 application.yml에 설정해두세요.
    private String kakaoAdminKey;
    private final WebClient webClient;
    private final MemberRepository memberRepository;
    private final SuggestionRepository suggestionRepository;

    @Transactional(readOnly = true)
    public MemberDto memberInfo(String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        return MemberDto.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public Map<String, String> additionalInfoCheck(String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        Map<String, String> response = new HashMap<>();

        if (member.getMemberTier() == null) {
            response.put("status", "incomplete");
        } else {
            response.put("status", "complete");
        }

        return response;
    }

    @Transactional
    public MemberDto memberEdit(MemberEditRequest request, String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        System.out.println("alplatform: "+request.getAlPlatform());
        member.updateMember(request);
        return MemberDto.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public Member findByMemberKeyOrThrow(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<MemberListDto> getAllMember() {
        List<Member> members = memberRepository.findAllByOrderByCreatedDateDesc();
        return members.stream()
                .map(MemberListDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMember(String memberKey) {
        Member targetMember = memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        String tid = targetMember.getTid();

        // 카카오 계정 연결 끊기
        unlinkKakaoAccount(tid);

        suggestionRepository.deleteAllByMemberMemberKey(memberKey);;
        // DB에서 삭제
        memberRepository.deleteByMemberKey(memberKey);
    }

    private void unlinkKakaoAccount(String targetId) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        // WebClient를 사용하여 POST 요청
        webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoAdminKey)
                .body(BodyInserters.fromFormData("target_id_type", "user_id")
                        .with("target_id", String.valueOf(targetId)))  // long을 Stri
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 비동기 처리 필요에 따라 조정
    }
}