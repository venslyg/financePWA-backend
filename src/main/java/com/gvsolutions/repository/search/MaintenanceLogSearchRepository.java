package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.MaintenanceLog;
import com.gvsolutions.repository.MaintenanceLogRepository;
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
 * Spring Data Elasticsearch repository for the {@link MaintenanceLog} entity.
 */
public interface MaintenanceLogSearchRepository
    extends ElasticsearchRepository<MaintenanceLog, Long>, MaintenanceLogSearchRepositoryInternal {}

interface MaintenanceLogSearchRepositoryInternal {
    Page<MaintenanceLog> search(String query, Pageable pageable);

    Page<MaintenanceLog> search(Query query);

    @Async
    void index(MaintenanceLog entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MaintenanceLogSearchRepositoryInternalImpl implements MaintenanceLogSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MaintenanceLogRepository repository;

    MaintenanceLogSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MaintenanceLogRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MaintenanceLog> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MaintenanceLog> search(Query query) {
        SearchHits<MaintenanceLog> searchHits = elasticsearchTemplate.search(query, MaintenanceLog.class);
        List<MaintenanceLog> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MaintenanceLog entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MaintenanceLog.class);
    }
}
