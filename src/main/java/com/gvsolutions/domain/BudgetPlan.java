package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.BudgetAlertStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Persistable;

/**
 * A BudgetPlan.
 */
@Entity
@Table(name = "budget_plan")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "budgetplan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetPlan extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Column(name = "account_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String accountCode;

    @Column(name = "budget_plan_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String budgetPlanCode;

    @Column(name = "department_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String departmentName;

    @Column(name = "year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer year;

    @Column(name = "allocated_amount", precision = 21, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "spent_amount", precision = 21, scale = 2)
    private BigDecimal spentAmount;

    @Column(name = "remaining_amount", precision = 21, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "used_percentage", precision = 21, scale = 2)
    private BigDecimal usedPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private BudgetAlertStatus alertStatus;

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

    public BudgetPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public BudgetPlan branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public BudgetPlan branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getAccountCode() {
        return this.accountCode;
    }

    public BudgetPlan accountCode(String accountCode) {
        this.setAccountCode(accountCode);
        return this;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getBudgetPlanCode() {
        return this.budgetPlanCode;
    }

    public BudgetPlan budgetPlanCode(String budgetPlanCode) {
        this.setBudgetPlanCode(budgetPlanCode);
        return this;
    }

    public void setBudgetPlanCode(String budgetPlanCode) {
        this.budgetPlanCode = budgetPlanCode;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public BudgetPlan departmentName(String departmentName) {
        this.setDepartmentName(departmentName);
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getYear() {
        return this.year;
    }

    public BudgetPlan year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getAllocatedAmount() {
        return this.allocatedAmount;
    }

    public BudgetPlan allocatedAmount(BigDecimal allocatedAmount) {
        this.setAllocatedAmount(allocatedAmount);
        return this;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getSpentAmount() {
        return this.spentAmount;
    }

    public BudgetPlan spentAmount(BigDecimal spentAmount) {
        this.setSpentAmount(spentAmount);
        return this;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getRemainingAmount() {
        return this.remainingAmount;
    }

    public BudgetPlan remainingAmount(BigDecimal remainingAmount) {
        this.setRemainingAmount(remainingAmount);
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getUsedPercentage() {
        return this.usedPercentage;
    }

    public BudgetPlan usedPercentage(BigDecimal usedPercentage) {
        this.setUsedPercentage(usedPercentage);
        return this;
    }

    public void setUsedPercentage(BigDecimal usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public BudgetAlertStatus getAlertStatus() {
        return this.alertStatus;
    }

    public BudgetPlan alertStatus(BudgetAlertStatus alertStatus) {
        this.setAlertStatus(alertStatus);
        return this;
    }

    public void setAlertStatus(BudgetAlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }

    // Inherited createdBy methods
    public BudgetPlan createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public BudgetPlan createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public BudgetPlan lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public BudgetPlan lastModifiedDate(Instant lastModifiedDate) {
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

    public BudgetPlan setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((BudgetPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetPlan{" +
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
