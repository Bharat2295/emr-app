package net.javaguides.emrapp.service.impl;

import net.javaguides.emrapp.dto.*;
import net.javaguides.emrapp.entity.*;
import net.javaguides.emrapp.repository.*;
import net.javaguides.emrapp.service.InvoiceDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InvoiceDetailsServiceImpl implements InvoiceDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceDetailsServiceImpl.class);


    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    private OpticalPrescriptionRepository opticalPrescriptionRepository;

    @Autowired
    private InvoiceItemsRepository invoiceItemsRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    private static final String INVOICE_PREFIX = "INV_AK_";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    @Override
    public String generateNewInvoiceId() {
    	// Fetch the latest invoice ID from the repository
        Optional<InvoiceDetails> latestInvoiceDetailsOpt = invoiceDetailsRepository.findTopByOrderByIdDesc();
        String newInvoiceId;
        String currentDateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        int newSequenceNumber = 1; // Start with 1 if there is no latest invoice ID

        if (latestInvoiceDetailsOpt.isPresent()) {
            String latestInvoiceId = latestInvoiceDetailsOpt.get().getInvoiceId();

            // Extract the sequence number
            Pattern pattern = Pattern.compile("INV_AK_\\d{14}_(\\d+)");
            Matcher matcher = pattern.matcher(latestInvoiceId);

            if (matcher.matches()) {
                int latestSequenceNumber = Integer.parseInt(matcher.group(1));
                newSequenceNumber = latestSequenceNumber + 1;
            }
        }

        newInvoiceId = String.format("%s%s_%02d", INVOICE_PREFIX, currentDateTime, newSequenceNumber);

        return newInvoiceId;
    }

    @Override
    public void createInvoice(InvoicePayloadDto invoicePayloadDto) {
    	
    	// Generate a new invoice ID
        String newInvoiceId = generateNewInvoiceId();
        
        // // Set the generated invoice ID and current date in the invoice details DTO
        InvoiceDetailsDto invoiceDetailsDto = invoicePayloadDto.getInvoiceDetails();
        invoiceDetailsDto.setInvoiceId(newInvoiceId);
        invoiceDetailsDto.setInvoiceDate(LocalDateTime.now()); 
        
        // Map and save PersonalDetails
        PersonalDetailsDto personalDetailsDto = invoicePayloadDto.getPersonalDetails();
        PersonalDetails personalDetails = modelMapper.map(personalDetailsDto, PersonalDetails.class);
        personalDetails = personalDetailsRepository.save(personalDetails);

        // Map and save InvoiceDetails
        InvoiceDetails invoiceDetails = modelMapper.map(invoiceDetailsDto, InvoiceDetails.class);
        invoiceDetails.setPersonalDetails(personalDetails);
        InvoiceDetails savedInvoiceDetails = invoiceDetailsRepository.save(invoiceDetails);

     // Map and save OpticalPrescription
        OpticalPrescriptionDto opticalPrescriptionDto = invoicePayloadDto.getOpticalPrescriptionData();
        if (opticalPrescriptionDto != null) {
            OpticalPrescription opticalPrescription = new OpticalPrescription();
            opticalPrescription.setInvoiceDetails(invoiceDetails);
            opticalPrescription.setRightEyeDvSph(opticalPrescriptionDto.getRightEye().getDvSph());
            opticalPrescription.setRightEyeDvCyl(opticalPrescriptionDto.getRightEye().getDvCyl());
            opticalPrescription.setRightEyeDvAxis(opticalPrescriptionDto.getRightEye().getDvAxis());
            opticalPrescription.setRightEyeDvVn(opticalPrescriptionDto.getRightEye().getDvVn());
            opticalPrescription.setRightEyeNvSph(opticalPrescriptionDto.getRightEye().getNvSph());
            opticalPrescription.setRightEyeNvCyl(opticalPrescriptionDto.getRightEye().getNvCyl());
            opticalPrescription.setRightEyeNvAxis(opticalPrescriptionDto.getRightEye().getNvAxis());
            opticalPrescription.setRightEyeNvVn(opticalPrescriptionDto.getRightEye().getNvVn());
            opticalPrescription.setRightEyeAddition(opticalPrescriptionDto.getRightEye().getAddition());
            opticalPrescription.setLeftEyeDvSph(opticalPrescriptionDto.getLeftEye().getDvSph());
            opticalPrescription.setLeftEyeDvCyl(opticalPrescriptionDto.getLeftEye().getDvCyl());
            opticalPrescription.setLeftEyeDvAxis(opticalPrescriptionDto.getLeftEye().getDvAxis());
            opticalPrescription.setLeftEyeDvVn(opticalPrescriptionDto.getLeftEye().getDvVn());
            opticalPrescription.setLeftEyeNvSph(opticalPrescriptionDto.getLeftEye().getNvSph());
            opticalPrescription.setLeftEyeNvCyl(opticalPrescriptionDto.getLeftEye().getNvCyl());
            opticalPrescription.setLeftEyeNvAxis(opticalPrescriptionDto.getLeftEye().getNvAxis());
            opticalPrescription.setLeftEyeNvVn(opticalPrescriptionDto.getLeftEye().getNvVn());
            opticalPrescription.setLeftEyeAddition(opticalPrescriptionDto.getLeftEye().getAddition());

            opticalPrescriptionRepository.save(opticalPrescription);
        } else {
            logger.warn("OpticalPrescription data is null in the received payload.");
        }

     // Map and save InvoiceItems
        if (invoicePayloadDto.getInvoiceItems() != null) {
            List<InvoiceItems> invoiceItemsList = invoicePayloadDto.getInvoiceItems().stream()
                .map(itemDto -> {
                    InvoiceItems invoiceItems = modelMapper.map(itemDto, InvoiceItems.class);
                    invoiceItems.setId(null);
                    invoiceItems.setInvoiceDetails(savedInvoiceDetails);
                    
                    // Log each mapped InvoiceItems object
                    logger.info("Mapped InvoiceItems: {}", invoiceItems);
                    
                    return invoiceItems;
                }).collect(Collectors.toList());
            
            // Log the list of InvoiceItems before saving
            logger.info("InvoiceItems list to be saved: {}", invoiceItemsList);
            
            invoiceItemsRepository.saveAll(invoiceItemsList);
            
            // Log a message after saving
            logger.info("InvoiceItems saved successfully");
        } else {
            logger.warn("InvoiceItems is null in the received payload.");
        }
    }

    @Override
    public String getCurrentInvoiceId() {
        // Retrieve the latest invoice ID
    	Optional<InvoiceDetails> latestInvoiceDetailsOpt = invoiceDetailsRepository.findTopByOrderByIdDesc();
    	String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    	if (latestInvoiceDetailsOpt.isPresent()) {
            InvoiceDetails latestInvoice = latestInvoiceDetailsOpt.get();
            String latestInvoiceId = latestInvoice.getInvoiceId();
            // Extract the sequence number
            String sequencePart = latestInvoiceId.substring(latestInvoiceId.lastIndexOf('_') + 1);
            int sequenceNumber = Integer.parseInt(sequencePart);
            // Increment the sequence number
            int newSequenceNumber = sequenceNumber + 1;
            return String.format("INVAK%s%02d", currentDate, newSequenceNumber);
        } else {
            return String.format("INVAK%s%02d", currentDate, 1);
        }
    }
    
    @Override
    public List<GetInvoiceDto> getAllInvoices() {
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        return invoiceDetailsList.stream().map(invoiceDetails -> {
            GetInvoiceDto getInvoiceDto = new GetInvoiceDto();
            getInvoiceDto.setId(invoiceDetails.getId());
            getInvoiceDto.setInvoiceId(invoiceDetails.getInvoiceId());
            getInvoiceDto.setFirstName(invoiceDetails.getPersonalDetails().getFirstName());
            getInvoiceDto.setInvoiceDate(invoiceDetails.getInvoiceDate());
            getInvoiceDto.setTotalAmount(invoiceDetails.getTotalAmount());
            getInvoiceDto.setBalanceAmount(invoiceDetails.getBalanceAmount());
            // Set status based on balance amount
            if (invoiceDetails.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
                getInvoiceDto.setStatus("Pending");
            } else {
                getInvoiceDto.setStatus("Paid");
            }
            return getInvoiceDto;
        }).collect(Collectors.toList());
    }

	@Override
	@Transactional
	public void deleteInvoiceById(Long id) {
        Optional<InvoiceDetails> invoiceDetailsOpt = invoiceDetailsRepository.findById(id);

        if (invoiceDetailsOpt.isPresent()) {
            InvoiceDetails invoiceDetails = invoiceDetailsOpt.get();

            // Delete related optical prescriptions
            List<OpticalPrescription> opticalPrescriptions = opticalPrescriptionRepository.findByInvoiceDetails(invoiceDetails);
            opticalPrescriptionRepository.deleteAll(opticalPrescriptions);

            // Delete related invoice items
            List<InvoiceItems> invoiceItems = invoiceItemsRepository.findByInvoiceDetails(invoiceDetails);
            invoiceItemsRepository.deleteAll(invoiceItems);

            // Delete the invoice itself
            invoiceDetailsRepository.delete(invoiceDetails);

            logger.info("Deleted invoice with id: {}", id);
        } else {
            logger.warn("Invoice with id: {} not found", id);
        }
    }
	
	 @Override
	    public InvoicePayloadDto getInvoiceById(Long id) {
	        Optional<InvoiceDetails> invoiceDetailsOpt = invoiceDetailsRepository.findById(id);

	        if (invoiceDetailsOpt.isPresent()) {
	            InvoiceDetails invoiceDetails = invoiceDetailsOpt.get();
	            InvoicePayloadDto responseDto = new InvoicePayloadDto();

	            // Map InvoiceDetails
	            InvoiceDetailsDto invoiceDetailsDto = modelMapper.map(invoiceDetails, InvoiceDetailsDto.class);
	            responseDto.setInvoiceDetails(invoiceDetailsDto);

	            // Map PersonalDetails
	            Long personalDetailsId = invoiceDetails.getPersonalDetails().getId(); // Assuming this is how you access the ID
	            PersonalDetails personalDetails = personalDetailsRepository.findById(personalDetailsId)
	                .orElseThrow(() -> new RuntimeException("PersonalDetails not found with id: " + personalDetailsId));
	            PersonalDetailsDto personalDetailsDto = modelMapper.map(personalDetails, PersonalDetailsDto.class);
	            responseDto.setPersonalDetails(personalDetailsDto);

	            // Map OpticalPrescription
	            List<OpticalPrescription> opticalPrescriptions = opticalPrescriptionRepository.findByInvoiceDetails(invoiceDetails);
	            if (!opticalPrescriptions.isEmpty()) {
	                OpticalPrescription opticalPrescription = opticalPrescriptions.get(0); // Assuming you want the first entry
	                OpticalPrescriptionDto opticalPrescriptionDto = new OpticalPrescriptionDto();

	                EyePrescriptionDto rightEyeDto = new EyePrescriptionDto();
	                rightEyeDto.setDvSph(opticalPrescription.getRightEyeDvSph());
	                rightEyeDto.setDvCyl(opticalPrescription.getRightEyeDvCyl());
	                rightEyeDto.setDvAxis(opticalPrescription.getRightEyeDvAxis());
	                rightEyeDto.setDvVn(opticalPrescription.getRightEyeDvVn());
	                rightEyeDto.setNvSph(opticalPrescription.getRightEyeNvSph());
	                rightEyeDto.setNvCyl(opticalPrescription.getRightEyeNvCyl());
	                rightEyeDto.setNvAxis(opticalPrescription.getRightEyeNvAxis());
	                rightEyeDto.setNvVn(opticalPrescription.getRightEyeNvVn());
	                rightEyeDto.setAddition(opticalPrescription.getRightEyeAddition());

	                EyePrescriptionDto leftEyeDto = new EyePrescriptionDto();
	                leftEyeDto.setDvSph(opticalPrescription.getLeftEyeDvSph());
	                leftEyeDto.setDvCyl(opticalPrescription.getLeftEyeDvCyl());
	                leftEyeDto.setDvAxis(opticalPrescription.getLeftEyeDvAxis());
	                leftEyeDto.setDvVn(opticalPrescription.getLeftEyeDvVn());
	                leftEyeDto.setNvSph(opticalPrescription.getLeftEyeNvSph());
	                leftEyeDto.setNvCyl(opticalPrescription.getLeftEyeNvCyl());
	                leftEyeDto.setNvAxis(opticalPrescription.getLeftEyeNvAxis());
	                leftEyeDto.setNvVn(opticalPrescription.getLeftEyeNvVn());
	                leftEyeDto.setAddition(opticalPrescription.getLeftEyeAddition());

	                opticalPrescriptionDto.setRightEye(rightEyeDto);
	                opticalPrescriptionDto.setLeftEye(leftEyeDto);
	                responseDto.setOpticalPrescriptionData(opticalPrescriptionDto);
	            } else {
	                responseDto.setOpticalPrescriptionData(null);
	            }

	            // Map InvoiceItems
	            List<InvoiceItems> invoiceItems = invoiceItemsRepository.findByInvoiceDetails(invoiceDetails);
	            List<InvoiceItemsDto> invoiceItemsDto = invoiceItems.stream()
	                .map(item -> modelMapper.map(item, InvoiceItemsDto.class))
	                .collect(Collectors.toList());
	            responseDto.setInvoiceItems(invoiceItemsDto);

	            return responseDto;
	        } else {
	            throw new RuntimeException("Invoice not found with id: " + id);
	        }
	    }
	 
	 @Override
	 @Transactional
	 public void updateInvoiceById(Long id, InvoicePayloadDto invoicePayloadDto) {
	     Optional<InvoiceDetails> invoiceDetailsOpt = invoiceDetailsRepository.findById(id);

	     if (invoiceDetailsOpt.isPresent()) {
	         InvoiceDetails invoiceDetails = invoiceDetailsOpt.get();

	         // Update InvoiceDetails
	         InvoiceDetailsDto invoiceDetailsDto = invoicePayloadDto.getInvoiceDetails();
	         invoiceDetails.setInvoiceDate(LocalDateTime.now()); // Set to current LocalDateTime
	         invoiceDetails.setDiscount(invoiceDetailsDto.getDiscount());
	         invoiceDetails.setGst(invoiceDetailsDto.getGst());
	         invoiceDetails.setTotalAmount(invoiceDetailsDto.getTotalAmount());
	         invoiceDetails.setAdvanceAmount(invoiceDetailsDto.getAdvanceAmount());
	         invoiceDetails.setBalanceAmount(invoiceDetailsDto.getBalanceAmount());
	         invoiceDetails.setPaymentType(invoiceDetailsDto.getPaymentType());
	         invoiceDetailsRepository.save(invoiceDetails);

	         // Update PersonalDetails
	         PersonalDetailsDto personalDetailsDto = invoicePayloadDto.getPersonalDetails();
	         PersonalDetails personalDetails = invoiceDetails.getPersonalDetails();
	         personalDetails.setAddress(personalDetailsDto.getAddress());
	         personalDetails.setAge(personalDetailsDto.getAge());
	         personalDetails.setEmail(personalDetailsDto.getEmail());
	         personalDetails.setFirstName(personalDetailsDto.getFirstName());
	         personalDetails.setGender(personalDetailsDto.getGender());
	         personalDetails.setLastName(personalDetailsDto.getLastName());
	         personalDetails.setPhone(personalDetailsDto.getPhone());
	         personalDetailsRepository.save(personalDetails);

	         // Update OpticalPrescription
	         OpticalPrescriptionDto opticalPrescriptionDto = invoicePayloadDto.getOpticalPrescriptionData();
	         if (opticalPrescriptionDto != null) {
	             List<OpticalPrescription> opticalPrescriptions = opticalPrescriptionRepository.findByInvoiceDetails(invoiceDetails);
	             if (!opticalPrescriptions.isEmpty()) {
	                 OpticalPrescription opticalPrescription = opticalPrescriptions.get(0); // Assuming you want the first entry
	                 opticalPrescription.setRightEyeDvSph(opticalPrescriptionDto.getRightEye().getDvSph());
	                 opticalPrescription.setRightEyeDvCyl(opticalPrescriptionDto.getRightEye().getDvCyl());
	                 opticalPrescription.setRightEyeDvAxis(opticalPrescriptionDto.getRightEye().getDvAxis());
	                 opticalPrescription.setRightEyeDvVn(opticalPrescriptionDto.getRightEye().getDvVn());
	                 opticalPrescription.setRightEyeNvSph(opticalPrescriptionDto.getRightEye().getNvSph());
	                 opticalPrescription.setRightEyeNvCyl(opticalPrescriptionDto.getRightEye().getNvCyl());
	                 opticalPrescription.setRightEyeNvAxis(opticalPrescriptionDto.getRightEye().getNvAxis());
	                 opticalPrescription.setRightEyeNvVn(opticalPrescriptionDto.getRightEye().getNvVn());
	                 opticalPrescription.setRightEyeAddition(opticalPrescriptionDto.getRightEye().getAddition());
	                 opticalPrescription.setLeftEyeDvSph(opticalPrescriptionDto.getLeftEye().getDvSph());
	                 opticalPrescription.setLeftEyeDvCyl(opticalPrescriptionDto.getLeftEye().getDvCyl());
	                 opticalPrescription.setLeftEyeDvAxis(opticalPrescriptionDto.getLeftEye().getDvAxis());
	                 opticalPrescription.setLeftEyeDvVn(opticalPrescriptionDto.getLeftEye().getDvVn());
	                 opticalPrescription.setLeftEyeNvSph(opticalPrescriptionDto.getLeftEye().getNvSph());
	                 opticalPrescription.setLeftEyeNvCyl(opticalPrescriptionDto.getLeftEye().getNvCyl());
	                 opticalPrescription.setLeftEyeNvAxis(opticalPrescriptionDto.getLeftEye().getNvAxis());
	                 opticalPrescription.setLeftEyeNvVn(opticalPrescriptionDto.getLeftEye().getNvVn());
	                 opticalPrescription.setLeftEyeAddition(opticalPrescriptionDto.getLeftEye().getAddition());
	                 opticalPrescriptionRepository.save(opticalPrescription);
	             }
	         }

	         // Update InvoiceItems
	         List<InvoiceItemsDto> invoiceItemsDto = invoicePayloadDto.getInvoiceItems();
	         if (invoiceItemsDto != null) {
	             List<InvoiceItems> existingInvoiceItems = invoiceItemsRepository.findByInvoiceDetails(invoiceDetails);
	             invoiceItemsRepository.deleteAll(existingInvoiceItems); // Delete existing items

	             List<InvoiceItems> newInvoiceItemsList = invoiceItemsDto.stream()
	                 .map(itemDto -> {
	                     InvoiceItems invoiceItems = modelMapper.map(itemDto, InvoiceItems.class);
	                     invoiceItems.setId(null); // Ensure new ID is generated
	                     invoiceItems.setInvoiceDetails(invoiceDetails);
	                     return invoiceItems;
	                 }).collect(Collectors.toList());
	             invoiceItemsRepository.saveAll(newInvoiceItemsList); // Save new items
	         }
	     } else {
	         throw new RuntimeException("Invoice not found with id: " + id);
	     }
	 }

  
}