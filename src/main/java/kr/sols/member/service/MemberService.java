package kr.sols.member.service;
import static kr.sols.exception.ErrorCode.MEMBER_NOT_FOUND;
import kr.sols.member.domain.Member;
import kr.sols.member.dto.MemberDto;
import kr.sols.member.dto.MemberEditRequest;
import kr.sols.member.exception.MemberException;
import kr.sols.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto memberInfo(String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        return MemberDto.fromEntity(member);
    }

    @Transactional
    public MemberDto memberEdit(MemberEditRequest request, String memberKey) {
        Member member = findByMemberKeyOrThrow(memberKey);
        member.updateMember(request);
        return MemberDto.fromEntity(member);
    }

    public Member findByMemberKeyOrThrow(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}