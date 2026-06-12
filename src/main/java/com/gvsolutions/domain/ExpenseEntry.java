package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * Expense Entries & Bank/Petty Cash Transactions
 */
@Entity
@Table(name = "expense_entry")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "expenseentry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseEntry extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "account_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String accountCode;

    @Column(name = "expense_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String expenseCode;

    @Column(name = "expense_category_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String expenseCategoryCode;

    @Column(name = "expense_sub_category_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String expenseSubCategoryCode;

    @Column(name = "created_by_username")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdByUsername;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "voucher_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String voucherNo;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ApprovalStatus approvalStatus;

    @Column(name = "approved_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String approvedBy;

    @Column(name = "vendor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vendor;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SyncStatus syncStatus;

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

    public ExpenseEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public ExpenseEntry branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAccountCode() {
        return this.accountCode;
    }

    public ExpenseEntry accountCode(String accountCode) {
        this.setAccountCode(accountCode);
        return this;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getExpenseCode() {
        return this.expenseCode;
    }

    public ExpenseEntry expenseCode(String expenseCode) {
        this.setExpenseCode(expenseCode);
        return this;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }

    public String getExpenseCategoryCode() {
        return this.expenseCategoryCode;
    }

    public ExpenseEntry expenseCategoryCode(String expenseCategoryCode) {
        this.setExpenseCategoryCode(expenseCategoryCode);
        return this;
    }

    public void setExpenseCategoryCode(String expenseCategoryCode) {
        this.expenseCategoryCode = expenseCategoryCode;
    }

    public String getExpenseSubCategoryCode() {
        return this.expenseSubCategoryCode;
    }

    public ExpenseEntry expenseSubCategoryCode(String expenseSubCategoryCode) {
        this.setExpenseSubCategoryCode(expenseSubCategoryCode);
        return this;
    }

    public void setExpenseSubCategoryCode(String expenseSubCategoryCode) {
        this.expenseSubCategoryCode = expenseSubCategoryCode;
    }

    public String getCreatedByUsername() {
        return this.createdByUsername;
    }

    public ExpenseEntry createdByUsername(String createdByUsername) {
        this.setCreatedByUsername(createdByUsername);
        return this;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ExpenseEntry date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVoucherNo() {
        return this.voucherNo;
    }

    public ExpenseEntry voucherNo(String voucherNo) {
        this.setVoucherNo(voucherNo);
        return this;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getDescription() {
        return this.description;
    }

    public ExpenseEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ExpenseEntry amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMode() {
        return this.paymentMode;
    }

    public ExpenseEntry paymentMode(PaymentMode paymentMode) {
        this.setPaymentMode(paymentMode);
        return this;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public ApprovalStatus getApprovalStatus() {
        return this.approvalStatus;
    }

    public ExpenseEntry approvalStatus(ApprovalStatus approvalStatus) {
        this.setApprovalStatus(approvalStatus);
        return this;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public ExpenseEntry approvedBy(String approvedBy) {
        this.setApprovedBy(approvedBy);
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getVendor() {
        return this.vendor;
    }

    public ExpenseEntry vendor(String vendor) {
        this.setVendor(vendor);
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public SyncStatus getSyncStatus() {
        return this.syncStatus;
    }

    public ExpenseEntry syncStatus(SyncStatus syncStatus) {
        this.setSyncStatus(syncStatus);
        return this;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    // Inherited createdBy methods
    public ExpenseEntry createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public ExpenseEntry createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public ExpenseEntry lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public ExpenseEntry lastModifiedDate(Instant lastModifiedDate) {
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

    public ExpenseEntry setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpenseEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((ExpenseEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseEntry{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", accountCode='" + getAccountCode() + "'" +
            ", expenseCode='" + getExpenseCode() + "'" +
            ", expenseCategoryCode='" + getExpenseCategoryCode() + "'" +
            ", expenseSubCategoryCode='" + getExpenseSubCategoryCode() + "'" +
            ", createdByUsername='" + getCreatedByUsername() + "'" +
            ", date='" + getDate() + "'" +
            ", voucherNo='" + getVoucherNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", paymentMode='" + getPaymentMode() + "'" +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", vendor='" + getVendor() + "'" +
            ", syncStatus='" + getSyncStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
