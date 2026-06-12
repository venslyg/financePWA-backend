package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.PaymentMode;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.DonationTracker} entity. This class is used
 * in {@link com.gvsolutions.web.rest.DonationTrackerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /donation-trackers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonationTrackerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentMode
     */
    public static class PaymentModeFilter extends Filter<PaymentMode> {

        public PaymentModeFilter() {}

        public PaymentModeFilter(PaymentModeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentModeFilter copy() {
            return new PaymentModeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter donationIdCode;

    private LocalDateFilter date;

    private StringFilter donorNameOrOrg;

    private StringFilter contactDetails;

    private BigDecimalFilter amount;

    private StringFilter purpose;

    private PaymentModeFilter receivedViaMode;

    private StringFilter notes;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public DonationTrackerCriteria() {}

    public DonationTrackerCriteria(DonationTrackerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.donationIdCode = other.optionalDonationIdCode().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.donorNameOrOrg = other.optionalDonorNameOrOrg().map(StringFilter::copy).orElse(null);
        this.contactDetails = other.optionalContactDetails().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.purpose = other.optionalPurpose().map(StringFilter::copy).orElse(null);
        this.receivedViaMode = other.optionalReceivedViaMode().map(PaymentModeFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DonationTrackerCriteria copy() {
        return new DonationTrackerCriteria(this);
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

    public StringFilter getDonationIdCode() {
        return donationIdCode;
    }

    public Optional<StringFilter> optionalDonationIdCode() {
        return Optional.ofNullable(donationIdCode);
    }

    public StringFilter donationIdCode() {
        if (donationIdCode == null) {
            setDonationIdCode(new StringFilter());
        }
        return donationIdCode;
    }

    public void setDonationIdCode(StringFilter donationIdCode) {
        this.donationIdCode = donationIdCode;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getDonorNameOrOrg() {
        return donorNameOrOrg;
    }

    public Optional<StringFilter> optionalDonorNameOrOrg() {
        return Optional.ofNullable(donorNameOrOrg);
    }

    public StringFilter donorNameOrOrg() {
        if (donorNameOrOrg == null) {
            setDonorNameOrOrg(new StringFilter());
        }
        return donorNameOrOrg;
    }

    public void setDonorNameOrOrg(StringFilter donorNameOrOrg) {
        this.donorNameOrOrg = donorNameOrOrg;
    }

    public StringFilter getContactDetails() {
        return contactDetails;
    }

    public Optional<StringFilter> optionalContactDetails() {
        return Optional.ofNullable(contactDetails);
    }

    public StringFilter contactDetails() {
        if (contactDetails == null) {
            setContactDetails(new StringFilter());
        }
        return contactDetails;
    }

    public void setContactDetails(StringFilter contactDetails) {
        this.contactDetails = contactDetails;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getPurpose() {
        return purpose;
    }

    public Optional<StringFilter> optionalPurpose() {
        return Optional.ofNullable(purpose);
    }

    public StringFilter purpose() {
        if (purpose == null) {
            setPurpose(new StringFilter());
        }
        return purpose;
    }

    public void setPurpose(StringFilter purpose) {
        this.purpose = purpose;
    }

    public PaymentModeFilter getReceivedViaMode() {
        return receivedViaMode;
    }

    public Optional<PaymentModeFilter> optionalReceivedViaMode() {
        return Optional.ofNullable(receivedViaMode);
    }

    public PaymentModeFilter receivedViaMode() {
        if (receivedViaMode == null) {
            setReceivedViaMode(new PaymentModeFilter());
        }
        return receivedViaMode;
    }

    public void setReceivedViaMode(PaymentModeFilter receivedViaMode) {
        this.receivedViaMode = receivedViaMode;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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
        final DonationTrackerCriteria that = (DonationTrackerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(donationIdCode, that.donationIdCode) &&
            Objects.equals(date, that.date) &&
            Objects.equals(donorNameOrOrg, that.donorNameOrOrg) &&
            Objects.equals(contactDetails, that.contactDetails) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(purpose, that.purpose) &&
            Objects.equals(receivedViaMode, that.receivedViaMode) &&
            Objects.equals(notes, that.notes) &&
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
            donationIdCode,
            date,
            donorNameOrOrg,
            contactDetails,
            amount,
            purpose,
            receivedViaMode,
            notes,
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
        return "DonationTrackerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalDonationIdCode().map(f -> "donationIdCode=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalDonorNameOrOrg().map(f -> "donorNameOrOrg=" + f + ", ").orElse("") +
            optionalContactDetails().map(f -> "contactDetails=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalPurpose().map(f -> "purpose=" + f + ", ").orElse("") +
            optionalReceivedViaMode().map(f -> "receivedViaMode=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
