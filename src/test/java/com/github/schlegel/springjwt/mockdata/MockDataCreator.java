package com.github.schlegel.springjwt.mockdata;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.domain.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockDataCreator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public void createUsers() {
        // create normal user
        User user = new User();
        user.setEmail("user@example.de");
        user.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");   // password = password
        user.setSalt(null); // no salt needed
        user.setUsername("myUser");
        user.setRole(UserRole.USER);

        userRepository.save(user);

        // create company admin user
        User companyAdmin = new User();
        companyAdmin.setEmail("companyadmin@example.de");
        companyAdmin.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");   // password = password
        companyAdmin.setSalt(null); // no salt needed
        companyAdmin.setUsername("myUser");
        companyAdmin.setRole(UserRole.COMPANY_ADMIN);

        userRepository.save(companyAdmin);

        // create super admin user
        User superAdmin = new User();
        superAdmin.setEmail("superadmin@example.de");
        superAdmin.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");   // password = password
        superAdmin.setSalt(null); // no salt needed
        superAdmin.setUsername("myUser");
        superAdmin.setRole(UserRole.SUPER_ADMIN);

        userRepository.save(superAdmin);
    }


    public void createCompany() {

        Company company = new Company();
        company.setName("comp1");
        User user = userRepository.findByEmail("companyadmin@example.de");
        user.setCompany(company);

        companyRepository.save(company);
        userRepository.save(user);

    }
}
