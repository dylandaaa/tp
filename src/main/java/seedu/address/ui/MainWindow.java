package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    private PersonListPanel personListPanel;
    private PersonDetailsPanel personDetailsPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private Person currentlySelectedPerson;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane personDetailsPlaceholder;

    @FXML
    private SplitPane splitPane;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        helpWindow = new HelpWindow();
    }


    @FXML
    private void initialize() {
        final double min = 0.40;
        final double max = 0.60;
        SplitPane.Divider d = splitPane.getDividers().get(0);

        d.positionProperty().addListener((obs, oldV, newV) -> {
            double v = newV.doubleValue();
            if (v < min) {
                d.setPosition(min);
            }
            else if (v > max) {
                d.setPosition(max);
            }
        });

        d.setPosition(Math.max(min, Math.min(max, 0.40)));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(
                logic.getFilteredPersonList(),
                this::onPersonSelected
        );
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        personDetailsPanel = new PersonDetailsPanel(null);
        personDetailsPlaceholder.getChildren().add(personDetailsPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    private void onPersonSelected(Person person) {
        currentlySelectedPerson = person;
        personDetailsPanel.setPerson(person);
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            // Refresh the details panel if a person is currently selected
            refreshDetailsPanel();

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Refreshes the person details panel to show the latest data of the currently selected person.
     * This ensures that any changes made through commands (edit, link, unlink, etc.) are immediately
     * reflected in the details panel.
     */
    private void refreshDetailsPanel() {
        if (currentlySelectedPerson == null) {
            return;
        }

        // Find the updated person object in the filtered list by matching phone number (unique identifier)
        Person updatedPerson = logic.getFilteredPersonList().stream()
                .filter(p -> p.getPhone().equals(currentlySelectedPerson.getPhone()))
                .findFirst()
                .orElse(null);

        // Update the details panel with the refreshed person data
        if (updatedPerson != null) {
            currentlySelectedPerson = updatedPerson;
            personDetailsPanel.setPerson(updatedPerson);
        } else {
            // Person was deleted, clear the details panel
            currentlySelectedPerson = null;
            personDetailsPanel.setPerson(null);
        }
    }
}
