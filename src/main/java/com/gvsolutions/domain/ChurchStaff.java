package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.StaffType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Persistable;

/**
 * Staff and Worker Directories tracking variable skill roles
 */
@Entity
@Table(name = "church_staff")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "churchstaff")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChurchStaff extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "staff_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String staffCode;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "branch_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchId;

    @Column(name = "full_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fullName;

    @Column(name = "position")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private StaffType staffType;

    @Column(name = "contact_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contactNumber;

    @Column(name = "hourly_rate_or_monthly_salary", precision = 21, scale = 2)
    private BigDecimal hourlyRateOrMonthlySalary;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChurchStaff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffCode() {
        return this.staffCode;
    }

    public ChurchStaff staffCode(String staffCode) {
        this.setStaffCode(staffCode);
        return this;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public ChurchStaff branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public ChurchStaff branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public ChurchStaff fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return this.position;
    }

    public ChurchStaff position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public StaffType getStaffType() {
        return this.staffType;
    }

    public ChurchStaff staffType(StaffType staffType) {
        this.setStaffType(staffType);
        return this;
    }

    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public ChurchStaff contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public BigDecimal getHourlyRateOrMonthlySalary() {
        return this.hourlyRateOrMonthlySalary;
    }

    public ChurchStaff hourlyRateOrMonthlySalary(BigDecimal hourlyRateOrMonthlySalary) {
        this.setHourlyRateOrMonthlySalary(hourlyRateOrMonthlySalary);
        return this;
    }

    public void setHourlyRateOrMonthlySalary(BigDecimal hourlyRateOrMonthlySalary) {
        this.hourlyRateOrMonthlySalary = hourlyRateOrMonthlySalary;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ChurchStaff isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Inherited createdBy methods
    public ChurchStaff createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public ChurchStaff createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public ChurchStaff lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public ChurchStaff lastModifiedDate(Instant lastModifiedDate) {
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

    public ChurchStaff setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChurchStaff)) {
            return false;
        }
        return getId() != null && getId().equals(((ChurchStaff) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChurchStaff{" +
            "id=" + getId() +
            ", staffCode='" + getStaffCode() + "'" +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", position='" + getPosition() + "'" +
            ", staffType='" + getStaffType() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", hourlyRateOrMonthlySalary=" + getHourlyRateOrMonthlySalary() +
            ", isActive='" + getIsActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
