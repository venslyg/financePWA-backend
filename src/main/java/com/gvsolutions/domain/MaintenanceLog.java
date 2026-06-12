package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.MaintenanceLogType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * Asset Maintenance Log
 */
@Entity
@Table(name = "maintenance_log")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "maintenancelog")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceLog extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "maintenance_log_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maintenanceLogCode;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MaintenanceLogType logType;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    @Column(name = "vendor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vendor;

    @Column(name = "next_service_date")
    private LocalDate nextServiceDate;

    @Column(name = "note")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String note;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    private AssetRegister asset;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaintenanceLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaintenanceLogCode() {
        return this.maintenanceLogCode;
    }

    public MaintenanceLog maintenanceLogCode(String maintenanceLogCode) {
        this.setMaintenanceLogCode(maintenanceLogCode);
        return this;
    }

    public void setMaintenanceLogCode(String maintenanceLogCode) {
        this.maintenanceLogCode = maintenanceLogCode;
    }

    public LocalDate getLogDate() {
        return this.logDate;
    }

    public MaintenanceLog logDate(LocalDate logDate) {
        this.setLogDate(logDate);
        return this;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public MaintenanceLogType getLogType() {
        return this.logType;
    }

    public MaintenanceLog logType(MaintenanceLogType logType) {
        this.setLogType(logType);
        return this;
    }

    public void setLogType(MaintenanceLogType logType) {
        this.logType = logType;
    }

    public String getDescription() {
        return this.description;
    }

    public MaintenanceLog description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public MaintenanceLog cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getVendor() {
        return this.vendor;
    }

    public MaintenanceLog vendor(String vendor) {
        this.setVendor(vendor);
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getNextServiceDate() {
        return this.nextServiceDate;
    }

    public MaintenanceLog nextServiceDate(LocalDate nextServiceDate) {
        this.setNextServiceDate(nextServiceDate);
        return this;
    }

    public void setNextServiceDate(LocalDate nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public String getNote() {
        return this.note;
    }

    public MaintenanceLog note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Inherited createdBy methods
    public MaintenanceLog createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public MaintenanceLog createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public MaintenanceLog lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public MaintenanceLog lastModifiedDate(Instant lastModifiedDate) {
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

    public MaintenanceLog setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public AssetRegister getAsset() {
        return this.asset;
    }

    public void setAsset(AssetRegister assetRegister) {
        this.asset = assetRegister;
    }

    public MaintenanceLog asset(AssetRegister assetRegister) {
        this.setAsset(assetRegister);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintenanceLog)) {
            return false;
        }
        return getId() != null && getId().equals(((MaintenanceLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceLog{" +
            "id=" + getId() +
            ", maintenanceLogCode='" + getMaintenanceLogCode() + "'" +
            ", logDate='" + getLogDate() + "'" +
            ", logType='" + getLogType() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            ", vendor='" + getVendor() + "'" +
            ", nextServiceDate='" + getNextServiceDate() + "'" +
            ", note='" + getNote() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
