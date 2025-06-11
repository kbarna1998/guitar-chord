package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.NonNull;
import model.Fingering;
import model.GuitarChord;
import repository.ChordRepository;

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
    @FXML
    private TableColumn<GuitarChord, String> capoTableColumn;

    private String mode = "search";
    private List<GuitarChord> allChords = new ArrayList<>();
    private List<GuitarChord> filteredChords = new ArrayList<>();
    private final String filePath = "D:/DE_IK_PTI/4. félév/Szoftverfejlesztés/guitar-chord/chords.json";
    private final ChordRepository chordRepository = new ChordRepository(filePath);

    @FXML
    private void initialize() {
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(chordlistTableView.getItems().indexOf(cellData.getValue()) + 1)));
        chordTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                            cellData.getValue().getRoot() +
                                cellData.getValue().getType() +
                                (cellData.getValue().getModifier() != null ? cellData.getValue().getModifier() : "") +
                                (cellData.getValue().getBass() != null && !cellData.getValue().getBass().isEmpty() ? "/" + cellData.getValue().getBass() : "")
                ));
        fingeringTableColumn.setCellValueFactory(cellData -> {
            String formattedFingering = String.join(" ",
                    cellData.getValue().getFingering().getFirst().getFingers());
            return new SimpleStringProperty(formattedFingering);
        });
        capoTableColumn.setCellValueFactory(cellData -> {
            Fingering fingering = cellData.getValue().getFingering().getFirst();
            String[] fingers = fingering.getFingers();
            int capoMax  = getMinFret(fingers);
            return new SimpleStringProperty(String.valueOf(capoMax));
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

    public int getMinFret(String[] fingers) {
        int min = Integer.MAX_VALUE;
        for (String finger : fingers) {
            try {
                int fret = Integer.parseInt(finger);
                if (fret < min) {
                    min = fret;
                }
            } catch (NumberFormatException ignored) {
                //így ignoráljuk
            }
        }
        return min;
    }

    private void firstRunShowChords() {
        loadChordsFromJson();
        searchChords();
        showSearchResult();
    }

    private void loadChordsFromJson() {
        try {
            allChords = chordRepository.loadChords();
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
            boolean match = (root.isEmpty() || chord.getRoot().equalsIgnoreCase(root)) &&
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
            List<GuitarChord> updatedChords = chordRepository.updateChord(newChord);
            allChords = updatedChords;
        } catch (IllegalStateException e) {
            showAlert("Már létezik", e.getMessage());
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
        if (string.isEmpty()) {
            return string;
        } else if (string.length() == 1) {
            return string.toUpperCase();
        } else {
            return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        }
    }
}