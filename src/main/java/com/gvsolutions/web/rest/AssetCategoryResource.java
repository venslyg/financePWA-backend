package com.gvsolutions.web.rest;

import com.gvsolutions.repository.AssetCategoryRepository;
import com.gvsolutions.service.AssetCategoryQueryService;
import com.gvsolutions.service.AssetCategoryService;
import com.gvsolutions.service.criteria.AssetCategoryCriteria;
import com.gvsolutions.service.dto.AssetCategoryDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.AssetCategory}.
 */
@RestController
@RequestMapping("/api/asset-categories")
public class AssetCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssetCategoryResource.class);

    private static final String ENTITY_NAME = "assetCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetCategoryService assetCategoryService;

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryQueryService assetCategoryQueryService;

    public AssetCategoryResource(
        AssetCategoryService assetCategoryService,
        AssetCategoryRepository assetCategoryRepository,
        AssetCategoryQueryService assetCategoryQueryService
    ) {
        this.assetCategoryService = assetCategoryService;
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryQueryService = assetCategoryQueryService;
    }

    /**
     * {@code POST  /asset-categories} : Create a new assetCategory.
     *
     * @param assetCategoryDTO the assetCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetCategoryDTO, or with status {@code 400 (Bad Request)} if the assetCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssetCategoryDTO> createAssetCategory(@RequestBody AssetCategoryDTO assetCategoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save AssetCategory : {}", assetCategoryDTO);
        if (assetCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assetCategoryDTO = assetCategoryService.save(assetCategoryDTO);
        return ResponseEntity.created(new URI("/api/asset-categories/" + assetCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, assetCategoryDTO.getId().toString()))
            .body(assetCategoryDTO);
    }

    /**
     * {@code PUT  /asset-categories/:id} : Updates an existing assetCategory.
     *
     * @param id the id of the assetCategoryDTO to save.
     * @param assetCategoryDTO the assetCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetCategoryDTO> updateAssetCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetCategoryDTO assetCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssetCategory : {}, {}", id, assetCategoryDTO);
        if (assetCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assetCategoryDTO = assetCategoryService.update(assetCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetCategoryDTO.getId().toString()))
            .body(assetCategoryDTO);
    }

    /**
     * {@code PATCH  /asset-categories/:id} : Partial updates given fields of an existing assetCategory, field will ignore if it is null
     *
     * @param id the id of the assetCategoryDTO to save.
     * @param assetCategoryDTO the assetCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetCategoryDTO> partialUpdateAssetCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetCategoryDTO assetCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssetCategory partially : {}, {}", id, assetCategoryDTO);
        if (assetCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetCategoryDTO> result = assetCategoryService.partialUpdate(assetCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-categories} : get all the assetCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssetCategoryDTO>> getAllAssetCategories(
        AssetCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AssetCategories by criteria: {}", criteria);

        Page<AssetCategoryDTO> page = assetCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-categories/count} : count all the assetCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAssetCategories(AssetCategoryCriteria criteria) {
        LOG.debug("REST request to count AssetCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-categories/:id} : get the "id" assetCategory.
     *
     * @param id the id of the assetCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetCategoryDTO> getAssetCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AssetCategory : {}", id);
        Optional<AssetCategoryDTO> assetCategoryDTO = assetCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetCategoryDTO);
    }

    /**
     * {@code DELETE  /asset-categories/:id} : delete the "id" assetCategory.
     *
     * @param id the id of the assetCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AssetCategory : {}", id);
        assetCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /asset-categories/_search?query=:query} : search for the assetCategory corresponding
     * to the query.
     *
     * @param query the query of the assetCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AssetCategoryDTO>> searchAssetCategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AssetCategories for query {}", query);
        try {
            Page<AssetCategoryDTO> page = assetCategoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
