package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison based on phone number)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     * Also updates all other persons that have {@code target} in their linked persons.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);

        // Update all persons that have the target in their linkedPersons
        updateLinkedPersonReferences(target, editedPerson);
    }

    /**
     * Updates all persons that have {@code oldPerson} in their linkedPersons to reference {@code newPerson} instead.
     */
    private void updateLinkedPersonReferences(Person oldPerson, Person newPerson) {
        List<Person> personList = persons.asUnmodifiableObservableList();

        for (Person person : personList) {
            // Skip the person we just updated
            if (person.isSamePerson(newPerson)) {
                continue;
            }

            // Check if this person has the old person in their linked persons
            if (person.getLinkedPersons().stream().anyMatch(p -> p.isSamePerson(oldPerson))) {
                // Create a new set with the updated reference
                java.util.Set<Person> updatedLinkedPersons = new java.util.HashSet<>();
                for (Person linkedPerson : person.getLinkedPersons()) {
                    if (linkedPerson.isSamePerson(oldPerson)) {
                        updatedLinkedPersons.add(newPerson);
                    } else {
                        updatedLinkedPersons.add(linkedPerson);
                    }
                }

                // Create updated person with new linked persons
                Person updatedPerson = createPersonWithUpdatedLinks(person, updatedLinkedPersons);
                persons.setPerson(person, updatedPerson);
            }
        }
    }

    /**
     * Creates a new Person with updated linked persons, preserving all other fields.
     */
    private Person createPersonWithUpdatedLinks(Person person, java.util.Set<Person> updatedLinkedPersons) {
        if (person.getType() == seedu.address.model.person.PersonType.VENDOR) {
            return new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getAddress(),
                    person.getType(),
                    person.getTags(),
                    updatedLinkedPersons,
                    person.getPrice().orElse(null)
            );
        } else {
            return new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getAddress(),
                    person.getWeddingDate().orElse(null),
                    person.getType(),
                    person.getTags(),
                    updatedLinkedPersons,
                    person.getPrice().orElse(null),
                    person.getBudget().orElse(null),
                    person.getPartner()
            );
        }
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
