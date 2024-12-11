package kr.sols.domain.testReview.repository;

import kr.sols.domain.testReview.entity.TestReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestReviewRepository extends JpaRepository<TestReview, UUID> {
    Page<TestReview> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Optional<TestReview> findByMemberKeyAndCompanyId(String memberKey, UUID companyId);
    List<TestReview> findAllByCompanyId(UUID companyId);
    boolean existsByMemberKey(String memberKey);
    long countByCompanyId(UUID companyId);
}
