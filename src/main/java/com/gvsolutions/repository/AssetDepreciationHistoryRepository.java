package com.gvsolutions.repository;

import com.gvsolutions.domain.AssetDepreciationHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssetDepreciationHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetDepreciationHistoryRepository
    extends JpaRepository<AssetDepreciationHistory, Long>, JpaSpecificationExecutor<AssetDepreciationHistory> {}
