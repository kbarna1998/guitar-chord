package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import model.Fingering;
import model.GuitarChord;

import java.io.FileInputStream;
import java.util.*;

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
    private TableColumn<GuitarChord, String> idTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> chordTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> fingeringTableColumn;

    private String mode = "search";
    private List<GuitarChord> allChords = new ArrayList<>();
    private List<GuitarChord> filteredChords = new ArrayList<>();
    private final String filePath = "D:/DE_IK_PTI/4. félév/Szoftverfejlesztés/guitar-chord/chords.json";

    @FXML
    private void initialize() {
        idTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(chordlistTableView.getItems().indexOf(cellData.getValue()) + 1)));
        chordTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getRoot() +
                                cellData.getValue().getType() +
                                (cellData.getValue().getModifier() != null ? cellData.getValue().getModifier() : "") +
                                (cellData.getValue().getBass() != null && !cellData.getValue().getBass().isEmpty() ? "/" + cellData.getValue().getBass() : "")
                ));
        fingeringTableColumn.setCellValueFactory(cellData -> {
            String formattedFingering = String.join(" ",
                    cellData.getValue().getFingering().get(0).getFingers());
            return new javafx.beans.property.SimpleStringProperty(formattedFingering);
        });
        actionButton.setOnAction(event -> {
            if (mode.equals("search")) {
                loadChordsFromJson();
                searchChords();
                showSearchResult();
            } else if (mode.equals("create")) {
                createChords();
                loadChordsFromJson();
                searchChords();
                showSearchResult();
            }
        });
        Platform.runLater(() -> rootTextField.requestFocus());
        fingeringLabel.setVisible(false);
        fingeringTextField.setVisible(false);
        firstRunShowChords();
    }

    private void firstRunShowChords() {
        loadChordsFromJson();
        searchChords();
        showSearchResult();
    }

    private void loadChordsFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream chordsStream = new FileInputStream(filePath);
            allChords = mapper.readValue(chordsStream, new TypeReference<List<GuitarChord>>() {});
        } catch (Exception e) {
            showAlert("Hiba", "Nem sikerült beolvasni a JSON fájlt");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void searchChords() {
        String root = rootTextField.getText().trim();
        String type = typeTextField.getText().trim();
        String modifier = modifierTextField.getText().trim();
        String bass = bassTextField.getText().trim();

        filteredChords = new ArrayList<>();

        for (GuitarChord chord : allChords) {
            boolean match =
                            (root.isEmpty() || chord.getRoot().equalsIgnoreCase(root)) &&
                            (type.isEmpty() || chord.getType().equalsIgnoreCase(type)) &&
                            (modifier.isEmpty() || chord.getModifier().equalsIgnoreCase(modifier)) &&
                            (bass.isEmpty() || chord.getBass().equalsIgnoreCase(bass));

            if (match) {
                for (Fingering fingering : chord.getFingering()) {
                    GuitarChord chordCopy = new GuitarChord(
                            chord.getRoot(),
                            chord.getType(),
                            chord.getModifier(),
                            chord.getBass(),
                            List.of(fingering)
                    );
                    filteredChords.add(chordCopy);
                }
            }
        }
    }

    private void showSearchResult() {
        ObservableList<GuitarChord> items = FXCollections.observableArrayList(filteredChords);
        chordlistTableView.setItems(items);
    }

    private void createChords() {
        String root = firstLetterUpperCase(rootTextField.getText().trim());;
        String type = typeTextField.getText().trim().toLowerCase();
        String modifier = modifierTextField.getText().trim().toLowerCase();
        String bass = firstLetterUpperCase(bassTextField.getText().trim());
        String fingeringInput = fingeringTextField.getText().trim().toUpperCase();

        String[] fingers = fingeringInput.split("\\s+");
        if (fingers.length != 6) {
            showAlert("Beviteli hiba", "A 6 húr helyett " + fingers.length + " húrt adtál meg.");
            return;
        }

        for (int i = 0; i < fingers.length; i++) {
            fingers[i] = firstLetterUpperCase(fingers[i]);
        }

        Fingering fingering = new Fingering();
        fingering.setFingers(fingeringInput.split("\\s+"));

        GuitarChord newChord = new GuitarChord(
                root,
                type.isEmpty() ? "" : type,
                modifier.isEmpty() ? "" : modifier,
                bass.isEmpty() ? "" : bass,
                List.of(fingering));
        writeChords(newChord);
    }

    private void writeChords(GuitarChord newChord) {
        try {
            var objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            InputStream chordsStream = new FileInputStream(filePath);
            List<GuitarChord> chords = objectMapper.readValue(chordsStream, new TypeReference<List<GuitarChord>>() {});

            boolean found = false;
            for (GuitarChord chord : chords) {
                if (Objects.equals(chord.getRoot(), newChord.getRoot()) &&
                Objects.equals(chord.getType(), newChord.getType()) &&
                Objects.equals(chord.getModifier(), newChord.getModifier()) &&
                Objects.equals(chord.getBass(), newChord.getBass())) {
                    found = true;

                    boolean fingeringExists = chord.getFingering().stream().anyMatch(f -> f.equals(newChord.getFingering().get(0)));
                    if (!fingeringExists) {
                        chord.getFingering().addAll(newChord.getFingering());
                    } else {
                        showAlert("Már létezik", "Az akkord már létezik a megadott lefogással.");
                        return;
                    }
                    break;
                }
            }
            if (!found) {
                chords.add(newChord);
            }
            try (var writer = new java.io.FileWriter(filePath)) {
                objectMapper.writeValue(writer, chords);
            }
            allChords = chords;
        } catch (Exception e) {
            showAlert("Hiba", "Nem sikerült menteni a JSON fájlt:\n" + e.getMessage());
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
    }

    @FXML
    private void switchToCreateMode(ActionEvent event) {
        mode = "create";
        fingeringLabel.setVisible(true);
        fingeringTextField.setVisible(true);
        fingeringTextField.setText("");
        actionButton.setText("Mentés");
        Platform.runLater(() -> rootTextField.requestFocus());
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }

    public String firstLetterUpperCase(String string) {
        if (string.equals("")) {
            return string;
        } else if (string.length() == 1) {
            return string.toUpperCase();
        } else {
            return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        }
    }
}