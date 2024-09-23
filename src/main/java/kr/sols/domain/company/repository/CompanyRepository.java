package kr.sols.domain.company.repository;

import io.lettuce.core.dynamic.annotation.Param;
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

    @Query(value = "SELECT * FROM company c " +
            "WHERE c.company_name LIKE CONCAT(:searchTerm, '%') " +
            "OR EXISTS (SELECT 1 FROM JSON_TABLE(c.search_terms, '$[*]' COLUMNS(term VARCHAR(255) PATH '$')) AS jt " +
            "WHERE jt.term LIKE CONCAT(:searchTerm, '%')) " +
            "ORDER BY c.company_name",
            nativeQuery = true)
    List<Company> searchCompanyByTerm(@Param("searchTerm") String searchTerm);


    @Query(value = "SELECT * FROM company ORDER BY RAND() LIMIT 15", nativeQuery = true)
    List<Company> findRandomCompaniesForHome();

}