package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.InventoryItem;
import com.gvsolutions.repository.InventoryItemRepository;
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
 * Spring Data Elasticsearch repository for the {@link InventoryItem} entity.
 */
public interface InventoryItemSearchRepository
    extends ElasticsearchRepository<InventoryItem, Long>, InventoryItemSearchRepositoryInternal {}

interface InventoryItemSearchRepositoryInternal {
    Page<InventoryItem> search(String query, Pageable pageable);

    Page<InventoryItem> search(Query query);

    @Async
    void index(InventoryItem entity);

    @Async
    void deleteFromIndexById(Long id);
}

class InventoryItemSearchRepositoryInternalImpl implements InventoryItemSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InventoryItemRepository repository;

    InventoryItemSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, InventoryItemRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InventoryItem> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InventoryItem> search(Query query) {
        SearchHits<InventoryItem> searchHits = elasticsearchTemplate.search(query, InventoryItem.class);
        List<InventoryItem> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InventoryItem entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), InventoryItem.class);
    }
}
