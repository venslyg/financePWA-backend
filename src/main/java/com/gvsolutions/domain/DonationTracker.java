package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.PaymentMode;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A DonationTracker.
 */
@Entity
@Table(name = "donation_tracker")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "donationtracker")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonationTracker extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "donation_id_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String donationIdCode;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "donor_name_or_org")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String donorNameOrOrg;

    @Column(name = "contact_details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contactDetails;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "purpose")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "received_via_mode")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentMode receivedViaMode;

    @Column(name = "notes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String notes;

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

    public DonationTracker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public DonationTracker branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDonationIdCode() {
        return this.donationIdCode;
    }

    public DonationTracker donationIdCode(String donationIdCode) {
        this.setDonationIdCode(donationIdCode);
        return this;
    }

    public void setDonationIdCode(String donationIdCode) {
        this.donationIdCode = donationIdCode;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public DonationTracker date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDonorNameOrOrg() {
        return this.donorNameOrOrg;
    }

    public DonationTracker donorNameOrOrg(String donorNameOrOrg) {
        this.setDonorNameOrOrg(donorNameOrOrg);
        return this;
    }

    public void setDonorNameOrOrg(String donorNameOrOrg) {
        this.donorNameOrOrg = donorNameOrOrg;
    }

    public String getContactDetails() {
        return this.contactDetails;
    }

    public DonationTracker contactDetails(String contactDetails) {
        this.setContactDetails(contactDetails);
        return this;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public DonationTracker amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public DonationTracker purpose(String purpose) {
        this.setPurpose(purpose);
        return this;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public PaymentMode getReceivedViaMode() {
        return this.receivedViaMode;
    }

    public DonationTracker receivedViaMode(PaymentMode receivedViaMode) {
        this.setReceivedViaMode(receivedViaMode);
        return this;
    }

    public void setReceivedViaMode(PaymentMode receivedViaMode) {
        this.receivedViaMode = receivedViaMode;
    }

    public String getNotes() {
        return this.notes;
    }

    public DonationTracker notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Inherited createdBy methods
    public DonationTracker createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public DonationTracker createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public DonationTracker lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public DonationTracker lastModifiedDate(Instant lastModifiedDate) {
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

    public DonationTracker setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonationTracker)) {
            return false;
        }
        return getId() != null && getId().equals(((DonationTracker) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationTracker{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
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
