package com.gvsolutions.repository;

import com.gvsolutions.domain.AssetCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssetCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long>, JpaSpecificationExecutor<AssetCategory> {}
