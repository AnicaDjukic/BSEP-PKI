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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
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
    public ResponseEntity<CertificateData> createCertificate(@RequestBody NewCertificateDto newCertificateDto) throws Exception {
        CertificateData newCertificate = certificateService.createCertificate(newCertificateDto);
        return ResponseEntity.ok(newCertificate);
    }

    @GetMapping
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

    @PutMapping(value = "/{id}/revoke")
    public ResponseEntity<HttpStatus> revoke(@PathVariable Long id) {
        certificateService.revoke(id);
        return ResponseEntity.ok().build();
    }
}
