package net.javaguides.emrapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.emrapp.entity.PersonalDetails;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long>{

}
