package kr.sols.domain.company.repository;

import jakarta.transaction.Transactional;
import kr.sols.domain.company.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(properties = "spring.profiles.active=prod")
@Transactional
@Rollback(false)
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void testSaveAndRetrieveCompany() {
        List<String> companyList = List.of();

        for (String s : companyList) {
            if (companyRepository.existsByCompanyName(s)) {
                System.out.println(s + " - 이미 존재하는 기업, continue 처리");
                continue;
            }
            Company company = Company.builder()
                    .companyName(s)
                    .isPublic(false)
                    .build();
            Company savedCompany = companyRepository.save(company);

            // 확인
            assertNotNull(savedCompany);
            assertEquals(s, savedCompany.getCompanyName());
            assertEquals(false, savedCompany.isPublic());
        }
    }
}
