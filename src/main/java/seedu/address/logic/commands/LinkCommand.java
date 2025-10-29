package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;

/**
 * Links a client to a vendor in the address book.
 */
public class LinkCommand extends Command {

    public static final String COMMAND_WORD = "link";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Links a client to a vendor using their index numbers in the displayed person list.\n"
            + "Parameters: client/CLIENT_INDEX vendor/VENDOR_INDEX "
            + "(both indexes must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " client/3 vendor/5";

    public static final String MESSAGE_LINK_SUCCESS = "Link made successfully!";
    public static final String MESSAGE_LINK_ALREADY_EXISTS = "This client and vendor are already linked.";
    public static final String MESSAGE_INVALID_CLIENT_INDEX = "Invalid client index. "
            + "It must be a valid number referring to an existing client.";
    public static final String MESSAGE_INVALID_VENDOR_INDEX = "Invalid vendor index. "
            + "It must be a valid number referring to an existing vendor.";
    public static final String MESSAGE_OUT_OF_BOUNDS_CLIENT_INDEX = "Client index is out of bounds. "
        + "\n\nPlease enter a number within the range of the displayed person list.";
    public static final String MESSAGE_OUT_OF_BOUNDS_VENDOR_INDEX = "Vendor index is out of bounds. "
        + "\n\nPlease enter a number within the range of the displayed person list.";

    private final Index clientIndex;
    private final Index vendorIndex;

    /**
     * Creates a LinkCommand to link the specified client and vendor.
     *
     * @param clientIndex Index of the client in the filtered person list.
     * @param vendorIndex Index of the vendor in the filtered person list.
     */
    public LinkCommand(Index clientIndex, Index vendorIndex) {
        requireNonNull(clientIndex);
        requireNonNull(vendorIndex);
        this.clientIndex = clientIndex;
        this.vendorIndex = vendorIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_OUT_OF_BOUNDS_CLIENT_INDEX);
        }

        if (vendorIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_OUT_OF_BOUNDS_VENDOR_INDEX);
        }

        Person client = lastShownList.get(clientIndex.getZeroBased());
        Person vendor = lastShownList.get(vendorIndex.getZeroBased());

        // Validate that the person at clientIndex is actually a client
        if (client.getType() != PersonType.CLIENT) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_INDEX);
        }

        // Validate that the person at vendorIndex is actually a vendor
        if (vendor.getType() != PersonType.VENDOR) {
            throw new CommandException(MESSAGE_INVALID_VENDOR_INDEX);
        }

        // Check if already linked
        if (client.isLinkedTo(vendor)) {
            throw new CommandException(MESSAGE_LINK_ALREADY_EXISTS);
        }

        // Create updated persons with the new links
        Person updatedClient = createPersonWithLink(client, vendor);
        Person updatedVendor = createPersonWithLink(vendor, client);

        // Update both persons in the model
        model.setPerson(client, updatedClient);
        model.setPerson(vendor, updatedVendor);

        return new CommandResult(MESSAGE_LINK_SUCCESS);
    }

    /**
     * Creates a new Person with an additional link to the specified person.
     */
    private Person createPersonWithLink(Person person, Person linkedPerson) {
        Set<Person> updatedLinks = new HashSet<>(person.getLinkedPersons());
        updatedLinks.add(linkedPerson);

        if (person.getType() == PersonType.VENDOR) {
            return new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getAddress(),
                    person.getType(),
                    person.getTags(),
                    updatedLinks,
                    person.getPrice().orElse(null));
        } else {
            return new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getAddress(),
                    person.getWeddingDate().orElse(null),
                    person.getType(),
                    person.getTags(),
                    updatedLinks,
                    person.getPrice().orElse(null),
                    person.getBudget().orElse(null),
                    person.getPartner());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LinkCommand)) {
            return false;
        }

        LinkCommand otherLinkCommand = (LinkCommand) other;
        return clientIndex.equals(otherLinkCommand.clientIndex)
                && vendorIndex.equals(otherLinkCommand.vendorIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("vendorIndex", vendorIndex)
                .toString();
    }
}
