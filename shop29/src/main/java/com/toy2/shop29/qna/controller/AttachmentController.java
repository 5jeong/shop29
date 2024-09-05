package com.toy2.shop29.qna.controller;

import com.toy2.shop29.qna.service.attachment.AttachmentService;
import com.toy2.shop29.qna.util.FileUploadHandler;
import com.toy2.shop29.qna.util.FirebaseStorage;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    private AttachmentService attachmentService;
    private FileUploadHandler fileUploadHandler;

    public AttachmentController(AttachmentService attachmentService, FileUploadHandler fileUploadHandler) {
        this.attachmentService = attachmentService;
        this.fileUploadHandler = fileUploadHandler;
    }

    /**
     * [POST] 첨부파일 업로드. 파일 저장소에 파일을 저장.
     * @param file 업로드할 파일. html form에서 name="file"로 전달받음. 한 건씩만 전달 가능
     * @return 저장된 파일에 접근할 수 있는 URL
     */
    @PostMapping()
    private ResponseEntity<String> saveMultipartFile(
            @RequestParam(value = "file",required = true) MultipartFile file) {
        try{
            // MultipartFile 유효성 검사
            fileUploadHandler.validateMultipartFile(file);
            String savedFileName = attachmentService.saveMultipartFile(file);
            return new ResponseEntity<>(savedFileName, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
