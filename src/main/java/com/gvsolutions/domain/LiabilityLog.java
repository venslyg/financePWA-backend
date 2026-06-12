package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.LiabilityType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * Liabilities Log
 */
@Entity
@Table(name = "liability_log")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "liabilitylog")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LiabilityLog extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Column(name = "liability_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String liabilityCode;

    @Column(name = "loan_from")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String loanFrom;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "liability_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private LiabilityType liabilityType;

    @Column(name = "total_loan_amount", precision = 21, scale = 2)
    private BigDecimal totalLoanAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "interest_percentage", precision = 21, scale = 2)
    private BigDecimal interestPercentage;

    @Column(name = "monthly_payment_amount", precision = 21, scale = 2)
    private BigDecimal monthlyPaymentAmount;

    @Column(name = "principal_paid", precision = 21, scale = 2)
    private BigDecimal principalPaid;

    @Column(name = "balance_to_pay", precision = 21, scale = 2)
    private BigDecimal balanceToPay;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ApprovalStatus status;

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

    public LiabilityLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public LiabilityLog branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public LiabilityLog branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getLiabilityCode() {
        return this.liabilityCode;
    }

    public LiabilityLog liabilityCode(String liabilityCode) {
        this.setLiabilityCode(liabilityCode);
        return this;
    }

    public void setLiabilityCode(String liabilityCode) {
        this.liabilityCode = liabilityCode;
    }

    public String getLoanFrom() {
        return this.loanFrom;
    }

    public LiabilityLog loanFrom(String loanFrom) {
        this.setLoanFrom(loanFrom);
        return this;
    }

    public void setLoanFrom(String loanFrom) {
        this.loanFrom = loanFrom;
    }

    public String getDescription() {
        return this.description;
    }

    public LiabilityLog description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LiabilityType getLiabilityType() {
        return this.liabilityType;
    }

    public LiabilityLog liabilityType(LiabilityType liabilityType) {
        this.setLiabilityType(liabilityType);
        return this;
    }

    public void setLiabilityType(LiabilityType liabilityType) {
        this.liabilityType = liabilityType;
    }

    public BigDecimal getTotalLoanAmount() {
        return this.totalLoanAmount;
    }

    public LiabilityLog totalLoanAmount(BigDecimal totalLoanAmount) {
        this.setTotalLoanAmount(totalLoanAmount);
        return this;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LiabilityLog startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public LiabilityLog endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInterestPercentage() {
        return this.interestPercentage;
    }

    public LiabilityLog interestPercentage(BigDecimal interestPercentage) {
        this.setInterestPercentage(interestPercentage);
        return this;
    }

    public void setInterestPercentage(BigDecimal interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public BigDecimal getMonthlyPaymentAmount() {
        return this.monthlyPaymentAmount;
    }

    public LiabilityLog monthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
        this.setMonthlyPaymentAmount(monthlyPaymentAmount);
        return this;
    }

    public void setMonthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
        this.monthlyPaymentAmount = monthlyPaymentAmount;
    }

    public BigDecimal getPrincipalPaid() {
        return this.principalPaid;
    }

    public LiabilityLog principalPaid(BigDecimal principalPaid) {
        this.setPrincipalPaid(principalPaid);
        return this;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getBalanceToPay() {
        return this.balanceToPay;
    }

    public LiabilityLog balanceToPay(BigDecimal balanceToPay) {
        this.setBalanceToPay(balanceToPay);
        return this;
    }

    public void setBalanceToPay(BigDecimal balanceToPay) {
        this.balanceToPay = balanceToPay;
    }

    public ApprovalStatus getStatus() {
        return this.status;
    }

    public LiabilityLog status(ApprovalStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    // Inherited createdBy methods
    public LiabilityLog createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public LiabilityLog createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public LiabilityLog lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public LiabilityLog lastModifiedDate(Instant lastModifiedDate) {
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

    public LiabilityLog setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LiabilityLog)) {
            return false;
        }
        return getId() != null && getId().equals(((LiabilityLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LiabilityLog{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", liabilityCode='" + getLiabilityCode() + "'" +
            ", loanFrom='" + getLoanFrom() + "'" +
            ", description='" + getDescription() + "'" +
            ", liabilityType='" + getLiabilityType() + "'" +
            ", totalLoanAmount=" + getTotalLoanAmount() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", interestPercentage=" + getInterestPercentage() +
            ", monthlyPaymentAmount=" + getMonthlyPaymentAmount() +
            ", principalPaid=" + getPrincipalPaid() +
            ", balanceToPay=" + getBalanceToPay() +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
