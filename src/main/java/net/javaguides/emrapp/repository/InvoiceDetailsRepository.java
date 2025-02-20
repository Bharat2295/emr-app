package net.javaguides.emrapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.emrapp.entity.InvoiceDetails;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, Long>{
	Optional<InvoiceDetails> findTopByOrderByIdDesc();
}
