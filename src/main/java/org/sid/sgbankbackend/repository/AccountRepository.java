package org.sid.sgbankbackend.repository;

import org.sid.sgbankbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}