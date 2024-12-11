package kr.sols.domain.testReview.service;

import kr.sols.domain.company.dto.CompanyDetailDto;
import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.repository.CompanyRepository;
import kr.sols.domain.company.service.CompanyService;
import kr.sols.domain.member.dto.MemberListDto;
import kr.sols.domain.member.dto.MemberPageDto;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.member.repository.MemberRepository;
import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.testReview.dto.*;
import kr.sols.domain.testReview.entity.TestReview;
import kr.sols.domain.testReview.exception.TestReviewException;
import kr.sols.domain.testReview.repository.TestReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TestReviewService {
    private final MemberRepository memberRepository;
    private final TestReviewRepository testReviewRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    @Transactional
    public TestReviewCreatedResponse createTestReview(TestReviewRequest request, String memberKey) {
        Member member = memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // companyId 입력이 됐을 때
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId()).orElse(null);
            if (request.getCompanyId() != null & !Objects.equals(company.getCompanyName(), request.getCompanyName())) {
                throw new TestReviewException(TR_COMPANY_NOT_MATCH);
            }
        }

        if (member.getMemberTier() == null){
            throw new TestReviewException(MEMBER_TIER_UNDEFINED);
        }

        TestReview testReview = TestReview.builder()
                .memberKey(memberKey)
                .memberName(member.getName())
                .companyId(request.getCompanyId())
                .companyName(request.getCompanyName())
                .memberTier(member.getMemberTier())
                .trYear(request.getTrYear())
                .trPosition(request.getTrPosition())
                .trCareer(request.getTrCareer())
                .trProblemNum(request.getTrProblemNum())
                .trSolvedNum(request.getTrSolvedNum())
                .trPassStatus(request.getTrPassStatus())
                .trProblemType(request.getTrProblemType())
                .trComment(request.getTrComment())
                .build();

        TestReview savedTestReview = testReviewRepository.save(testReview);
        if (request.getCompanyId() != null) {
            companyService.updateCompanyStatus(request.getCompanyId());
        }

        return new TestReviewCreatedResponse(savedTestReview.getId());
    }

    @Transactional(readOnly = true)
    public TestReviewPageDto getAllTestReviews(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<TestReview> trPage = testReviewRepository.findAllByOrderByCreatedDateDesc(pageable);
        int totalPageNum = trPage.getTotalPages();
        int currentPageNum = trPage.getNumber() + 1;

        if (currentPageNum > totalPageNum || currentPageNum < 1) throw new TestReviewException(PAGE_NOT_FOUND);

        List<TestReviewListDto> trs = trPage.getContent()
                .stream()
                .map(TestReviewListDto::fromEntity)
                .toList();

        return TestReviewPageDto.builder()
                .testReviews(trs)
                .totalElements((int) trPage.getTotalElements())
                .totalPages(totalPageNum)
                .currentPage(currentPageNum)
                .pageSize(pageSize)
                .build();
    }

    @Transactional(readOnly = true)
    public TestReviewDto getTestReview(UUID trId) {
        TestReview tr = testReviewRepository.findById(trId).orElseThrow(() -> new TestReviewException(TEST_REVIEW_NOT_FOUND));

        return TestReviewDto.fromEntity(tr);
    }

    @Transactional(readOnly = true)
    public List<TestReivewDataLabDto> getTestReviewsByCompanyId(UUID companyId, Authentication auth) {
        List<TestReview> trs = testReviewRepository.findAllByCompanyId(companyId);
        if (trs.isEmpty()) {
            throw new TestReviewException(EMPTY_DATALAB);
        }
        else if (auth == null) { // 비회원 접근제한
            throw new TestReviewException(NO_ACCESS);
        }

        String memberKey = auth.getName();
        // 인증된 사용자가 해당 회사에 리뷰를 작성했는지 검사
        boolean isReviewWritten = testReviewRepository.existsByMemberKey(memberKey);
        if (!isReviewWritten) {
            throw new TestReviewException(NO_ACCESS);
        }


        return trs.stream()
                .map(TestReivewDataLabDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public long countTestReviewsByCompanyId(UUID companyId) {
        return testReviewRepository.countByCompanyId(companyId);
    }
}
