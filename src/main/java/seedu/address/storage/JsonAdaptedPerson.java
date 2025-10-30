package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.date.WeddingDate;
import seedu.address.model.person.Address;
import seedu.address.model.person.Budget;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Partner;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Price;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String weddingDate;
    private final String type;
    private final String price;
    private final String budget;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String partner;
    private final List<String> linkedPersonNames = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("weddingDate") String weddingDate, @JsonProperty("type") String type,
            @JsonProperty("price") String price, @JsonProperty("budget") String budget,
            @JsonProperty("partner") String partner, @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("linkedPersonNames") List<String> linkedPersonNames) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.weddingDate = weddingDate;
        this.type = type;
        this.price = price;
        this.budget = budget;
        this.partner = partner;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (linkedPersonNames != null) {
            this.linkedPersonNames.addAll(linkedPersonNames);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        weddingDate = (source.getType() == PersonType.VENDOR)
                ? null
                : source.getWeddingDate().map(WeddingDate::toString).orElse(null);
        type = source.getType().toString();
        price = source.getPrice().map(p -> p.value).orElse(null);
        budget = source.getBudget().map(b -> b.value).orElse(null);
        this.partner = source.getPartner().map(p -> p.value).orElse(null);
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        linkedPersonNames.addAll(source.getLinkedPersons().stream()
                .map(person -> person.getName().fullName)
                .collect(Collectors.toList()));
    }

    /**
     * Returns the list of linked person names.
     */
    public List<String> getLinkedPersonNames() {
        return new ArrayList<>(linkedPersonNames);
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's
     * {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final WeddingDate modelWeddingDate;
        if (weddingDate == null) {
            modelWeddingDate = null;
        } else {
            try {
                modelWeddingDate = WeddingDate.parse(weddingDate);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(WeddingDate.MESSAGE_CONSTRAINTS);
            }
        }

        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "type"));
        }
        final PersonType modelType;
        try {
            modelType = PersonType.parse(type);
        } catch (IllegalArgumentException ex) {
            // exact message asserted in JsonAdaptedPersonTest
            throw new IllegalValueException("Type must be 'client' or 'vendor'.");
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final Price modelPrice;
        if (price != null && !price.isEmpty()) {
            if (!Price.isValidPrice(price)) {
                throw new IllegalValueException(Price.MESSAGE_CONSTRAINTS);
            }
            modelPrice = new Price(price);
        } else {
            modelPrice = null;
        }

        final Budget modelBudget;
        if (budget != null && !budget.isEmpty()) {
            if (!Budget.isValidBudget(budget)) {
                throw new IllegalValueException(Budget.MESSAGE_CONSTRAINTS);
            }
            modelBudget = new Budget(budget);
        } else {
            modelBudget = null;
        }

        final Optional<Partner> modelPartner = (partner == null || partner.isBlank())
                ? Optional.empty()
                : Optional.of(new Partner(partner.trim()));

        if (modelType == PersonType.CLIENT && modelPartner.isEmpty()) {
            throw new IllegalValueException(Person.MSG_PARTNER_REQUIRED_FOR_CLIENT);
        }
        if (modelType == PersonType.VENDOR && modelPartner.isPresent()) {
            throw new IllegalValueException(Person.MSG_PARTNER_FORBIDDEN_FOR_VENDOR);
        }
        if (modelType == PersonType.CLIENT && modelWeddingDate == null) {
            throw new IllegalValueException(Person.MSG_WEDDING_DATE_REQUIRED_FOR_CLIENT);
        }
        if (modelType == PersonType.VENDOR && modelWeddingDate != null) {
            throw new IllegalValueException(Person.MSG_WEDDING_DATE_FORBIDDEN_FOR_VENDOR);
        }

        // Create a set to hold linked persons (will be populated later in JsonSerializableAddressBook)
        Set<Person> modelLinkedPersons = new HashSet<>();

        if (modelType == PersonType.VENDOR) {
            return new Person(modelName, modelPhone, modelEmail, modelAddress, modelType,
                    modelTags, modelLinkedPersons, modelPrice);
        } else {
            return new Person(modelName, modelPhone, modelEmail, modelAddress, modelWeddingDate,
                    modelType, modelTags, modelLinkedPersons, modelPrice, modelBudget, modelPartner);
        }
    }

}
