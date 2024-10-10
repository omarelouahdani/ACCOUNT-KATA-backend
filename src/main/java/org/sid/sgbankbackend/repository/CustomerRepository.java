package org.sid.sgbankbackend.repository;

import org.sid.sgbankbackend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
