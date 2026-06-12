package com.gvsolutions.service;

import com.gvsolutions.domain.DonationTracker;
import com.gvsolutions.repository.DonationTrackerRepository;
import com.gvsolutions.repository.search.DonationTrackerSearchRepository;
import com.gvsolutions.service.dto.DonationTrackerDTO;
import com.gvsolutions.service.mapper.DonationTrackerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.DonationTracker}.
 */
@Service
@Transactional
public class DonationTrackerService {

    private static final Logger LOG = LoggerFactory.getLogger(DonationTrackerService.class);

    private final DonationTrackerRepository donationTrackerRepository;

    private final DonationTrackerMapper donationTrackerMapper;

    private final DonationTrackerSearchRepository donationTrackerSearchRepository;

    public DonationTrackerService(
        DonationTrackerRepository donationTrackerRepository,
        DonationTrackerMapper donationTrackerMapper,
        DonationTrackerSearchRepository donationTrackerSearchRepository
    ) {
        this.donationTrackerRepository = donationTrackerRepository;
        this.donationTrackerMapper = donationTrackerMapper;
        this.donationTrackerSearchRepository = donationTrackerSearchRepository;
    }

    /**
     * Save a donationTracker.
     *
     * @param donationTrackerDTO the entity to save.
     * @return the persisted entity.
     */
    public DonationTrackerDTO save(DonationTrackerDTO donationTrackerDTO) {
        LOG.debug("Request to save DonationTracker : {}", donationTrackerDTO);
        DonationTracker donationTracker = donationTrackerMapper.toEntity(donationTrackerDTO);
        donationTracker = donationTrackerRepository.save(donationTracker);
        donationTrackerSearchRepository.index(donationTracker);
        return donationTrackerMapper.toDto(donationTracker);
    }

    /**
     * Update a donationTracker.
     *
     * @param donationTrackerDTO the entity to save.
     * @return the persisted entity.
     */
    public DonationTrackerDTO update(DonationTrackerDTO donationTrackerDTO) {
        LOG.debug("Request to update DonationTracker : {}", donationTrackerDTO);
        DonationTracker donationTracker = donationTrackerMapper.toEntity(donationTrackerDTO);
        donationTracker.setIsPersisted();
        donationTracker = donationTrackerRepository.save(donationTracker);
        donationTrackerSearchRepository.index(donationTracker);
        return donationTrackerMapper.toDto(donationTracker);
    }

    /**
     * Partially update a donationTracker.
     *
     * @param donationTrackerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DonationTrackerDTO> partialUpdate(DonationTrackerDTO donationTrackerDTO) {
        LOG.debug("Request to partially update DonationTracker : {}", donationTrackerDTO);

        return donationTrackerRepository
            .findById(donationTrackerDTO.getId())
            .map(existingDonationTracker -> {
                donationTrackerMapper.partialUpdate(existingDonationTracker, donationTrackerDTO);

                return existingDonationTracker;
            })
            .map(donationTrackerRepository::save)
            .map(savedDonationTracker -> {
                donationTrackerSearchRepository.index(savedDonationTracker);
                return savedDonationTracker;
            })
            .map(donationTrackerMapper::toDto);
    }

    /**
     * Get one donationTracker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DonationTrackerDTO> findOne(Long id) {
        LOG.debug("Request to get DonationTracker : {}", id);
        return donationTrackerRepository.findById(id).map(donationTrackerMapper::toDto);
    }

    /**
     * Delete the donationTracker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DonationTracker : {}", id);
        donationTrackerRepository.deleteById(id);
        donationTrackerSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the donationTracker corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DonationTrackerDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DonationTrackers for query {}", query);
        return donationTrackerSearchRepository.search(query, pageable).map(donationTrackerMapper::toDto);
    }
}
