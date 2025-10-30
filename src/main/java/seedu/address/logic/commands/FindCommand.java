package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameStartsWithPredicate;

/**
 * Finds and lists all persons whose names contain a word that starts with the given query
 * (case-insensitive). Also matches a CLIENT's partner name.
 */
public class FindCommand extends Command {

    /** Primary command word used in the CLI. */
    public static final String COMMAND_WORD = "find";

    /**
     * Usage message shown when the command format is invalid.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain a word "
            + "that starts with the given query (case-insensitive). Also matches a CLIENT's partner name.\n"
            + "Parameters: QUERY\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " ma  (matches Maria Chen)\n"
            + "  " + COMMAND_WORD + " ch  (matches Maria Chen)";

    private final NameStartsWithPredicate predicate;

    /**
     * Constructs a {@code FindCommand} with the provided word-start predicate.
     *
     * @param predicate predicate that returns true when a person's (or CLIENT partner's) name
     *                  has any word that starts with the query (case-insensitive).
     */
    public FindCommand(NameStartsWithPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int count = model.getFilteredPersonList().size();
        String message = Messages.getPersonsListedMessage(count)
                + "\nUse the 'list' command to go back and view all contacts.";
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindCommand
                && predicate.equals(((FindCommand) other).predicate));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
