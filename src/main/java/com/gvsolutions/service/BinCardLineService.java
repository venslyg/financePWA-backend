package com.gvsolutions.service;

import com.gvsolutions.domain.BinCardLine;
import com.gvsolutions.repository.BinCardLineRepository;
import com.gvsolutions.repository.search.BinCardLineSearchRepository;
import com.gvsolutions.service.dto.BinCardLineDTO;
import com.gvsolutions.service.mapper.BinCardLineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.BinCardLine}.
 */
@Service
@Transactional
public class BinCardLineService {

    private static final Logger LOG = LoggerFactory.getLogger(BinCardLineService.class);

    private final BinCardLineRepository binCardLineRepository;

    private final BinCardLineMapper binCardLineMapper;

    private final BinCardLineSearchRepository binCardLineSearchRepository;

    public BinCardLineService(
        BinCardLineRepository binCardLineRepository,
        BinCardLineMapper binCardLineMapper,
        BinCardLineSearchRepository binCardLineSearchRepository
    ) {
        this.binCardLineRepository = binCardLineRepository;
        this.binCardLineMapper = binCardLineMapper;
        this.binCardLineSearchRepository = binCardLineSearchRepository;
    }

    /**
     * Save a binCardLine.
     *
     * @param binCardLineDTO the entity to save.
     * @return the persisted entity.
     */
    public BinCardLineDTO save(BinCardLineDTO binCardLineDTO) {
        LOG.debug("Request to save BinCardLine : {}", binCardLineDTO);
        BinCardLine binCardLine = binCardLineMapper.toEntity(binCardLineDTO);
        binCardLine = binCardLineRepository.save(binCardLine);
        binCardLineSearchRepository.index(binCardLine);
        return binCardLineMapper.toDto(binCardLine);
    }

    /**
     * Update a binCardLine.
     *
     * @param binCardLineDTO the entity to save.
     * @return the persisted entity.
     */
    public BinCardLineDTO update(BinCardLineDTO binCardLineDTO) {
        LOG.debug("Request to update BinCardLine : {}", binCardLineDTO);
        BinCardLine binCardLine = binCardLineMapper.toEntity(binCardLineDTO);
        binCardLine.setIsPersisted();
        binCardLine = binCardLineRepository.save(binCardLine);
        binCardLineSearchRepository.index(binCardLine);
        return binCardLineMapper.toDto(binCardLine);
    }

    /**
     * Partially update a binCardLine.
     *
     * @param binCardLineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BinCardLineDTO> partialUpdate(BinCardLineDTO binCardLineDTO) {
        LOG.debug("Request to partially update BinCardLine : {}", binCardLineDTO);

        return binCardLineRepository
            .findById(binCardLineDTO.getId())
            .map(existingBinCardLine -> {
                binCardLineMapper.partialUpdate(existingBinCardLine, binCardLineDTO);

                return existingBinCardLine;
            })
            .map(binCardLineRepository::save)
            .map(savedBinCardLine -> {
                binCardLineSearchRepository.index(savedBinCardLine);
                return savedBinCardLine;
            })
            .map(binCardLineMapper::toDto);
    }

    /**
     * Get one binCardLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BinCardLineDTO> findOne(Long id) {
        LOG.debug("Request to get BinCardLine : {}", id);
        return binCardLineRepository.findById(id).map(binCardLineMapper::toDto);
    }

    /**
     * Delete the binCardLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BinCardLine : {}", id);
        binCardLineRepository.deleteById(id);
        binCardLineSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the binCardLine corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BinCardLineDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of BinCardLines for query {}", query);
        return binCardLineSearchRepository.search(query, pageable).map(binCardLineMapper::toDto);
    }
}
