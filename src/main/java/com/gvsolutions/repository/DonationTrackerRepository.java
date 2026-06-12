package com.gvsolutions.repository;

import com.gvsolutions.domain.DonationTracker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DonationTracker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationTrackerRepository extends JpaRepository<DonationTracker, Long>, JpaSpecificationExecutor<DonationTracker> {}
