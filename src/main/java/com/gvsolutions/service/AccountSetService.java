package com.gvsolutions.service;

import com.gvsolutions.domain.AccountSet;
import com.gvsolutions.repository.AccountSetRepository;
import com.gvsolutions.repository.search.AccountSetSearchRepository;
import com.gvsolutions.service.dto.AccountSetDTO;
import com.gvsolutions.service.mapper.AccountSetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.AccountSet}.
 */
@Service
@Transactional
public class AccountSetService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountSetService.class);

    private final AccountSetRepository accountSetRepository;

    private final AccountSetMapper accountSetMapper;

    private final AccountSetSearchRepository accountSetSearchRepository;

    public AccountSetService(
        AccountSetRepository accountSetRepository,
        AccountSetMapper accountSetMapper,
        AccountSetSearchRepository accountSetSearchRepository
    ) {
        this.accountSetRepository = accountSetRepository;
        this.accountSetMapper = accountSetMapper;
        this.accountSetSearchRepository = accountSetSearchRepository;
    }

    /**
     * Save a accountSet.
     *
     * @param accountSetDTO the entity to save.
     * @return the persisted entity.
     */
    public AccountSetDTO save(AccountSetDTO accountSetDTO) {
        LOG.debug("Request to save AccountSet : {}", accountSetDTO);
        AccountSet accountSet = accountSetMapper.toEntity(accountSetDTO);
        accountSet = accountSetRepository.save(accountSet);
        accountSetSearchRepository.index(accountSet);
        return accountSetMapper.toDto(accountSet);
    }

    /**
     * Update a accountSet.
     *
     * @param accountSetDTO the entity to save.
     * @return the persisted entity.
     */
    public AccountSetDTO update(AccountSetDTO accountSetDTO) {
        LOG.debug("Request to update AccountSet : {}", accountSetDTO);
        AccountSet accountSet = accountSetMapper.toEntity(accountSetDTO);
        accountSet.setIsPersisted();
        accountSet = accountSetRepository.save(accountSet);
        accountSetSearchRepository.index(accountSet);
        return accountSetMapper.toDto(accountSet);
    }

    /**
     * Partially update a accountSet.
     *
     * @param accountSetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AccountSetDTO> partialUpdate(AccountSetDTO accountSetDTO) {
        LOG.debug("Request to partially update AccountSet : {}", accountSetDTO);

        return accountSetRepository
            .findById(accountSetDTO.getId())
            .map(existingAccountSet -> {
                accountSetMapper.partialUpdate(existingAccountSet, accountSetDTO);

                return existingAccountSet;
            })
            .map(accountSetRepository::save)
            .map(savedAccountSet -> {
                accountSetSearchRepository.index(savedAccountSet);
                return savedAccountSet;
            })
            .map(accountSetMapper::toDto);
    }

    /**
     * Get one accountSet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AccountSetDTO> findOne(Long id) {
        LOG.debug("Request to get AccountSet : {}", id);
        return accountSetRepository.findById(id).map(accountSetMapper::toDto);
    }

    /**
     * Delete the accountSet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AccountSet : {}", id);
        accountSetRepository.deleteById(id);
        accountSetSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the accountSet corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountSetDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AccountSets for query {}", query);
        return accountSetSearchRepository.search(query, pageable).map(accountSetMapper::toDto);
    }
}
