package kr.sols.domain.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPageDto {
    private List<CompanyListDto> companies;
    private int totalElements; // 총 기업 수
    private int totalPages; // 총 페이지 수
    private int currentPage; // 현재 페이지 번호
    private int pageSize; // 페이지당 데이터 수
}

