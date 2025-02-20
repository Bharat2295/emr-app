package net.javaguides.emrapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsDto {
    private Long id;
    private String invoiceId;
    private LocalDateTime invoiceDate;
    private BigDecimal discount;
    private BigDecimal gst;
    private BigDecimal totalAmount;
    private BigDecimal advanceAmount;
    private BigDecimal balanceAmount;
    private String paymentType;
    
    private Long personalDetailsId; // Instead of PersonalDetails, we only keep the ID in the DTO
}
