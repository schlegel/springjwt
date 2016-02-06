package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import org.springframework.security.access.prepost.PreAuthorize;


public interface ICompanyService {

    @PreAuthorize("hasPermission(#companyId, 'company', 'create')")
    Company createCompany(CompanyDTO companyDTO);

    @PreAuthorize("hasPermission(#companyId, 'company', 'update')")
    Company editCompany(String companyId, CompanyDTO companyDTO);

    @PreAuthorize("hasPermission(#companyId, 'company', 'addUser')")
    Company addUserToCompany(String companyId, String userId);
}
