package org.sid.sgbankbackend.repositories;

import org.sid.sgbankbackend.model.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
