package com.lal.userservice.repository;

import com.lal.userservice.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin,String> {
}
