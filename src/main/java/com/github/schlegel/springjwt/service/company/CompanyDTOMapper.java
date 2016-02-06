package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyDTOMapper {

    CompanyDTO companyToCompanyDTO (Company car);

    Company companyDTOtoCompany (CompanyDTO companyDTO);

    Company updateCompanyFromCompanyDTO (CompanyDTO companyDTO, @MappingTarget Company company);
}
