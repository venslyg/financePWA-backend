package com.gvsolutions.web.rest;

import com.gvsolutions.repository.ChurchStaffRepository;
import com.gvsolutions.service.ChurchStaffQueryService;
import com.gvsolutions.service.ChurchStaffService;
import com.gvsolutions.service.criteria.ChurchStaffCriteria;
import com.gvsolutions.service.dto.ChurchStaffDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.ChurchStaff}.
 */
@RestController
@RequestMapping("/api/church-staffs")
public class ChurchStaffResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChurchStaffResource.class);

    private static final String ENTITY_NAME = "churchStaff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchStaffService churchStaffService;

    private final ChurchStaffRepository churchStaffRepository;

    private final ChurchStaffQueryService churchStaffQueryService;

    public ChurchStaffResource(
        ChurchStaffService churchStaffService,
        ChurchStaffRepository churchStaffRepository,
        ChurchStaffQueryService churchStaffQueryService
    ) {
        this.churchStaffService = churchStaffService;
        this.churchStaffRepository = churchStaffRepository;
        this.churchStaffQueryService = churchStaffQueryService;
    }

    /**
     * {@code POST  /church-staffs} : Create a new churchStaff.
     *
     * @param churchStaffDTO the churchStaffDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new churchStaffDTO, or with status {@code 400 (Bad Request)} if the churchStaff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChurchStaffDTO> createChurchStaff(@RequestBody ChurchStaffDTO churchStaffDTO) throws URISyntaxException {
        LOG.debug("REST request to save ChurchStaff : {}", churchStaffDTO);
        if (churchStaffDTO.getId() != null) {
            throw new BadRequestAlertException("A new churchStaff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        churchStaffDTO = churchStaffService.save(churchStaffDTO);
        return ResponseEntity.created(new URI("/api/church-staffs/" + churchStaffDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, churchStaffDTO.getId().toString()))
            .body(churchStaffDTO);
    }

    /**
     * {@code PUT  /church-staffs/:id} : Updates an existing churchStaff.
     *
     * @param id the id of the churchStaffDTO to save.
     * @param churchStaffDTO the churchStaffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchStaffDTO,
     * or with status {@code 400 (Bad Request)} if the churchStaffDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the churchStaffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChurchStaffDTO> updateChurchStaff(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChurchStaffDTO churchStaffDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChurchStaff : {}, {}", id, churchStaffDTO);
        if (churchStaffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, churchStaffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!churchStaffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        churchStaffDTO = churchStaffService.update(churchStaffDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, churchStaffDTO.getId().toString()))
            .body(churchStaffDTO);
    }

    /**
     * {@code PATCH  /church-staffs/:id} : Partial updates given fields of an existing churchStaff, field will ignore if it is null
     *
     * @param id the id of the churchStaffDTO to save.
     * @param churchStaffDTO the churchStaffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchStaffDTO,
     * or with status {@code 400 (Bad Request)} if the churchStaffDTO is not valid,
     * or with status {@code 404 (Not Found)} if the churchStaffDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the churchStaffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChurchStaffDTO> partialUpdateChurchStaff(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChurchStaffDTO churchStaffDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChurchStaff partially : {}, {}", id, churchStaffDTO);
        if (churchStaffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, churchStaffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!churchStaffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChurchStaffDTO> result = churchStaffService.partialUpdate(churchStaffDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, churchStaffDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /church-staffs} : get all the churchStaffs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churchStaffs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChurchStaffDTO>> getAllChurchStaffs(
        ChurchStaffCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ChurchStaffs by criteria: {}", criteria);

        Page<ChurchStaffDTO> page = churchStaffQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /church-staffs/count} : count all the churchStaffs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countChurchStaffs(ChurchStaffCriteria criteria) {
        LOG.debug("REST request to count ChurchStaffs by criteria: {}", criteria);
        return ResponseEntity.ok().body(churchStaffQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /church-staffs/:id} : get the "id" churchStaff.
     *
     * @param id the id of the churchStaffDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the churchStaffDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChurchStaffDTO> getChurchStaff(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChurchStaff : {}", id);
        Optional<ChurchStaffDTO> churchStaffDTO = churchStaffService.findOne(id);
        return ResponseUtil.wrapOrNotFound(churchStaffDTO);
    }

    /**
     * {@code DELETE  /church-staffs/:id} : delete the "id" churchStaff.
     *
     * @param id the id of the churchStaffDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChurchStaff(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChurchStaff : {}", id);
        churchStaffService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /church-staffs/_search?query=:query} : search for the churchStaff corresponding
     * to the query.
     *
     * @param query the query of the churchStaff search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ChurchStaffDTO>> searchChurchStaffs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ChurchStaffs for query {}", query);
        try {
            Page<ChurchStaffDTO> page = churchStaffService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
