package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.GuitarChord;

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
    private Button actionButton;
    @FXML
    private TableView<GuitarChord> chordlistTableView;
    @FXML
    private TableColumn<GuitarChord, String> idTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> chordTableColumn;
    @FXML
    private TableColumn<GuitarChord, String> fingeringTableColumn;

    private ObservableList<GuitarChord> chordList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idTableColumn.setCellValueFactory(cellData -> {
            int index = chordList.indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(index));
        });

        chordTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getChordName()));

        fingeringTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFingeringAsString()));

        // Manuálisan 5 akkord hozzáadása
        chordList.addAll(
                new GuitarChord("C", "maj", "", new String[]{"X", "3", "2", "0", "1", "0"}),
                new GuitarChord("D", "maj", "", new String[]{"X", "X", "0", "2", "3", "2"}),
                new GuitarChord("E", "min", "", new String[]{"0", "2", "2", "0", "0", "0"}),
                new GuitarChord("G", "maj", "", new String[]{"3", "2", "0", "0", "0", "3"}),
                new GuitarChord("A", "min", "", new String[]{"X", "0", "2", "2", "1", "0"})
        );

        chordlistTableView.setItems(chordList);
    }
}