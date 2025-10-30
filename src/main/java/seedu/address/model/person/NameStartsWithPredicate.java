package seedu.address.model.person;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Matches if a {@link Person}'s name OR (for CLIENTs) their partner's name
 * contains a word that starts with the given query (case-insensitive).
 * <p>
 * Implementation detail: uses {@code toString()} of {@code Name} and {@code Partner}
 * rather than any {@code fullName} field.
 */
public class NameStartsWithPredicate implements Predicate<Person> {

    private final String query;

    private final Pattern wordStartPattern;

    /**
     * @param rawQuery user-provided query; must be non-null and non-empty after trim.
     */
    public NameStartsWithPredicate(String rawQuery) {
        Objects.requireNonNull(rawQuery, "Query must not be null");
        String q = rawQuery.trim();
        if (q.isEmpty()) {
            throw new IllegalArgumentException("Query must not be empty");
        }
        this.query = q.toLowerCase(); // for equals/toString only
        this.wordStartPattern = Pattern.compile("(?i)\\b" + Pattern.quote(q));
    }

    @Override
    public boolean test(Person person) {
        // Self name via toString (since there is no fullName field)
        String selfName = person.getName() == null ? "" : person.getName().toString();
        if (wordStartPattern.matcher(selfName).find()) {
            return true;
        }

        // For CLIENTs, also match partner name at word-start
        if (person.getType() == PersonType.CLIENT) {
            String partnerName = person.getPartner().map(Partner::toString).orElse("");
            if (wordStartPattern.matcher(partnerName).find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof NameStartsWithPredicate
                && query.equals(((NameStartsWithPredicate) other).query));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("query", query).toString();
    }
}
