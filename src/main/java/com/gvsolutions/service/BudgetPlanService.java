package com.gvsolutions.service;

import com.gvsolutions.domain.BudgetPlan;
import com.gvsolutions.repository.BudgetPlanRepository;
import com.gvsolutions.repository.search.BudgetPlanSearchRepository;
import com.gvsolutions.service.dto.BudgetPlanDTO;
import com.gvsolutions.service.mapper.BudgetPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.BudgetPlan}.
 */
@Service
@Transactional
public class BudgetPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetPlanService.class);

    private final BudgetPlanRepository budgetPlanRepository;

    private final BudgetPlanMapper budgetPlanMapper;

    private final BudgetPlanSearchRepository budgetPlanSearchRepository;

    public BudgetPlanService(
        BudgetPlanRepository budgetPlanRepository,
        BudgetPlanMapper budgetPlanMapper,
        BudgetPlanSearchRepository budgetPlanSearchRepository
    ) {
        this.budgetPlanRepository = budgetPlanRepository;
        this.budgetPlanMapper = budgetPlanMapper;
        this.budgetPlanSearchRepository = budgetPlanSearchRepository;
    }

    /**
     * Save a budgetPlan.
     *
     * @param budgetPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetPlanDTO save(BudgetPlanDTO budgetPlanDTO) {
        LOG.debug("Request to save BudgetPlan : {}", budgetPlanDTO);
        BudgetPlan budgetPlan = budgetPlanMapper.toEntity(budgetPlanDTO);
        budgetPlan = budgetPlanRepository.save(budgetPlan);
        budgetPlanSearchRepository.index(budgetPlan);
        return budgetPlanMapper.toDto(budgetPlan);
    }

    /**
     * Update a budgetPlan.
     *
     * @param budgetPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetPlanDTO update(BudgetPlanDTO budgetPlanDTO) {
        LOG.debug("Request to update BudgetPlan : {}", budgetPlanDTO);
        BudgetPlan budgetPlan = budgetPlanMapper.toEntity(budgetPlanDTO);
        budgetPlan.setIsPersisted();
        budgetPlan = budgetPlanRepository.save(budgetPlan);
        budgetPlanSearchRepository.index(budgetPlan);
        return budgetPlanMapper.toDto(budgetPlan);
    }

    /**
     * Partially update a budgetPlan.
     *
     * @param budgetPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BudgetPlanDTO> partialUpdate(BudgetPlanDTO budgetPlanDTO) {
        LOG.debug("Request to partially update BudgetPlan : {}", budgetPlanDTO);

        return budgetPlanRepository
            .findById(budgetPlanDTO.getId())
            .map(existingBudgetPlan -> {
                budgetPlanMapper.partialUpdate(existingBudgetPlan, budgetPlanDTO);

                return existingBudgetPlan;
            })
            .map(budgetPlanRepository::save)
            .map(savedBudgetPlan -> {
                budgetPlanSearchRepository.index(savedBudgetPlan);
                return savedBudgetPlan;
            })
            .map(budgetPlanMapper::toDto);
    }

    /**
     * Get one budgetPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BudgetPlanDTO> findOne(Long id) {
        LOG.debug("Request to get BudgetPlan : {}", id);
        return budgetPlanRepository.findById(id).map(budgetPlanMapper::toDto);
    }

    /**
     * Delete the budgetPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BudgetPlan : {}", id);
        budgetPlanRepository.deleteById(id);
        budgetPlanSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the budgetPlan corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BudgetPlanDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of BudgetPlans for query {}", query);
        return budgetPlanSearchRepository.search(query, pageable).map(budgetPlanMapper::toDto);
    }
}
