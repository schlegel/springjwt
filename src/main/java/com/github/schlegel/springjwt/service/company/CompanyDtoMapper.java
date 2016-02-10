package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyOutputDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUpdateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUserOutputDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class CompanyDtoMapper {

    abstract CompanyOutputDto companyToCompanyOutputDto (Company company);
    abstract CompanyUserOutputDto userToCompanyUserOutputDto (User user);

    abstract Company companyInputDtoToCompany(CompanyCreateDto companyCreateDto);

    Company updateCompanyFromCompanyInputDto(CompanyUpdateDto companyUpdateDto, @MappingTarget Company company) {
        Optional<CompanyUpdateDto> optionalCompany = Optional.ofNullable(companyUpdateDto);

        optionalCompany.map(CompanyUpdateDto::getName).ifPresent(x -> company.setName(x));
        optionalCompany.map(CompanyUpdateDto::getDescription).ifPresent(x -> company.setDescription(x));
        optionalCompany.map(CompanyUpdateDto::getAddress).ifPresent(x -> company.setAddress(x));
        optionalCompany.map(CompanyUpdateDto::getMainContact).ifPresent(x -> company.setMainContact(x));

        return company;
    }
}