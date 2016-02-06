package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Transactional
@Validated
public interface ICompanyService {

    @PreAuthorize("hasPermission(#companyId, 'company', 'create')")
    Company createCompany(@Valid CompanyDTO companyDTO);

    @PreAuthorize("hasPermission(#companyId, 'company', 'update')")
    Company editCompany(String companyId, @Valid CompanyDTO companyDTO);

    @PreAuthorize("hasPermission(#companyId, 'company', 'addUser')")
    Company addUserToCompany(String companyId, String userId);
}
