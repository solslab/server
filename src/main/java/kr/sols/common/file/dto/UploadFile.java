package kr.sols.common.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFile {
    private String originalFilename;
    private String storeFilename;
}