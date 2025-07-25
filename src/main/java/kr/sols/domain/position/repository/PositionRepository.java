package kr.sols.domain.position.repository;

import kr.sols.domain.position.entity.Position;
import kr.sols.domain.position.repository.projection.SupportLanguagesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    boolean existsByCompanyIdAndPositionName(UUID companyId, String positionName);
    List<Position> findAllByCompanyIdOrderByCreatedDateDesc(UUID companyId);
    void deleteAllByCompanyId(UUID companyId);
    long countByCompanyId(UUID companyId);
    Optional<SupportLanguagesProjection> findSupportLanguagesById(UUID positionId);
}
