package com.gvsolutions.repository;

import com.gvsolutions.domain.MaintenanceLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaintenanceLog entity.
 */
@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long>, JpaSpecificationExecutor<MaintenanceLog> {
    default Optional<MaintenanceLog> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MaintenanceLog> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MaintenanceLog> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select maintenanceLog from MaintenanceLog maintenanceLog left join fetch maintenanceLog.asset",
        countQuery = "select count(maintenanceLog) from MaintenanceLog maintenanceLog"
    )
    Page<MaintenanceLog> findAllWithToOneRelationships(Pageable pageable);

    @Query("select maintenanceLog from MaintenanceLog maintenanceLog left join fetch maintenanceLog.asset")
    List<MaintenanceLog> findAllWithToOneRelationships();

    @Query("select maintenanceLog from MaintenanceLog maintenanceLog left join fetch maintenanceLog.asset where maintenanceLog.id =:id")
    Optional<MaintenanceLog> findOneWithToOneRelationships(@Param("id") Long id);
}
