package kr.sols.domain.testReview.service;

import kr.sols.domain.company.dto.CompanyDetailDto;
import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.repository.CompanyRepository;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.testReview.dto.TestReviewCreatedResponse;
import kr.sols.domain.testReview.dto.TestReviewDto;
import kr.sols.domain.testReview.dto.TestReviewListDto;
import kr.sols.domain.testReview.dto.TestReviewRequest;
import kr.sols.domain.testReview.entity.TestReview;
import kr.sols.domain.testReview.exception.TestReviewException;
import kr.sols.domain.testReview.repository.TestReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TestReviewService {
    private final MemberRepository memberRepository;
    private final TestReviewRepository testReviewRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public TestReviewCreatedResponse createTestReview(TestReviewRequest request, UUID companyId, String memberKey) {
        Member member = memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        if (member.getMemberTier() == null){
            throw new TestReviewException(MEMBER_TIER_UNDEFINED);
        }

        TestReview testReview = TestReview.builder()
                .memberKey(memberKey)
                .memberName(member.getName())
                .companyId(companyId)
                .companyName(company.getCompanyName())
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

    @Transactional(readOnly = true)
    public List<TestReviewListDto> getAllTestReviews() {
        List<TestReview> trs = testReviewRepository.findAllByOrderByCreatedDateDesc();

        return trs.stream()
                .map(TestReviewListDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestReviewDto getTestReview(UUID trId) {
        TestReview tr = testReviewRepository.findById(trId).orElseThrow(() -> new TestReviewException(TEST_REVIEW_NOT_FOUND));

        return TestReviewDto.fromEntity(tr);
    }
}
