package net.javaguides.emrapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.emrapp.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}