package com.gvsolutions.repository;

import com.gvsolutions.domain.AssetRegister;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssetRegister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetRegisterRepository extends JpaRepository<AssetRegister, Long>, JpaSpecificationExecutor<AssetRegister> {}
