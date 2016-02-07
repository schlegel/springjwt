package com.github.schlegel.springjwt.web.api.controller.company;

import com.github.schlegel.springjwt.service.company.CompanyService;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyOutputDto;
import com.github.schlegel.springjwt.service.company.transport.CompanyUpdateDto;
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
    public CompanyOutputDto createCompany(@RequestBody CompanyCreateDto companyCreateDto) {
        return companyService.createCompany(companyCreateDto);
    }

    @RequestMapping(value = "/{companyId}", method = RequestMethod.PATCH)
    @ResponseStatus(value = HttpStatus.OK)
    public CompanyOutputDto editCompany(@PathVariable UUID companyId, @RequestBody CompanyUpdateDto companyUpdateDto) {
        return companyService.updateCompany(companyId, companyUpdateDto);
    }
}
