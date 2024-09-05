package com.toy2.shop29.qna.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Component
public class FileUploadHandler {

    @Value("${file.permit-mime-types}")
    private String[] PERMIT_MIME_TYPES;

    @Value("${file.max-file-size}")
    private long MAX_FILE_SIZE;

    @Value("${file.permit-extentions}")
    private String[] PERMIT_FILE_EXT_ARRAY;

    @Value("${file.image-extentions}")
    private String[] IMAGE_EXT_ARRAY;

    @Value("${file.upload.file-path}")
    private String FILE_PATH;

    @Value("${file.upload.temp-file-path}")
    private String TEMP_FILE_PATH;

    private final FirebaseStorage firebaseStorage;

    public FileUploadHandler(FirebaseStorage firebaseStorage) {
        this.firebaseStorage = firebaseStorage;
    }

//    @Value("${file.base-url}")
//    private String FILE_BASE_URL;

//    /**
//     * 파일 URL 생성. DB 테이블에 저장할 URL 생성 목적.
//     * @param fileName 파일명
//     * @return 파일 URL
//     */
//    public String createFileUrl(String fileName){
//        return FILE_BASE_URL + fileName;
//    }

    /**
     * MultipartFile 유효성 검사. 크기, MIME 타입, 확장자 검사
     * @param multipartFile 유효성 검사할 MultipartFile 객체
     * @return 유효성 검사 결과
     */
    public void validateMultipartFile(MultipartFile multipartFile) throws IllegalArgumentException {
        // 1. MultipartFile 이 null이거나 비어있다면, false 반환
        if(multipartFile == null || multipartFile.isEmpty()){
            throw new IllegalArgumentException("MultipartFile이 비어있습니다.");
        }

        // 2. 파일 크기 검사
        if(multipartFile.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException("파일 크기가 허용치를 초과했습니다.");
        }

        // 3. 파일 MIME 타입 검사
        String contentType = multipartFile.getContentType();
        if (!isPermitContentType(contentType)){
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }

        // 4. 파일 확장자 검사
        String fileName = multipartFile.getOriginalFilename();
        if (!isPermitExt(fileName)){
            throw new IllegalArgumentException("허용되지 않는 파일 확장자입니다.");
        }
    }

    private boolean isPermitExt(String fileName) {
        String fileExt = getExtension(fileName);
        boolean isPermitFileExt = false;
        for(String permitFileExt : PERMIT_FILE_EXT_ARRAY){
            if(permitFileExt.equalsIgnoreCase(fileExt)){
                isPermitFileExt = true;
                break;
            }
        }
        return isPermitFileExt;
    }

    private boolean isPermitContentType(String contentType) {
        boolean isPermitMimeType = false;
        for(String permitMimeType : PERMIT_MIME_TYPES){
            if(contentType.equals(permitMimeType)){
                isPermitMimeType = true;
                break;
            }
        }
        return isPermitMimeType;
    }

    /**
     * @return 저장된 파일의 URL 경로 반환
     */
    public String saveMultipartFile(MultipartFile multipartFile, String savedFileName) throws IOException {
        // 1. MultipartFile 이 null이거나 비어있다면 예외
        if(multipartFile == null || multipartFile.isEmpty()){
            throw new IllegalArgumentException("MultipartFile이 비어있습니다.");
        }

        // 2. 파일명 중복여부 체크
        if(firebaseStorage.isExist(FILE_PATH, savedFileName)){
            throw new IllegalStateException("이미 존재하는 파일명입니다.");
        }

        // Firebase Storage 에 파일 저장
        return firebaseStorage.uploadMultipartFile(multipartFile, FILE_PATH, savedFileName);
    }

    public String saveFile(File file, String savedFileName) throws IOException {
        // 2. 파일명 중복여부 체크
        if(firebaseStorage.isExist(FILE_PATH, savedFileName)){
            throw new IllegalStateException("이미 존재하는 파일명입니다.");
        }

        // Firebase Storage 에 파일 저장
        return firebaseStorage.uploadFile(file, FILE_PATH, savedFileName);
    }

    public File downloadFile(String filePath, String fileName) {
        try{
            return firebaseStorage.downloadFile(filePath, fileName);
        }catch (IOException e) {
            return null;
        }
    }

