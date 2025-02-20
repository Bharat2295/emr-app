package net.javaguides.emrapp.controller;

import net.javaguides.emrapp.dto.GetInvoiceDto;
import net.javaguides.emrapp.dto.InvoicePayloadDto;
import net.javaguides.emrapp.service.InvoiceDetailsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/invoices")
public class InvoiceDetailsController {

    @Autowired
    private InvoiceDetailsService invoiceDetailsService;

    // Create new invoice
    @PostMapping
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<String> createInvoice(@RequestBody InvoicePayloadDto invoicePayloadDto) {
        invoiceDetailsService.createInvoice(invoicePayloadDto);
        return new ResponseEntity<>("Invoice created successfully", HttpStatus.CREATED);
    }
    
    // Get InvoiceId (Generated Invoice Id Only)
    @GetMapping("/current-id")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<String> getCurrentInvoiceId() {
        String currentInvoiceId = invoiceDetailsService.getCurrentInvoiceId();
        return new ResponseEntity<>(currentInvoiceId, HttpStatus.OK);
    }
    
    // Get Invoice by Id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<InvoicePayloadDto> getInvoiceById(@PathVariable Long id) {
        // Call the service method to get the invoice details
        InvoicePayloadDto invoicePayloadDto = invoiceDetailsService.getInvoiceById(id);
        // Return the response with OK status
        return new ResponseEntity<>(invoicePayloadDto, HttpStatus.OK);
    }
    
	// Update Invoice by Id
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_Admin')")
	public ResponseEntity<String> updateInvoiceById(@PathVariable Long id,
			@RequestBody InvoicePayloadDto invoicePayloadDto) {
		invoiceDetailsService.updateInvoiceById(id, invoicePayloadDto);
		return new ResponseEntity<>("Invoice updated successfully", HttpStatus.OK);
	}
    
    // Get all invoices
    @GetMapping("all")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<List<GetInvoiceDto>> getAllInvoices() {
        List<GetInvoiceDto> allInvoices = invoiceDetailsService.getAllInvoices();
        return new ResponseEntity<>(allInvoices, HttpStatus.OK);
    }
    
	// Delete invoice by id
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_Admin')")
	public ResponseEntity<String> deleteInvoiceById(@PathVariable Long id) {
		invoiceDetailsService.deleteInvoiceById(id);
		return new ResponseEntity<>("Invoice deleted successfully", HttpStatus.OK);
	}
    
}