package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.AccountSet;
import com.gvsolutions.repository.AccountSetRepository;
import com.gvsolutions.repository.search.AccountSetSearchRepository;
import com.gvsolutions.service.criteria.AccountSetCriteria;
import com.gvsolutions.service.dto.AccountSetDTO;
import com.gvsolutions.service.mapper.AccountSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AccountSet} entities in the database.
 * The main input is a {@link AccountSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AccountSetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountSetQueryService extends QueryService<AccountSet> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountSetQueryService.class);

    private final AccountSetRepository accountSetRepository;

    private final AccountSetMapper accountSetMapper;

    private final AccountSetSearchRepository accountSetSearchRepository;

    public AccountSetQueryService(
        AccountSetRepository accountSetRepository,
        AccountSetMapper accountSetMapper,
        AccountSetSearchRepository accountSetSearchRepository
    ) {
        this.accountSetRepository = accountSetRepository;
        this.accountSetMapper = accountSetMapper;
        this.accountSetSearchRepository = accountSetSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AccountSetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountSetDTO> findByCriteria(AccountSetCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountSet> specification = createSpecification(criteria);
        return accountSetRepository.findAll(specification, page).map(accountSetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountSetCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AccountSet> specification = createSpecification(criteria);
        return accountSetRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountSetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountSet> createSpecification(AccountSetCriteria criteria) {
        Specification<AccountSet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AccountSet_.id),
                buildStringSpecification(criteria.getBranchCode(), AccountSet_.branchCode),
                buildStringSpecification(criteria.getAccountCode(), AccountSet_.accountCode),
                buildStringSpecification(criteria.getAccountName(), AccountSet_.accountName),
                buildSpecification(criteria.getAccountType(), AccountSet_.accountType),
                buildStringSpecification(criteria.getSubCategory(), AccountSet_.subCategory),
                buildStringSpecification(criteria.getRemark(), AccountSet_.remark),
                buildStringSpecification(criteria.getCreatedBy(), AccountSet_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), AccountSet_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), AccountSet_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), AccountSet_.lastModifiedDate)
            );
        }
        return specification;
    }
}
