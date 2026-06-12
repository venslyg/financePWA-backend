package com.gvsolutions.service;

import com.gvsolutions.domain.Branch;
import com.gvsolutions.repository.BranchRepository;
import com.gvsolutions.repository.search.BranchSearchRepository;
import com.gvsolutions.service.dto.BranchDTO;
import com.gvsolutions.service.mapper.BranchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.Branch}.
 */
@Service
@Transactional
public class BranchService {

    private static final Logger LOG = LoggerFactory.getLogger(BranchService.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final BranchSearchRepository branchSearchRepository;

    public BranchService(BranchRepository branchRepository, BranchMapper branchMapper, BranchSearchRepository branchSearchRepository) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.branchSearchRepository = branchSearchRepository;
    }

    /**
     * Save a branch.
     *
     * @param branchDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchDTO save(BranchDTO branchDTO) {
        LOG.debug("Request to save Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        branchSearchRepository.index(branch);
        return branchMapper.toDto(branch);
    }

    /**
     * Update a branch.
     *
     * @param branchDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchDTO update(BranchDTO branchDTO) {
        LOG.debug("Request to update Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch.setIsPersisted();
        branch = branchRepository.save(branch);
        branchSearchRepository.index(branch);
        return branchMapper.toDto(branch);
    }

    /**
     * Partially update a branch.
     *
     * @param branchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BranchDTO> partialUpdate(BranchDTO branchDTO) {
        LOG.debug("Request to partially update Branch : {}", branchDTO);

        return branchRepository
            .findById(branchDTO.getId())
            .map(existingBranch -> {
                branchMapper.partialUpdate(existingBranch, branchDTO);

                return existingBranch;
            })
            .map(branchRepository::save)
            .map(savedBranch -> {
                branchSearchRepository.index(savedBranch);
                return savedBranch;
            })
            .map(branchMapper::toDto);
    }

    /**
     * Get one branch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchDTO> findOne(Long id) {
        LOG.debug("Request to get Branch : {}", id);
        return branchRepository.findById(id).map(branchMapper::toDto);
    }

    /**
     * Delete the branch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Branch : {}", id);
        branchRepository.deleteById(id);
        branchSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the branch corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Branches for query {}", query);
        return branchSearchRepository.search(query, pageable).map(branchMapper::toDto);
    }
}
