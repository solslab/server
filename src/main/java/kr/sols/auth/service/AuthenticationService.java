package kr.sols.auth.service;

import static kr.sols.exception.ErrorCode.MEMBER_NOT_FOUND;
import static kr.sols.exception.ErrorCode.NO_ACCESS;

import kr.sols.auth.exception.AuthException;
import kr.sols.member.domain.Member;
import kr.sols.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;

    public Member getMemberOrThrow(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));
    }

    public void checkAccess(String memberKey, Member member) {
        if (!member.getMemberKey().equals(memberKey)) {
            throw new AuthException(NO_ACCESS);
        }
    }
}