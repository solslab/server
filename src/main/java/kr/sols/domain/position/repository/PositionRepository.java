package kr.sols.domain.position.repository;

import kr.sols.domain.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    boolean existsByCompanyIdAndPositionName(UUID companyId, String positionName);
    List<Position> findAllByCompanyIdOrderByPositionNameAsc(UUID companyId);
}
