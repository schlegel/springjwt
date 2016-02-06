package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CompanyDTOMapper {


    abstract CompanyDTO companyToCompanyDTO (Company car);

    abstract Company companyDTOtoCompany (CompanyDTO companyDTO);

    Company updateCompanyFromCompanyDTO(CompanyDTO companyDTO, @MappingTarget Company company) {
        if ( companyDTO == null ) {
            return null;
        }

        if(companyDTO.getName() != null) {
            company.setName( companyDTO.getName() );
        }

        if(companyDTO.getDescription() != null) {
            company.setDescription( companyDTO.getDescription() );
        }

        return company;
    }
}
