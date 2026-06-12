package com.gvsolutions.service;

import com.gvsolutions.domain.LiabilityLog;
import com.gvsolutions.repository.LiabilityLogRepository;
import com.gvsolutions.repository.search.LiabilityLogSearchRepository;
import com.gvsolutions.service.dto.LiabilityLogDTO;
import com.gvsolutions.service.mapper.LiabilityLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.LiabilityLog}.
 */
@Service
@Transactional
public class LiabilityLogService {

    private static final Logger LOG = LoggerFactory.getLogger(LiabilityLogService.class);

    private final LiabilityLogRepository liabilityLogRepository;

    private final LiabilityLogMapper liabilityLogMapper;

    private final LiabilityLogSearchRepository liabilityLogSearchRepository;

    public LiabilityLogService(
        LiabilityLogRepository liabilityLogRepository,
        LiabilityLogMapper liabilityLogMapper,
        LiabilityLogSearchRepository liabilityLogSearchRepository
    ) {
        this.liabilityLogRepository = liabilityLogRepository;
        this.liabilityLogMapper = liabilityLogMapper;
        this.liabilityLogSearchRepository = liabilityLogSearchRepository;
    }

    /**
     * Save a liabilityLog.
     *
     * @param liabilityLogDTO the entity to save.
     * @return the persisted entity.
     */
    public LiabilityLogDTO save(LiabilityLogDTO liabilityLogDTO) {
        LOG.debug("Request to save LiabilityLog : {}", liabilityLogDTO);
        LiabilityLog liabilityLog = liabilityLogMapper.toEntity(liabilityLogDTO);
        liabilityLog = liabilityLogRepository.save(liabilityLog);
        liabilityLogSearchRepository.index(liabilityLog);
        return liabilityLogMapper.toDto(liabilityLog);
    }

    /**
     * Update a liabilityLog.
     *
     * @param liabilityLogDTO the entity to save.
     * @return the persisted entity.
     */
    public LiabilityLogDTO update(LiabilityLogDTO liabilityLogDTO) {
        LOG.debug("Request to update LiabilityLog : {}", liabilityLogDTO);
        LiabilityLog liabilityLog = liabilityLogMapper.toEntity(liabilityLogDTO);
        liabilityLog.setIsPersisted();
        liabilityLog = liabilityLogRepository.save(liabilityLog);
        liabilityLogSearchRepository.index(liabilityLog);
        return liabilityLogMapper.toDto(liabilityLog);
    }

    /**
     * Partially update a liabilityLog.
     *
     * @param liabilityLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LiabilityLogDTO> partialUpdate(LiabilityLogDTO liabilityLogDTO) {
        LOG.debug("Request to partially update LiabilityLog : {}", liabilityLogDTO);

        return liabilityLogRepository
            .findById(liabilityLogDTO.getId())
            .map(existingLiabilityLog -> {
                liabilityLogMapper.partialUpdate(existingLiabilityLog, liabilityLogDTO);

                return existingLiabilityLog;
            })
            .map(liabilityLogRepository::save)
            .map(savedLiabilityLog -> {
                liabilityLogSearchRepository.index(savedLiabilityLog);
                return savedLiabilityLog;
            })
            .map(liabilityLogMapper::toDto);
    }

    /**
     * Get one liabilityLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LiabilityLogDTO> findOne(Long id) {
        LOG.debug("Request to get LiabilityLog : {}", id);
        return liabilityLogRepository.findById(id).map(liabilityLogMapper::toDto);
    }

    /**
     * Delete the liabilityLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LiabilityLog : {}", id);
        liabilityLogRepository.deleteById(id);
        liabilityLogSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the liabilityLog corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LiabilityLogDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of LiabilityLogs for query {}", query);
        return liabilityLogSearchRepository.search(query, pageable).map(liabilityLogMapper::toDto);
    }
}
