package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.service.company.CompanyDTO;
import com.github.schlegel.springjwt.service.company.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

    @Autowired
    ICompanyService companyService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Company createCompany(@RequestBody CompanyDTO companyDTO) {
        return companyService.createCompany(companyDTO);
    }

    @RequestMapping(value = "/{companyId}", method = RequestMethod.PATCH)
    @ResponseStatus(value = HttpStatus.OK)
    public Company editCompany(@PathVariable String companyId, @RequestBody CompanyDTO companyDTO) {
        return companyService.editCompany(companyId, companyDTO);
    }
}
