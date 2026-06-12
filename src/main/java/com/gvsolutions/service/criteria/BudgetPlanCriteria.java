package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.BudgetAlertStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.BudgetPlan} entity. This class is used
 * in {@link com.gvsolutions.web.rest.BudgetPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /budget-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetPlanCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BudgetAlertStatus
     */
    public static class BudgetAlertStatusFilter extends Filter<BudgetAlertStatus> {

        public BudgetAlertStatusFilter() {}

        public BudgetAlertStatusFilter(BudgetAlertStatusFilter filter) {
            super(filter);
        }

        @Override
        public BudgetAlertStatusFilter copy() {
            return new BudgetAlertStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter accountCode;

    private StringFilter budgetPlanCode;

    private StringFilter departmentName;

    private IntegerFilter year;

    private BigDecimalFilter allocatedAmount;

    private BigDecimalFilter spentAmount;

    private BigDecimalFilter remainingAmount;

    private BigDecimalFilter usedPercentage;

    private BudgetAlertStatusFilter alertStatus;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public BudgetPlanCriteria() {}

    public BudgetPlanCriteria(BudgetPlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.accountCode = other.optionalAccountCode().map(StringFilter::copy).orElse(null);
        this.budgetPlanCode = other.optionalBudgetPlanCode().map(StringFilter::copy).orElse(null);
        this.departmentName = other.optionalDepartmentName().map(StringFilter::copy).orElse(null);
        this.year = other.optionalYear().map(IntegerFilter::copy).orElse(null);
        this.allocatedAmount = other.optionalAllocatedAmount().map(BigDecimalFilter::copy).orElse(null);
        this.spentAmount = other.optionalSpentAmount().map(BigDecimalFilter::copy).orElse(null);
        this.remainingAmount = other.optionalRemainingAmount().map(BigDecimalFilter::copy).orElse(null);
        this.usedPercentage = other.optionalUsedPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.alertStatus = other.optionalAlertStatus().map(BudgetAlertStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BudgetPlanCriteria copy() {
        return new BudgetPlanCriteria(this);
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

    public StringFilter getAccountCode() {
        return accountCode;
    }

    public Optional<StringFilter> optionalAccountCode() {
        return Optional.ofNullable(accountCode);
    }

    public StringFilter accountCode() {
        if (accountCode == null) {
            setAccountCode(new StringFilter());
        }
        return accountCode;
    }

    public void setAccountCode(StringFilter accountCode) {
        this.accountCode = accountCode;
    }

    public StringFilter getBudgetPlanCode() {
        return budgetPlanCode;
    }

    public Optional<StringFilter> optionalBudgetPlanCode() {
        return Optional.ofNullable(budgetPlanCode);
    }

    public StringFilter budgetPlanCode() {
        if (budgetPlanCode == null) {
            setBudgetPlanCode(new StringFilter());
        }
        return budgetPlanCode;
    }

    public void setBudgetPlanCode(StringFilter budgetPlanCode) {
        this.budgetPlanCode = budgetPlanCode;
    }

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public Optional<StringFilter> optionalDepartmentName() {
        return Optional.ofNullable(departmentName);
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            setDepartmentName(new StringFilter());
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public Optional<IntegerFilter> optionalYear() {
        return Optional.ofNullable(year);
    }

    public IntegerFilter year() {
        if (year == null) {
            setYear(new IntegerFilter());
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public BigDecimalFilter getAllocatedAmount() {
        return allocatedAmount;
    }

    public Optional<BigDecimalFilter> optionalAllocatedAmount() {
        return Optional.ofNullable(allocatedAmount);
    }

    public BigDecimalFilter allocatedAmount() {
        if (allocatedAmount == null) {
            setAllocatedAmount(new BigDecimalFilter());
        }
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimalFilter allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimalFilter getSpentAmount() {
        return spentAmount;
    }

    public Optional<BigDecimalFilter> optionalSpentAmount() {
        return Optional.ofNullable(spentAmount);
    }

    public BigDecimalFilter spentAmount() {
        if (spentAmount == null) {
            setSpentAmount(new BigDecimalFilter());
        }
        return spentAmount;
    }

    public void setSpentAmount(BigDecimalFilter spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimalFilter getRemainingAmount() {
        return remainingAmount;
    }

    public Optional<BigDecimalFilter> optionalRemainingAmount() {
        return Optional.ofNullable(remainingAmount);
    }

    public BigDecimalFilter remainingAmount() {
        if (remainingAmount == null) {
            setRemainingAmount(new BigDecimalFilter());
        }
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimalFilter remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimalFilter getUsedPercentage() {
        return usedPercentage;
    }

    public Optional<BigDecimalFilter> optionalUsedPercentage() {
        return Optional.ofNullable(usedPercentage);
    }

    public BigDecimalFilter usedPercentage() {
        if (usedPercentage == null) {
            setUsedPercentage(new BigDecimalFilter());
        }
        return usedPercentage;
    }

    public void setUsedPercentage(BigDecimalFilter usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public BudgetAlertStatusFilter getAlertStatus() {
        return alertStatus;
    }

    public Optional<BudgetAlertStatusFilter> optionalAlertStatus() {
        return Optional.ofNullable(alertStatus);
    }

    public BudgetAlertStatusFilter alertStatus() {
        if (alertStatus == null) {
            setAlertStatus(new BudgetAlertStatusFilter());
        }
        return alertStatus;
    }

    public void setAlertStatus(BudgetAlertStatusFilter alertStatus) {
        this.alertStatus = alertStatus;
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
        final BudgetPlanCriteria that = (BudgetPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(accountCode, that.accountCode) &&
            Objects.equals(budgetPlanCode, that.budgetPlanCode) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(year, that.year) &&
            Objects.equals(allocatedAmount, that.allocatedAmount) &&
            Objects.equals(spentAmount, that.spentAmount) &&
            Objects.equals(remainingAmount, that.remainingAmount) &&
            Objects.equals(usedPercentage, that.usedPercentage) &&
            Objects.equals(alertStatus, that.alertStatus) &&
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
            accountCode,
            budgetPlanCode,
            departmentName,
            year,
            allocatedAmount,
            spentAmount,
            remainingAmount,
            usedPercentage,
            alertStatus,
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
        return "BudgetPlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalAccountCode().map(f -> "accountCode=" + f + ", ").orElse("") +
            optionalBudgetPlanCode().map(f -> "budgetPlanCode=" + f + ", ").orElse("") +
            optionalDepartmentName().map(f -> "departmentName=" + f + ", ").orElse("") +
            optionalYear().map(f -> "year=" + f + ", ").orElse("") +
            optionalAllocatedAmount().map(f -> "allocatedAmount=" + f + ", ").orElse("") +
            optionalSpentAmount().map(f -> "spentAmount=" + f + ", ").orElse("") +
            optionalRemainingAmount().map(f -> "remainingAmount=" + f + ", ").orElse("") +
            optionalUsedPercentage().map(f -> "usedPercentage=" + f + ", ").orElse("") +
            optionalAlertStatus().map(f -> "alertStatus=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
