package org.sid.sgbankbackend.mapper;

import org.sid.sgbankbackend.dto.AccountOperationDTO;
import org.sid.sgbankbackend.model.AccountOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Service responsible for mapping between AccountOperation and AccountOperationDTO.
 */
@Service
public class AccountMapperImpl {

    /**
     * Converts an AccountOperation entity to an AccountOperationDTO.
     *
     * @param accountOperation The AccountOperation entity to be converted.
     * @return The corresponding AccountOperationDTO.
     */
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

}
