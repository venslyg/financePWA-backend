package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.ChurchStaff;
import com.gvsolutions.repository.ChurchStaffRepository;
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
 * Spring Data Elasticsearch repository for the {@link ChurchStaff} entity.
 */
public interface ChurchStaffSearchRepository extends ElasticsearchRepository<ChurchStaff, Long>, ChurchStaffSearchRepositoryInternal {}

interface ChurchStaffSearchRepositoryInternal {
    Page<ChurchStaff> search(String query, Pageable pageable);

    Page<ChurchStaff> search(Query query);

    @Async
    void index(ChurchStaff entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ChurchStaffSearchRepositoryInternalImpl implements ChurchStaffSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ChurchStaffRepository repository;

    ChurchStaffSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ChurchStaffRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ChurchStaff> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ChurchStaff> search(Query query) {
        SearchHits<ChurchStaff> searchHits = elasticsearchTemplate.search(query, ChurchStaff.class);
        List<ChurchStaff> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ChurchStaff entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ChurchStaff.class);
    }
}
