package com.Bsep.service;

import com.Bsep.dto.CertificateDto;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.text.ParseException;
import java.util.List;

public interface CerificateService {

    CertificateData createCertificate(NewCertificateDto newCertificateDto) throws UnrecoverableKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, ParseException;

    List<CertificateDto> getAll(Boolean isCa);
}
