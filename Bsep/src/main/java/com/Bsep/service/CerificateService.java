package com.Bsep.service;

import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import com.Bsep.model.IssuerData;
import com.Bsep.model.SubjectData;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CerificateService {

    CertificateData createCertificate(NewCertificateDto newCertificateDto) throws UnrecoverableKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException;

    List<CertificateData> getAll(Boolean isCa);
}
