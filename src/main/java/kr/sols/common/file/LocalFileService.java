package kr.sols.common.file;

import kr.sols.common.file.dto.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileService {

    @Value("${file.dir}")
    private String fileDir;

    // 파일 저장 로직
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFileName(originalFilename);

        // 디렉토리 존재 확인 및 생성
        File directory = new File(fileDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Path 사용해서 안전하게 경로 결합
        Path targetPath = Paths.get(fileDir, storeFilename);
        multipartFile.transferTo(targetPath.toFile());

        return new UploadFile(originalFilename, storeFilename);
    }

    // 파일 삭제 로직
    public void deleteFile(String storeFilename) {
        if (storeFilename == null || storeFilename.isBlank()) {
            return;
        }

        Path targetPath = Paths.get(fileDir, storeFilename);
        File file = targetPath.toFile();
        if (file.exists()) {
            file.delete();
        }
    }

    // UUID 기반 파일명 생성
    private String createStoreFileName(String originalFilename) {
        String ext = "";
        int pos = originalFilename.lastIndexOf(".");
        if (pos != -1) {
            ext = originalFilename.substring(pos + 1);
        }
        String uuid = UUID.randomUUID().toString();
        return uuid + (ext.isEmpty() ? "" : "." + ext);
    }
}
