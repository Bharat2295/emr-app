package net.javaguides.emrapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.emrapp.entity.InvoiceDetails;
import net.javaguides.emrapp.entity.InvoiceItems;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItems, Long>{
	List<InvoiceItems> findByInvoiceDetails(InvoiceDetails invoiceDetails);
}
