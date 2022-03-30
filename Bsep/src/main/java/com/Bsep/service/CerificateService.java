package com.Bsep.service;

import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import com.Bsep.model.IssuerData;
import com.Bsep.model.SubjectData;

import java.security.cert.X509Certificate;

public interface CerificateService {

    CertificateData createCertificate(NewCertificateDto newCertificateDto);
}
