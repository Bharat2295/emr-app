package net.javaguides.emrapp.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePayloadDto {
	private InvoiceDetailsDto invoiceDetails;
    private PersonalDetailsDto personalDetails;
    private OpticalPrescriptionDto opticalPrescriptionData;
    private List<InvoiceItemsDto> invoiceItems = new ArrayList<>();
}
