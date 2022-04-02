package com.Bsep.controller;

import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import com.Bsep.service.impl.CertificateServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/certificate")
public class CertificateController {

    private final CertificateServiceImpl certificateService;

    public CertificateController(CertificateServiceImpl certificateService) {
        this.certificateService = certificateService;
    }


    @PostMapping(value = "/create")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")	
    public ResponseEntity<CertificateData> createCertificate(@RequestBody NewCertificateDto newCertificateDto) {
        CertificateData newCertificate = certificateService.createCertificate(newCertificateDto);
        return ResponseEntity.ok(newCertificate);
    }
}
