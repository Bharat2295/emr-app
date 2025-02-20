package net.javaguides.emrapp.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemsDto {
    private Long id;
    private Long invoiceDetailsId; // Instead of InvoiceDetails entity, we keep the ID in the DTO
    private String itemName;
    private BigDecimal itemPrice;
    private int itemQuantity;
    private BigDecimal itemTotal;
}
