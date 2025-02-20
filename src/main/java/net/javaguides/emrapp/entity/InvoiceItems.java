package net.javaguides.emrapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "InvoiceItems")
public class InvoiceItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_details_id", nullable = false)
    private InvoiceDetails invoiceDetails;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_price", nullable = false)
    private BigDecimal itemPrice;

    @Column(name = "item_quantity", nullable = false)
    private int itemQuantity;

    @Column(name = "item_total", nullable = false)
    private BigDecimal itemTotal;
    
}
