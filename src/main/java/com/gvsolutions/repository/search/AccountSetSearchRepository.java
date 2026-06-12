package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.AccountSet;
import com.gvsolutions.repository.AccountSetRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccountSet} entity.
 */
public interface AccountSetSearchRepository extends ElasticsearchRepository<AccountSet, Long>, AccountSetSearchRepositoryInternal {}

interface AccountSetSearchRepositoryInternal {
    Page<AccountSet> search(String query, Pageable pageable);

    Page<AccountSet> search(Query query);

    @Async
    void index(AccountSet entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AccountSetSearchRepositoryInternalImpl implements AccountSetSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccountSetRepository repository;

    AccountSetSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AccountSetRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccountSet> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccountSet> search(Query query) {
        SearchHits<AccountSet> searchHits = elasticsearchTemplate.search(query, AccountSet.class);
        List<AccountSet> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccountSet entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccountSet.class);
    }
}
