package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.domain.Persistable;

/**
 * A AssetSubCategory.
 */
@Entity
@Table(name = "asset_sub_category")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetsubcategory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetSubCategory extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "branch_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchId;

    @Column(name = "asset_category_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetCategoryCode;

    @Column(name = "asset_sub_category_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetSubCategoryCode;

    @Column(name = "asset_sub_category_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetSubCategoryName;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    private AssetCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetSubCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public AssetSubCategory branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public AssetSubCategory branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getAssetCategoryCode() {
        return this.assetCategoryCode;
    }

    public AssetSubCategory assetCategoryCode(String assetCategoryCode) {
        this.setAssetCategoryCode(assetCategoryCode);
        return this;
    }

    public void setAssetCategoryCode(String assetCategoryCode) {
        this.assetCategoryCode = assetCategoryCode;
    }

    public String getAssetSubCategoryCode() {
        return this.assetSubCategoryCode;
    }

    public AssetSubCategory assetSubCategoryCode(String assetSubCategoryCode) {
        this.setAssetSubCategoryCode(assetSubCategoryCode);
        return this;
    }

    public void setAssetSubCategoryCode(String assetSubCategoryCode) {
        this.assetSubCategoryCode = assetSubCategoryCode;
    }

    public String getAssetSubCategoryName() {
        return this.assetSubCategoryName;
    }

    public AssetSubCategory assetSubCategoryName(String assetSubCategoryName) {
        this.setAssetSubCategoryName(assetSubCategoryName);
        return this;
    }

    public void setAssetSubCategoryName(String assetSubCategoryName) {
        this.assetSubCategoryName = assetSubCategoryName;
    }

    // Inherited createdBy methods
    public AssetSubCategory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public AssetSubCategory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public AssetSubCategory lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public AssetSubCategory lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AssetSubCategory setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public AssetCategory getCategory() {
        return this.category;
    }

    public void setCategory(AssetCategory assetCategory) {
        this.category = assetCategory;
    }

    public AssetSubCategory category(AssetCategory assetCategory) {
        this.setCategory(assetCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetSubCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((AssetSubCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetSubCategory{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", assetCategoryCode='" + getAssetCategoryCode() + "'" +
            ", assetSubCategoryCode='" + getAssetSubCategoryCode() + "'" +
            ", assetSubCategoryName='" + getAssetSubCategoryName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
