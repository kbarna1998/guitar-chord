package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuitarChord {
    private String root;
    private String type;
    private String modifier;
    private String bass;
    private List<Fingering> fingering;
}