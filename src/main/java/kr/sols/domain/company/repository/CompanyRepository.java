package kr.sols.domain.company.repository;

import kr.sols.domain.company.dto.CompanyLogoDto;
import kr.sols.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCompanyName(String companyName);
    List<Company> findAll();
    Optional<Company> findById(UUID id);

    Optional<CompanyLogoDto> findCompanyLogoById(UUID id);
}
