package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.service.company.transport.CompanyInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CompanyDtoMapper {


    abstract CompanyInputDto companyToCompanyDTO (Company company);

    abstract Company companyDTOtoCompany (CompanyInputDto companyInputDto);

    Company updateCompanyFromCompanyDTO(CompanyInputDto companyInputDto, @MappingTarget Company company) {
        if ( companyInputDto == null ) {
            return null;
        }

        if(companyInputDto.getName() != null) {
            company.setName( companyInputDto.getName() );
        }

        if(companyInputDto.getDescription() != null) {
            company.setDescription( companyInputDto.getDescription() );
        }

        return company;
    }
}
