package com.toy2.shop29.qna.service;

import com.toy2.shop29.qna.domain.AttachmentDto;
import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.qna.service.attachment.AttachmentService;
import com.toy2.shop29.qna.util.FileUploadHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttachmentServiceTest {

    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    QnaDao qnaDao;
    @Autowired
    AttachmentDao attachmentDao;
    @Autowired
    AttachmentService attachmentService;

    // 강제로 IOException 을 발생시켜, 롤백 처리를 테스트하기 위함
    @SpyBean
    FileUploadHandler fileUploadHandler;

    private final String TEST_FILE_NAME = "sample-img.PNG";
    private final String TEST_FILE_PATH = "static/test/";

    private QnaDto qnaDto;
    private String ADMIN_ID = "admin";

    /*
        * 테스트 시나리오
        - createAttachments(int qnaId, List<File> files) 메서드 테스트
            1. 성공 - 파일 저장 성공 & DB에 저장 성공
            2. 예외 - files가 null이거나, 비어있을 경우
            3. 예외 - qnaId에 해당하는 문의글이 존재하지 않을 경우
            4. 롤백 & 예외 - 파일 저장 중 예외 발생 시, 기 저장된 파일들 삭제
        - deleteAttachmentsBy(int qnaId) 메서드 테스트
            1. 성공 - 파일 삭제 성공 & DB 테이블 SoftDelete 성공 & 백업파일 삭제 성공
            2. 예외 - qnaId에 해당하는 문의글이 존재하지 않을 경우
            3. 롤백 & 예외 - 백업파일 생성 중 예외 발생시
            4. 파일 복구 & 예외 - 첨부파일 삭제 중 예외 발생시, 백업파일을 사용하여 파일 복구
     */

    @BeforeEach
    void before() throws IOException {
        // DB 테이블 데이터 초기화
        attachmentDao.deleteAll();
        qnaDao.deleteAll();
        qnaTypeDao.deleteAll();
        parentQnaTypeDao.deleteAll();

        // 파일 저장소 초기화
        fileUploadHandler.deleteAllFiles();

        // 테스트 데이터 생성
        // 1. 부모 문의유형 등록
        ParentQnaTypeDto parentQnaTypeDto = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("부모문의유형1")
                .description("부모문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(parentQnaTypeDao.insert(parentQnaTypeDto) == 1);


        // 2. 문의유형 등록
        QnaTypeDto qnaTypeDto = QnaTypeDto.builder()
                .qnaTypeId("QNA_TYPE_1")
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("문의유형1")
                .description("문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaTypeDao.insert(qnaTypeDto) == 1);

        // 3. 문의글 등록
        QnaDto qDto = QnaDto.builder()
                .userId(ADMIN_ID)
                .qnaTypeId("QNA_TYPE_1")
                .title("문의글 제목")
                .content("문의글 내용")
                .build();
        assertTrue(qnaDao.insert(qDto) == 1);
        qnaDto = qDto;
    }

    File createTestFile() {
        return Paths.get(TEST_FILE_PATH + TEST_FILE_NAME).toFile();
    }

    @DisplayName("첨부파일 여러건 저장 - 성공 - 파일 저장 & DB에 성공")
    @Test
    void createAttachments_1() throws IOException {
        // 1단계 데이터 선택
        // 1-1. qnaId에 해당하는 문의글 생성 -> init()에서 수행
        // 1-2. 첨부파일에 해당하는 File 리스트 생성
        List<File> files = new LinkedList<>();
        int fileCount = 2;
        for(int i = 0; i < fileCount; i++){
            files.add(createTestFile());
        }

        // 2단계 데이터 처리 -> 첨부파일들 저장
        attachmentService.createAttachments(qnaDto.getQnaId(), files);

        // 3단계 검증
        // 3-1. DB의 Attachment 테이블에 데이터가 저장되었는지 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAll(null);
        assertTrue(savedAttachments != null);
        assertTrue(savedAttachments.size() == files.size());

        // 3-2. Attachment 레코드에 첨부파일 메타정보가 등록되었는지 확인
        for(AttachmentDto attachmentDto : savedAttachments){
            // 문의글 ID 확인
            assertTrue(attachmentDto.getQnaId().equals(qnaDto.getQnaId()));
            // 첨부파일 이름 동록 여부 확인
            assertTrue(attachmentDto.getFileName() != null);
            // 첨부파일 사이즈, 너비, 높이, 확장자, 파일경로 확인
            assertTrue(attachmentDto.getSize() > 0);
            assertTrue(attachmentDto.getWidth() > 0);
            assertTrue(attachmentDto.getHeight() > 0);
            assertTrue(fileUploadHandler.getExtension(TEST_FILE_NAME).equalsIgnoreCase(attachmentDto.getExtension()));
            assertTrue(attachmentDto.getFilePath() != null);
        }

        // 3-3. 파일 저장소에 파일이 저장되었는지 확인
        for(AttachmentDto attachmentDto : savedAttachments){
            File foundFile = fileUploadHandler.getFile(attachmentDto.getFileName());
            assertTrue(foundFile != null);
            assertTrue(foundFile.exists());
        }
    }

    @DisplayName("첨부파일 여러건 저장 - 예외 - files가 null이거나, 비어있을 경우")
    @Test
    void createAttachments_2(){
        // 1단계 데이터 선택
        final List<File> files_1 = null;

        // 2단계 데이터 처리 & 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            attachmentService.createAttachments(qnaDto.getQnaId(), files_1);
        });

        // 1단계 데이터 선택
        final List<File> files_2 = new LinkedList<>();

        // 2단계 데이터 처리 & 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            attachmentService.createAttachments(qnaDto.getQnaId(), files_2);
        });
    }

    @DisplayName("첨부파일 여러건 저장 - 예외 - qnaId에 해당하는 문의글이 존재하지 않을 경우")
    @Test
    void createAttachments_3(){
        // 1단계 데이터 선택 -> 존재하지 않는 qnaId 지정
        int qnaId = 9999;
        assertTrue(qnaDao.select(qnaId, false) == null);
        List<File> files = new LinkedList<>();
        files.add(createTestFile());

        // 2단계 데이터 처리 & 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            attachmentService.createAttachments(qnaId, files);
        });
    }

    @DisplayName("첨부파일 여러건 저장 - 롤백 & 예외 - 파일 저장 중 예외 발생 시, 트랜잭션 롤백 및 '앞서 저장된 첨부파일' 삭제")
    @Test
    void createAttachments_4() throws IOException {
        // 1단계 데이터 선택
        // 1-1. 트랜잭션 시작 전 데이터 저장 -> 테이블 레코드 및 파일 저장소에 파일 저장
        for(int i = 0; i < 2; i++){
            File file = createTestFile();
            attachmentService.createAttachments(qnaDto.getQnaId(), List.of(file));
        }
        // 1-1-1. Attachment 테이블에 레코드들 기록
        List<AttachmentDto> beforeAttachments = attachmentDao.selectAll(null);
        // 1-1-2. 파일 저장소의 파일목록 기록
        File[] beforeFileArr = Paths.get(FileUploadHandler.FILE_PATH).toFile().listFiles();
        // 1-2. 트랜잭션에서 사용될 첨부파일 리스트 생성
        List<File> files = new LinkedList<>();
        files.add(createTestFile());
        files.add(createTestFile());


        // 2단계 데이터 처리
        // 2-1. 일부 파일들을 저장한 이후 예외를 발생시켜,
        // 예외발생 시 앞서 저장된 파일들이 삭제되는지 확인하기 위함
        final int[] callCnt = {0}; // 메서드 호출 횟수
        doAnswer(invocation -> {
            callCnt[0]++; // 메서드 호출 횟수 카운트

            File file = invocation.getArgument(0); // 첫번째 매개변수
            String fileName = invocation.getArgument(1); // 두번째 매개변수

            if(callCnt[0] >= files.size()){ // 마지막 호출 시 예외 발생
                throw new IOException("파일 저장 중 예외 발생");
            }else{
                // 마지막 호출이 아니라면, 파일을 정상적으로 저장
                return fileUploadHandler.saveFile(file, fileName);
            }
            // fileUploadHandelr 객체의 saveFile() 메서드가 호출될 때, doAnswer() 메서드가 동작함
        }).when(fileUploadHandler).saveFile(any(File.class), anyString());

        // 2-2. 첨부파일 저장 메서드 호출 및 예외 발생 여부 확인
        assertThrows(IOException.class, () -> {
            attachmentService.createAttachments(qnaDto.getQnaId(), files);
        });

        // 3단계 검증
        // 3-1. DB에 저장된 레코드가 롤백되었는지 확인
        List<AttachmentDto> seletedAttachments = attachmentDao.selectAll(null);
        // 3-1-1. 테이블의 기존 레코드 수와 동일한지 확인
        assertTrue(seletedAttachments.size() == beforeAttachments.size());
        // 3-1-2. 기존 레코드들이 모두 존재하는지 확인
        for(AttachmentDto attachmentDto : beforeAttachments){
            boolean isExist = false;
            for(AttachmentDto selectedAttachment : seletedAttachments){
                if(attachmentDto.getAttachmentId().equals(selectedAttachment.getAttachmentId())){
                    isExist = true;
                    break;
                }
            }
            assertTrue(isExist);
        }

        // 3-2. 파일 저장소의 파일목록이 이전과 동일한지 확인
        File[] afterFileArr = Paths.get(FileUploadHandler.FILE_PATH).toFile().listFiles();
        assertTrue(beforeFileArr.length == afterFileArr.length); // 파일 수가 동일한지 확인
        for(File beforeFile : beforeFileArr){ // 이전 파일목록의 파일명이, 이후 파일목록에 존재하는지 확인
            boolean isExist = false;
            for(File afterFile : afterFileArr){
                if(beforeFile.getName().equals(afterFile.getName())){
                    isExist = true;
                    break;
                }
            }
            assertTrue(isExist);
        }
    }

    // 1. 성공 - 파일 삭제 성공 & DB 테이블 SoftDelete 성공 & 백업파일 삭제 성공
    @DisplayName("문의ID에 해당하는 첨부파일들 삭제 - 성공 - 파일 삭제 성공 & DB 테이블 SoftDelete 성공 & 백업파일 삭제 성공")
    @Test
    void deleteAttachmentsBy_1(){
        // 1단계 데이터 선택
        // 2단계 데이터 처리
        // 3단계 검증
    }

    // 2. 예외 - qnaId에 해당하는 문의글이 존재하지 않을 경우
    @DisplayName("문의ID에 해당하는 첨부파일들 삭제 - 예외 - qnaId에 해당하는 문의글이 존재하지 않을 경우")
    @Test
    void deleteAttachmentsBy_2(){
        // 1단계 데이터 선택 -> 존재하지 않는 qnaId 지정
        int qnaId = 9999;
        assertTrue(qnaDao.select(qnaId, false) == null);
        List<File> files = new LinkedList<>();
        files.add(createTestFile());

        // 2단계 데이터 처리 & 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            attachmentService.deleteAttachmentsBy(qnaId);
        });
    }

    // 3. 롤백 & 예외 - 백업파일 생성 중 예외 발생시
    @DisplayName("문의ID에 해당하는 첨부파일들 삭제 - 롤백 & 예외 - 백업파일 생성 중 예외 발생시")
    @Test
    void deleteAttachmentsBy_3() throws IOException {
        // 1단계 데이터 선택
        // 1-1. 테이블 첨부파일 레코드 등록 및 파일 저장소에 파일 저장
        for(int i = 0; i < 2; i++){
            File file = createTestFile();
            attachmentService.createAttachments(qnaDto.getQnaId(), List.of(file));
        }
        // 1-2. Attachment 테이블에 레코드 조회
        List<AttachmentDto> attachmentDtos = attachmentDao.selectAll(null);
        // 1-3. 파일 저장소의 파일목록 조회
        File[] beforefileArr = Paths.get(FileUploadHandler.FILE_PATH).toFile().listFiles();


        // 2단계 데이터 처리
        // 2-1. 백업파일 생성 중 예외 발생하도록 설정
        doThrow(new IOException("백업파일 생성 중 예외 발생"))
                .when(fileUploadHandler).copyFile(any(File.class));
        // 2-2. 첨부파일 삭제 메서드 호출 및 예외 발생 여부 확인
        assertThrows(IOException.class, () -> {
            attachmentService.deleteAttachmentsBy(qnaDto.getQnaId());
        });

        // 3단계 검증
        // 3-1. DB 테이블 롤백 확인
        List<AttachmentDto> selectedAttachments = attachmentDao.selectAll(true);
        assertTrue(selectedAttachments.size() == attachmentDtos.size());
        // 3-2. 파일 저장소의 파일목록 확인
        File[] afterFileArr = Paths.get(FileUploadHandler.FILE_PATH).toFile().listFiles();
        assertTrue(beforefileArr.length == afterFileArr.length);
        for(File beforeFile : beforefileArr){
            boolean isExist = false;
            for(File afterFile : afterFileArr){
                if(beforeFile.getName().equals(afterFile.getName())){
                    isExist = true;
                    break;
                }
            }
            assertTrue(isExist);
        }
    }

    // 4. 파일 복구 & 예외 - 첨부파일 삭제 중 예외 발생시, 백업파일을 사용하여 파일 복구
    @DisplayName("문의ID에 해당하는 첨부파일들 삭제 - 파일 복구 & 예외 - 첨부파일 삭제 중 예외 발생시, 백업파일을 사용하여 파일 복구")
    @Test
    void deleteAttachmentsBy_4(){
        // 1단계 데이터 선택
        // 2단계 데이터 처리
        // 3단계 검증
    }



    private AttachmentDto createSampleAttachmentDto() {
        AttachmentDto attachmentDto = AttachmentDto.builder()
                .qnaId(qnaDto.getQnaId())
                .fileName("test-file.PNG")
                .size(1000)
                .width(100)
                .height(100)
                .extension("PNG")
                .filePath("test-file.PNG")
                .createdId(qnaDto.getUserId())
                .updatedId(qnaDto.getUserId())
                .build();
        return attachmentDto;
    }
}
