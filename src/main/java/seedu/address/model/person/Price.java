package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Vendor's price in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {

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
     * Constructs a {@code Price}.
     *
     * @param price A valid price.
     */
    public Price(String price) {
        requireNonNull(price);
        String trimmed = price.trim();
        checkArgument(isValidPrice(trimmed), MESSAGE_CONSTRAINTS);
        value = trimmed;
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String test) {
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
        return formatPrice(value);
    }

    /**
     * Formats a price string with $ prefix and thousand separators.
     * Handles both single values and ranges.
     */
    private static String formatPrice(String price) {
        if (price.contains("-")) {
            String[] parts = price.split("-");
            return "$" + formatNumber(parts[0]) + "-$" + formatNumber(parts[1]);
        }
        return "$" + formatNumber(price);
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
        if (!(other instanceof Price)) {
            return false;
        }

        Price otherPrice = (Price) other;
        return value.equals(otherPrice.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
