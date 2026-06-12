package com.gvsolutions.repository;

import com.gvsolutions.domain.LiabilityLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LiabilityLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LiabilityLogRepository extends JpaRepository<LiabilityLog, Long>, JpaSpecificationExecutor<LiabilityLog> {}
