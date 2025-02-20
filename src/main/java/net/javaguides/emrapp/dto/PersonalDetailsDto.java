package net.javaguides.emrapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetailsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private Integer age;
    // private List<InvoiceDetailsDto> invoiceDetails; // List of DTOs for associated InvoiceDetails
}
