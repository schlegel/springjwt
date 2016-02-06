package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.domain.company.Company;
import com.github.schlegel.springjwt.service.company.CompanyService;
import com.github.schlegel.springjwt.service.company.transport.CompanyInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Company createCompany(@RequestBody CompanyInputDto companyInputDto) {
        return companyService.createCompany(companyInputDto);
    }

    @RequestMapping(value = "/{companyId}", method = RequestMethod.PATCH)
    @ResponseStatus(value = HttpStatus.OK)
    public Company editCompany(@PathVariable UUID companyId, @RequestBody CompanyInputDto companyInputDto) {
        return companyService.editCompany(companyId, companyInputDto);
    }
}
