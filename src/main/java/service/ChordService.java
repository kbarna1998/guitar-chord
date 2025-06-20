package service;

import model.Fingering;
import model.GuitarChord;
import repository.ChordRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChordService {
    private final String[] validNotes = { "C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "B", "H" };
    private final ChordRepository repository;

    public ChordService(ChordRepository repository) {
        this.repository = repository;
    }

    public List<GuitarChord> searchChord(String root, String type, String modifier, String bass) throws IOException {
        List<GuitarChord> allChords = repository.loadChords();

        return allChords.stream().filter(chord ->
                (root == null || root.isEmpty() || chord.getRoot().equalsIgnoreCase(root)) &&
                        (type == null || type.isEmpty() || chord.getType().equalsIgnoreCase(type)) &&
                        (modifier == null || modifier.isEmpty() || chord.getModifier().equalsIgnoreCase(modifier)) &&
                        (bass == null || bass.isEmpty() || chord.getBass().equalsIgnoreCase(bass))
        ).collect(Collectors.toList());
    }

    public GuitarChord createChord(String root, String type, String modifier, String bass, String fingeringInput) throws InvalidInput {
        isNoteExist(root, "alaphang");
        validateNoteInput(root, "alaphang");
        validateNoteInput(bass, "basszushang");

        String formattedRoot = StringFormatter.firstLetterUpperCase(root.trim());
        String formattedType = type.trim().toLowerCase();
        String formattedModifier = modifier.trim().toLowerCase();
        String formattedBass = StringFormatter.firstLetterUpperCase(bass.trim());
        String[] fingers = fingeringValidate(fingeringInput);

        Fingering fingering = new Fingering();
        fingering.setFingers(fingers);

        return new GuitarChord(
                formattedRoot,
                formattedType.isEmpty() ? "" : formattedType,
                formattedModifier.isEmpty() ? "" : formattedModifier,
                formattedBass.isEmpty()? "" : formattedBass,
                List.of(fingering)
        );
    }

    public void validateNoteInput(String note, String noteType) throws InvalidInput {
        if (note == null) {
            throw new InvalidInput("The note is null");
        }
        if (note.isEmpty()) {
            DialogHandler.showAlert("Beviteli hiba", "A megadott " + noteType + " üres!");
            throw new InvalidInput("The note is empty");
        }
        if (Arrays.stream(validNotes).noneMatch(valid -> valid.equalsIgnoreCase(note))) {
            DialogHandler.showAlert("Beviteli hiba", "A megadott " + noteType + " nem érvényes!");
            throw new InvalidInput("Wrong root or bass detected");
        }
    }

    public void isNoteExist(String note, String noteType) throws InvalidInput {
        if (note == null || note.isEmpty()) {
            DialogHandler.showAlert("Beviteli hiba", "A(z) " + noteType + " megadása kötelező!");
            throw new InvalidInput("Missing root field");
        }
    }

    public String[] fingeringValidate(String fingeringInput) throws InvalidInput {
        String[] fingers = fingeringInput.trim().toUpperCase().split("\\s+");
        if (fingers.length != 6) {
            DialogHandler.showAlert("Beviteli hiba", "A 6 húr helyett " + fingers.length + " húrt adtál meg.");
            throw new InvalidInput("Wrong number of fingers");
        }
        for (int i = 0; i < fingers.length; i++) {
            fingers[i] = fingers[i].toUpperCase();
            if (fingers[i].equals("X") || fingers[i].equals("O")) {
                continue;
            }
            try {
                if (Integer.parseInt(fingers[i]) < 1) {
                    DialogHandler.showAlert("Beviteli hiba", "A húrok lehogásánál nem szerepelhet negatív szám!");
                    throw new InvalidInput("Invalid finger input, the number cannot be negative");
                }
            } catch (NumberFormatException e) {
                DialogHandler.showAlert("Beviteli hiba", "A húrok lehogásánál nem szerepelhet X-től különböző betű!\nHa nyitott húrt szeretnél megadni, akkor azt a 0 (nulla) számmal tudod megtenni.");
                throw new InvalidInput("Invalid finger input, the number cannot be negative");
            }
        }
        return fingers;
    }

    public List<GuitarChord> saveChord(GuitarChord chord) throws Exception {
        return repository.updateChord(chord);
    }

    public List<GuitarChord> loadAllChords() throws IOException {
        return repository.loadChords();
    }

    public static int getMinFret(String[] fingers) {
        int min = Integer.MAX_VALUE;
        for (String finger : fingers) {
            try {
                int fret = Integer.parseInt(finger);
                if (fret < min) {
                    min = fret;
                }
            } catch (NumberFormatException ignored) {}
        }
        return min;
    }
}
