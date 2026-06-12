package com.gvsolutions.web.rest;

import com.gvsolutions.repository.DonationTrackerRepository;
import com.gvsolutions.service.DonationTrackerQueryService;
import com.gvsolutions.service.DonationTrackerService;
import com.gvsolutions.service.criteria.DonationTrackerCriteria;
import com.gvsolutions.service.dto.DonationTrackerDTO;
import com.gvsolutions.web.rest.errors.BadRequestAlertException;
import com.gvsolutions.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gvsolutions.domain.DonationTracker}.
 */
@RestController
@RequestMapping("/api/donation-trackers")
public class DonationTrackerResource {

    private static final Logger LOG = LoggerFactory.getLogger(DonationTrackerResource.class);

    private static final String ENTITY_NAME = "donationTracker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonationTrackerService donationTrackerService;

    private final DonationTrackerRepository donationTrackerRepository;

    private final DonationTrackerQueryService donationTrackerQueryService;

    public DonationTrackerResource(
        DonationTrackerService donationTrackerService,
        DonationTrackerRepository donationTrackerRepository,
        DonationTrackerQueryService donationTrackerQueryService
    ) {
        this.donationTrackerService = donationTrackerService;
        this.donationTrackerRepository = donationTrackerRepository;
        this.donationTrackerQueryService = donationTrackerQueryService;
    }

    /**
     * {@code POST  /donation-trackers} : Create a new donationTracker.
     *
     * @param donationTrackerDTO the donationTrackerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donationTrackerDTO, or with status {@code 400 (Bad Request)} if the donationTracker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DonationTrackerDTO> createDonationTracker(@RequestBody DonationTrackerDTO donationTrackerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DonationTracker : {}", donationTrackerDTO);
        if (donationTrackerDTO.getId() != null) {
            throw new BadRequestAlertException("A new donationTracker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        donationTrackerDTO = donationTrackerService.save(donationTrackerDTO);
        return ResponseEntity.created(new URI("/api/donation-trackers/" + donationTrackerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, donationTrackerDTO.getId().toString()))
            .body(donationTrackerDTO);
    }

    /**
     * {@code PUT  /donation-trackers/:id} : Updates an existing donationTracker.
     *
     * @param id the id of the donationTrackerDTO to save.
     * @param donationTrackerDTO the donationTrackerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationTrackerDTO,
     * or with status {@code 400 (Bad Request)} if the donationTrackerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donationTrackerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonationTrackerDTO> updateDonationTracker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DonationTrackerDTO donationTrackerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DonationTracker : {}, {}", id, donationTrackerDTO);
        if (donationTrackerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donationTrackerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donationTrackerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        donationTrackerDTO = donationTrackerService.update(donationTrackerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, donationTrackerDTO.getId().toString()))
            .body(donationTrackerDTO);
    }

    /**
     * {@code PATCH  /donation-trackers/:id} : Partial updates given fields of an existing donationTracker, field will ignore if it is null
     *
     * @param id the id of the donationTrackerDTO to save.
     * @param donationTrackerDTO the donationTrackerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationTrackerDTO,
     * or with status {@code 400 (Bad Request)} if the donationTrackerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donationTrackerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donationTrackerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DonationTrackerDTO> partialUpdateDonationTracker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DonationTrackerDTO donationTrackerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DonationTracker partially : {}, {}", id, donationTrackerDTO);
        if (donationTrackerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donationTrackerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donationTrackerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DonationTrackerDTO> result = donationTrackerService.partialUpdate(donationTrackerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, donationTrackerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /donation-trackers} : get all the donationTrackers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donationTrackers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DonationTrackerDTO>> getAllDonationTrackers(
        DonationTrackerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DonationTrackers by criteria: {}", criteria);

        Page<DonationTrackerDTO> page = donationTrackerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /donation-trackers/count} : count all the donationTrackers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDonationTrackers(DonationTrackerCriteria criteria) {
        LOG.debug("REST request to count DonationTrackers by criteria: {}", criteria);
        return ResponseEntity.ok().body(donationTrackerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /donation-trackers/:id} : get the "id" donationTracker.
     *
     * @param id the id of the donationTrackerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationTrackerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonationTrackerDTO> getDonationTracker(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DonationTracker : {}", id);
        Optional<DonationTrackerDTO> donationTrackerDTO = donationTrackerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(donationTrackerDTO);
    }

    /**
     * {@code DELETE  /donation-trackers/:id} : delete the "id" donationTracker.
     *
     * @param id the id of the donationTrackerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonationTracker(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DonationTracker : {}", id);
        donationTrackerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /donation-trackers/_search?query=:query} : search for the donationTracker corresponding
     * to the query.
     *
     * @param query the query of the donationTracker search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DonationTrackerDTO>> searchDonationTrackers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of DonationTrackers for query {}", query);
        try {
            Page<DonationTrackerDTO> page = donationTrackerService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
