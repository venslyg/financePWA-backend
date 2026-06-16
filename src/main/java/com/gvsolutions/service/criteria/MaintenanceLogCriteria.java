package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.MaintenanceLogType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.MaintenanceLog} entity. This class is used
 * in {@link com.gvsolutions.web.rest.MaintenanceLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintenance-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceLogCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MaintenanceLogType
     */
    public static class MaintenanceLogTypeFilter extends Filter<MaintenanceLogType> {

        public MaintenanceLogTypeFilter() {}

        public MaintenanceLogTypeFilter(MaintenanceLogTypeFilter filter) {
            super(filter);
        }

        @Override
        public MaintenanceLogTypeFilter copy() {
            return new MaintenanceLogTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter maintenanceLogCode;

    private LocalDateFilter logDate;

    private MaintenanceLogTypeFilter logType;

    private StringFilter description;

    private BigDecimalFilter cost;

    private StringFilter vendor;

    private LocalDateFilter nextServiceDate;

    private StringFilter note;

    private BooleanFilter isActive;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter assetId;

    private Boolean distinct;

    public MaintenanceLogCriteria() {}

    public MaintenanceLogCriteria(MaintenanceLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.maintenanceLogCode = other.optionalMaintenanceLogCode().map(StringFilter::copy).orElse(null);
        this.logDate = other.optionalLogDate().map(LocalDateFilter::copy).orElse(null);
        this.logType = other.optionalLogType().map(MaintenanceLogTypeFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.cost = other.optionalCost().map(BigDecimalFilter::copy).orElse(null);
        this.vendor = other.optionalVendor().map(StringFilter::copy).orElse(null);
        this.nextServiceDate = other.optionalNextServiceDate().map(LocalDateFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.assetId = other.optionalAssetId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaintenanceLogCriteria copy() {
        return new MaintenanceLogCriteria(this);
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

    public StringFilter getMaintenanceLogCode() {
        return maintenanceLogCode;
    }

    public Optional<StringFilter> optionalMaintenanceLogCode() {
        return Optional.ofNullable(maintenanceLogCode);
    }

    public StringFilter maintenanceLogCode() {
        if (maintenanceLogCode == null) {
            setMaintenanceLogCode(new StringFilter());
        }
        return maintenanceLogCode;
    }

    public void setMaintenanceLogCode(StringFilter maintenanceLogCode) {
        this.maintenanceLogCode = maintenanceLogCode;
    }

    public LocalDateFilter getLogDate() {
        return logDate;
    }

    public Optional<LocalDateFilter> optionalLogDate() {
        return Optional.ofNullable(logDate);
    }

    public LocalDateFilter logDate() {
        if (logDate == null) {
            setLogDate(new LocalDateFilter());
        }
        return logDate;
    }

    public void setLogDate(LocalDateFilter logDate) {
        this.logDate = logDate;
    }

    public MaintenanceLogTypeFilter getLogType() {
        return logType;
    }

    public Optional<MaintenanceLogTypeFilter> optionalLogType() {
        return Optional.ofNullable(logType);
    }

    public MaintenanceLogTypeFilter logType() {
        if (logType == null) {
            setLogType(new MaintenanceLogTypeFilter());
        }
        return logType;
    }

    public void setLogType(MaintenanceLogTypeFilter logType) {
        this.logType = logType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getCost() {
        return cost;
    }

    public Optional<BigDecimalFilter> optionalCost() {
        return Optional.ofNullable(cost);
    }

    public BigDecimalFilter cost() {
        if (cost == null) {
            setCost(new BigDecimalFilter());
        }
        return cost;
    }

    public void setCost(BigDecimalFilter cost) {
        this.cost = cost;
    }

    public StringFilter getVendor() {
        return vendor;
    }

    public Optional<StringFilter> optionalVendor() {
        return Optional.ofNullable(vendor);
    }

    public StringFilter vendor() {
        if (vendor == null) {
            setVendor(new StringFilter());
        }
        return vendor;
    }

    public void setVendor(StringFilter vendor) {
        this.vendor = vendor;
    }

    public LocalDateFilter getNextServiceDate() {
        return nextServiceDate;
    }

    public Optional<LocalDateFilter> optionalNextServiceDate() {
        return Optional.ofNullable(nextServiceDate);
    }

    public LocalDateFilter nextServiceDate() {
        if (nextServiceDate == null) {
            setNextServiceDate(new LocalDateFilter());
        }
        return nextServiceDate;
    }

    public void setNextServiceDate(LocalDateFilter nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public StringFilter getNote() {
        return note;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
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

    public LongFilter getAssetId() {
        return assetId;
    }

    public Optional<LongFilter> optionalAssetId() {
        return Optional.ofNullable(assetId);
    }

    public LongFilter assetId() {
        if (assetId == null) {
            setAssetId(new LongFilter());
        }
        return assetId;
    }

    public void setAssetId(LongFilter assetId) {
        this.assetId = assetId;
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
        final MaintenanceLogCriteria that = (MaintenanceLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(maintenanceLogCode, that.maintenanceLogCode) &&
            Objects.equals(logDate, that.logDate) &&
            Objects.equals(logType, that.logType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(vendor, that.vendor) &&
            Objects.equals(nextServiceDate, that.nextServiceDate) &&
            Objects.equals(note, that.note) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(assetId, that.assetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            branchCode,
            branchId,
            maintenanceLogCode,
            logDate,
            logType,
            description,
            cost,
            vendor,
            nextServiceDate,
            note,
            isActive,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            assetId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalMaintenanceLogCode().map(f -> "maintenanceLogCode=" + f + ", ").orElse("") +
            optionalLogDate().map(f -> "logDate=" + f + ", ").orElse("") +
            optionalLogType().map(f -> "logType=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCost().map(f -> "cost=" + f + ", ").orElse("") +
            optionalVendor().map(f -> "vendor=" + f + ", ").orElse("") +
            optionalNextServiceDate().map(f -> "nextServiceDate=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalAssetId().map(f -> "assetId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
