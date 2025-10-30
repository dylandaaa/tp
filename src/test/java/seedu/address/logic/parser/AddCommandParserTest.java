package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.BUDGET_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.BUDGET_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_BUDGET_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WEDDING_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEDDING_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.WEDDING_DATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.WEDDING_DATE_DESC_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARTNER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEDDING_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.date.WeddingDate;
import seedu.address.model.person.Address;
import seedu.address.model.person.Budget;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Price;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private static final String TYPE_DESC_CLIENT = " type/client";
    private static final String TYPE_DESC_VENDOR = " type/vendor";
    private static final String TYPE_DESC_INVALID = " type/photographer";
    private static final String TYPE_DESC_VENDOR_UPPER = " type/VENDOR";

    private static final String PARTNER_DESC_AMY = " pr/Alex Tan";
    private static final String PARTNER_DESC_BOB = " pr/Bob Partner";

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // Test with vendor (vendors can have tags)
        // Use VALID_PRICE_BOB which is "500-1500" to match PRICE_DESC_BOB
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND)
                .withType(PersonType.VENDOR).withPrice(VALID_PRICE_BOB).withoutPartner().build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + TYPE_DESC_VENDOR + PRICE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPerson));

        // multiple tags - all accepted for vendors
        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .withType(PersonType.VENDOR).withPrice(VALID_PRICE_BOB).withoutPartner().build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + TYPE_DESC_VENDOR + PRICE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple wedding dates
        assertParseFailure(parser, WEDDING_DATE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_WEDDING_DATE));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + WEDDING_DATE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_PHONE, PREFIX_WEDDING_DATE, PREFIX_TYPE, PREFIX_PARTNER));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid wedding date
        assertParseFailure(parser, INVALID_WEDDING_DATE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_WEDDING_DATE));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid wedding date
        assertParseFailure(parser, validExpectedPersonString + INVALID_WEDDING_DATE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_WEDDING_DATE));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().withType(PersonType.CLIENT)
                .withPartner("Alex Tan").withoutBudget().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT + PARTNER_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                        + WEDDING_DATE_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // missing type prefix (type is mandatory)
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + TAG_DESC_HUSBAND
                        + TAG_DESC_FRIEND,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + TAG_DESC_HUSBAND
                        + TAG_DESC_FRIEND,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + TAG_DESC_HUSBAND
                        + TAG_DESC_FRIEND,
                Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + TAG_DESC_HUSBAND
                        + TAG_DESC_FRIEND,
                Address.MESSAGE_CONSTRAINTS);

        // invalid wedding date
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_WEDDING_DATE_DESC + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + TAG_DESC_HUSBAND
                        + TAG_DESC_FRIEND,
                WeddingDate.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + INVALID_TAG_DESC
                        + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_typeClient_success() {
        // Clients cannot have tags
        Person expected = new PersonBuilder(BOB).withTags()
                .withWeddingDate(VALID_WEDDING_DATE_BOB).withType(PersonType.CLIENT)
                .withPartner("Bob Partner").build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB,
                new AddCommand(expected));
    }

    @Test
    public void parse_typeVendor_successCaseInsensitive() {
        Person expected = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .withType(PersonType.VENDOR).withoutPrice().withoutPartner().build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + TYPE_DESC_VENDOR_UPPER,
                new AddCommand(expected));
    }

    @Test
    public void parse_typeInvalid_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TAG_DESC_FRIEND + TYPE_DESC_INVALID,
                "Invalid type. Please choose either 'client' or 'vendor'.");
    }

    @Test
    public void parse_repeatedType_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + WEDDING_DATE_DESC_BOB + TAG_DESC_FRIEND + PARTNER_DESC_BOB;

        assertParseFailure(parser,
                validExpectedPersonString + TYPE_DESC_CLIENT + " type/vendor",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TYPE));
    }

    @Test
    public void parse_vendorWithPrice_success() {
        Person expected = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND)
                .withType(PersonType.VENDOR)
                .withPrice("500-1500")
                .build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + TYPE_DESC_VENDOR + PRICE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expected));
    }

    @Test
    public void parse_vendorWithoutPrice_success() {
        Person expected = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND)
                .withType(PersonType.VENDOR)
                .withoutPrice()
                .build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + TYPE_DESC_VENDOR + TAG_DESC_FRIEND,
                new AddCommand(expected));
    }

    @Test
    public void parse_clientWithPrice_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_CLIENT + PARTNER_DESC_BOB + PRICE_DESC_BOB
                        + TAG_DESC_FRIEND,
                "Price is only applicable for vendors.");
    }

    @Test
    public void parse_invalidPrice_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + TYPE_DESC_VENDOR + INVALID_PRICE_DESC + TAG_DESC_FRIEND,
                Price.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_clientWithBudget_success() {
        // Clients cannot have tags
        Person expected = new PersonBuilder(AMY)
                .withTags()
                .withType(PersonType.CLIENT)
                .withBudget("5000-10000")
                .withPartner("Alex Tan")
                .build();

        assertParseSuccess(parser,
                NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT + PARTNER_DESC_AMY + BUDGET_DESC_AMY,
                new AddCommand(expected));
    }

    @Test
    public void parse_clientWithoutBudget_success() {
        // Clients cannot have tags
        Person expected = new PersonBuilder(AMY)
                .withTags()
                .withType(PersonType.CLIENT)
                .withPartner("Alex Tan")
                .withoutBudget()
                .build();

        assertParseSuccess(parser,
                NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT + PARTNER_DESC_AMY,
                new AddCommand(expected));
    }

    @Test
    public void parse_vendorWithBudget_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + TYPE_DESC_VENDOR + BUDGET_DESC_BOB + TAG_DESC_FRIEND,
                "Budget is only applicable for clients.");
    }

    @Test
    public void parse_vendorWithWeddingDate_failure() {
        // Wedding date is only applicable for clients
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + WEDDING_DATE_DESC_BOB + TYPE_DESC_VENDOR + TAG_DESC_FRIEND,
                Person.MSG_WEDDING_DATE_FORBIDDEN_FOR_VENDOR);
    }

    @Test
    public void parse_invalidBudget_failure() {
        assertParseFailure(parser,
                NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT + PARTNER_DESC_AMY + INVALID_BUDGET_DESC
                        + TAG_DESC_FRIEND,
                Budget.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrice_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + WEDDING_DATE_DESC_BOB + TYPE_DESC_VENDOR + TAG_DESC_FRIEND;

        assertParseFailure(parser,
                validExpectedPersonString + PRICE_DESC_BOB + " price/1000",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PRICE));
    }

    @Test
    public void parse_vendorWithoutWeddingDate_success() {
        String userInput = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TYPE_DESC_VENDOR + PRICE_DESC_BOB + TAG_DESC_FRIEND;

        Person expectedPerson = new PersonBuilder(BOB).withType(PersonType.VENDOR)
                .withWeddingDate((WeddingDate) null).withPrice(VALID_PRICE_BOB).withTags(VALID_TAG_FRIEND).build();

        assertParseSuccess(parser, userInput, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_clientWithoutWeddingDate_failure() {
        String userInput = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TYPE_DESC_CLIENT + BUDGET_DESC_BOB + PARTNER_DESC_BOB + TAG_DESC_FRIEND;

        assertParseFailure(parser, userInput, Person.MSG_WEDDING_DATE_REQUIRED_FOR_CLIENT);
    }

    @Test
    public void parse_clientWithPartner_success() {
        Person expected = new PersonBuilder().withName("Amy Bee").withPhone("11111111")
                .withEmail("amy@example.com").withAddress("Block 312, Amy Street 1")
                .withWeddingDate("01-01-2024").withType(PersonType.CLIENT).withPartner("Alex Tan")
                .withTags().build();

        String userInput = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT + PARTNER_DESC_AMY;

        assertParseSuccess(parser, userInput, new AddCommand(expected));
    }

    @Test
    public void parse_clientMissingPartner_failure() {
        String userInput = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT;

        String expectedMessage = AddCommand.MESSAGE_PARTNER_REQUIRED_FOR_CLIENT;
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_vendorWithoutPartner_success() {
        Person expected = new PersonBuilder().withName("Amy Bee").withPhone("11111111")
                .withEmail("amy@example.com").withAddress("Block 312, Amy Street 1")
                .withWeddingDate((WeddingDate) null).withType(PersonType.VENDOR).withTags().build();

        String userInput = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + TYPE_DESC_VENDOR;

        assertParseSuccess(parser, userInput, new AddCommand(expected));
    }

    @Test
    public void parse_vendorWithPartner_failure() {
        String userInput = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + TYPE_DESC_VENDOR + PARTNER_DESC_AMY;

        String expectedMessage = AddCommand.MESSAGE_PARTNER_FORBIDDEN_FOR_VENDOR;
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_clientWithTags_failure() {
        String userInput = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + WEDDING_DATE_DESC_AMY + TYPE_DESC_CLIENT + PARTNER_DESC_AMY + TAG_DESC_FRIEND;

        assertParseFailure(parser, userInput, Person.MSG_TAGS_FORBIDDEN_FOR_CLIENT);
    }
}
