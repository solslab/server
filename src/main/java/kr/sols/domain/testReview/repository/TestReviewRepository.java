package kr.sols.domain.testReview.repository;

import kr.sols.domain.testReview.entity.TestReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestReviewRepository extends JpaRepository<TestReview, UUID> {
    List<TestReview> findAllByOrderByCreatedDateDesc();
    Optional<TestReview> findByMemberKeyAndCompanyId(String memberKey, UUID companyId);
}
