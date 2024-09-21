package kr.sols.domain.company.repository;

import kr.sols.domain.company.dto.CompanyDetailDto;
import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.company.dto.CompanyLogoDto;
import kr.sols.domain.company.dto.CompanyRequestDto;
import kr.sols.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCompanyName(String companyName);
    List<Company> findAllByOrderByCompanyNameAsc();;
    Optional<Company> findById(UUID id);
    List<Company> findByCompanyNameStartingWith(String searchTerm);

    @Query(value = "SELECT * FROM company ORDER BY RAND() LIMIT 15", nativeQuery = true)
    List<Company> findRandomCompaniesForHome();

}