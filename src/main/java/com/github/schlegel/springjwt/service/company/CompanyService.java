package com.github.schlegel.springjwt.service.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CompanyService implements ICompanyService {
    @Autowired
    CompanyDTOMapper companyDTOMapper;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Company createCompany(CompanyDTO companyDTO) {
        Company company = companyDTOMapper.companyDTOtoCompany(companyDTO);
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company editCompany(String companyId, CompanyDTO companyDTO) {
        //TODO
        return null;
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
