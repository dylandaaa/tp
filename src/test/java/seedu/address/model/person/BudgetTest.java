package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class BudgetTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Budget(null));
    }

    @Test
    public void constructor_invalidBudget_throwsIllegalArgumentException() {
        String invalidBudget = "";
        assertThrows(IllegalArgumentException.class, () -> new Budget(invalidBudget));
    }

    @Test
    public void isValidBudget() {
        // null budget
        assertThrows(NullPointerException.class, () -> Budget.isValidBudget(null));

        // invalid budgets
        assertFalse(Budget.isValidBudget("")); // empty string
        assertFalse(Budget.isValidBudget(" ")); // spaces only
        assertFalse(Budget.isValidBudget("abc")); // non-numeric
        assertFalse(Budget.isValidBudget("12.50")); // decimal not allowed
        assertFalse(Budget.isValidBudget("-500")); // negative number
        assertFalse(Budget.isValidBudget("100-")); // incomplete range
        assertFalse(Budget.isValidBudget("-100")); // incomplete range
        assertFalse(Budget.isValidBudget("100--200")); // double hyphen
        assertFalse(Budget.isValidBudget("100-200-300")); // multiple ranges
        assertFalse(Budget.isValidBudget("12345678901")); // too many digits (>10)
        assertFalse(Budget.isValidBudget("5000-1000")); // invalid range (min > max)
        assertFalse(Budget.isValidBudget("1000-1000")); // equal min and max

        // invalid budgets - with special characters
        assertFalse(Budget.isValidBudget("$1000")); // dollar sign not allowed
        assertFalse(Budget.isValidBudget("1,000")); // comma not allowed
        assertFalse(Budget.isValidBudget("$1,000")); // dollar and comma not allowed
        assertFalse(Budget.isValidBudget("$800-$1500")); // dollar signs not allowed
        assertFalse(Budget.isValidBudget("1,000-2,000")); // commas not allowed
        assertFalse(Budget.isValidBudget("800–1500")); // en-dash not allowed
        assertFalse(Budget.isValidBudget("10 000"));

        // valid budgets - single numbers
        assertTrue(Budget.isValidBudget("1000")); // simple number
        assertTrue(Budget.isValidBudget("0")); // zero
        assertTrue(Budget.isValidBudget("1")); // single digit
        assertTrue(Budget.isValidBudget("1234567890")); // 10 digits (max)
        assertTrue(Budget.isValidBudget("10000000")); // large number

        // valid budgets - ranges
        assertTrue(Budget.isValidBudget("800-1500")); // simple range
        assertTrue(Budget.isValidBudget("100-200")); // small range
        assertTrue(Budget.isValidBudget("1000-5000")); // medium range
    }

    @Test
    public void equals() {
        Budget budget = new Budget("1000");

        // same values -> returns true
        assertTrue(budget.equals(new Budget("1000")));

        // same object -> returns true
        assertTrue(budget.equals(budget));

        // null -> returns false
        assertFalse(budget.equals(null));

        // different types -> returns false
        assertFalse(budget.equals(5.0f));

        // different values -> returns false
        assertFalse(budget.equals(new Budget("2000")));
        assertFalse(budget.equals(new Budget("1000-2000")));
    }

    @Test
    public void hashCode_sameBudget_sameHashCode() {
        Budget budget1 = new Budget("1500");
        Budget budget2 = new Budget("1500");
        assertTrue(budget1.hashCode() == budget2.hashCode());
    }

    @Test
    public void toString_returnsCorrectString() {
        Budget budget = new Budget("1000-2000");
        assertTrue(budget.toString().equals("$1,000-$2,000"));
        Budget singleBudget = new Budget("5000");
        assertTrue(singleBudget.toString().equals("$5,000"));
        Budget largeBudget = new Budget("10000");
        assertTrue(largeBudget.toString().equals("$10,000"));
    }
}
