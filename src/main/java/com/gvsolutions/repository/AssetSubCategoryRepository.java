package com.gvsolutions.repository;

import com.gvsolutions.domain.AssetSubCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssetSubCategory entity.
 */
@Repository
public interface AssetSubCategoryRepository extends JpaRepository<AssetSubCategory, Long>, JpaSpecificationExecutor<AssetSubCategory> {
    default Optional<AssetSubCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AssetSubCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AssetSubCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select assetSubCategory from AssetSubCategory assetSubCategory left join fetch assetSubCategory.category",
        countQuery = "select count(assetSubCategory) from AssetSubCategory assetSubCategory"
    )
    Page<AssetSubCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select assetSubCategory from AssetSubCategory assetSubCategory left join fetch assetSubCategory.category")
    List<AssetSubCategory> findAllWithToOneRelationships();

    @Query(
        "select assetSubCategory from AssetSubCategory assetSubCategory left join fetch assetSubCategory.category where assetSubCategory.id =:id"
    )
    Optional<AssetSubCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
