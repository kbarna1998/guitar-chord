package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Fingering;
import model.GuitarChord;
import repository.ChordRepository;
import service.ChordService;

import java.util.*;

import static service.Dialog.*;

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
    public TextField modifierTextField;
    @FXML
    private TextField bassTextField;
    @FXML
    private Label fingeringLabel;
    @FXML
    private TextField fingeringTextField;
    @FXML
    private Button actionButton;
    @FXML
    private TableView<GuitarChord> chordlistTableView;
    @FXML
    private TableColumn<GuitarChord, String> indexTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> chordTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> fingeringTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> capoTableColumn;

    private String mode = "search";
    private List<GuitarChord> allChords = new ArrayList<>();
    private List<GuitarChord> filteredChords = new ArrayList<>();

    private final String filePath = "chords.json";
    private final ChordRepository chordRepository = new ChordRepository(filePath);
    private final ChordService chordService = new ChordService(chordRepository);

    @FXML
    private void initialize() throws Exception {
        initializeTableColumns();
        actionButton.setOnAction(event -> {
            try {
                onActionButton();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Platform.runLater(() -> rootTextField.requestFocus());
        fingeringLabel.setVisible(false);
        fingeringTextField.setVisible(false);
        searchButton.setStyle("-fx-background-color: lightblue;");
        loadAndShowChords();
    }

    private void initializeTableColumns() {
        indexTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(chordlistTableView.getItems().indexOf(cellData.getValue()) + 1)));
        chordTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDisplayName()));
        fingeringTableColumn.setCellValueFactory(cellData -> {
            String formattedFingering = String.join(" ",
                    cellData.getValue().getFingering().getFirst().getFingers());
            return new SimpleStringProperty(formattedFingering);
        });
        capoTableColumn.setCellValueFactory(cellData -> {
            Fingering fingering = cellData.getValue().getFingering().getFirst();
            String[] fingers = fingering.getFingers();
            int capoMax = ChordService.getMinFret(fingers);
            return new SimpleStringProperty(String.valueOf(capoMax));
        });
    }

    private void onActionButton() throws Exception {
        if (mode.equals("search")) {
            searchChords();
            updateTableView();
        } else if (mode.equals("create")) {
            createChord();
            searchChords();
            updateTableView();
        }
    }

    private void searchChords() throws Exception {
        filteredChords = chordService.searchChord(rootTextField.getText(), typeTextField.getText(), modifierTextField.getText(), bassTextField.getText());
    }

    private void createChord() throws Exception {
        GuitarChord newChord = chordService.createChord(rootTextField.getText(), typeTextField.getText(), modifierTextField.getText(), bassTextField.getText(), fingeringTextField.getText());
        allChords = chordService.saveChord(newChord);
        filteredChords = new ArrayList<>(allChords);
    }

    private void updateTableView() {
        chordlistTableView.setItems(FXCollections.observableArrayList(filteredChords));
    }

    private void loadAndShowChords() {
        try {
            allChords = chordService.loadAllChords();
            filteredChords = new ArrayList<>(allChords);
            updateTableView();
        } catch (Exception e) {
            showAlert("Hiba", "Nem sikerült beolvasni a JSON fájlt");
        }
    }

    @FXML
    private void switchToSearchMode(ActionEvent event) {
        mode = "search";
        fingeringLabel.setVisible(false);
        fingeringTextField.setVisible(false);
        rootTextField.setText("");
        typeTextField.setText("");
        modifierTextField.setText("");
        bassTextField.setText("");
        actionButton.setText("Keresés");
        Platform.runLater(() -> rootTextField.requestFocus());
        searchButton.setStyle("-fx-background-color: lightblue;");
        createButton.setStyle("");
    }

    @FXML
    private void switchToCreateMode(ActionEvent event) {
        mode = "create";
        fingeringLabel.setVisible(true);
        fingeringTextField.setVisible(true);
        fingeringTextField.setText("");
        actionButton.setText("Mentés");
        Platform.runLater(() -> rootTextField.requestFocus());
        searchButton.setStyle("");
        createButton.setStyle("-fx-background-color: lightblue;");
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}