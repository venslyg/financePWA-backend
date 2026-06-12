package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.ExpenseEntry} entity.
 */
@Schema(description = "Expense Entries & Bank/Petty Cash Transactions")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseEntryDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String accountCode;

    private String expenseCode;

    private String expenseCategoryCode;

    private String expenseSubCategoryCode;

    private String createdByUsername;

    private LocalDate date;

    private String voucherNo;

    private String description;

    private BigDecimal amount;

    private PaymentMode paymentMode;

    private ApprovalStatus approvalStatus;

    private String approvedBy;

    private String vendor;

    private SyncStatus syncStatus;

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

    public String getExpenseCode() {
        return expenseCode;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }

    public String getExpenseCategoryCode() {
        return expenseCategoryCode;
    }

    public void setExpenseCategoryCode(String expenseCategoryCode) {
        this.expenseCategoryCode = expenseCategoryCode;
    }

    public String getExpenseSubCategoryCode() {
        return expenseSubCategoryCode;
    }

    public void setExpenseSubCategoryCode(String expenseSubCategoryCode) {
        this.expenseSubCategoryCode = expenseSubCategoryCode;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
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
        if (!(o instanceof ExpenseEntryDTO)) {
            return false;
        }

        ExpenseEntryDTO expenseEntryDTO = (ExpenseEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, expenseEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseEntryDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
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
