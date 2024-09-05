package com.toy2.shop29.qna.util;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        properties = {
                "file.upload.file-path=test/",
                "file.upload.temp-file-path=test/temp/",
        }
)
public class FileUploadHandlerTest {

    @Autowired
    private FileUploadHandler fileUploadHandler;


    @Value("${file.upload.temp-file-path}")
    private String TEMP_FILE_PATH;
    @Value("${file.upload.file-path}")
    private String FILE_PATH;
    @Value("${file.upload.test-file-path}")
    private String TEST_FILE_PATH;
    @Value("${file.upload.test-file-name}")
    private String TEST_FILE_NAME;
    @Autowired
    private FirebaseStorage firebaseStorage;


    @BeforeEach
    void setUp() {
        cleanFileStorage();
    }

    @AfterEach
    void after() {
        cleanFileStorage();
    }

    private void cleanFileStorage() {
        List<String> filePathList = List.of(FILE_PATH, TEMP_FILE_PATH);
        fileUploadHandler.deleteAllFilesFrom(filePathList);
    }

    @DisplayName("파일 저장 테스트 - 성공")
    @Test
    void saveFile() throws IOException, URISyntaxException {
        // 1단계 데이터 선택
        File imageFile = fileUploadHandler.downloadFile(TEST_FILE_PATH, TEST_FILE_NAME);
        String newFileName = fileUploadHandler.generateTimestampedFileName(TEST_FILE_NAME);

        // 2단계 데이터 처리
        String fileUrl = fileUploadHandler.saveFile(imageFile,newFileName);

        // 3단계 검증
        File file = fileUploadHandler.getFileFromUrl(fileUrl);
        assertTrue(file != null && file.exists());
    }

    @DisplayName("파일 삭제 테스트 - 성공")
    @Test
    void deleteFile() throws IOException {
        // 1단계 데이터 선택
        File imageFile = fileUploadHandler.downloadFile(TEST_FILE_PATH, TEST_FILE_NAME);
        String newFileName = fileUploadHandler.generateTimestampedFileName(TEST_FILE_NAME);

        String fileUrl = fileUploadHandler.saveFile(imageFile, newFileName);

        File file = fileUploadHandler.getFileFromUrl(fileUrl);
        assertTrue(file != null);
        assertTrue(file.exists());

        // 2단계 데이터 처리
        assertTrue(fileUploadHandler.deleteFile(newFileName, FILE_PATH));

        // 3단계 검증
        assertTrue(fileUploadHandler.downloadFile(FILE_PATH, newFileName) == null);
    }


    @DisplayName("이미지 파일 확인 테스트 - 성공")
    @Test
    void isImageFile(){
        // 1단계 데이터 선택
        String[] trueFileNames = {"test.jpg", "test.jpeg", "test.png", "test.gif"};
        String[] falseFileNames = {"test.txt", "test.doc", "test.xlsx", "test.ppt"};

        // 2단계 데이터 처리
        // 3단계 검증
        for(String trueFileName : trueFileNames) {
            boolean result = fileUploadHandler.isImageFile(trueFileName);
            assertTrue(result);
        }
        for(String falseFileName : falseFileNames) {
            boolean result = fileUploadHandler.isImageFile(falseFileName);
            assertTrue(!result);
        }

    }


    @DisplayName("이미지 width & height 추출 - 성공")
    @Test
    void getImageSize() throws IOException {
        // 1단계 데이터 선택
        File imageFile = fileUploadHandler.downloadFile(TEST_FILE_PATH, TEST_FILE_NAME);

        // 2단계 데이터 처리
        Integer[] sizeArr = fileUploadHandler.getImageSize(imageFile);

        // 3단계 검증
        assertTrue(sizeArr != null);
        assertTrue(sizeArr.length == 2);
        assertTrue(sizeArr[0] > 0);
        assertTrue(sizeArr[1] > 0);
    }

    @DisplayName("파일 사이즈 추출")
    @Test
    void getFileSize() throws IOException {
        // 1단계 데이터 선택
        File imageFile = firebaseStorage.downloadFile(TEST_FILE_PATH, TEST_FILE_NAME);

        // 2단계 데이터 처리
        long fileSize = fileUploadHandler.getFileSize(imageFile);

        // 3단계 검증
        assertTrue(fileSize > 0);
    }

    @DisplayName("파일명 치환 테스트 - 성공")
    @Test
    void sanitizeFileName(){
        // 1단계 데이터 선택
        String fileName = "test<test>test:test\"test/test\\test|test?test*test";

        // 2단계 데이터 처리
        String sanitizedFileName = fileUploadHandler.sanitizeFileName(fileName);

        // 3단계 검증
        assertTrue(sanitizedFileName != null);
        assertTrue(!sanitizedFileName.contains("<"));
        assertTrue(!sanitizedFileName.contains(">"));
        assertTrue(!sanitizedFileName.contains(":"));
        assertTrue(!sanitizedFileName.contains("\""));
        assertTrue(!sanitizedFileName.contains("/"));
        assertTrue(!sanitizedFileName.contains("\\"));
        assertTrue(!sanitizedFileName.contains("|"));
        assertTrue(!sanitizedFileName.contains("?"));
        assertTrue(!sanitizedFileName.contains("*"));
    }
}
