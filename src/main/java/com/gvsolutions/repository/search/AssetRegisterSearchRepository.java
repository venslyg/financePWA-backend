package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.AssetRegister;
import com.gvsolutions.repository.AssetRegisterRepository;
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
 * Spring Data Elasticsearch repository for the {@link AssetRegister} entity.
 */
public interface AssetRegisterSearchRepository
    extends ElasticsearchRepository<AssetRegister, Long>, AssetRegisterSearchRepositoryInternal {}

interface AssetRegisterSearchRepositoryInternal {
    Page<AssetRegister> search(String query, Pageable pageable);

    Page<AssetRegister> search(Query query);

    @Async
    void index(AssetRegister entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AssetRegisterSearchRepositoryInternalImpl implements AssetRegisterSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AssetRegisterRepository repository;

    AssetRegisterSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AssetRegisterRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AssetRegister> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AssetRegister> search(Query query) {
        SearchHits<AssetRegister> searchHits = elasticsearchTemplate.search(query, AssetRegister.class);
        List<AssetRegister> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AssetRegister entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AssetRegister.class);
    }
}
