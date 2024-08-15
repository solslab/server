package kr.sols.domain.testReview.repository;

import kr.sols.domain.testReview.entity.TestReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestReviewRepository extends JpaRepository<TestReview, UUID> {
}
