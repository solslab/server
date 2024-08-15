package kr.sols.domain.testReview.service;

import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.testReview.dto.TestReviewCreatedResponse;
import kr.sols.domain.testReview.dto.TestReviewRequest;
import kr.sols.domain.testReview.entity.TestReview;
import kr.sols.domain.testReview.exception.TestReviewException;
import kr.sols.domain.testReview.repository.TestReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static kr.sols.exception.ErrorCode.MEMBER_NOT_FOUND;
import static kr.sols.exception.ErrorCode.MEMBER_TIER_UNDEFINED;

@Service
@RequiredArgsConstructor
public class TestReviewService {
    private final MemberRepository memberRepository;
    private final TestReviewRepository testReviewRepository;

    @Transactional
    public TestReviewCreatedResponse createTestReview(TestReviewRequest request, UUID companyId, String memberKey) {
        Member member = memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        if (member.getMemberTier() == null){
            throw new TestReviewException(MEMBER_TIER_UNDEFINED);
        }

        TestReview testReview = TestReview.builder()
                .memberKey(memberKey)
                .companyId(companyId)
                .memberTier(member.getMemberTier())
                .trYear(request.getTrYear())
                .trPosition(request.getTrPostion())
                .trCareer(request.getTrCareer())
                .trProblemNum(request.getTrProblemNum())
                .trSolvedNum(request.getTrSolvedNum())
                .trPassStatus(request.getTrPassStatus())
                .trProblemType(request.getTrProblemType())
                .trComment(request.getTrComment())
                .build();

        TestReview savedTestReview = testReviewRepository.save(testReview);
        return new TestReviewCreatedResponse(savedTestReview.getId());
    }
}
