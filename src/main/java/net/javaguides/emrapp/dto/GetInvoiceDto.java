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
	public class GetInvoiceDto {
		private Long id;
	    private String invoiceId;
	    private String firstName;
	    private LocalDateTime invoiceDate;
	    private BigDecimal totalAmount;
	    private BigDecimal balanceAmount;
	    private String status;
	}