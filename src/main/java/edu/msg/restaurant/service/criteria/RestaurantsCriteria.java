package edu.msg.restaurant.service.criteria;

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
 * Criteria class for the {@link edu.msg.restaurant.domain.Restaurants} entity. This class is used
 * in {@link edu.msg.restaurant.web.rest.RestaurantsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /restaurants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestaurantsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter location;

    private StringFilter name;

    private StringFilter description;

    private LongFilter meseId;

    public RestaurantsCriteria() {}

    public RestaurantsCriteria(RestaurantsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.meseId = other.meseId == null ? null : other.meseId.copy();
    }

    @Override
    public RestaurantsCriteria copy() {
        return new RestaurantsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
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

    public LongFilter getMeseId() {
        return meseId;
    }

    public LongFilter meseId() {
        if (meseId == null) {
            meseId = new LongFilter();
        }
        return meseId;
    }

    public void setMeseId(LongFilter meseId) {
        this.meseId = meseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RestaurantsCriteria that = (RestaurantsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(location, that.location) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(meseId, that.meseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, name, description, meseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (meseId != null ? "meseId=" + meseId + ", " : "") +
            "}";
    }
}
