package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.service.company.transport.CompanyInputDto;
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
    public Company createCompany(CompanyInputDto companyInputDto) {
        Company company = companyDtoMapper.companyDTOtoCompany(companyInputDto);
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company editCompany(UUID companyId, CompanyInputDto companyInputDto) {
        Company company = companyRepository.findOne(companyId);
        Assert.notNull(company);

        return companyDtoMapper.updateCompanyFromCompanyDTO(companyInputDto, company);
    }

    @Override
    public Company addUserToCompany(UUID companyId, String userId) {
        Company company = companyRepository.findOne(companyId);
        User user = userRepository.findOne(userId);

        if(!company.getUsers().stream().anyMatch(u -> u.getId().equals(userId) )) {
            company.getUsers().add(user);
        }

        return company;
    }
}
