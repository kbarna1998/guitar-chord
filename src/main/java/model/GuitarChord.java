package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuitarChord {
    private String root;
    private String type;
    private String modifier;
    private String bass;
    private String[] fingering;
}