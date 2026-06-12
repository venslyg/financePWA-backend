package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.Branch;
import com.gvsolutions.repository.BranchRepository;
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
 * Spring Data Elasticsearch repository for the {@link Branch} entity.
 */
public interface BranchSearchRepository extends ElasticsearchRepository<Branch, Long>, BranchSearchRepositoryInternal {}

interface BranchSearchRepositoryInternal {
    Page<Branch> search(String query, Pageable pageable);

    Page<Branch> search(Query query);

    @Async
    void index(Branch entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BranchSearchRepositoryInternalImpl implements BranchSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BranchRepository repository;

    BranchSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BranchRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Branch> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Branch> search(Query query) {
        SearchHits<Branch> searchHits = elasticsearchTemplate.search(query, Branch.class);
        List<Branch> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Branch entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Branch.class);
    }
}
