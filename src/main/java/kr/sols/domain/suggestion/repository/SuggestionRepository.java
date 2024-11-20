package kr.sols.domain.suggestion.repository;

import io.lettuce.core.dynamic.annotation.Param;
import kr.sols.domain.suggestion.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SuggestionRepository extends JpaRepository<Suggestion, UUID> {

    // 리스트 조회
    @Query("SELECT s FROM Suggestion s " +
            "JOIN FETCH s.member m " +
            "JOIN FETCH s.position p " +
            "JOIN FETCH p.company c " +
            "ORDER BY s.createdDate DESC")
    Page<Suggestion> findAllWithMembersPositionsAndCompanies(Pageable pageable);

    // 조회
    @Query("SELECT s FROM Suggestion s " +
            "JOIN FETCH s.member m " +
            "JOIN FETCH s.position p " +
            "JOIN FETCH p.company c " +
            "WHERE s.id = :id")
    Optional<Suggestion> findByIdWithMemberPositionAndCompany(UUID id);


    void deleteAllByMemberMemberKey(String memberKey);
}
