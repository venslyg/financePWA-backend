package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A SalaryPayout.
 */
@Entity
@Table(name = "salary_payout")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "salarypayout")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryPayout extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Column(name = "salary_payout_code", unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String salaryPayoutCode;

    @Column(name = "staff_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String staffCode;

    @Column(name = "pay_period")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String payPeriod;

    @Column(name = "base_salary", precision = 21, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "allowances", precision = 21, scale = 2)
    private BigDecimal allowances;

    @Column(name = "deductions", precision = 21, scale = 2)
    private BigDecimal deductions;

    @Column(name = "net_pay", precision = 21, scale = 2)
    private BigDecimal netPay;

    @Column(name = "payout_date")
    private LocalDate payoutDate;

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

    public SalaryPayout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public SalaryPayout branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public SalaryPayout branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getSalaryPayoutCode() {
        return this.salaryPayoutCode;
    }

    public SalaryPayout salaryPayoutCode(String salaryPayoutCode) {
        this.setSalaryPayoutCode(salaryPayoutCode);
        return this;
    }

    public void setSalaryPayoutCode(String salaryPayoutCode) {
        this.salaryPayoutCode = salaryPayoutCode;
    }

    public String getStaffCode() {
        return this.staffCode;
    }

    public SalaryPayout staffCode(String staffCode) {
        this.setStaffCode(staffCode);
        return this;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getPayPeriod() {
        return this.payPeriod;
    }

    public SalaryPayout payPeriod(String payPeriod) {
        this.setPayPeriod(payPeriod);
        return this;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public BigDecimal getBaseSalary() {
        return this.baseSalary;
    }

    public SalaryPayout baseSalary(BigDecimal baseSalary) {
        this.setBaseSalary(baseSalary);
        return this;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getAllowances() {
        return this.allowances;
    }

    public SalaryPayout allowances(BigDecimal allowances) {
        this.setAllowances(allowances);
        return this;
    }

    public void setAllowances(BigDecimal allowances) {
        this.allowances = allowances;
    }

    public BigDecimal getDeductions() {
        return this.deductions;
    }

    public SalaryPayout deductions(BigDecimal deductions) {
        this.setDeductions(deductions);
        return this;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetPay() {
        return this.netPay;
    }

    public SalaryPayout netPay(BigDecimal netPay) {
        this.setNetPay(netPay);
        return this;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public LocalDate getPayoutDate() {
        return this.payoutDate;
    }

    public SalaryPayout payoutDate(LocalDate payoutDate) {
        this.setPayoutDate(payoutDate);
        return this;
    }

    public void setPayoutDate(LocalDate payoutDate) {
        this.payoutDate = payoutDate;
    }

    // Inherited createdBy methods
    public SalaryPayout createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public SalaryPayout createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public SalaryPayout lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public SalaryPayout lastModifiedDate(Instant lastModifiedDate) {
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

    public SalaryPayout setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaryPayout)) {
            return false;
        }
        return getId() != null && getId().equals(((SalaryPayout) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryPayout{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", salaryPayoutCode='" + getSalaryPayoutCode() + "'" +
            ", staffCode='" + getStaffCode() + "'" +
            ", payPeriod='" + getPayPeriod() + "'" +
            ", baseSalary=" + getBaseSalary() +
            ", allowances=" + getAllowances() +
            ", deductions=" + getDeductions() +
            ", netPay=" + getNetPay() +
            ", payoutDate='" + getPayoutDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
