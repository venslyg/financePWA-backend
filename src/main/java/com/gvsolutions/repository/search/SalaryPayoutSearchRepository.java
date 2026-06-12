package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.SalaryPayout;
import com.gvsolutions.repository.SalaryPayoutRepository;
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
 * Spring Data Elasticsearch repository for the {@link SalaryPayout} entity.
 */
public interface SalaryPayoutSearchRepository extends ElasticsearchRepository<SalaryPayout, Long>, SalaryPayoutSearchRepositoryInternal {}

interface SalaryPayoutSearchRepositoryInternal {
    Page<SalaryPayout> search(String query, Pageable pageable);

    Page<SalaryPayout> search(Query query);

    @Async
    void index(SalaryPayout entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SalaryPayoutSearchRepositoryInternalImpl implements SalaryPayoutSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SalaryPayoutRepository repository;

    SalaryPayoutSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SalaryPayoutRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SalaryPayout> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SalaryPayout> search(Query query) {
        SearchHits<SalaryPayout> searchHits = elasticsearchTemplate.search(query, SalaryPayout.class);
        List<SalaryPayout> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SalaryPayout entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SalaryPayout.class);
    }
}
