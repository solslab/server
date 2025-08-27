package kr.sols.common.file;

import kr.sols.common.file.dto.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service // 바로 빈으로 등록
public class LocalFileService {

    @Value("${file.dir}")
    private String fileDir;

    public String getCompanyLogoFullPath(String filename) {
        return "/images/" + filename;
    }

    // 파일 저장 로직
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFileName(originalFilename);

        File directory = new File(fileDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일을 실제 경로에 저장
        multipartFile.transferTo(new File(fileDir + storeFilename));

        return new UploadFile(originalFilename, storeFilename);
    }

    // 파일 삭제 로직
    public void deleteFile(String storeFilename) {
        if (storeFilename == null || storeFilename.isBlank()) {
            return;
        }
        File file = new File(fileDir + storeFilename);
        if (file.exists()) {
            file.delete();
        }
    }

    private String createStoreFileName(String originalFilename) {
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
}