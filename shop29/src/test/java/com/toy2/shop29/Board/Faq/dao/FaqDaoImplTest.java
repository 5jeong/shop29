//import com.toy2.shop29.Board.Faq.dao.FaqDao;
//import com.toy2.shop29.Board.Faq.domain.FaqDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = AppConfig.class)
//public class FaqDaoImplTest {
//
//    @Autowired
//    private FaqDao faqDao;
//
//    @Test
//    public void testSelect() {
//        Integer faqId = 1;
//        FaqDto faq = faqDao.select(faqId);
//        assertNotNull(faq);
//    }
//
//    @Test
//    public void testInsert() {
//        FaqDto faq = new FaqDto();
//        faq.setFaqTitle("Sample Title");
//        faq.setFaqContent("Sample Content");
//        faq.setFaqCreatorId("creator1");
//        int result = faqDao.insert(faq);
//        assertEquals(1, result);
//    }
//
//    // 나머지 테스트도 추가
//}
