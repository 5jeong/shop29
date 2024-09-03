package com.toy2.shop29.qna.util;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageException;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public String uploadFile(MultipartFile file, String path, String fileName) throws IOException, StorageException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        InputStream content = new ByteArrayInputStream(file.getBytes());
        String filePath = encodeFilePath(path + fileName);
        bucket.create(filePath, content, file.getContentType());
        String fileUrl = getFileUrl(firebaseBucket, filePath);
        return fileUrl;
    }

    /**
     * Firebase Storage에 저장된 파일을 삭제한다.
     * @param path : Firebase Storage 內 파일이 저장된 폴더 경로. ex) "prod/images/"
     * @param fileName : Firebase Storage 內 파일명. ex) "image.jpg"
     * @return : 파일 삭제 성공 여부
     */
    public boolean deleteFile(String path, String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        String filePath = encodeFilePath(path + fileName);
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

    private String encodeFilePath(String filePath){
        return filePath.replace("/", "%2F");
    }

    private String getFileUrl(String bucketName, String filePath){
        return new StringBuilder()
                .append("https://firebasestorage.googleapis.com/v0/b/")
                .append(bucketName)
                .append("/o/")
                .append(filePath)
                .append("?alt=media")
                .toString();
    }
}