    /**
     * TEMP_FILE_PATH 경로에 파일 복사
     * @param fileName 복사할 파일명
     */
    public void copyFile(String fileName) throws RuntimeException{
        try {
            File originFile = firebaseStorage.downloadFile(FILE_PATH, fileName);
            firebaseStorage.uploadFile(originFile,TEMP_FILE_PATH,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 복사 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 백업 파일 복사
     * @param fileName 백업할 파일명
     */
    public void restoreFileFromBackup(String fileName) throws IOException{
        try {
            File tempFile = firebaseStorage.downloadFile(TEMP_FILE_PATH, fileName);
            firebaseStorage.uploadFile(tempFile,FILE_PATH,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 복구 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * FILE_PATH 경로에서 지정된 이름의 파일 삭제
     * @param fileName 삭제하고자 하는 파일명
     * @return 파일 삭제 성공 여부
     */
    public boolean deleteFile(String fileName, String filePath) {
        return firebaseStorage.deleteFile(filePath + fileName);
    }

    public String generateTimestampedFileName(String fileName) {
        // 랜덤숫자 + 특수문자가 제외된 파일명
        UUID uuid = UUID.randomUUID();
        return uuid + "_" + sanitizeFileName(fileName);
    }

    // 특정 폴더 內 파일 전체 삭제

    /**
     * 주어진 경로 內 모든 파일 삭제 <br/>
     * 주의: 폴더 내 모든 파일이 삭제됨 <br/>
     * <strong>테스트 목적으로만 사용할 것</strong>
     * @throws IOException
     */
    public void deleteAllFilesFrom(List<String> filePathList) {
        for(String filePathStr : filePathList){
            firebaseStorage.getFileNames(filePathStr).forEach(fileName -> {
                firebaseStorage.deleteFile(fileName);
            });
        }
    }

    /**
     * 파일명에 포함된 특수문자를 '_'로 치환 <br/>
     * 특수문자 : <, >, :, ", /, \, |, ?, *
     * @param fileName 치환하고자 하는 파일명
     * @return 치환된 파일명
     */
    public String sanitizeFileName(String fileName) {
        String sanitizedFileName = fileName;
        char[] invalidChars = {'<', '>', ':', '"', '/', '\\', '|', '?', '*'};

        for (char ch : invalidChars) {
            sanitizedFileName = sanitizedFileName.replace(ch, '_');
        }

        return sanitizedFileName;
    }

    /*
        파일 메타정보 추출
     */

    public File getFileFromUrl(String fileURL) {
        try{
            String fileName = extractFileNameFromUrl(fileURL);
            return firebaseStorage.downloadFile(FILE_PATH, fileName);
        }catch (IOException e) {
            return null;
        }
    }

    public String extractFileNameFromUrl(String fileURL) {
        String cleanUrl = fileURL.split("\\?")[0];
        String decodedFileName = cleanUrl.replace("%2F", "/");
        return decodedFileName.substring(decodedFileName.lastIndexOf("/") + 1);
    }

    /**
     * 파일명이 이미지 파일 확장자를 포함하는지 확인
     * @param fileName 파일명
     */
    public boolean isImageFile(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        for(String imageExt : IMAGE_EXT_ARRAY) {
            if(imageExt.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 이미지 파일의 width, height 추출
     * 이미지 파일이 아닐 경우, Integer[2]{null, null} 반환
     * @param imageFile
     * @return int[0] : width, int[1] : height
     */
    public Integer[] getImageSize(File imageFile){
        // 이미지 파일인지 확인
        if(!imageFile.exists() || !isImageFile(imageFile.getName())) {
            return new Integer[2];
        }

        // BufferedImage 메모리 해제 必
        BufferedImage image = null;
        try{
            image = ImageIO.read(imageFile);
            if(image == null) {
                return new Integer[2];
            }
            return new Integer[]{image.getWidth(), image.getHeight()};
        }catch (IOException e) {
            e.printStackTrace();
            return new Integer[2];
        }
        finally {
            if(image != null) {
                image.flush();
            }
        }
    }

    // 확장자 추출

    /**
     * 파일명으로 부터 확장자를 추출함
     * @param fileName 파일명
     * @return 확장자. ex) jpg, png, gif
     */
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 파일 크기 추출
     * @param file
     * @return 파일 크기(단위: byte). ex) 1024
     */
    public long getFileSize(File file) {
        return file.length();
    }
}
