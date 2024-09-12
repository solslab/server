package kr.sols.domain.suggestion.repository;

import io.lettuce.core.dynamic.annotation.Param;
import kr.sols.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SuggestionRepository extends JpaRepository<Suggestion, UUID> {

    // 리스트 조회
    @Query("SELECT s FROM Suggestion s " +
            "JOIN FETCH s.member m " +
            "JOIN FETCH s.position p " +
            "JOIN FETCH p.company c " +
            "ORDER BY s.createdDate DESC")
    List<Suggestion> findAllWithMembersPositionsAndCompanies();

    // 그냥 조회
    @Query("SELECT s FROM Suggestion s " +
            "JOIN FETCH s.member m " +
            "JOIN FETCH s.position p " +
            "JOIN FETCH p.company c " +
            "WHERE s.id = :id " +
            "ORDER BY s.createdDate DESC")
    Optional<Suggestion> findByIdWithMemberPositionAndCompany(UUID id);


    void deleteAllByMemberMemberKey(String memberKey);
}
