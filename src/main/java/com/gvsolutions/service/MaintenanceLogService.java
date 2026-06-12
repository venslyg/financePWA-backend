package com.gvsolutions.service;

import com.gvsolutions.domain.MaintenanceLog;
import com.gvsolutions.repository.MaintenanceLogRepository;
import com.gvsolutions.repository.search.MaintenanceLogSearchRepository;
import com.gvsolutions.service.dto.MaintenanceLogDTO;
import com.gvsolutions.service.mapper.MaintenanceLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.MaintenanceLog}.
 */
@Service
@Transactional
public class MaintenanceLogService {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceLogService.class);

    private final MaintenanceLogRepository maintenanceLogRepository;

    private final MaintenanceLogMapper maintenanceLogMapper;

    private final MaintenanceLogSearchRepository maintenanceLogSearchRepository;

    public MaintenanceLogService(
        MaintenanceLogRepository maintenanceLogRepository,
        MaintenanceLogMapper maintenanceLogMapper,
        MaintenanceLogSearchRepository maintenanceLogSearchRepository
    ) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.maintenanceLogMapper = maintenanceLogMapper;
        this.maintenanceLogSearchRepository = maintenanceLogSearchRepository;
    }

    /**
     * Save a maintenanceLog.
     *
     * @param maintenanceLogDTO the entity to save.
     * @return the persisted entity.
     */
    public MaintenanceLogDTO save(MaintenanceLogDTO maintenanceLogDTO) {
        LOG.debug("Request to save MaintenanceLog : {}", maintenanceLogDTO);
        MaintenanceLog maintenanceLog = maintenanceLogMapper.toEntity(maintenanceLogDTO);
        maintenanceLog = maintenanceLogRepository.save(maintenanceLog);
        maintenanceLogSearchRepository.index(maintenanceLog);
        return maintenanceLogMapper.toDto(maintenanceLog);
    }

    /**
     * Update a maintenanceLog.
     *
     * @param maintenanceLogDTO the entity to save.
     * @return the persisted entity.
     */
    public MaintenanceLogDTO update(MaintenanceLogDTO maintenanceLogDTO) {
        LOG.debug("Request to update MaintenanceLog : {}", maintenanceLogDTO);
        MaintenanceLog maintenanceLog = maintenanceLogMapper.toEntity(maintenanceLogDTO);
        maintenanceLog.setIsPersisted();
        maintenanceLog = maintenanceLogRepository.save(maintenanceLog);
        maintenanceLogSearchRepository.index(maintenanceLog);
        return maintenanceLogMapper.toDto(maintenanceLog);
    }

    /**
     * Partially update a maintenanceLog.
     *
     * @param maintenanceLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaintenanceLogDTO> partialUpdate(MaintenanceLogDTO maintenanceLogDTO) {
        LOG.debug("Request to partially update MaintenanceLog : {}", maintenanceLogDTO);

        return maintenanceLogRepository
            .findById(maintenanceLogDTO.getId())
            .map(existingMaintenanceLog -> {
                maintenanceLogMapper.partialUpdate(existingMaintenanceLog, maintenanceLogDTO);

                return existingMaintenanceLog;
            })
            .map(maintenanceLogRepository::save)
            .map(savedMaintenanceLog -> {
                maintenanceLogSearchRepository.index(savedMaintenanceLog);
                return savedMaintenanceLog;
            })
            .map(maintenanceLogMapper::toDto);
    }

    /**
     * Get all the maintenanceLogs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MaintenanceLogDTO> findAllWithEagerRelationships(Pageable pageable) {
        return maintenanceLogRepository.findAllWithEagerRelationships(pageable).map(maintenanceLogMapper::toDto);
    }

    /**
     * Get one maintenanceLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaintenanceLogDTO> findOne(Long id) {
        LOG.debug("Request to get MaintenanceLog : {}", id);
        return maintenanceLogRepository.findOneWithEagerRelationships(id).map(maintenanceLogMapper::toDto);
    }

    /**
     * Delete the maintenanceLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MaintenanceLog : {}", id);
        maintenanceLogRepository.deleteById(id);
        maintenanceLogSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the maintenanceLog corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintenanceLogDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MaintenanceLogs for query {}", query);
        return maintenanceLogSearchRepository.search(query, pageable).map(maintenanceLogMapper::toDto);
    }
}
