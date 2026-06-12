package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.BankLedger;
import com.gvsolutions.repository.BankLedgerRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link BankLedger} entity.
 */
public interface BankLedgerSearchRepository extends ElasticsearchRepository<BankLedger, Long>, BankLedgerSearchRepositoryInternal {}

interface BankLedgerSearchRepositoryInternal {
    Page<BankLedger> search(String query, Pageable pageable);

    Page<BankLedger> search(Query query);

    @Async
    void index(BankLedger entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BankLedgerSearchRepositoryInternalImpl implements BankLedgerSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BankLedgerRepository repository;

    BankLedgerSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BankLedgerRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<BankLedger> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<BankLedger> search(Query query) {
        SearchHits<BankLedger> searchHits = elasticsearchTemplate.search(query, BankLedger.class);
        List<BankLedger> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(BankLedger entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), BankLedger.class);
    }
}
