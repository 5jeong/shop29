package com.toy2.shop29.qna.util;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseStorage {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    /**
     * Firebase Storage에 파일을 업로드한다.
     * @param file : 컨트롤러에서 전달받은 MultipartFile 객체
     * @param path : Firebase Storage 內 파일이 저장될 폴더 경로. ex) "prod/images/"
     * @param fileName : Firebase Storage 內 파일명. ex) "image.jpg"
     * @return : 업로드된 파일의 URL
     * @throws IOException : MultipartFile 객체의 byte[] 변환 시 발생가능
     * @throws StorageException : Firebase Storage에 파일 업로드 시 발생가능
     */
    public String uploadMultipartFile(MultipartFile file, String path, String fileName) throws IOException, StorageException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        InputStream content = new ByteArrayInputStream(file.getBytes());
        String filePath = path + fileName;
        bucket.create(filePath, content, file.getContentType());
        String fileUrl = getFileUrl(firebaseBucket, filePath);
        return fileUrl;
    }

    public String uploadFile(File file, String path, String fileName) throws IOException, StorageException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        InputStream content = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
        String filePath = path + fileName;
        bucket.create(filePath, content, Files.probeContentType(file.toPath()));
        String fileUrl = getFileUrl(firebaseBucket, filePath);
        return fileUrl;
    }

    /**
     * Firebase Storage에서 파일을 다운로드
     * @param path : Firebase Storage 內 파일이 저장된 폴더 경로. ex) "prod/images/"
     * @param fileName : Firebase Storage 內 파일명. ex) "image.jpg"
     * @return : 다운로드된 파일
     * @throws IOException
     */
    public File downloadFile(String path, String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        String filePath = path + fileName;
        Blob blob = bucket.get(filePath);
        if(blob == null){
            return null;
        }

        String extension = fileName.substring(fileName.lastIndexOf("."));
        File tempFile = File.createTempFile("temp", extension);
        blob.downloadTo(tempFile.toPath());
        return tempFile;
    }

    public boolean isExist(String path, String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        String filePath = path + fileName;
        Blob blob = bucket.get(filePath);
        return blob != null;
    }

    /**
     * Firebase Storage에 저장된 파일을 삭제한다.
     * @param filePath : Firebase Storage 內 파일이 저장된 폴더 경로. ex) "prod/images/"
     * @return : 파일 삭제 성공 여부
     */
    public boolean deleteFile(String filePath) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        Blob blob = bucket.get(filePath);
        try{
            if(blob != null){
                blob.delete();
                return true;
            }else{
                return false;
            }
        }catch (StorageException e){
            return false;
        }
    }

    /**
     * Firebase Storage에 저장된 파일 목록을 조회한다.
     * @param path : Firebase Storage 內 파일이 저장된 폴더 경로. ex) "prod/images/"
     * @return : 파일명 목록. ex) ["prod/images/image1.jpg", "prod/images/image2.jpg", ...]
     */
    public List<String> getFileNames(String path) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        String filePath = path;
        Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(filePath));
        List<String> fileNameList = new ArrayList<>();
        for(Blob blob : blobs.iterateAll()){
            if(blob.getName().equals(filePath)) continue;
            fileNameList.add(blob.getName());
        }
        return fileNameList;
    }

    private String encodeFilePath(String filePath){
        return filePath.replace("/", "%2F");
    }

    private String getFileUrl(String bucketName, String filePath){
        return new StringBuilder()
                .append("https://firebasestorage.googleapis.com/v0/b/")
                .append(bucketName)
                .append("/o/")
                .append(encodeFilePath(filePath))
                .append("?alt=media")
                .toString();
    }
}
