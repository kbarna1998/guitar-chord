package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public String getDisplayName () {
        return root + type + (modifier != null ? modifier : "") + (bass != null && !bass.isEmpty() ? "/" + bass : "");
    }
}