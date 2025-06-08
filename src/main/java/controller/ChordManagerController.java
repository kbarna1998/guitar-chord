package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.InputStream;

public class ChordManagerController {
    @FXML
    private Button searchButton;
    @FXML
    private Button createButton;
    @FXML
    private Button exitButton;
    @FXML
    private TextField rootTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField bassTextField;
    @FXML
    private TextField fingeringTextField;
    @FXML
    private Label fingeringLabel;
    @FXML
    private Button actionButton;
    @FXML
    private TableView<?> chordlistTableView;
    @FXML
    private TableColumn<?,?> idTableColumn;
    @FXML
    private TableColumn<?,?> chordTableColumn;
    @FXML
    private TableColumn<?,?> fingeringTableColumn;

    private String mode = "search";
    public String getMode() {
        return mode;
    }

    @FXML
    private void initialize() {
        searchButton.setOnAction(e -> switchToSearchMode());
        createButton.setOnAction(e -> switchToCreateMode());
        exitButton.setOnAction(e -> Platform.exit());
    }

    private void switchToCreateMode() {
        mode = "create";
        fingeringLabel.setVisible(false);
        fingeringTextField.setVisible(false);
        actionButton.setText("Mentés");
    }

    private void switchToSearchMode() {
        mode = "search";
        fingeringLabel.setVisible(true);
        fingeringTextField.setVisible(true);
        actionButton.setText("Keresés");
    }
}