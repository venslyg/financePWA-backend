package com.gvsolutions.repository;

import com.gvsolutions.domain.ExpenseSubCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExpenseSubCategory entity.
 */
@Repository
public interface ExpenseSubCategoryRepository
    extends JpaRepository<ExpenseSubCategory, Long>, JpaSpecificationExecutor<ExpenseSubCategory> {
    default Optional<ExpenseSubCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ExpenseSubCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ExpenseSubCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select expenseSubCategory from ExpenseSubCategory expenseSubCategory left join fetch expenseSubCategory.category",
        countQuery = "select count(expenseSubCategory) from ExpenseSubCategory expenseSubCategory"
    )
    Page<ExpenseSubCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select expenseSubCategory from ExpenseSubCategory expenseSubCategory left join fetch expenseSubCategory.category")
    List<ExpenseSubCategory> findAllWithToOneRelationships();

    @Query(
        "select expenseSubCategory from ExpenseSubCategory expenseSubCategory left join fetch expenseSubCategory.category where expenseSubCategory.id =:id"
    )
    Optional<ExpenseSubCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
