package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import org.springframework.security.access.prepost.PreAuthorize;


public interface ICompanyService {

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    Company createCompany(CompanyDTO companyDTO);

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') OR hasPermission(#companyId, companyEdit)")
    Company editCompany(String companyId, CompanyDTO companyDTO);

    @PreAuthorize("hasPermission(#companyId, companyAddUser)")
    Company addUserToCompany(String companyId, String userId);
}
