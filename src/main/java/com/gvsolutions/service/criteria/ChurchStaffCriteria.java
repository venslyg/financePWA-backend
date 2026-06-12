package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.StaffType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.ChurchStaff} entity. This class is used
 * in {@link com.gvsolutions.web.rest.ChurchStaffResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /church-staffs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChurchStaffCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StaffType
     */
    public static class StaffTypeFilter extends Filter<StaffType> {

        public StaffTypeFilter() {}

        public StaffTypeFilter(StaffTypeFilter filter) {
            super(filter);
        }

        @Override
        public StaffTypeFilter copy() {
            return new StaffTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter staffCode;

    private StringFilter branchCode;

    private StringFilter fullName;

    private StringFilter position;

    private StaffTypeFilter staffType;

    private StringFilter contactNumber;

    private BigDecimalFilter hourlyRateOrMonthlySalary;

    private BooleanFilter isActive;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public ChurchStaffCriteria() {}

    public ChurchStaffCriteria(ChurchStaffCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.staffCode = other.optionalStaffCode().map(StringFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.position = other.optionalPosition().map(StringFilter::copy).orElse(null);
        this.staffType = other.optionalStaffType().map(StaffTypeFilter::copy).orElse(null);
        this.contactNumber = other.optionalContactNumber().map(StringFilter::copy).orElse(null);
        this.hourlyRateOrMonthlySalary = other.optionalHourlyRateOrMonthlySalary().map(BigDecimalFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ChurchStaffCriteria copy() {
        return new ChurchStaffCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getPosition() {
        return position;
    }

    public Optional<StringFilter> optionalPosition() {
        return Optional.ofNullable(position);
    }

    public StringFilter position() {
        if (position == null) {
            setPosition(new StringFilter());
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StaffTypeFilter getStaffType() {
        return staffType;
    }

    public Optional<StaffTypeFilter> optionalStaffType() {
        return Optional.ofNullable(staffType);
    }

    public StaffTypeFilter staffType() {
        if (staffType == null) {
            setStaffType(new StaffTypeFilter());
        }
        return staffType;
    }

    public void setStaffType(StaffTypeFilter staffType) {
        this.staffType = staffType;
    }

    public StringFilter getContactNumber() {
        return contactNumber;
    }

    public Optional<StringFilter> optionalContactNumber() {
        return Optional.ofNullable(contactNumber);
    }

    public StringFilter contactNumber() {
        if (contactNumber == null) {
            setContactNumber(new StringFilter());
        }
        return contactNumber;
    }

    public void setContactNumber(StringFilter contactNumber) {
        this.contactNumber = contactNumber;
    }

    public BigDecimalFilter getHourlyRateOrMonthlySalary() {
        return hourlyRateOrMonthlySalary;
    }

    public Optional<BigDecimalFilter> optionalHourlyRateOrMonthlySalary() {
        return Optional.ofNullable(hourlyRateOrMonthlySalary);
    }

    public BigDecimalFilter hourlyRateOrMonthlySalary() {
        if (hourlyRateOrMonthlySalary == null) {
            setHourlyRateOrMonthlySalary(new BigDecimalFilter());
        }
        return hourlyRateOrMonthlySalary;
    }

    public void setHourlyRateOrMonthlySalary(BigDecimalFilter hourlyRateOrMonthlySalary) {
        this.hourlyRateOrMonthlySalary = hourlyRateOrMonthlySalary;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final ChurchStaffCriteria that = (ChurchStaffCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(staffCode, that.staffCode) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(position, that.position) &&
            Objects.equals(staffType, that.staffType) &&
            Objects.equals(contactNumber, that.contactNumber) &&
            Objects.equals(hourlyRateOrMonthlySalary, that.hourlyRateOrMonthlySalary) &&
            Objects.equals(isActive, that.isActive) &&
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
            staffCode,
            branchCode,
            fullName,
            position,
            staffType,
            contactNumber,
            hourlyRateOrMonthlySalary,
            isActive,
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
        return "ChurchStaffCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStaffCode().map(f -> "staffCode=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalPosition().map(f -> "position=" + f + ", ").orElse("") +
            optionalStaffType().map(f -> "staffType=" + f + ", ").orElse("") +
            optionalContactNumber().map(f -> "contactNumber=" + f + ", ").orElse("") +
            optionalHourlyRateOrMonthlySalary().map(f -> "hourlyRateOrMonthlySalary=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
