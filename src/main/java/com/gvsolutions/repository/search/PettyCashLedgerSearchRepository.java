package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.PettyCashLedger;
import com.gvsolutions.repository.PettyCashLedgerRepository;
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
 * Spring Data Elasticsearch repository for the {@link PettyCashLedger} entity.
 */
public interface PettyCashLedgerSearchRepository
    extends ElasticsearchRepository<PettyCashLedger, Long>, PettyCashLedgerSearchRepositoryInternal {}

interface PettyCashLedgerSearchRepositoryInternal {
    Page<PettyCashLedger> search(String query, Pageable pageable);

    Page<PettyCashLedger> search(Query query);

    @Async
    void index(PettyCashLedger entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PettyCashLedgerSearchRepositoryInternalImpl implements PettyCashLedgerSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PettyCashLedgerRepository repository;

    PettyCashLedgerSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PettyCashLedgerRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PettyCashLedger> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PettyCashLedger> search(Query query) {
        SearchHits<PettyCashLedger> searchHits = elasticsearchTemplate.search(query, PettyCashLedger.class);
        List<PettyCashLedger> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PettyCashLedger entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PettyCashLedger.class);
    }
}
