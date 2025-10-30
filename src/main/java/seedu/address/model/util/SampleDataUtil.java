package seedu.address.model.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
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
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                WeddingDate.parse("15-06-2020"), PersonType.CLIENT, getTagSet(), Collections.emptySet(),
                null, new Budget("50000-100000"), Optional.of(new Partner("Cierra Wang"))),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                WeddingDate.parse("22-03-2019"), PersonType.CLIENT, getTagSet(), Collections.emptySet(),
                null, new Budget("40000"), Optional.of(new Partner("Jack Chia"))),
            new Person(new Name("Charlotte K Photography"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                PersonType.VENDOR, getTagSet("Photography & Videography"),
                Collections.emptySet(), new Price("3500-10000")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                WeddingDate.parse("05-08-2018"), PersonType.CLIENT, getTagSet(), Collections.emptySet(),
                null, new Budget("30000-60000"), Optional.of(new Partner("Jia Min"))),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                WeddingDate.parse("18-11-2022"), PersonType.CLIENT, getTagSet(), Collections.emptySet(),
                null, new Budget("50000-65000"), Optional.of(new Partner("Siti Nur"))),
            new Person(new Name("The Bloom Boutique co."), new Phone("92624417"),
                new Email("bloomboutique@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                PersonType.VENDOR, getTagSet("Florist"),
                Collections.emptySet(), new Price("2000-3500"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
