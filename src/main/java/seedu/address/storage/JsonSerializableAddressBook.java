package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON =
            "Persons list contains duplicate person(s) (same phone number).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        // First pass: Load all persons without links
        Map<String, Person> personByName = new HashMap<>();
        Map<Person, List<String>> personToLinkedNames = new HashMap<>();

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
            personByName.put(person.getName().fullName, person);
            personToLinkedNames.put(person, jsonAdaptedPerson.getLinkedPersonNames());
        }

        // Second pass: Resolve and add links
        for (Map.Entry<Person, List<String>> entry : personToLinkedNames.entrySet()) {
            Person person = entry.getKey();
            List<String> linkedNames = entry.getValue();

            if (!linkedNames.isEmpty()) {
                Set<Person> linkedPersons = new HashSet<>();
                for (String linkedName : linkedNames) {
                    Person linkedPerson = personByName.get(linkedName);
                    if (linkedPerson != null) {
                        linkedPersons.add(linkedPerson);
                    }
                    // Silently ignore if linked person doesn't exist (data integrity issue)
                }

                if (!linkedPersons.isEmpty()) {
                    // Create updated person with links
                    Person updatedPerson;
                    if (person.getType() == PersonType.CLIENT) {
                        // For clients, use constructor with wedding date and budget
                        updatedPerson = new Person(
                                person.getName(),
                                person.getPhone(),
                                person.getEmail(),
                                person.getAddress(),
                                person.getWeddingDate().orElse(null),
                                person.getType(),
                                person.getTags(),
                                linkedPersons,
                                person.getPrice().orElse(null),
                                person.getBudget().orElse(null),
                                person.getPartner()
                        );
                    } else {
                        // For vendors, use constructor without wedding date
                        updatedPerson = new Person(
                                person.getName(),
                                person.getPhone(),
                                person.getEmail(),
                                person.getAddress(),
                                person.getType(),
                                person.getTags(),
                                linkedPersons,
                                person.getPrice().orElse(null)
                        );
                    }
                    addressBook.setPerson(person, updatedPerson);
                }
            }
        }

        return addressBook;
    }

}
