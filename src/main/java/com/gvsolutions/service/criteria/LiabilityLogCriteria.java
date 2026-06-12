package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.LiabilityType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.LiabilityLog} entity. This class is used
 * in {@link com.gvsolutions.web.rest.LiabilityLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /liability-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LiabilityLogCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LiabilityType
     */
    public static class LiabilityTypeFilter extends Filter<LiabilityType> {

        public LiabilityTypeFilter() {}

        public LiabilityTypeFilter(LiabilityTypeFilter filter) {
            super(filter);
        }

        @Override
        public LiabilityTypeFilter copy() {
            return new LiabilityTypeFilter(this);
        }
    }

    /**
     * Class for filtering ApprovalStatus
     */
    public static class ApprovalStatusFilter extends Filter<ApprovalStatus> {

        public ApprovalStatusFilter() {}

        public ApprovalStatusFilter(ApprovalStatusFilter filter) {
            super(filter);
        }

        @Override
        public ApprovalStatusFilter copy() {
            return new ApprovalStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter liabilityCode;

    private StringFilter loanFrom;

    private StringFilter description;

    private LiabilityTypeFilter liabilityType;

    private BigDecimalFilter totalLoanAmount;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BigDecimalFilter interestPercentage;

    private BigDecimalFilter monthlyPaymentAmount;

    private BigDecimalFilter principalPaid;

    private BigDecimalFilter balanceToPay;

    private ApprovalStatusFilter status;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public LiabilityLogCriteria() {}

    public LiabilityLogCriteria(LiabilityLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.liabilityCode = other.optionalLiabilityCode().map(StringFilter::copy).orElse(null);
        this.loanFrom = other.optionalLoanFrom().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.liabilityType = other.optionalLiabilityType().map(LiabilityTypeFilter::copy).orElse(null);
        this.totalLoanAmount = other.optionalTotalLoanAmount().map(BigDecimalFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.interestPercentage = other.optionalInterestPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.monthlyPaymentAmount = other.optionalMonthlyPaymentAmount().map(BigDecimalFilter::copy).orElse(null);
        this.principalPaid = other.optionalPrincipalPaid().map(BigDecimalFilter::copy).orElse(null);
        this.balanceToPay = other.optionalBalanceToPay().map(BigDecimalFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ApprovalStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LiabilityLogCriteria copy() {
        return new LiabilityLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBranchCode() {
        return branchCode;
    }

    public Optional<StringFilter> optionalBranchCode() {
        return Optional.ofNullable(branchCode);
    }

    public StringFilter branchCode() {
        if (branchCode == null) {
            setBranchCode(new StringFilter());
        }
        return branchCode;
    }

    public void setBranchCode(StringFilter branchCode) {
        this.branchCode = branchCode;
    }

    public StringFilter getBranchId() {
        return branchId;
    }

    public Optional<StringFilter> optionalBranchId() {
        return Optional.ofNullable(branchId);
    }

    public StringFilter branchId() {
        if (branchId == null) {
            setBranchId(new StringFilter());
        }
        return branchId;
    }

    public void setBranchId(StringFilter branchId) {
        this.branchId = branchId;
    }

    public StringFilter getLiabilityCode() {
        return liabilityCode;
    }

    public Optional<StringFilter> optionalLiabilityCode() {
        return Optional.ofNullable(liabilityCode);
    }

    public StringFilter liabilityCode() {
        if (liabilityCode == null) {
            setLiabilityCode(new StringFilter());
        }
        return liabilityCode;
    }

    public void setLiabilityCode(StringFilter liabilityCode) {
        this.liabilityCode = liabilityCode;
    }

    public StringFilter getLoanFrom() {
        return loanFrom;
    }

    public Optional<StringFilter> optionalLoanFrom() {
        return Optional.ofNullable(loanFrom);
    }

    public StringFilter loanFrom() {
        if (loanFrom == null) {
            setLoanFrom(new StringFilter());
        }
        return loanFrom;
    }

    public void setLoanFrom(StringFilter loanFrom) {
        this.loanFrom = loanFrom;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LiabilityTypeFilter getLiabilityType() {
        return liabilityType;
    }

    public Optional<LiabilityTypeFilter> optionalLiabilityType() {
        return Optional.ofNullable(liabilityType);
    }

    public LiabilityTypeFilter liabilityType() {
        if (liabilityType == null) {
            setLiabilityType(new LiabilityTypeFilter());
        }
        return liabilityType;
    }

    public void setLiabilityType(LiabilityTypeFilter liabilityType) {
        this.liabilityType = liabilityType;
    }

    public BigDecimalFilter getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalLoanAmount() {
        return Optional.ofNullable(totalLoanAmount);
    }

    public BigDecimalFilter totalLoanAmount() {
        if (totalLoanAmount == null) {
            setTotalLoanAmount(new BigDecimalFilter());
        }
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimalFilter totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BigDecimalFilter getInterestPercentage() {
        return interestPercentage;
    }

    public Optional<BigDecimalFilter> optionalInterestPercentage() {
        return Optional.ofNullable(interestPercentage);
    }

    public BigDecimalFilter interestPercentage() {
        if (interestPercentage == null) {
            setInterestPercentage(new BigDecimalFilter());
        }
        return interestPercentage;
    }

    public void setInterestPercentage(BigDecimalFilter interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public BigDecimalFilter getMonthlyPaymentAmount() {
        return monthlyPaymentAmount;
    }

    public Optional<BigDecimalFilter> optionalMonthlyPaymentAmount() {
        return Optional.ofNullable(monthlyPaymentAmount);
    }

    public BigDecimalFilter monthlyPaymentAmount() {
        if (monthlyPaymentAmount == null) {
            setMonthlyPaymentAmount(new BigDecimalFilter());
        }
        return monthlyPaymentAmount;
    }

    public void setMonthlyPaymentAmount(BigDecimalFilter monthlyPaymentAmount) {
        this.monthlyPaymentAmount = monthlyPaymentAmount;
    }

    public BigDecimalFilter getPrincipalPaid() {
        return principalPaid;
    }

    public Optional<BigDecimalFilter> optionalPrincipalPaid() {
        return Optional.ofNullable(principalPaid);
    }

    public BigDecimalFilter principalPaid() {
        if (principalPaid == null) {
            setPrincipalPaid(new BigDecimalFilter());
        }
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimalFilter principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimalFilter getBalanceToPay() {
        return balanceToPay;
    }

    public Optional<BigDecimalFilter> optionalBalanceToPay() {
        return Optional.ofNullable(balanceToPay);
    }

    public BigDecimalFilter balanceToPay() {
        if (balanceToPay == null) {
            setBalanceToPay(new BigDecimalFilter());
        }
        return balanceToPay;
    }

    public void setBalanceToPay(BigDecimalFilter balanceToPay) {
        this.balanceToPay = balanceToPay;
    }

    public ApprovalStatusFilter getStatus() {
        return status;
    }

    public Optional<ApprovalStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ApprovalStatusFilter status() {
        if (status == null) {
            setStatus(new ApprovalStatusFilter());
        }
        return status;
    }

    public void setStatus(ApprovalStatusFilter status) {
        this.status = status;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LiabilityLogCriteria that = (LiabilityLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(liabilityCode, that.liabilityCode) &&
            Objects.equals(loanFrom, that.loanFrom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(liabilityType, that.liabilityType) &&
            Objects.equals(totalLoanAmount, that.totalLoanAmount) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(interestPercentage, that.interestPercentage) &&
            Objects.equals(monthlyPaymentAmount, that.monthlyPaymentAmount) &&
            Objects.equals(principalPaid, that.principalPaid) &&
            Objects.equals(balanceToPay, that.balanceToPay) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            branchCode,
            branchId,
            liabilityCode,
            loanFrom,
            description,
            liabilityType,
            totalLoanAmount,
            startDate,
            endDate,
            interestPercentage,
            monthlyPaymentAmount,
            principalPaid,
            balanceToPay,
            status,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LiabilityLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalLiabilityCode().map(f -> "liabilityCode=" + f + ", ").orElse("") +
            optionalLoanFrom().map(f -> "loanFrom=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalLiabilityType().map(f -> "liabilityType=" + f + ", ").orElse("") +
            optionalTotalLoanAmount().map(f -> "totalLoanAmount=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalInterestPercentage().map(f -> "interestPercentage=" + f + ", ").orElse("") +
            optionalMonthlyPaymentAmount().map(f -> "monthlyPaymentAmount=" + f + ", ").orElse("") +
            optionalPrincipalPaid().map(f -> "principalPaid=" + f + ", ").orElse("") +
            optionalBalanceToPay().map(f -> "balanceToPay=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
