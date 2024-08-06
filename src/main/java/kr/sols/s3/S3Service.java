package kr.sols.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;

import static kr.sols.exception.ErrorCode.FAIL_TO_CONVERT_FILE;

@Slf4j
    @Service
    public class S3Service{
        @Autowired
        private AmazonS3 amazonS3;

        @Value("${cloud.aws.s3.bucket}")
        private String bucket;

        private final String DIR_NAME = "company_logo";

        public String upload(String fileName, MultipartFile multipartFile, String extend) throws IOException { // dirName의 디렉토리가 S3 Bucket 내부에 생성됨

            File uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new S3Exception(FAIL_TO_CONVERT_FILE));
            return upload(fileName, uploadFile, extend);
        }

        private String upload(String fileName, File uploadFile, String extend) {
            fileName = UUID.randomUUID().toString();
            String uploadImageUrl = putS3(uploadFile, DIR_NAME + "/" + fileName + extend);
            removeNewFile(uploadFile);  // convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성)
            return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
        }

        private String putS3(File uploadFile, String fileName) {
            amazonS3.putObject(
                    new PutObjectRequest(bucket, fileName, uploadFile)
                            .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드
            );
            return amazonS3.getUrl(bucket, fileName).toString();
        }

        private void removeNewFile(File targetFile) {
            if(targetFile.delete()) {
                log.info("파일이 삭제되었습니다.");
            }else {
                log.info("파일이 삭제되지 못했습니다.");
            }
        }

        private Optional<File> convert(MultipartFile file) throws IOException {
            log.info(file.getOriginalFilename());
            File convertFile = new File(file.getOriginalFilename()); // 업로드한 파일의 이름
            if(convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
            return Optional.empty();
        }

        public ResponseEntity<byte[]> download(String fileName) throws IOException {
            S3Object awsS3Object = amazonS3.getObject(new GetObjectRequest(bucket, DIR_NAME + "/" + fileName));
            S3ObjectInputStream s3is = awsS3Object.getObjectContent();
            byte[] bytes = s3is.readAllBytes();

            String downloadedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", downloadedFileName);
            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        }

        public void deleteFile(String fileName) {
            try {
                String[] parts = fileName.split("/"); //파싱
                String key = DIR_NAME + "/" + parts[parts.length - 1];
                // 파일 삭제 요청
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));

                log.info("{} 파일이 S3에서 삭제되었습니다.", fileName);
            } catch (AmazonServiceException e) {
                log.error("S3에서 파일 삭제 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("S3에서 파일 삭제 중 오류 발생", e);
            }
        }

    }