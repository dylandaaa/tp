package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameStartsWithPredicate;
import seedu.address.model.person.PersonType;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameStartsWithPredicate firstPredicate = new NameStartsWithPredicate("al");
        NameStartsWithPredicate secondPredicate = new NameStartsWithPredicate("ku");

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_nonMatchingPrefix_noPersonFound() {
        String expectedMessage = Messages.getPersonsListedMessage(0)
                + "\nUse the 'list' command to go back and view all contacts.";
        NameStartsWithPredicate predicate = preparePredicate("ria"); // not a word-start in TypicalPersons
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_prefixMatchesMultiplePersons() {
        // "ku" matches word-starts in "Kurz" (CARL) and "Kunz" (FIONA)
        String expectedMessage = Messages.getPersonsListedMessage(2)
                + "\nUse the 'list' command to go back and view all contacts.";
        NameStartsWithPredicate predicate = preparePredicate("ku");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_findByPartnerName_clientShown() {
        var amira = new PersonBuilder()
                .withName("Amira Tan")
                .withType(PersonType.parse("CLIENT"))
                .withPartner("Kathleen Ong")
                .build();
        model.addPerson(amira);
        expectedModel.addPerson(amira);

        String expectedMessage = Messages.getPersonsListedMessage(1)
                + "\nUse the 'list' command to go back and view all contacts.";
        NameStartsWithPredicate predicate = preparePredicate("Ka"); // word-start "Al" in partner
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(amira), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        NameStartsWithPredicate predicate = new NameStartsWithPredicate("al");
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    private NameStartsWithPredicate preparePredicate(String userInput) {
        return new NameStartsWithPredicate(userInput);
    }
}
