package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.PaymentMode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.DonationTracker} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonationTrackerDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String donationIdCode;

    private LocalDate date;

    private String donorNameOrOrg;

    private String contactDetails;

    private BigDecimal amount;

    private String purpose;

    private PaymentMode receivedViaMode;

    private String notes;

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

    public String getDonationIdCode() {
        return donationIdCode;
    }

    public void setDonationIdCode(String donationIdCode) {
        this.donationIdCode = donationIdCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDonorNameOrOrg() {
        return donorNameOrOrg;
    }

    public void setDonorNameOrOrg(String donorNameOrOrg) {
        this.donorNameOrOrg = donorNameOrOrg;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public PaymentMode getReceivedViaMode() {
        return receivedViaMode;
    }

    public void setReceivedViaMode(PaymentMode receivedViaMode) {
        this.receivedViaMode = receivedViaMode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(o instanceof DonationTrackerDTO)) {
            return false;
        }

        DonationTrackerDTO donationTrackerDTO = (DonationTrackerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, donationTrackerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationTrackerDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", donationIdCode='" + getDonationIdCode() + "'" +
            ", date='" + getDate() + "'" +
            ", donorNameOrOrg='" + getDonorNameOrOrg() + "'" +
            ", contactDetails='" + getContactDetails() + "'" +
            ", amount=" + getAmount() +
            ", purpose='" + getPurpose() + "'" +
            ", receivedViaMode='" + getReceivedViaMode() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
