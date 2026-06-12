package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.DonationTracker;
import com.gvsolutions.repository.DonationTrackerRepository;
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
 * Spring Data Elasticsearch repository for the {@link DonationTracker} entity.
 */
public interface DonationTrackerSearchRepository
    extends ElasticsearchRepository<DonationTracker, Long>, DonationTrackerSearchRepositoryInternal {}

interface DonationTrackerSearchRepositoryInternal {
    Page<DonationTracker> search(String query, Pageable pageable);

    Page<DonationTracker> search(Query query);

    @Async
    void index(DonationTracker entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DonationTrackerSearchRepositoryInternalImpl implements DonationTrackerSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DonationTrackerRepository repository;

    DonationTrackerSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DonationTrackerRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DonationTracker> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DonationTracker> search(Query query) {
        SearchHits<DonationTracker> searchHits = elasticsearchTemplate.search(query, DonationTracker.class);
        List<DonationTracker> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DonationTracker entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DonationTracker.class);
    }
}
