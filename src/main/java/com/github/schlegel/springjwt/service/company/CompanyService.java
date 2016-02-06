package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private CompanyDTOMapper companyDTOMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Company createCompany(CompanyDTO companyDTO) {
        Company company = companyDTOMapper.companyDTOtoCompany(companyDTO);
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company editCompany(String companyId, CompanyDTO companyDTO) {
        Company company = companyRepository.findOne(companyId);
        Assert.notNull(company);

        return companyDTOMapper.updateCompanyFromCompanyDTO(companyDTO, company);
    }

    @Override
    public Company addUserToCompany(String companyId, String userId) {
        Company company = companyRepository.findOne(companyId);
        User user = userRepository.findOne(userId);

        if(!company.getUsers().stream().anyMatch(u -> u.getId().equals(userId) )) {
            company.getUsers().add(user);
        }

        return company;
    }
}
