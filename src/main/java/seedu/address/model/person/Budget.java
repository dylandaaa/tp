package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Client's budget in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBudget(String)}
 */
public class Budget {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid amount. Enter a number (e.g., 1200) or a range (e.g., 800-1500).\n"
                    + "Only digits and a single hyphen are allowed (no spaces, commas, or $).\n"
                    + "For ranges, the left value must be strictly less than the right value.";

    /*
     * Accepts:
     * - Single number: "1200"
     * - Range with hyphen: "800-1500"
     * Only digits and hyphens allowed (no commas, no dollar signs)
     */
    public static final String VALIDATION_REGEX = "^\\d{1,10}(-\\d{1,10})?$";

    public final String value;

    /**
     * Constructs a {@code Budget}.
     *
     * @param budget A valid budget.
     */
    public Budget(String budget) {
        requireNonNull(budget);
        String trimmed = budget.trim();
        checkArgument(isValidBudget(trimmed), MESSAGE_CONSTRAINTS);
        value = trimmed;
    }

    /**
     * Returns true if a given string is a valid budget.
     */
    public static boolean isValidBudget(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        // If it's a range, validate that min <= max
        if (test.contains("-")) {
            String[] parts = test.split("-");
            if (parts.length == 2) {
                try {
                    long min = Long.parseLong(parts[0]);
                    long max = Long.parseLong(parts[1]);
                    return min < max;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns a formatted string representation with $ and commas.
     */
    @Override
    public String toString() {
        return formatBudget(value);
    }

    /**
     * Formats a budget string with $ prefix and thousand separators.
     * Handles both single values and ranges.
     */
    private static String formatBudget(String budget) {
        if (budget.contains("-")) {
            String[] parts = budget.split("-");
            return "$" + formatNumber(parts[0]) + "-$" + formatNumber(parts[1]);
        }
        return "$" + formatNumber(budget);
    }

    /**
     * Formats a number with thousand separators.
     */
    private static String formatNumber(String number) {
        try {
            long num = Long.parseLong(number);
            return String.format("%,d", num);
        } catch (NumberFormatException e) {
            return number;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Budget)) {
            return false;
        }

        Budget otherBudget = (Budget) other;
        return value.equals(otherBudget.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
