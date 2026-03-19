package com.yaoan.module.econtract.dal.mysql.term;

import com.yaoan.module.econtract.dal.dataobject.term.SimpleTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-8 14:36
 */
@Repository
public interface TermRepository extends ElasticsearchRepository<SimpleTerm, String> {
   default void saveSimple(Term term){
        SimpleTerm simpleTerm =    new SimpleTerm().setId(term.getId())
                .setName(term.getName())
                .setStatus(Integer.valueOf(term.getStatus()))
                .setTermLibrary(term.getTermLibrary())
                .setTenantId(term.getTenantId())
                .setTermContentTxt(new String(term.getTermContent(), StandardCharsets.UTF_8))
                .setUpdateTime(term.getUpdateTime())
                ;
        save(simpleTerm);
    }


}
