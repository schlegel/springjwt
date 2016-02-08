package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyOutputDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUpdateDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDtoMapper companyDtoMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CompanyOutputDto createCompany(CompanyCreateDto companyCreateDto) {
        Company company = companyDtoMapper.companyInputDtoToCompany(companyCreateDto);
        company.setCreatedAt(DateTime.now());
        companyRepository.save(company);
        return companyDtoMapper.companyToCompanyOutputDto(company);
    }

    @Override
    public CompanyOutputDto updateCompany(UUID companyId, CompanyUpdateDto companyCreateDto) {
        Company company = companyRepository.findOne(companyId);
        Assert.notNull(company);

        companyDtoMapper.updateCompanyFromCompanyInputDto(companyCreateDto, company);
        company.setUpdatedAt(DateTime.now());
        companyRepository.save(company);

        return companyDtoMapper.companyToCompanyOutputDto(company);
    }

    @Override
    public Company addUserToCompany(UUID companyId, UUID userId) {
        Company company = companyRepository.findOne(companyId);
        User user = userRepository.findOne(userId);

        if(!company.getUsers().stream().anyMatch(u -> u.getId().equals(userId) )) {
            company.getUsers().add(user);
        }

        return company;
    }
}
