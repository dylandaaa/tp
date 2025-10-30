package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARTNER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEDDING_DATE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a contact (vendor or client) to KnotBook.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_TYPE_INVALID = "Invalid type. Please choose either 'client' or 'vendor'.";
    public static final String MESSAGE_PRICE_ONLY_FOR_VENDOR = "Price is only applicable for vendors.";
    public static final String MESSAGE_BUDGET_ONLY_FOR_CLIENT = "Budget is only applicable for clients.";
    public static final String MESSAGE_PARTNER_REQUIRED_FOR_CLIENT = "Partner is required when type is CLIENT.";
    public static final String MESSAGE_PARTNER_FORBIDDEN_FOR_VENDOR = "Partner must be empty when type is VENDOR.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a contact (vendor or client) to KnotBook.\n\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_WEDDING_DATE + "DATE "
            + PREFIX_TYPE + "(client|vendor) "
            + "[" + PREFIX_PRICE + "PRICE] "
            + "[" + PREFIX_BUDGET + "BUDGET] "
            + "[" + PREFIX_PARTNER + "PARTNER] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "  For clients only: " + PREFIX_PARTNER + "PARTNER, [" + PREFIX_BUDGET
            + "BUDGET] (e.g., 5000 or 5000-10000)\n"
            + "  For vendors only: [" + PREFIX_PRICE + "PRICE] (e.g., 1000 or 1000-2000), [" + PREFIX_TAG
            + "TAG]...\n\n"
            + "Example 1 (Client): " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_WEDDING_DATE + "15-06-2020 "
            + PREFIX_TYPE + "client "
            + PREFIX_PARTNER + "Jane Doe "
            + PREFIX_BUDGET + "5000-10000 "
            + "\n\n"
            + "Example 2 (Vendor): " + COMMAND_WORD + " "
            + PREFIX_NAME + "Jane Smith "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "jane@example.com "
            + PREFIX_ADDRESS + "123 Orchard Road, #03-45 "
            + PREFIX_TYPE + "vendor "
            + PREFIX_PRICE + "1500 "
            + PREFIX_TAG + "photographer";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists"
            + " in the address book (same phone number)";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
