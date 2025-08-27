package kr.sols.domain.company.dto;

import kr.sols.common.file.LocalFileService;
import kr.sols.common.file.dto.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyLogoDto {
    private String companyLogo;

    public static CompanyLogoDto from(UploadFile uploadFile, LocalFileService fileService) {
        String logoUrl = null;
        if (uploadFile != null && uploadFile.getStoreFilename() != null) {
            logoUrl = fileService.getCompanyLogoFullPath(uploadFile.getStoreFilename());
        }
        return new CompanyLogoDto(logoUrl);
    }
}
