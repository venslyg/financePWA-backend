package com.gvsolutions.service;

import com.gvsolutions.domain.BankLedger;
import com.gvsolutions.repository.BankLedgerRepository;
import com.gvsolutions.repository.search.BankLedgerSearchRepository;
import com.gvsolutions.service.dto.BankLedgerDTO;
import com.gvsolutions.service.mapper.BankLedgerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.BankLedger}.
 */
@Service
@Transactional
public class BankLedgerService {

    private static final Logger LOG = LoggerFactory.getLogger(BankLedgerService.class);

    private final BankLedgerRepository bankLedgerRepository;

    private final BankLedgerMapper bankLedgerMapper;

    private final BankLedgerSearchRepository bankLedgerSearchRepository;

    public BankLedgerService(
        BankLedgerRepository bankLedgerRepository,
        BankLedgerMapper bankLedgerMapper,
        BankLedgerSearchRepository bankLedgerSearchRepository
    ) {
        this.bankLedgerRepository = bankLedgerRepository;
        this.bankLedgerMapper = bankLedgerMapper;
        this.bankLedgerSearchRepository = bankLedgerSearchRepository;
    }

    /**
     * Save a bankLedger.
     *
     * @param bankLedgerDTO the entity to save.
     * @return the persisted entity.
     */
    public BankLedgerDTO save(BankLedgerDTO bankLedgerDTO) {
        LOG.debug("Request to save BankLedger : {}", bankLedgerDTO);
        BankLedger bankLedger = bankLedgerMapper.toEntity(bankLedgerDTO);
        bankLedger = bankLedgerRepository.save(bankLedger);
        bankLedgerSearchRepository.index(bankLedger);
        return bankLedgerMapper.toDto(bankLedger);
    }

    /**
     * Update a bankLedger.
     *
     * @param bankLedgerDTO the entity to save.
     * @return the persisted entity.
     */
    public BankLedgerDTO update(BankLedgerDTO bankLedgerDTO) {
        LOG.debug("Request to update BankLedger : {}", bankLedgerDTO);
        BankLedger bankLedger = bankLedgerMapper.toEntity(bankLedgerDTO);
        bankLedger.setIsPersisted();
        bankLedger = bankLedgerRepository.save(bankLedger);
        bankLedgerSearchRepository.index(bankLedger);
        return bankLedgerMapper.toDto(bankLedger);
    }

    /**
     * Partially update a bankLedger.
     *
     * @param bankLedgerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BankLedgerDTO> partialUpdate(BankLedgerDTO bankLedgerDTO) {
        LOG.debug("Request to partially update BankLedger : {}", bankLedgerDTO);

        return bankLedgerRepository
            .findById(bankLedgerDTO.getId())
            .map(existingBankLedger -> {
                bankLedgerMapper.partialUpdate(existingBankLedger, bankLedgerDTO);

                return existingBankLedger;
            })
            .map(bankLedgerRepository::save)
            .map(savedBankLedger -> {
                bankLedgerSearchRepository.index(savedBankLedger);
                return savedBankLedger;
            })
            .map(bankLedgerMapper::toDto);
    }

    /**
     * Get one bankLedger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BankLedgerDTO> findOne(Long id) {
        LOG.debug("Request to get BankLedger : {}", id);
        return bankLedgerRepository.findById(id).map(bankLedgerMapper::toDto);
    }

    /**
     * Delete the bankLedger by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BankLedger : {}", id);
        bankLedgerRepository.deleteById(id);
        bankLedgerSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the bankLedger corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BankLedgerDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of BankLedgers for query {}", query);
        return bankLedgerSearchRepository.search(query, pageable).map(bankLedgerMapper::toDto);
    }
}
