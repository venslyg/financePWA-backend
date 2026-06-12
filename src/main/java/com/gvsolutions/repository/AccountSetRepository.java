package com.gvsolutions.repository;

import com.gvsolutions.domain.AccountSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountSetRepository extends JpaRepository<AccountSet, Long>, JpaSpecificationExecutor<AccountSet> {}
