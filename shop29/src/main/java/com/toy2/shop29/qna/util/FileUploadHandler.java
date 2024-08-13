package com.toy2.shop29.qna.util;

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

@Component
public class FileUploadHandler {

    /**
     * 허용되는 이미지 확장자
     */
    public static final String[] IMAGE_EXT = {"jpg", "jpeg", "png", "gif"};

    /**
     * 파일 저장 경로 - 프로젝트 루트 경로를 기준으로함
     */
    public static final String FILE_PATH = "static/uploads/";

    /**
     * 임시파일 저장 경로 - 프로젝트 루트 경로를 기준으로함
     */
    public static final String TEMP_FILE_PATH = "static/temp/";

    /**
     * 파일 URL - 브라우저에서 접근 가능한 URL의 기준점
     */
    public static final String FILE_BASE_URL = "http://localhost:8080/uploads/";

    /**
     * 파일 URL 생성. DB 테이블에 저장할 URL 생성 목적.
     * @param fileName 파일명
     * @return 파일 URL
     */
    public String createFileUrl(String fileName){
        return FILE_BASE_URL + fileName;
    }

    /**
     * 파일 저장
     * @param file 저장할 파일. MultiPartFile로 받은 파일을 File로 변환하여 사용
     * @return 저장된 파일명
     * @throws IOException
     * @throws URISyntaxException
     */
    public String saveFile(File file, String savedFileName) throws IOException, IllegalStateException {
        if(file == null || !file.exists()) {
            throw new IllegalStateException("파일이 존재하지 않습니다.");
        }

        Path path = Paths.get(FILE_PATH + savedFileName);

        // 파일명 중복시, 예외 던짐
        if(Files.exists(path)) {
            throw new IllegalStateException("이미 존재하는 파일명입니다.");
        }

        Files.copy(file.toPath(), path);
        return savedFileName;
    }

    /**
     * TEMP_FILE_PATH 경로에 파일 복사
     * @param originFile 복사할 파일
     */
    public void copyFile(File originFile) throws IOException{
        Path originPath = originFile.toPath();
        Path copiedPath = Paths.get(TEMP_FILE_PATH + originFile.getName());

        try {
            Files.copy(originPath, copiedPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 백업 파일 복사
     * @param fileName 백업할 파일명
     */
    public void restoreFileFromBackup(String fileName) throws IOException{
        Path backupPath = Paths.get(TEMP_FILE_PATH + fileName);
        Path restorePath = Paths.get(FILE_PATH + fileName);

        try {
            Files.copy(backupPath, restorePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * FILE_PATH 경로에서 지정된 이름의 파일 삭제
     * @param fileName 삭제하고자 하는 파일명
     * @throws IOException - 파일이 존재하지 않거나, 삭제 실패시 발생
     */
    public void deleteFile(String fileName) throws IOException {
        Path path = Paths.get(FILE_PATH + fileName);
        Files.delete(path);
    }

    public String generateTimestampedFileName(String fileName) {
        // 현재시각 + 특수문자가 제외된 파일명
        return System.currentTimeMillis() + "_" + sanitizeFileName(fileName);
    }

    // 특정 폴더 內 파일 전체 삭제

    /**
     * FILE_PATH 경로 內 모든 파일 삭제 <br/>
     * 주의: 폴더 내 모든 파일이 삭제됨 <br/>
     * <strong>테스트 목적으로만 사용할 것</strong>
     * @throws IOException
     */
    public void deleteAllFiles() throws IOException {
        Path path = Paths.get(FILE_PATH);
        Files.list(path).forEach(p -> {
            try {
                Files.delete(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * MultipartFile을 File로 변환 <br/>
     * MultipartFile은 Form에서 전송된 파일을 받는 객체
     * @param multipartFile 변환하고자 하는 MultipartFile 객체
     * @return 변환된 File 객체
     * @throws IllegalArgumentException multipartFile이 존재하지 않을 경우
     * @throws IOException
     */
    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
        File file = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
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

    /**
     * 파일명으로 파일을 찾아 File 객체 생성 <br/>
     * 파일경로는 `FILE_PATH : 프로젝트루트/static/uploads`를 따름
     * @param fileName 찾고자하는 실제 파일명
     * @return File을 반환, 파일이 존재하지 않을 경우 null 반환
     */
    public File getFile(String fileName) {
        Path path = Paths.get(FILE_PATH + fileName);
        if(Files.exists(path)){
            return path.toFile();
        }else{
            return null;
        }
    }

    /**
     * 파일명이 이미지 파일 확장자를 포함하는지 확인
     * @param fileName 파일명
     */
    public boolean isImageFile(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        for(String imageExt : IMAGE_EXT) {
            if(imageExt.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 이미지 파일의 width, height 추출
     * @param imageFile
     * @return int[0] : width, int[1] : height
     * @throws IOException
     * @throws IllegalArgumentException 파일이 존재하지 않거나, 이미지 파일이 아닐 경우
     */
    public int[] getImageSize(File imageFile) throws IOException  {
        if(!imageFile.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
        // 이미지 파일인지 확인
        if(!isImageFile(imageFile.getName())) {
            throw new IllegalArgumentException("이미지 파일이 아닙니다.");
        }

        // BufferedImage 메모리 해제 必
        BufferedImage image = null;
        try{
            image = ImageIO.read(imageFile);
            return new int[]{image.getWidth(), image.getHeight()};
        }finally {
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
