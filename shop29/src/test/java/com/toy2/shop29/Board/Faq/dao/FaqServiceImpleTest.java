//import com.toy2.shop29.Board.Faq.dao.FaqDao;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class FaqServiceImplTest {
//
//    @InjectMocks
//    private FaqServiceImpl
//
//    @Mock
//    private FaqDao faqDao;
//
//    @Test
//    public void testGetCountWithSearchQuery() {
//        // Given
//        Map<String, Object> params = Map.of("option", "A", "searchQuery", "example");
//        when(faqDao.countBySearchQuery(params)).thenReturn(10);
//
//        // When
//        int count = faqService.getCountWithSearchQuery("A", "example");
//
//        // Then
//        assertEquals(10, count);
//    }
//
//    // 다른 메서드들에 대한 테스트도 추가
//}