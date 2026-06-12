package com.gvsolutions.repository;

import com.gvsolutions.domain.ChurchStaff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChurchStaff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchStaffRepository extends JpaRepository<ChurchStaff, Long>, JpaSpecificationExecutor<ChurchStaff> {}
