package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyOutputDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUpdateDto;
import com.github.schlegel.springjwt.validation.annotations.CompanyExists;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@Transactional
@Validated
public interface CompanyService {

    @PreAuthorize("hasPermission(null, 'company', 'create')")
    CompanyOutputDto createCompany(@Valid CompanyCreateDto companyCreateDto);

    @PreAuthorize("hasPermission(#companyId, 'company', 'update')")
    CompanyOutputDto updateCompany(@CompanyExists UUID companyId, @Valid CompanyUpdateDto companyCreateDto);

    @PreAuthorize("hasPermission(#companyId, 'company', 'addUser')")
    Company addUserToCompany(@CompanyExists UUID companyId, UUID userId);
}
