package com.gvsolutions.web.rest;

import com.gvsolutions.repository.AssetSubCategoryRepository;
import com.gvsolutions.service.AssetSubCategoryQueryService;
import com.gvsolutions.service.AssetSubCategoryService;
import com.gvsolutions.service.criteria.AssetSubCategoryCriteria;
import com.gvsolutions.service.dto.AssetSubCategoryDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.AssetSubCategory}.
 */
@RestController
@RequestMapping("/api/asset-sub-categories")
public class AssetSubCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssetSubCategoryResource.class);

    private static final String ENTITY_NAME = "assetSubCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetSubCategoryService assetSubCategoryService;

    private final AssetSubCategoryRepository assetSubCategoryRepository;

    private final AssetSubCategoryQueryService assetSubCategoryQueryService;

    public AssetSubCategoryResource(
        AssetSubCategoryService assetSubCategoryService,
        AssetSubCategoryRepository assetSubCategoryRepository,
        AssetSubCategoryQueryService assetSubCategoryQueryService
    ) {
        this.assetSubCategoryService = assetSubCategoryService;
        this.assetSubCategoryRepository = assetSubCategoryRepository;
        this.assetSubCategoryQueryService = assetSubCategoryQueryService;
    }

    /**
     * {@code POST  /asset-sub-categories} : Create a new assetSubCategory.
     *
     * @param assetSubCategoryDTO the assetSubCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetSubCategoryDTO, or with status {@code 400 (Bad Request)} if the assetSubCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssetSubCategoryDTO> createAssetSubCategory(@RequestBody AssetSubCategoryDTO assetSubCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AssetSubCategory : {}", assetSubCategoryDTO);
        if (assetSubCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetSubCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assetSubCategoryDTO = assetSubCategoryService.save(assetSubCategoryDTO);
        return ResponseEntity.created(new URI("/api/asset-sub-categories/" + assetSubCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, assetSubCategoryDTO.getId().toString()))
            .body(assetSubCategoryDTO);
    }

    /**
     * {@code PUT  /asset-sub-categories/:id} : Updates an existing assetSubCategory.
     *
     * @param id the id of the assetSubCategoryDTO to save.
     * @param assetSubCategoryDTO the assetSubCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetSubCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetSubCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetSubCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetSubCategoryDTO> updateAssetSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetSubCategoryDTO assetSubCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssetSubCategory : {}, {}", id, assetSubCategoryDTO);
        if (assetSubCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetSubCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetSubCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assetSubCategoryDTO = assetSubCategoryService.update(assetSubCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetSubCategoryDTO.getId().toString()))
            .body(assetSubCategoryDTO);
    }

    /**
     * {@code PATCH  /asset-sub-categories/:id} : Partial updates given fields of an existing assetSubCategory, field will ignore if it is null
     *
     * @param id the id of the assetSubCategoryDTO to save.
     * @param assetSubCategoryDTO the assetSubCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetSubCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetSubCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetSubCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetSubCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetSubCategoryDTO> partialUpdateAssetSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetSubCategoryDTO assetSubCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssetSubCategory partially : {}, {}", id, assetSubCategoryDTO);
        if (assetSubCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetSubCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetSubCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetSubCategoryDTO> result = assetSubCategoryService.partialUpdate(assetSubCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetSubCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-sub-categories} : get all the assetSubCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetSubCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssetSubCategoryDTO>> getAllAssetSubCategories(
        AssetSubCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AssetSubCategories by criteria: {}", criteria);

        Page<AssetSubCategoryDTO> page = assetSubCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-sub-categories/count} : count all the assetSubCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAssetSubCategories(AssetSubCategoryCriteria criteria) {
        LOG.debug("REST request to count AssetSubCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetSubCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-sub-categories/:id} : get the "id" assetSubCategory.
     *
     * @param id the id of the assetSubCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetSubCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetSubCategoryDTO> getAssetSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AssetSubCategory : {}", id);
        Optional<AssetSubCategoryDTO> assetSubCategoryDTO = assetSubCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetSubCategoryDTO);
    }

    /**
     * {@code DELETE  /asset-sub-categories/:id} : delete the "id" assetSubCategory.
     *
     * @param id the id of the assetSubCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AssetSubCategory : {}", id);
        assetSubCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /asset-sub-categories/_search?query=:query} : search for the assetSubCategory corresponding
     * to the query.
     *
     * @param query the query of the assetSubCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AssetSubCategoryDTO>> searchAssetSubCategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AssetSubCategories for query {}", query);
        try {
            Page<AssetSubCategoryDTO> page = assetSubCategoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
