package com.gvsolutions.web.rest;

import com.gvsolutions.repository.AssetDepreciationHistoryRepository;
import com.gvsolutions.service.AssetDepreciationHistoryQueryService;
import com.gvsolutions.service.AssetDepreciationHistoryService;
import com.gvsolutions.service.criteria.AssetDepreciationHistoryCriteria;
import com.gvsolutions.service.dto.AssetDepreciationHistoryDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.AssetDepreciationHistory}.
 */
@RestController
@RequestMapping("/api/asset-depreciation-histories")
public class AssetDepreciationHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssetDepreciationHistoryResource.class);

    private static final String ENTITY_NAME = "assetDepreciationHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetDepreciationHistoryService assetDepreciationHistoryService;

    private final AssetDepreciationHistoryRepository assetDepreciationHistoryRepository;

    private final AssetDepreciationHistoryQueryService assetDepreciationHistoryQueryService;

    public AssetDepreciationHistoryResource(
        AssetDepreciationHistoryService assetDepreciationHistoryService,
        AssetDepreciationHistoryRepository assetDepreciationHistoryRepository,
        AssetDepreciationHistoryQueryService assetDepreciationHistoryQueryService
    ) {
        this.assetDepreciationHistoryService = assetDepreciationHistoryService;
        this.assetDepreciationHistoryRepository = assetDepreciationHistoryRepository;
        this.assetDepreciationHistoryQueryService = assetDepreciationHistoryQueryService;
    }

    /**
     * {@code POST  /asset-depreciation-histories} : Create a new assetDepreciationHistory.
     *
     * @param assetDepreciationHistoryDTO the assetDepreciationHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDepreciationHistoryDTO, or with status {@code 400 (Bad Request)} if the assetDepreciationHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssetDepreciationHistoryDTO> createAssetDepreciationHistory(
        @RequestBody AssetDepreciationHistoryDTO assetDepreciationHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AssetDepreciationHistory : {}", assetDepreciationHistoryDTO);
        if (assetDepreciationHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetDepreciationHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assetDepreciationHistoryDTO = assetDepreciationHistoryService.save(assetDepreciationHistoryDTO);
        return ResponseEntity.created(new URI("/api/asset-depreciation-histories/" + assetDepreciationHistoryDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, assetDepreciationHistoryDTO.getId().toString())
            )
            .body(assetDepreciationHistoryDTO);
    }

    /**
     * {@code PUT  /asset-depreciation-histories/:id} : Updates an existing assetDepreciationHistory.
     *
     * @param id the id of the assetDepreciationHistoryDTO to save.
     * @param assetDepreciationHistoryDTO the assetDepreciationHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDepreciationHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetDepreciationHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDepreciationHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetDepreciationHistoryDTO> updateAssetDepreciationHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetDepreciationHistoryDTO assetDepreciationHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssetDepreciationHistory : {}, {}", id, assetDepreciationHistoryDTO);
        if (assetDepreciationHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDepreciationHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDepreciationHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assetDepreciationHistoryDTO = assetDepreciationHistoryService.update(assetDepreciationHistoryDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetDepreciationHistoryDTO.getId().toString())
            )
            .body(assetDepreciationHistoryDTO);
    }

    /**
     * {@code PATCH  /asset-depreciation-histories/:id} : Partial updates given fields of an existing assetDepreciationHistory, field will ignore if it is null
     *
     * @param id the id of the assetDepreciationHistoryDTO to save.
     * @param assetDepreciationHistoryDTO the assetDepreciationHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDepreciationHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetDepreciationHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetDepreciationHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetDepreciationHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetDepreciationHistoryDTO> partialUpdateAssetDepreciationHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetDepreciationHistoryDTO assetDepreciationHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssetDepreciationHistory partially : {}, {}", id, assetDepreciationHistoryDTO);
        if (assetDepreciationHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDepreciationHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDepreciationHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetDepreciationHistoryDTO> result = assetDepreciationHistoryService.partialUpdate(assetDepreciationHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetDepreciationHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-depreciation-histories} : get all the assetDepreciationHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDepreciationHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssetDepreciationHistoryDTO>> getAllAssetDepreciationHistories(
        AssetDepreciationHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AssetDepreciationHistories by criteria: {}", criteria);

        Page<AssetDepreciationHistoryDTO> page = assetDepreciationHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-depreciation-histories/count} : count all the assetDepreciationHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAssetDepreciationHistories(AssetDepreciationHistoryCriteria criteria) {
        LOG.debug("REST request to count AssetDepreciationHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetDepreciationHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-depreciation-histories/:id} : get the "id" assetDepreciationHistory.
     *
     * @param id the id of the assetDepreciationHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDepreciationHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetDepreciationHistoryDTO> getAssetDepreciationHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AssetDepreciationHistory : {}", id);
        Optional<AssetDepreciationHistoryDTO> assetDepreciationHistoryDTO = assetDepreciationHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDepreciationHistoryDTO);
    }

    /**
     * {@code DELETE  /asset-depreciation-histories/:id} : delete the "id" assetDepreciationHistory.
     *
     * @param id the id of the assetDepreciationHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetDepreciationHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AssetDepreciationHistory : {}", id);
        assetDepreciationHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /asset-depreciation-histories/_search?query=:query} : search for the assetDepreciationHistory corresponding
     * to the query.
     *
     * @param query the query of the assetDepreciationHistory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AssetDepreciationHistoryDTO>> searchAssetDepreciationHistories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AssetDepreciationHistories for query {}", query);
        try {
            Page<AssetDepreciationHistoryDTO> page = assetDepreciationHistoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
