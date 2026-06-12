package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.ExpenseEntry;
import com.gvsolutions.repository.ExpenseEntryRepository;
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
 * Spring Data Elasticsearch repository for the {@link ExpenseEntry} entity.
 */
public interface ExpenseEntrySearchRepository extends ElasticsearchRepository<ExpenseEntry, Long>, ExpenseEntrySearchRepositoryInternal {}

interface ExpenseEntrySearchRepositoryInternal {
    Page<ExpenseEntry> search(String query, Pageable pageable);

    Page<ExpenseEntry> search(Query query);

    @Async
    void index(ExpenseEntry entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ExpenseEntrySearchRepositoryInternalImpl implements ExpenseEntrySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ExpenseEntryRepository repository;

    ExpenseEntrySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ExpenseEntryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ExpenseEntry> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ExpenseEntry> search(Query query) {
        SearchHits<ExpenseEntry> searchHits = elasticsearchTemplate.search(query, ExpenseEntry.class);
        List<ExpenseEntry> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ExpenseEntry entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ExpenseEntry.class);
    }
}
