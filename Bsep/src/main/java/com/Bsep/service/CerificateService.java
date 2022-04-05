package com.Bsep.service;

import com.Bsep.dto.CertificateDto;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import org.springframework.core.io.Resource;

import java.util.List;

public interface CerificateService {

    CertificateData createCertificate(NewCertificateDto newCertificateDto) throws Exception;

    List<CertificateDto> getAll(Boolean isCa);

    Resource getCertificateResource(Long id);
}
