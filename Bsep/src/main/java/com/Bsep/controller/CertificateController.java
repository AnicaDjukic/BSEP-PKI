package com.Bsep.controller;

import com.Bsep.dto.CertificateDto;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import com.Bsep.service.impl.CertificateServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/certificate")
public class CertificateController {

    private final CertificateServiceImpl certificateService;

    public CertificateController(CertificateServiceImpl certificateService) {
        this.certificateService = certificateService;
    }


    @PostMapping(value = "/create")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")	
    public ResponseEntity<CertificateData> createCertificate(@RequestBody NewCertificateDto newCertificateDto) throws UnrecoverableKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException {
        CertificateData newCertificate = certificateService.createCertificate(newCertificateDto);
        return ResponseEntity.ok(newCertificate);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<CertificateDto>> getAllCertificates(@RequestParam Boolean isCa) {
        List<CertificateData> certificates = certificateService.getAll(isCa);
        return null;
    }
}
