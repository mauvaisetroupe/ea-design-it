package com.mauvaisetroupe.eadesignit.service.criteria;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mauvaisetroupe.eadesignit.domain.Application} entity. This class is used
 * in {@link com.mauvaisetroupe.eadesignit.web.rest.ApplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ApplicationType
     */
    public static class ApplicationTypeFilter extends Filter<ApplicationType> {

        public ApplicationTypeFilter() {}

        public ApplicationTypeFilter(ApplicationTypeFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationTypeFilter copy() {
            return new ApplicationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter name;

    private StringFilter description;

    private ApplicationTypeFilter type;

    private StringFilter technology;

    private StringFilter comment;

    private LongFilter ownerId;

    private LongFilter applicationsListId;

    private Boolean distinct;

    public ApplicationCriteria() {}

    public ApplicationCriteria(ApplicationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.technology = other.technology == null ? null : other.technology.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.applicationsListId = other.applicationsListId == null ? null : other.applicationsListId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ApplicationCriteria copy() {
        return new ApplicationCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public StringFilter id() {
        if (id == null) {
            id = new StringFilter();
        }
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ApplicationTypeFilter getType() {
        return type;
    }

    public ApplicationTypeFilter type() {
        if (type == null) {
            type = new ApplicationTypeFilter();
        }
        return type;
    }

    public void setType(ApplicationTypeFilter type) {
        this.type = type;
    }

    public StringFilter getTechnology() {
        return technology;
    }

    public StringFilter technology() {
        if (technology == null) {
            technology = new StringFilter();
        }
        return technology;
    }

    public void setTechnology(StringFilter technology) {
        this.technology = technology;
    }

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getApplicationsListId() {
        return applicationsListId;
    }

    public LongFilter applicationsListId() {
        if (applicationsListId == null) {
            applicationsListId = new LongFilter();
        }
        return applicationsListId;
    }

    public void setApplicationsListId(LongFilter applicationsListId) {
        this.applicationsListId = applicationsListId;
    }

    public Boolean getDistinct() {
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
        final ApplicationCriteria that = (ApplicationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(technology, that.technology) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(applicationsListId, that.applicationsListId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, technology, comment, ownerId, applicationsListId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (technology != null ? "technology=" + technology + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (applicationsListId != null ? "applicationsListId=" + applicationsListId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
