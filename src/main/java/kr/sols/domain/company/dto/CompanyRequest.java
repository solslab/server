package kr.sols.domain.company.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {
    private String companyName;
    private List<String> industryType;
    private List<String> searchTerms;
    @JsonProperty("is_public")
    private boolean isPublic;
}