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
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEDDING_DATE;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
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
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PARTNER + "PARTNER] "
            + "[" + PREFIX_WEDDING_DATE + "WEDDING_DATE] "
            + "[" + PREFIX_PRICE + "PRICE] "
            + "[" + PREFIX_BUDGET + "BUDGET] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com \n"
            + "Note: Type (client/vendor) cannot be edited after creation.";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "This person already exists in the address book (same phone number).";
    public static final String MESSAGE_TYPE_IMMUTABLE = "Type cannot be changed.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson;
        try {
            editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        } catch (CommandException e) {
            throw e;
        }

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // Replace the edited person, preserving their links on the edited instance
        model.setPerson(personToEdit, editedPerson);

        // Update all counterparts that previously linked to the old person to now link
        // to the edited one
        for (Person other : model.getAddressBook().getPersonList()) {
            if (other.equals(editedPerson)) {
                continue;
            }
            if (other.isLinkedTo(personToEdit)) {
                Person updatedOther = replaceLink(other, personToEdit, editedPerson);
                model.setPerson(other, updatedOther);
            }
        }
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        WeddingDate updatedWeddingDate = editPersonDescriptor.getWeddingDate()
                .orElse(personToEdit.getWeddingDate().orElse(null));
        PersonType unchangedType = personToEdit.getType();
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Price updatedPrice = editPersonDescriptor.getPrice().orElse(personToEdit.getPrice().orElse(null));
        Budget updatedBudget = editPersonDescriptor.getBudget().orElse(personToEdit.getBudget().orElse(null));
        Optional<Partner> updatedPartner = editPersonDescriptor.getPartner().map(Optional::of)
                .orElse(personToEdit.getPartner());

        if (unchangedType == PersonType.CLIENT && updatedPartner.isEmpty()) {
            throw new CommandException(Person.MSG_PARTNER_REQUIRED_FOR_CLIENT);
        }
        if (unchangedType == PersonType.VENDOR && updatedPartner.isPresent()) {
            throw new CommandException(Person.MSG_PARTNER_FORBIDDEN_FOR_VENDOR);
        }
        if (unchangedType == PersonType.CLIENT && updatedPrice != null) {
            throw new CommandException("Price is only applicable for vendors.");
        }
        if (unchangedType == PersonType.VENDOR && updatedBudget != null) {
            throw new CommandException("Budget is only applicable for clients.");
        }

        if (unchangedType == PersonType.CLIENT && updatedWeddingDate == null) {
            throw new CommandException(Person.MSG_WEDDING_DATE_REQUIRED_FOR_CLIENT);
        }
        if (unchangedType != PersonType.CLIENT && updatedWeddingDate != null) {
            throw new CommandException(Person.MSG_WEDDING_DATE_FORBIDDEN_FOR_VENDOR);
        }

        // Validate tags - only vendors can have tags
        if (unchangedType == PersonType.CLIENT && !updatedTags.isEmpty()) {
            throw new CommandException(Person.MSG_TAGS_FORBIDDEN_FOR_CLIENT);
        }

        java.util.Set<Person> existingLinks = new java.util.HashSet<>(personToEdit.getLinkedPersons());

        if (unchangedType == PersonType.VENDOR) {
            // Preserve links for vendor
            return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, unchangedType, updatedTags,
                    existingLinks, updatedPrice);
        } else {
            // Preserve links and budget/partner for client
            return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedWeddingDate,
                    unchangedType, updatedTags, existingLinks, updatedPrice, updatedBudget, updatedPartner);
        }
    }

    /**
     * Returns a copy of {@code original} where {@code oldLink} in its link set is replaced with {@code newLink}.
     * Preserves all other fields as-is.
     */
    private static Person replaceLink(Person original, Person oldLink, Person newLink) {
        java.util.Set<Person> updatedLinks = new java.util.HashSet<>(original.getLinkedPersons());
        updatedLinks.remove(oldLink);
        updatedLinks.add(newLink);

        if (original.getType() == PersonType.VENDOR) {
            return new Person(
                    original.getName(),
                    original.getPhone(),
                    original.getEmail(),
                    original.getAddress(),
                    original.getType(),
                    original.getTags(),
                    updatedLinks,
                    original.getPrice().orElse(null)
            );
        } else {
            return new Person(
                    original.getName(),
                    original.getPhone(),
                    original.getEmail(),
                    original.getAddress(),
                    original.getWeddingDate().orElse(null),
                    original.getType(),
                    original.getTags(),
                    updatedLinks,
                    original.getPrice().orElse(null),
                    original.getBudget().orElse(null),
                    original.getPartner()
            );
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private WeddingDate weddingDate;
        private Price price;
        private Budget budget;
        private Set<Tag> tags;
        private Partner partner;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setWeddingDate(toCopy.weddingDate);
            setPrice(toCopy.price);
            setBudget(toCopy.budget);
            setPartner(toCopy.partner);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, weddingDate, price, budget,
                    partner, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setWeddingDate(WeddingDate weddingDate) {
            this.weddingDate = weddingDate;
        }

        public Optional<WeddingDate> getWeddingDate() {
            return Optional.ofNullable(weddingDate);
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        public void setBudget(Budget budget) {
            this.budget = budget;
        }

        public Optional<Budget> getBudget() {
            return Optional.ofNullable(budget);
        }

        public void setPartner(Partner partner) {
            this.partner = partner;
        }

        public Optional<Partner> getPartner() {
            return Optional.ofNullable(partner);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws
         * {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(weddingDate, otherEditPersonDescriptor.weddingDate)
                    && Objects.equals(price, otherEditPersonDescriptor.price)
                    && Objects.equals(budget, otherEditPersonDescriptor.budget)
                    && Objects.equals(partner, otherEditPersonDescriptor.partner)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("weddingDate", weddingDate)
                    .add("price", price)
                    .add("budget", budget)
                    .add("partner", partner)
                    .add("tags", tags)
                    .toString();
        }
    }
}
