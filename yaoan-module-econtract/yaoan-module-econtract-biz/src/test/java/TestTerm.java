import com.yaoan.framework.test.core.ut.BaseDbAndRedisUnitTest;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.service.term.es.TermRepositoryService;
import com.yaoan.module.econtract.service.term.TermService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 17:06
 */
@Import({TermService.class, Term.class})
public class TestTerm extends BaseDbAndRedisUnitTest {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private TermRepositoryService termRepositoryService;

    @Test
    public void testInit() {
        termRepositoryService.init();
    }

    @Test
    public void testAddTerm() {
        termRepositoryService.saveAll();

    }


}

