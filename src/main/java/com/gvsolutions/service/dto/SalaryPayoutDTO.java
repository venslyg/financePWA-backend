package com.gvsolutions.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.SalaryPayout} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryPayoutDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String salaryPayoutCode;

    private String staffCode;

    private String payPeriod;

    private BigDecimal baseSalary;

    private BigDecimal allowances;

    private BigDecimal deductions;

    private BigDecimal netPay;

    private LocalDate payoutDate;

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

    public String getSalaryPayoutCode() {
        return salaryPayoutCode;
    }

    public void setSalaryPayoutCode(String salaryPayoutCode) {
        this.salaryPayoutCode = salaryPayoutCode;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getAllowances() {
        return allowances;
    }

    public void setAllowances(BigDecimal allowances) {
        this.allowances = allowances;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public LocalDate getPayoutDate() {
        return payoutDate;
    }

    public void setPayoutDate(LocalDate payoutDate) {
        this.payoutDate = payoutDate;
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
        if (!(o instanceof SalaryPayoutDTO)) {
            return false;
        }

        SalaryPayoutDTO salaryPayoutDTO = (SalaryPayoutDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salaryPayoutDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryPayoutDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
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
