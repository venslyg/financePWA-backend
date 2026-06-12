package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.BudgetAlertStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.BudgetPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetPlanDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String accountCode;

    private String budgetPlanCode;

    private String departmentName;

    private Integer year;

    private BigDecimal allocatedAmount;

    private BigDecimal spentAmount;

    private BigDecimal remainingAmount;

    private BigDecimal usedPercentage;

    private BudgetAlertStatus alertStatus;

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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getBudgetPlanCode() {
        return budgetPlanCode;
    }

    public void setBudgetPlanCode(String budgetPlanCode) {
        this.budgetPlanCode = budgetPlanCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(BigDecimal usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public BudgetAlertStatus getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(BudgetAlertStatus alertStatus) {
        this.alertStatus = alertStatus;
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
        if (!(o instanceof BudgetPlanDTO)) {
            return false;
        }

        BudgetPlanDTO budgetPlanDTO = (BudgetPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetPlanDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", accountCode='" + getAccountCode() + "'" +
            ", budgetPlanCode='" + getBudgetPlanCode() + "'" +
            ", departmentName='" + getDepartmentName() + "'" +
            ", year=" + getYear() +
            ", allocatedAmount=" + getAllocatedAmount() +
            ", spentAmount=" + getSpentAmount() +
            ", remainingAmount=" + getRemainingAmount() +
            ", usedPercentage=" + getUsedPercentage() +
            ", alertStatus='" + getAlertStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
