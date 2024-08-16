package com.toy2.shop29.qna.controller;

import com.toy2.shop29.qna.service.attachment.AttachmentService;
import com.toy2.shop29.qna.util.FileUploadHandler;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    private AttachmentService attachmentService;
    private FileUploadHandler fileUploadHandler;

    public AttachmentController(AttachmentService attachmentService, FileUploadHandler fileUploadHandler) {
        this.attachmentService = attachmentService;
        this.fileUploadHandler = fileUploadHandler;
    }

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

    @GetMapping("/{fileName}")
    private ResponseEntity<Resource> downloadFile(@PathVariable(name = "fileName") String fileName) {
        Resource resource = attachmentService.getResourceFromFile(fileName);
        if(resource != null && resource.exists()){
            // MIME 타입 설정
            String mimeType = attachmentService.getMimeType(resource);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, mimeType);

            return new ResponseEntity(resource, httpHeaders, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
