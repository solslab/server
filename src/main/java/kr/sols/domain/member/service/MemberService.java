package kr.sols.domain.member.service;
import static kr.sols.exception.ErrorCode.MEMBER_NOT_FOUND;

import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.dto.MemberDto;
import kr.sols.domain.member.dto.MemberEditRequest;
import kr.sols.domain.member.dto.MemberListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDto memberInfo(String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        return MemberDto.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public Map<String, String> additionalInfoCheck(String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        Map<String, String> response = new HashMap<>();

        if (member.getMemberTier() == null ||
                member.getPreferIndustries() == null ||
                member.getPreferLanguages() == null) {
            response.put("status", "incomplete");
        } else {
            response.put("status", "complete");
        }

        return response;
    }

    @Transactional
    public MemberDto memberEdit(MemberEditRequest request, String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
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
}