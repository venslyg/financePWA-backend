package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.MaintenanceLog;
import com.gvsolutions.repository.MaintenanceLogRepository;
import com.gvsolutions.repository.search.MaintenanceLogSearchRepository;
import com.gvsolutions.service.criteria.MaintenanceLogCriteria;
import com.gvsolutions.service.dto.MaintenanceLogDTO;
import com.gvsolutions.service.mapper.MaintenanceLogMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MaintenanceLog} entities in the database.
 * The main input is a {@link MaintenanceLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaintenanceLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaintenanceLogQueryService extends QueryService<MaintenanceLog> {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceLogQueryService.class);

    private final MaintenanceLogRepository maintenanceLogRepository;

    private final MaintenanceLogMapper maintenanceLogMapper;

    private final MaintenanceLogSearchRepository maintenanceLogSearchRepository;

    public MaintenanceLogQueryService(
        MaintenanceLogRepository maintenanceLogRepository,
        MaintenanceLogMapper maintenanceLogMapper,
        MaintenanceLogSearchRepository maintenanceLogSearchRepository
    ) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.maintenanceLogMapper = maintenanceLogMapper;
        this.maintenanceLogSearchRepository = maintenanceLogSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MaintenanceLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintenanceLogDTO> findByCriteria(MaintenanceLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaintenanceLog> specification = createSpecification(criteria);
        return maintenanceLogRepository.findAll(specification, page).map(maintenanceLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaintenanceLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MaintenanceLog> specification = createSpecification(criteria);
        return maintenanceLogRepository.count(specification);
    }

    /**
     * Function to convert {@link MaintenanceLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaintenanceLog> createSpecification(MaintenanceLogCriteria criteria) {
        Specification<MaintenanceLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MaintenanceLog_.id),
                buildStringSpecification(criteria.getMaintenanceLogCode(), MaintenanceLog_.maintenanceLogCode),
                buildRangeSpecification(criteria.getLogDate(), MaintenanceLog_.logDate),
                buildSpecification(criteria.getLogType(), MaintenanceLog_.logType),
                buildStringSpecification(criteria.getDescription(), MaintenanceLog_.description),
                buildRangeSpecification(criteria.getCost(), MaintenanceLog_.cost),
                buildStringSpecification(criteria.getVendor(), MaintenanceLog_.vendor),
                buildRangeSpecification(criteria.getNextServiceDate(), MaintenanceLog_.nextServiceDate),
                buildStringSpecification(criteria.getNote(), MaintenanceLog_.note),
                buildStringSpecification(criteria.getCreatedBy(), MaintenanceLog_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), MaintenanceLog_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), MaintenanceLog_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), MaintenanceLog_.lastModifiedDate),
                buildSpecification(criteria.getAssetId(), root -> root.join(MaintenanceLog_.asset, JoinType.LEFT).get(AssetRegister_.id))
            );
        }
        return specification;
    }
}
