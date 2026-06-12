package com.gvsolutions.repository;

import com.gvsolutions.domain.BinCardLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BinCardLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BinCardLineRepository extends JpaRepository<BinCardLine, Long>, JpaSpecificationExecutor<BinCardLine> {}
