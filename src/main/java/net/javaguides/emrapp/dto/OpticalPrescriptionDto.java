package net.javaguides.emrapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpticalPrescriptionDto {
     // private Long id;
   // private Long personalDetailsId; // Instead of PersonalDetails entity, we keep the ID in the DTO
    private EyePrescriptionDto rightEye;
    private EyePrescriptionDto leftEye;
}
