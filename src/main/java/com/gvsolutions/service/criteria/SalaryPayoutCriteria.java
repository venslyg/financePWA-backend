package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.SalaryPayout} entity. This class is used
 * in {@link com.gvsolutions.web.rest.SalaryPayoutResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /salary-payouts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryPayoutCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter salaryPayoutCode;

    private StringFilter staffCode;

    private StringFilter payPeriod;

    private BigDecimalFilter baseSalary;

    private BigDecimalFilter allowances;

    private BigDecimalFilter deductions;

    private BigDecimalFilter netPay;

    private LocalDateFilter payoutDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public SalaryPayoutCriteria() {}

    public SalaryPayoutCriteria(SalaryPayoutCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.salaryPayoutCode = other.optionalSalaryPayoutCode().map(StringFilter::copy).orElse(null);
        this.staffCode = other.optionalStaffCode().map(StringFilter::copy).orElse(null);
        this.payPeriod = other.optionalPayPeriod().map(StringFilter::copy).orElse(null);
        this.baseSalary = other.optionalBaseSalary().map(BigDecimalFilter::copy).orElse(null);
        this.allowances = other.optionalAllowances().map(BigDecimalFilter::copy).orElse(null);
        this.deductions = other.optionalDeductions().map(BigDecimalFilter::copy).orElse(null);
        this.netPay = other.optionalNetPay().map(BigDecimalFilter::copy).orElse(null);
        this.payoutDate = other.optionalPayoutDate().map(LocalDateFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SalaryPayoutCriteria copy() {
        return new SalaryPayoutCriteria(this);
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

    public StringFilter getSalaryPayoutCode() {
        return salaryPayoutCode;
    }

    public Optional<StringFilter> optionalSalaryPayoutCode() {
        return Optional.ofNullable(salaryPayoutCode);
    }

    public StringFilter salaryPayoutCode() {
        if (salaryPayoutCode == null) {
            setSalaryPayoutCode(new StringFilter());
        }
        return salaryPayoutCode;
    }

    public void setSalaryPayoutCode(StringFilter salaryPayoutCode) {
        this.salaryPayoutCode = salaryPayoutCode;
    }

    public StringFilter getStaffCode() {
        return staffCode;
    }

    public Optional<StringFilter> optionalStaffCode() {
        return Optional.ofNullable(staffCode);
    }

    public StringFilter staffCode() {
        if (staffCode == null) {
            setStaffCode(new StringFilter());
        }
        return staffCode;
    }

    public void setStaffCode(StringFilter staffCode) {
        this.staffCode = staffCode;
    }

    public StringFilter getPayPeriod() {
        return payPeriod;
    }

    public Optional<StringFilter> optionalPayPeriod() {
        return Optional.ofNullable(payPeriod);
    }

    public StringFilter payPeriod() {
        if (payPeriod == null) {
            setPayPeriod(new StringFilter());
        }
        return payPeriod;
    }

    public void setPayPeriod(StringFilter payPeriod) {
        this.payPeriod = payPeriod;
    }

    public BigDecimalFilter getBaseSalary() {
        return baseSalary;
    }

    public Optional<BigDecimalFilter> optionalBaseSalary() {
        return Optional.ofNullable(baseSalary);
    }

    public BigDecimalFilter baseSalary() {
        if (baseSalary == null) {
            setBaseSalary(new BigDecimalFilter());
        }
        return baseSalary;
    }

    public void setBaseSalary(BigDecimalFilter baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimalFilter getAllowances() {
        return allowances;
    }

    public Optional<BigDecimalFilter> optionalAllowances() {
        return Optional.ofNullable(allowances);
    }

    public BigDecimalFilter allowances() {
        if (allowances == null) {
            setAllowances(new BigDecimalFilter());
        }
        return allowances;
    }

    public void setAllowances(BigDecimalFilter allowances) {
        this.allowances = allowances;
    }

    public BigDecimalFilter getDeductions() {
        return deductions;
    }

    public Optional<BigDecimalFilter> optionalDeductions() {
        return Optional.ofNullable(deductions);
    }

    public BigDecimalFilter deductions() {
        if (deductions == null) {
            setDeductions(new BigDecimalFilter());
        }
        return deductions;
    }

    public void setDeductions(BigDecimalFilter deductions) {
        this.deductions = deductions;
    }

    public BigDecimalFilter getNetPay() {
        return netPay;
    }

    public Optional<BigDecimalFilter> optionalNetPay() {
        return Optional.ofNullable(netPay);
    }

    public BigDecimalFilter netPay() {
        if (netPay == null) {
            setNetPay(new BigDecimalFilter());
        }
        return netPay;
    }

    public void setNetPay(BigDecimalFilter netPay) {
        this.netPay = netPay;
    }

    public LocalDateFilter getPayoutDate() {
        return payoutDate;
    }

    public Optional<LocalDateFilter> optionalPayoutDate() {
        return Optional.ofNullable(payoutDate);
    }

    public LocalDateFilter payoutDate() {
        if (payoutDate == null) {
            setPayoutDate(new LocalDateFilter());
        }
        return payoutDate;
    }

    public void setPayoutDate(LocalDateFilter payoutDate) {
        this.payoutDate = payoutDate;
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
        final SalaryPayoutCriteria that = (SalaryPayoutCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(salaryPayoutCode, that.salaryPayoutCode) &&
            Objects.equals(staffCode, that.staffCode) &&
            Objects.equals(payPeriod, that.payPeriod) &&
            Objects.equals(baseSalary, that.baseSalary) &&
            Objects.equals(allowances, that.allowances) &&
            Objects.equals(deductions, that.deductions) &&
            Objects.equals(netPay, that.netPay) &&
            Objects.equals(payoutDate, that.payoutDate) &&
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
            salaryPayoutCode,
            staffCode,
            payPeriod,
            baseSalary,
            allowances,
            deductions,
            netPay,
            payoutDate,
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
        return "SalaryPayoutCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalSalaryPayoutCode().map(f -> "salaryPayoutCode=" + f + ", ").orElse("") +
            optionalStaffCode().map(f -> "staffCode=" + f + ", ").orElse("") +
            optionalPayPeriod().map(f -> "payPeriod=" + f + ", ").orElse("") +
            optionalBaseSalary().map(f -> "baseSalary=" + f + ", ").orElse("") +
            optionalAllowances().map(f -> "allowances=" + f + ", ").orElse("") +
            optionalDeductions().map(f -> "deductions=" + f + ", ").orElse("") +
            optionalNetPay().map(f -> "netPay=" + f + ", ").orElse("") +
            optionalPayoutDate().map(f -> "payoutDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
