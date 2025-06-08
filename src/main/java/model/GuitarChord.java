package model;

import lombok.Data;

public class GuitarChord {
    private String root;
    private String type;
    private String modifier;
    private String bass;
    private String[] fingering;

    public GuitarChord(String root, String type, String bass, String[] fingering) {
        this.root = root;
        this.type = type;
        this.bass = bass;
        this.fingering = fingering;
    }

    public String getRoot() {
        return root;
    }

    public String getType() {
        return type;
    }

    public String getBass() {
        return bass;
    }

    public String getChordName() {
        return root + type + (bass.isEmpty() ? "" : ("/" + bass));
    }

    public String getFingeringAsString() {
        return String.join("", fingering);
    }
}

