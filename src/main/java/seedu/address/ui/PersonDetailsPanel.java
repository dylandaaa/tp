package seedu.address.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;

/**
 * A UI component that displays the details of a selected {@link Person}.
 */
public class PersonDetailsPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailsPanel.fxml";

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label address;
    @FXML
    private Label type;
    @FXML
    private Label weddingDate;
    @FXML
    private Label price;
    @FXML
    private Label budget;
    @FXML
    private Label tagsLine;
    @FXML
    private Label linkedPersonsLine;

    /**
     * Creates a new details panel bound to the given {@link Person}.
     * <p>
     * If {@code person} is {@code null}, the panel enters an empty state (no
     * selection).
     *
     * @param person The initial person to display; may be {@code null} to show the
     *               empty state.
     */
    public PersonDetailsPanel(Person person) {
        super(FXML);
        setPerson(person);
    }

    private static String fmtPhone(String raw) {
        return raw;
    }

    private static String fmtDate(Person p) {
        return p.getWeddingDate()
                .map(d -> d.toString())
                .orElse("—");
    }

    /**
     * Updates the panel to display the given {@link Person}.
     * <p>
     * Passing {@code null} resets the panel to an empty state.
     *
     * @param person The person to display; may be {@code null}.
     */
    public void setPerson(Person person) {
        if (person == null) {
            name.setText("No contact selected");
            phone.setText("");
            email.setText("");
            address.setText("");
            type.setText("");
            weddingDate.setText("");
            if (price != null) {
                price.setText("");
                price.setVisible(false);
                price.setManaged(false);
            }
            if (budget != null) {
                budget.setText("");
                budget.setVisible(false);
                budget.setManaged(false);
            }
            if (tagsLine != null) {
                tagsLine.setText("");
                tagsLine.setVisible(false);
                tagsLine.setManaged(false);
            }
            if (linkedPersonsLine != null) {
                linkedPersonsLine.setText("");
                linkedPersonsLine.setVisible(false);
                linkedPersonsLine.setManaged(false);
            }
            return;
        }

        name.setText(DisplayFormat.nameAndPartner(person));
        phone.setText("Phone: " + person.getPhone().value);
        email.setText("Email: " + person.getEmail().value);
        address.setText("Address: " + person.getAddress().value);
        String typeText = person.getType().display();
        type.setText("Type: " + typeText);

        if (person.getType() == PersonType.VENDOR) {
            weddingDate.setVisible(false);
            weddingDate.setManaged(false);
        } else {
            String wdText = person.getWeddingDate().isPresent()
                    ? person.getWeddingDate().get().toString()
                    : "-";
            weddingDate.setText("Wedding: " + wdText);
            weddingDate.setVisible(true);
            weddingDate.setManaged(true);
        }

        // Display price only for vendors with price
        if (person.getPrice().isPresent()) {
            price.setText("Price: " + person.getPrice().get().toString());
            price.setVisible(true);
            price.setManaged(true);
        } else {
            price.setText("");
            price.setVisible(false);
            price.setManaged(false);
        }

        // Display budget only for clients with budget
        if (person.getBudget().isPresent()) {
            budget.setText("Budget: " + person.getBudget().get().toString());
            budget.setVisible(true);
            budget.setManaged(true);
        } else {
            budget.setText("");
            budget.setVisible(false);
            budget.setManaged(false);
        }

        // Display tags only for vendors
        if (person.getType() == PersonType.CLIENT || person.getTags().isEmpty()) {
            tagsLine.setText("");
            tagsLine.setVisible(false);
            tagsLine.setManaged(false); // remove its layout space
        } else {
            String tagsCsv = person.getTags().stream()
                    .sorted(Comparator.comparing(t -> t.tagName))
                    .map(t -> t.tagName)
                    .collect(Collectors.joining(", "));
            tagsLine.setText("Tags: " + tagsCsv);
            tagsLine.setVisible(true);
            tagsLine.setManaged(true);
        }

        // Display linked persons with their type (CLIENT/VENDOR) and tags
        if (person.getLinkedPersons().isEmpty()) {
            linkedPersonsLine.setText("");
            linkedPersonsLine.setVisible(false);
            linkedPersonsLine.setManaged(false);
        } else {
            String linkedPersonsText = person.getLinkedPersons().stream()
                    .sorted(Comparator.comparing(p -> p.getName().fullName))
                    .map(p -> {
                        // Prefix = wedding date when the selected person is a VENDOR and the linked is a CLIENT
                        String prefix;
                        if (person.getType() == PersonType.VENDOR && p.getType() == PersonType.CLIENT) {
                            prefix = fmtDate(p);
                        } else {
                            // fallback to your existing category/type label
                            prefix = p.getTags().stream()
                                    .sorted(Comparator.comparing(t -> t.tagName))
                                    .findFirst()
                                    .map(t -> capitalize(t.tagName))
                                    .orElse(p.getType().display());
                        }

                        // For clients, keep Name & Partner; for vendors, just Name
                        String displayName = (p.getType() == PersonType.CLIENT)
                                ? DisplayFormat.nameAndPartner(p)
                                : p.getName().fullName;

                        String namePhone = displayName + " (" + fmtPhone(p.getPhone().value) + ")";
                        return "• " + prefix + ": " + namePhone;
                    })
                    .collect(Collectors.joining("\n"));


            // Determine if linked persons are vendors or clients
            String label = person.getLinkedPersons().stream()
                    .findFirst()
                    .map(p -> p.getType().display() + "s")
                    .orElse("Linked");

            linkedPersonsLine.setText(label + ":\n" + linkedPersonsText);
            linkedPersonsLine.setVisible(true);
            linkedPersonsLine.setManaged(true);
        }
    }

    /**
     * Capitalizes the first letter of a string.
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
