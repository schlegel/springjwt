package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.service.company.transport.CompanyInputDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@Transactional
@Validated
public interface CompanyService {

    @PreAuthorize("hasPermission(#companyId, 'company', 'create')")
    Company createCompany(@Valid CompanyInputDto companyInputDto);

    @PreAuthorize("hasPermission(#companyId, 'company', 'update')")
    Company editCompany(UUID companyId, @Valid CompanyInputDto companyInputDto);

    @PreAuthorize("hasPermission(#companyId, 'company', 'addUser')")
    Company addUserToCompany(UUID companyId, String userId);
}
