package com.Bsep.controller;

import com.Bsep.dto.CertificateDto;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import com.Bsep.service.impl.CertificateServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/certificate")
public class CertificateController {

    private final CertificateServiceImpl certificateService;

    public CertificateController(CertificateServiceImpl certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<CertificateData> createCertificate(@RequestBody NewCertificateDto newCertificateDto) throws Exception {
        CertificateData newCertificate = certificateService.createCertificate(newCertificateDto);
        if(newCertificate == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newCertificate);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<CertificateDto>> getAllCertificates(@RequestParam(required = false) Boolean isCa, Authentication authentication) {
        List<GrantedAuthority> roles= (List<GrantedAuthority>) authentication.getAuthorities();
        List<CertificateDto> certificates;
        if(roles.get(0).getAuthority().equals("ROLE_USER"))
            certificates = certificateService.getByUsername(authentication.getName());
         else
            certificates = certificateService.getAll(isCa);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping(value = "/{id}/download")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        try {
            Resource resource = certificateService.getCertificateResource(id);
            String contentType = "application/octet-stream";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{serial}/revoke")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> revoke(@PathVariable String serial) {
        certificateService.revoke(serial);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{serial}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Boolean> isRevoked(@PathVariable String serial) {
        boolean isRevoked = certificateService.isRevoked(serial);
        return ResponseEntity.ok(isRevoked);
    }

    @GetMapping(value = "/{serial}/valid")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Boolean> isValid(@PathVariable String serial) {
        boolean isValid = certificateService.checkIsValid(serial);
        return ResponseEntity.ok(isValid);
    }
}
