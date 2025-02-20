package net.javaguides.emrapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EyePrescriptionDto {
    private String dvSph;
    private String dvCyl;
    private String dvAxis;
    private String dvVn;
    private String nvSph;
    private String nvCyl;
    private String nvAxis;
    private String nvVn;
    private String addition;
}
