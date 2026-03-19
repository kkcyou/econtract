package com.yaoan.module.econtract.service.term.es;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.term.vo.TermPageReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.TermSimpleRespVO;
import com.yaoan.module.econtract.convert.term.TermConverter;
import com.yaoan.module.econtract.dal.dataobject.term.SimpleTerm;
import com.yaoan.module.econtract.dal.mysql.term.SimpleTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermRepository;
import com.yaoan.module.econtract.enums.term.TermLibraryEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.yaoan.module.econtract.enums.term.TermLibraryEnums.COMMON;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-8 14:31
 */
@Slf4j
@Service
public class TermRepositoryServiceImpl implements TermRepositoryService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Resource
    TermRepository termRepository;

    @Resource
    private SimpleTermMapper simpleTermMapper;

    // 演示环境用代码初始化1000条
    @PostConstruct
    @Override
    @Async
    public void init() {
        CompletableFuture.runAsync(() -> {
            IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(SimpleTerm.class);
            if (!indexOperations.exists()) {
                // 创建索引，会根据Item类的@Document注解信息来创建
                boolean result = indexOperations.create();
                logger.info("创建 elasticsearch 索引 Term, 创建结果={}", result);
                if (!result) {
                    log.error("创建 elasticsearch 索引失败");
                    throw new RuntimeException("创建 elasticsearch 索引失败");
                } else {
                    // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
                    indexOperations.createMapping();
                }
            }
            saveAll();
            log.info("条款索引初始化完成");
        });
    }


    @Override
    public void saveAll() {
//        List<SimpleTerm> list = simpleTermMapper.selectList();

        List<SimpleTerm> list = simpleTermMapper.selectList(new LambdaQueryWrapperX<SimpleTerm>().orderByDesc(SimpleTerm::getUpdateTime).last("limit 1000"));
        Iterable<SimpleTerm> rs = termRepository.saveAll(list);
        log.info(rs.toString());
        log.info("条款索引的初始化-完成");
    }


    @Override
    public PageResult<TermSimpleRespVO> pageTerm4es3(TermPageReqVO vo) {
        Long tenantId = Objects.requireNonNull(SecurityFrameworkUtils.getLoginUser()).getTenantId();
        if (StringUtils.isBlank(vo.getName())) {
            LambdaQueryWrapperX<SimpleTerm> wrapperX = new LambdaQueryWrapperX<SimpleTerm>()
                    .eq(SimpleTerm::getTermLibrary, vo.getTermLibrary())
                    .eq(SimpleTerm::getStatus, 1)
                    .orderByDesc(SimpleTerm::getUpdateTime);
            if (ObjectUtil.isNotNull(vo.getTermLibrary())) {
                TermLibraryEnums termLibraryEnums = TermLibraryEnums.getInstance(vo.getTermLibrary());
                if (ObjectUtil.isNotNull(termLibraryEnums)) {
                    switch (termLibraryEnums) {
                        case COMMON:
                            wrapperX.eq(SimpleTerm::getTermLibrary, vo.getTermLibrary()).eq(SimpleTerm::getTenantId, COMMON.getCode());
                            break;

                        case AGENCY:
                            wrapperX.eq(SimpleTerm::getTermLibrary, vo.getTermLibrary()).eq(SimpleTerm::getTenantId, tenantId);
                            break;

                        default:
                            break;
                    }
                }
            } else {
                //公用库和单位库全查
                wrapperX.in(SimpleTerm::getTenantId, Arrays.asList(tenantId, 0));
            }

            PageResult<SimpleTerm> result = simpleTermMapper.selectPage(vo, wrapperX);

            return TermConverter.INSTANCE.pageDo2Resp4Es(result);
        }
        String inputName = vo.getName();
        PageResult<TermSimpleRespVO> result = new PageResult<>();

        //1、创建查询请求对象
        SearchRequest searchRequest = new SearchRequest("es_term");
        // 2、构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("status", 1));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (ObjectUtil.isNotNull(vo.getTermLibrary())) {
            TermLibraryEnums termLibraryEnums = TermLibraryEnums.getInstance(vo.getTermLibrary());
            if (ObjectUtil.isNotNull(termLibraryEnums)) {
                switch (termLibraryEnums) {
                    case COMMON:
                        boolQuery.filter(QueryBuilders.termQuery("termLibrary", 0));
                        break;

                    case AGENCY:
                        boolQuery.filter(QueryBuilders.termQuery("termLibrary", 1))
                                .filter(QueryBuilders.termQuery("tenantId", tenantId));
                        break;

                    default:
                        break;
                }
            }
        } else {
            //公用库和单位库全查
            boolQuery.filter(QueryBuilders.termsQuery("tenantId", new Long[]{tenantId, 0L}));
        }
        // 构建多字段匹配查询
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(inputName, "name", "termContentTxt")
                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
//                    .fuzziness(Fuzziness.AUTO);
        // name字段的匹配得分权重是termContentTxt的5倍
        multiMatchQuery.field("name", 5.0f);
        searchSourceBuilder.query(boolQuery.must(multiMatchQuery));

        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("termContentTxt");
        highlightBuilder.preTags("<span class=\"highlight\">");
        highlightBuilder.postTags("</span>");

        // 允许跨字段高亮
        highlightBuilder.requireFieldMatch(false);

        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.from(vo.getPageNo() - 1);
        searchSourceBuilder.size(vo.getPageSize());

        //按照匹配权重降序
        searchSourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.DESC));
        searchSourceBuilder.sort(new FieldSortBuilder("updateTime").order(SortOrder.DESC));
        // 3、设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"id", "name", "termContentTxt", "updateTime"}, null);
        // 4、执行搜索
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 5、处理搜索结果
            List<TermSimpleRespVO> termList = new ArrayList<>();
            org.elasticsearch.search.SearchHits hits = searchResponse.getHits();
            if (ObjectUtil.isNull(hits) || 0 == hits.getHits().length) {
                return new PageResult<>();
            }
            for (org.elasticsearch.search.SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String id = (String) sourceAsMap.get("id");
                String name = (String) sourceAsMap.get("name");
                String termContentTxt = (String) sourceAsMap.get("termContentTxt");

                // 处理高亮部分
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields.containsKey("name")) {
                    name = highlightFields.get("name").fragments()[0].string();
                }
//                if (highlightFields.containsKey("termContentTxt")) {
//                    termContentTxt = highlightFields.get("termContentTxt").fragments()[0].string();
//                }
                if(StringUtils.isNotBlank(vo.getName())){
                    termContentTxt = highlightKeyword(termContentTxt,vo.getName());
                }

                TermSimpleRespVO term = new TermSimpleRespVO();
                term.setId(id);
                term.setName(name);
                term.setTermContentTxt(termContentTxt);
                termList.add(term);
            }
            return result.setTotal(hits.getTotalHits().value).setList(termList);
        } catch (IOException e) {
            // 处理异常
            log.error("搜索词库失败", e);
        }
        return new PageResult<>();
    }


    public static String highlightKeyword(String termContentTxt, String keyword) {
        if (termContentTxt == null || keyword == null || keyword.isEmpty()) {
            return termContentTxt; // 输入为空时直接返回
        }

        // 构建正则表达式：匹配完整单词（可根据需要调整）
        String regex = "\\b" + Pattern.quote(keyword) + "\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        // 执行替换
        Matcher matcher = pattern.matcher(termContentTxt);
        return matcher.replaceAll("<span class=\"highlight\">$0</span>");
    }

}
