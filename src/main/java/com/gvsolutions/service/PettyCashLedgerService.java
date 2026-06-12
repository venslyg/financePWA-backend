package com.gvsolutions.service;

import com.gvsolutions.domain.PettyCashLedger;
import com.gvsolutions.repository.PettyCashLedgerRepository;
import com.gvsolutions.repository.search.PettyCashLedgerSearchRepository;
import com.gvsolutions.service.dto.PettyCashLedgerDTO;
import com.gvsolutions.service.mapper.PettyCashLedgerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.PettyCashLedger}.
 */
@Service
@Transactional
public class PettyCashLedgerService {

    private static final Logger LOG = LoggerFactory.getLogger(PettyCashLedgerService.class);

    private final PettyCashLedgerRepository pettyCashLedgerRepository;

    private final PettyCashLedgerMapper pettyCashLedgerMapper;

    private final PettyCashLedgerSearchRepository pettyCashLedgerSearchRepository;

    public PettyCashLedgerService(
        PettyCashLedgerRepository pettyCashLedgerRepository,
        PettyCashLedgerMapper pettyCashLedgerMapper,
        PettyCashLedgerSearchRepository pettyCashLedgerSearchRepository
    ) {
        this.pettyCashLedgerRepository = pettyCashLedgerRepository;
        this.pettyCashLedgerMapper = pettyCashLedgerMapper;
        this.pettyCashLedgerSearchRepository = pettyCashLedgerSearchRepository;
    }

    /**
     * Save a pettyCashLedger.
     *
     * @param pettyCashLedgerDTO the entity to save.
     * @return the persisted entity.
     */
    public PettyCashLedgerDTO save(PettyCashLedgerDTO pettyCashLedgerDTO) {
        LOG.debug("Request to save PettyCashLedger : {}", pettyCashLedgerDTO);
        PettyCashLedger pettyCashLedger = pettyCashLedgerMapper.toEntity(pettyCashLedgerDTO);
        pettyCashLedger = pettyCashLedgerRepository.save(pettyCashLedger);
        pettyCashLedgerSearchRepository.index(pettyCashLedger);
        return pettyCashLedgerMapper.toDto(pettyCashLedger);
    }

    /**
     * Update a pettyCashLedger.
     *
     * @param pettyCashLedgerDTO the entity to save.
     * @return the persisted entity.
     */
    public PettyCashLedgerDTO update(PettyCashLedgerDTO pettyCashLedgerDTO) {
        LOG.debug("Request to update PettyCashLedger : {}", pettyCashLedgerDTO);
        PettyCashLedger pettyCashLedger = pettyCashLedgerMapper.toEntity(pettyCashLedgerDTO);
        pettyCashLedger.setIsPersisted();
        pettyCashLedger = pettyCashLedgerRepository.save(pettyCashLedger);
        pettyCashLedgerSearchRepository.index(pettyCashLedger);
        return pettyCashLedgerMapper.toDto(pettyCashLedger);
    }

    /**
     * Partially update a pettyCashLedger.
     *
     * @param pettyCashLedgerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PettyCashLedgerDTO> partialUpdate(PettyCashLedgerDTO pettyCashLedgerDTO) {
        LOG.debug("Request to partially update PettyCashLedger : {}", pettyCashLedgerDTO);

        return pettyCashLedgerRepository
            .findById(pettyCashLedgerDTO.getId())
            .map(existingPettyCashLedger -> {
                pettyCashLedgerMapper.partialUpdate(existingPettyCashLedger, pettyCashLedgerDTO);

                return existingPettyCashLedger;
            })
            .map(pettyCashLedgerRepository::save)
            .map(savedPettyCashLedger -> {
                pettyCashLedgerSearchRepository.index(savedPettyCashLedger);
                return savedPettyCashLedger;
            })
            .map(pettyCashLedgerMapper::toDto);
    }

    /**
     * Get one pettyCashLedger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PettyCashLedgerDTO> findOne(Long id) {
        LOG.debug("Request to get PettyCashLedger : {}", id);
        return pettyCashLedgerRepository.findById(id).map(pettyCashLedgerMapper::toDto);
    }

    /**
     * Delete the pettyCashLedger by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PettyCashLedger : {}", id);
        pettyCashLedgerRepository.deleteById(id);
        pettyCashLedgerSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the pettyCashLedger corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PettyCashLedgerDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of PettyCashLedgers for query {}", query);
        return pettyCashLedgerSearchRepository.search(query, pageable).map(pettyCashLedgerMapper::toDto);
    }
}
