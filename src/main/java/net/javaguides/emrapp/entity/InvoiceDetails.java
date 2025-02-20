package net.javaguides.emrapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "InvoiceDetails")
public class InvoiceDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "invoice_id", nullable = false, unique = true)
    private String invoiceId;
	
	@Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;
	
	@Column(name = "discount")
    private BigDecimal discount;
	
	@Column(name = "gst", nullable = false)
	private BigDecimal gst;
	
	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;
	
	@Column(name = "advanceAmount")
    private BigDecimal advanceAmount;
	
	@Column(name = "balanceAmount", nullable = false)
	private BigDecimal balanceAmount;
	
	@Column(name = "paymentType", nullable = false)
	private String paymentType;
		
	@ManyToOne
    @JoinColumn(name = "personal_details_id", nullable = false)
    private PersonalDetails personalDetails;
	
	@OneToMany(mappedBy = "invoiceDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItems> invoiceItems;

    @OneToMany(mappedBy = "invoiceDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpticalPrescription> opticalPrescriptions;
	
}
