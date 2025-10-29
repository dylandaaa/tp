package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.date.WeddingDate;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Person {
    public static final String MSG_PARTNER_FORBIDDEN_FOR_VENDOR =
            "Vendors cannot have a partner (remove pr/<PARTNER_NAME>).";
    public static final String MSG_PARTNER_REQUIRED_FOR_CLIENT =
            "Clients must have a partner (use pr/<PARTNER_NAME>).";

    public static final String MSG_WEDDING_DATE_REQUIRED_FOR_CLIENT = "Wedding date is required for clients.";
    public static final String MSG_WEDDING_DATE_FORBIDDEN_FOR_VENDOR = "Wedding date is only applicable for clients.";

    public static final String MSG_TAGS_FORBIDDEN_FOR_CLIENT =
            "Tags are not allowed for clients. Only vendors can have tags/categories.";

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final WeddingDate weddingDate;
    private final Set<Tag> tags = new HashSet<>();
    private final PersonType type;
    private final Price price; // only for vendors
    private final Set<Person> linkedPersons = new HashSet<>();
    private final Budget budget; // only for clients
    private final Optional<Partner> partner;

    /**
     * Constructor for clients - every field must be present and not null, except budget which is optional.
     */
    public Person(Name name, Phone phone, Email email, Address address, WeddingDate weddingDate, PersonType type,
            Set<Tag> tags, Price price, Budget budget, Optional<Partner> partner) {
        requireAllNonNull(name, phone, email, address, weddingDate, tags, type);
        checkArgument(isValidPartnerForType(type, partner),
                type == PersonType.CLIENT ? MSG_PARTNER_REQUIRED_FOR_CLIENT : MSG_PARTNER_FORBIDDEN_FOR_VENDOR);
        checkArgument(isValidTagsForType(type, tags), MSG_TAGS_FORBIDDEN_FOR_CLIENT);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.weddingDate = weddingDate;
        this.type = type;
        this.tags.addAll(tags);
        this.price = price;
        this.budget = budget;
        this.partner = partner;
    }

    /**
     * Constructor for vendors - no wedding date field.
     */
    public Person(Name name, Phone phone, Email email, Address address, PersonType type,
            Set<Tag> tags, Price price) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.weddingDate = null; // Vendors don't have wedding dates
        this.type = type;
        this.tags.addAll(tags);
        this.price = price;
        this.budget = null; // Vendors don't have budgets
        this.partner = Optional.empty(); // Vendors don't have partners
    }

    /**
     * Constructor with linked persons for clients.
     */
    public Person(Name name, Phone phone, Email email, Address address, WeddingDate weddingDate, PersonType type,
                  Set<Tag> tags, Set<Person> linkedPersons, Price price, Optional<Partner> partner) {
        requireAllNonNull(name, phone, email, address, weddingDate, tags, linkedPersons);
        checkArgument(isValidTagsForType(type, tags), MSG_TAGS_FORBIDDEN_FOR_CLIENT);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.weddingDate = weddingDate;
        this.type = type;
        this.tags.addAll(tags);
        this.price = price;
        this.linkedPersons.addAll(linkedPersons);
        this.budget = null;
        this.partner = partner;
    }

    /**
     * Constructor with linked persons for clients, preserving budget.
     */
    public Person(Name name, Phone phone, Email email, Address address, WeddingDate weddingDate, PersonType type,
                  Set<Tag> tags, Set<Person> linkedPersons, Price price, Budget budget, Optional<Partner> partner) {
        requireAllNonNull(name, phone, email, address, weddingDate, tags, linkedPersons);
        checkArgument(isValidTagsForType(type, tags), MSG_TAGS_FORBIDDEN_FOR_CLIENT);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.weddingDate = weddingDate;
        this.type = type;
        this.tags.addAll(tags);
        this.price = price;
        this.linkedPersons.addAll(linkedPersons);
        this.budget = budget;
        this.partner = partner;
    }

    /**
     * Constructor with linked persons for vendors.
     */
    public Person(Name name, Phone phone, Email email, Address address, PersonType type,
            Set<Tag> tags, Set<Person> linkedPersons, Price price) {
        requireAllNonNull(name, phone, email, address, tags, linkedPersons);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.weddingDate = null; // Vendors don't have wedding dates
        this.type = type;
        this.tags.addAll(tags);
        this.price = price;
        this.linkedPersons.addAll(linkedPersons);
        this.budget = null; // Vendors don't have budgets
        this.partner = Optional.empty(); // Vendors don't have partners
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Optional<WeddingDate> getWeddingDate() {
        return Optional.ofNullable(weddingDate);
    }

    public PersonType getType() {
        return type;
    }

    public Optional<Price> getPrice() {
        return Optional.ofNullable(price);
    }

    public Optional<Budget> getBudget() {
        return Optional.ofNullable(budget);
    }

    public Optional<Partner> getPartner() {
        return partner;
    }

    /**
     * Returns an immutable set of linked persons, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Person> getLinkedPersons() {
        return Collections.unmodifiableSet(linkedPersons);
    }

    /**
     * Returns true if this person is linked to the specified person.
     */
    public boolean isLinkedTo(Person other) {
        return linkedPersons.contains(other);
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same phone number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getPhone().equals(getPhone());
    }

    /** Validates the partner constraint for a {@link PersonType}. */
    public static boolean isValidPartnerForType(PersonType type, Optional<Partner> partner) {
        if (type == PersonType.CLIENT) {
            return partner != null && partner.isPresent();
        } else if (type == PersonType.VENDOR) {
            return partner != null && partner.isEmpty();
        }
        return partner != null && partner.isEmpty();
    }

    /** Validates the tags constraint for a {@link PersonType}. */
    public static boolean isValidTagsForType(PersonType type, Set<Tag> tags) {
        if (type == PersonType.CLIENT) {
            return tags.isEmpty();
        }
        return true; // Vendors can have tags
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && Objects.equals(weddingDate, otherPerson.weddingDate)
                && type == otherPerson.type
                && tags.equals(otherPerson.tags)
                && Objects.equals(price, otherPerson.price)
                && Objects.equals(budget, otherPerson.budget);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        // linkedPersons intentionally excluded from hashCode to avoid circular
        // references
        return Objects.hash(name, phone, email, address, weddingDate, type, tags, price, budget);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("weddingDate", weddingDate)
                .add("tags", tags)
                .add("type", type)
                .add("price", price)
                .add("linkedPersons", linkedPersons.size() + " link(s)")
                .toString();
    }

}
