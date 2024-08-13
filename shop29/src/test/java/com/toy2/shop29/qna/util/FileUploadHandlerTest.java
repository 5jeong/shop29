package com.toy2.shop29.qna.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUploadHandlerTest {

    private FileUploadHandler fileUploadHandler = new FileUploadHandler();
    private final String FILE_NAME = "sample-img.PNG";
    private final String TEST_FILE_PATH = "static/test/";

    @BeforeEach
    void setUp() throws IOException {
        fileUploadHandler.deleteAllFiles();
    }

    @DisplayName("파일 저장 테스트 - 성공")
    @Test
    void saveFile() throws IOException, URISyntaxException {
        // 1단계 데이터 선택
        File imageFile = Paths.get(TEST_FILE_PATH + FILE_NAME).toFile();
        String newFileName = fileUploadHandler.generateTimestampedFileName(FILE_NAME);

        // 2단계 데이터 처리
        String saveFileName = fileUploadHandler.saveFile(imageFile,newFileName);

        // 3단계 검증
        assertTrue(saveFileName != null);
        assertTrue(fileUploadHandler.getFile(saveFileName).exists());
    }

    @DisplayName("파일 삭제 테스트 - 성공")
    @Test
    void deleteFile() throws IOException, URISyntaxException {
        // 1단계 데이터 선택
        File imageFile = Paths.get(TEST_FILE_PATH + FILE_NAME).toFile();
        String newFileName = fileUploadHandler.generateTimestampedFileName(FILE_NAME);

        String saveFileName = fileUploadHandler.saveFile(imageFile, newFileName);

        assertTrue(saveFileName != null);
        File file = fileUploadHandler.getFile(saveFileName);
        assertTrue(file != null);
        assertTrue(file.exists());

        // 2단계 데이터 처리
        fileUploadHandler.deleteFile(saveFileName);

        // 3단계 검증
        assertTrue(fileUploadHandler.getFile(saveFileName) == null);
    }

    @DisplayName("파일 복사 테스트 - 성공")
    @Test
    void copyFile() throws IOException {
        // 1단계 데이터 선택
        File imageFile = Paths.get(TEST_FILE_PATH + FILE_NAME).toFile();
        String newFileName = fileUploadHandler.generateTimestampedFileName(FILE_NAME);

        String saveFileName = fileUploadHandler.saveFile(imageFile, newFileName);

        assertTrue(saveFileName != null);
        File file = fileUploadHandler.getFile(saveFileName);
        assertTrue(file != null);
        assertTrue(file.exists());

        // 2단계 데이터 처리
        fileUploadHandler.copyFile(file);

        // 3단계 검증
        File copiedFile = Paths.get(FileUploadHandler.TEMP_FILE_PATH + file.getName()).toFile();
        assertTrue(copiedFile != null);
        assertTrue(copiedFile.exists());
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

    @DisplayName("파일 가져오기 테스트 - 성공")
    @Test
    void getFile() throws IOException {
        // 1단계 데이터 선택
        String fileName = FILE_NAME;
        Files.createFile(Paths.get(FileUploadHandler.FILE_PATH + fileName));

        // 2단계 데이터 처리
        File file = fileUploadHandler.getFile(fileName);

        // 3단계 검증
        assertTrue(file != null);
        assertTrue(file.exists());
    }

    @DisplayName("이미지 width & height 추출 - 성공")
    @Test
    void getImageSize() throws IOException {
        // 1단계 데이터 선택
        File imageFile = Paths.get(TEST_FILE_PATH + FILE_NAME).toFile();

        // 2단계 데이터 처리
        int[] sizeArr = fileUploadHandler.getImageSize(imageFile);

        // 3단계 검증
        assertTrue(sizeArr != null);
        assertTrue(sizeArr.length == 2);
        assertTrue(sizeArr[0] > 0);
        assertTrue(sizeArr[1] > 0);
    }

    @DisplayName("확장자 추출 - 성공")
    @Test
    void getExtension(){
        // 1단계 데이터 선택
        String fileName = FILE_NAME;

        // 2단계 데이터 처리
        String extension = fileUploadHandler.getExtension(fileName);

        // 3단계 검증
        assertTrue(extension != null);
        assertTrue("PNG".equalsIgnoreCase(extension));
    }

    @DisplayName("파일 사이즈 추출")
    @Test
    void getFileSize(){
        // 1단계 데이터 선택
        File imageFile = Paths.get(TEST_FILE_PATH + FILE_NAME).toFile();

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
