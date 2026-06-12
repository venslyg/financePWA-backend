package com.gvsolutions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.gvsolutions.domain.BudgetPlan;
import com.gvsolutions.repository.BudgetPlanRepository;
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
 * Spring Data Elasticsearch repository for the {@link BudgetPlan} entity.
 */
public interface BudgetPlanSearchRepository extends ElasticsearchRepository<BudgetPlan, Long>, BudgetPlanSearchRepositoryInternal {}

interface BudgetPlanSearchRepositoryInternal {
    Page<BudgetPlan> search(String query, Pageable pageable);

    Page<BudgetPlan> search(Query query);

    @Async
    void index(BudgetPlan entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BudgetPlanSearchRepositoryInternalImpl implements BudgetPlanSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BudgetPlanRepository repository;

    BudgetPlanSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BudgetPlanRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<BudgetPlan> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<BudgetPlan> search(Query query) {
        SearchHits<BudgetPlan> searchHits = elasticsearchTemplate.search(query, BudgetPlan.class);
        List<BudgetPlan> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(BudgetPlan entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), BudgetPlan.class);
    }
}
