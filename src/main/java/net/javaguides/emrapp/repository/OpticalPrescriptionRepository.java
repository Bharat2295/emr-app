package net.javaguides.emrapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.emrapp.entity.InvoiceDetails;
import net.javaguides.emrapp.entity.OpticalPrescription;

public interface OpticalPrescriptionRepository extends JpaRepository<OpticalPrescription, Long>{
	List<OpticalPrescription> findByInvoiceDetails(InvoiceDetails invoiceDetails);
}
