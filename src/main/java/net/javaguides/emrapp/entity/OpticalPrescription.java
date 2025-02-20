package net.javaguides.emrapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OpticalPrescription")
public class OpticalPrescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_details_id", nullable = false)
    private InvoiceDetails  invoiceDetails;

    @Column(name = "right_eye_dv_sph")
    private String rightEyeDvSph;

    @Column(name = "right_eye_dv_cyl")
    private String rightEyeDvCyl;

    @Column(name = "right_eye_dv_axis")
    private String rightEyeDvAxis;

    @Column(name = "right_eye_dv_vn")
    private String rightEyeDvVn;

    @Column(name = "right_eye_nv_sph")
    private String rightEyeNvSph;

    @Column(name = "right_eye_nv_cyl")
    private String rightEyeNvCyl;

    @Column(name = "right_eye_nv_axis")
    private String rightEyeNvAxis;

    @Column(name = "right_eye_nv_vn")
    private String rightEyeNvVn;

    @Column(name = "right_eye_addition")
    private String rightEyeAddition;

    @Column(name = "left_eye_dv_sph")
    private String leftEyeDvSph;

    @Column(name = "left_eye_dv_cyl")
    private String leftEyeDvCyl;

    @Column(name = "left_eye_dv_axis")
    private String leftEyeDvAxis;

    @Column(name = "left_eye_dv_vn")
    private String leftEyeDvVn;

    @Column(name = "left_eye_nv_sph")
    private String leftEyeNvSph;

    @Column(name = "left_eye_nv_cyl")
    private String leftEyeNvCyl;

    @Column(name = "left_eye_nv_axis")
    private String leftEyeNvAxis;

    @Column(name = "left_eye_nv_vn")
    private String leftEyeNvVn;

    @Column(name = "left_eye_addition")
    private String leftEyeAddition;
}
