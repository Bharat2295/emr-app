package net.javaguides.emrapp.service;

import java.util.List;
import net.javaguides.emrapp.dto.GetInvoiceDto;
import net.javaguides.emrapp.dto.InvoicePayloadDto;

public interface InvoiceDetailsService {
	
	// Create Invoice for Optical Module
	void createInvoice(InvoicePayloadDto invoicePayloadDto);
	
	// Generate new Invoice Id
	String generateNewInvoiceId();
	
	//Get Current Invoice Id
	String getCurrentInvoiceId();

	// Get Invoice by Id
	InvoicePayloadDto getInvoiceById(Long id);
	
	// Update Invoice by Id
	void updateInvoiceById(Long id, InvoicePayloadDto invoicePayloadDto);
	
	// Get All Invoices
    List<GetInvoiceDto> getAllInvoices();
    
    // Delete Invoice By Id
    void deleteInvoiceById(Long id);
}
