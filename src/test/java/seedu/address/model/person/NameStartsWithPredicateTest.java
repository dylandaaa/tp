package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.testutil.PersonBuilder;

public class NameStartsWithPredicateTest {

    @Test
    public void equals() {
        NameStartsWithPredicate first = new NameStartsWithPredicate("al");
        NameStartsWithPredicate second = new NameStartsWithPredicate("ku");

        // same object -> true
        assertTrue(first.equals(first));

        // same value -> true
        NameStartsWithPredicate firstCopy = new NameStartsWithPredicate("al");
        assertTrue(first.equals(firstCopy));

        // different type -> false
        assertFalse(first.equals(1));

        // null -> false
        assertFalse(first.equals(null));

        // different query -> false
        assertFalse(first.equals(second));
    }

    @Test
    public void test_wordStartMatch_selfName() {
        // "Al" matches word-start in "Alice Bob"
        NameStartsWithPredicate p = new NameStartsWithPredicate("Al");
        assertTrue(p.test(new PersonBuilder().withName("Alice Bob").build()));

        // Case-insensitive: "ku" matches "Fiona Kunz"
        p = new NameStartsWithPredicate("ku");
        assertTrue(p.test(new PersonBuilder().withName("Fiona Kunz").build()));
    }

    @Test
    public void test_notWordStart_noMatch() {
        // "ria" does NOT match "Maria Chen" (substring but not at word start)
        NameStartsWithPredicate p = new NameStartsWithPredicate("ria");
        assertFalse(p.test(new PersonBuilder().withName("Maria Chen").build()));
    }

    @Test
    public void test_partnerNameMatch_forClientOnly() {
        NameStartsWithPredicate p = new NameStartsWithPredicate("Al");

        // CLIENT with partner "Alex Ong" -> match
        var client = new PersonBuilder()
                .withName("Amira Tan")
                .withType(PersonType.parse("CLIENT"))
                .withPartner("Alex Ong")
                .build();
        assertTrue(p.test(client));
    }

    @Test
    public void toStringMethod() {
        NameStartsWithPredicate predicate = new NameStartsWithPredicate("al");
        String expected = new ToStringBuilder(predicate).add("query", "al").toString();
        assertEquals(expected, predicate.toString());
    }
}
