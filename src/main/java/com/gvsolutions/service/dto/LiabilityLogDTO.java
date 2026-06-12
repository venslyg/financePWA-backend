package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.LiabilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.LiabilityLog} entity.
 */
@Schema(description = "Liabilities Log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LiabilityLogDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String liabilityCode;

    private String loanFrom;

    private String description;

    private LiabilityType liabilityType;

    private BigDecimal totalLoanAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal interestPercentage;

    private BigDecimal monthlyPaymentAmount;

    private BigDecimal principalPaid;

    private BigDecimal balanceToPay;

    private ApprovalStatus status;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getLiabilityCode() {
        return liabilityCode;
    }

    public void setLiabilityCode(String liabilityCode) {
        this.liabilityCode = liabilityCode;
    }

    public String getLoanFrom() {
        return loanFrom;
    }

    public void setLoanFrom(String loanFrom) {
        this.loanFrom = loanFrom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LiabilityType getLiabilityType() {
        return liabilityType;
    }

    public void setLiabilityType(LiabilityType liabilityType) {
        this.liabilityType = liabilityType;
    }

    public BigDecimal getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(BigDecimal interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public BigDecimal getMonthlyPaymentAmount() {
        return monthlyPaymentAmount;
    }

    public void setMonthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
        this.monthlyPaymentAmount = monthlyPaymentAmount;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getBalanceToPay() {
        return balanceToPay;
    }

    public void setBalanceToPay(BigDecimal balanceToPay) {
        this.balanceToPay = balanceToPay;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LiabilityLogDTO)) {
            return false;
        }

        LiabilityLogDTO liabilityLogDTO = (LiabilityLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, liabilityLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LiabilityLogDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
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
