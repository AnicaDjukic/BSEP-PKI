package com.Bsep.service.impl;

import com.Bsep.certificate.CertificateGenerator;
import com.Bsep.dto.CertificateDto;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.mapper.CertificateMapper;
import com.Bsep.model.CertificateData;
import com.Bsep.model.CertificateStatus;
import com.Bsep.model.CertificateType;
import com.Bsep.model.IssuerData;
import com.Bsep.model.SubjectData;
import com.Bsep.model.User;
import com.Bsep.repository.CertificateDataRepository;
import com.Bsep.repository.KeyStoreRepository;
import com.Bsep.service.CerificateService;
import com.Bsep.service.UserService;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CerificateService {

    private final UserService userService;

    private final CertificateDataRepository certificateDataRepository;

    private final KeyStoreRepository keyStoreRepository;

    private final CertificateMapper certificateMapper;


    public CertificateServiceImpl(UserService userService, CertificateDataRepository certificateDataRepository, KeyStoreRepository keyStoreRepository, CertificateMapper certificateMapper) {
        this.userService = userService;
        this.certificateDataRepository = certificateDataRepository;
        this.keyStoreRepository = keyStoreRepository;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public CertificateData createCertificate(NewCertificateDto newCertificateDto) throws UnrecoverableKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, ParseException {

        SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
        SubjectData subjectData = generateSubjectData(newCertificateDto);
        X509Certificate x509certificate = null;
        KeyPair keyPairIssuer = generateKeyPair();
        IssuerData issuerData;
        String issuerSerialNumber;

        if (newCertificateDto.getCertificateType().equals(CertificateType.ROOT)) {
            issuerSerialNumber = subjectData.getSerialNumber();
            issuerData = new IssuerData(keyPairIssuer.getPrivate(), subjectData.getX500name());
        } else {
            issuerSerialNumber = certificateDataRepository.findById(newCertificateDto.getIssuerCertificateId()).get().getSerialNumber();
            issuerData = generateIssuerData(newCertificateDto);
        }
        RDN cn = subjectData.getX500name().getRDNs(BCStyle.CN)[0];
        RDN org = subjectData.getX500name().getRDNs(BCStyle.O)[0];
        RDN orgUnit = subjectData.getX500name().getRDNs(BCStyle.OU)[0];
        RDN cCode = subjectData.getX500name().getRDNs(BCStyle.C)[0];

        x509certificate = new CertificateGenerator().generateCertificate(subjectData, issuerData, newCertificateDto.getCertificateType());
        keyStoreRepository.saveCertificate(keyPairIssuer.getPrivate(), x509certificate, newCertificateDto.getCertificateType());
        CertificateData certificateData = new CertificateData(subjectData.getSerialNumber(),
                IETFUtils.valueToString(cn.getFirst().getValue()),
                IETFUtils.valueToString(org.getFirst().getValue()),
                IETFUtils.valueToString(orgUnit.getFirst().getValue()),
                IETFUtils.valueToString(cCode.getFirst().getValue()),
                issuerSerialNumber,
                iso8601Formater.parse(newCertificateDto.getEndDate()),
                CertificateStatus.VALID,
                newCertificateDto.getCertificateType(),
                newCertificateDto.getCertificatePurposeType());
        return certificateDataRepository.save(certificateData);
    }

    @Override
    public List<CertificateDto> getAll(Boolean isCa) {
        List<CertificateDto> certificateDtos = new ArrayList<>();
        Set<CertificateData> certificates = certificateDataRepository.findAll(isCa);
        for (CertificateData certificate : certificates) {
            CertificateData issuerCertificate = certificateDataRepository.findBySerialNumber(certificate.getIssuerSerialNumber());
            certificateDtos.add(certificateMapper.toDTO(certificate, issuerCertificate.getSubjectUsername()));
        }
        return certificateDtos;
    }

    private SubjectData generateSubjectData(NewCertificateDto newCertificateDto) {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = new Date();
            Date endDate = iso8601Formater.parse(newCertificateDto.getEndDate());

            String sn = generateSerialNumber();
            User user = userService.findById(newCertificateDto.getSubjectUID());
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, user.getUsername());
            builder.addRDN(BCStyle.SURNAME, user.getFirstName());
            builder.addRDN(BCStyle.GIVENNAME, user.getLastName());
            builder.addRDN(BCStyle.O, newCertificateDto.getOrganization());
            builder.addRDN(BCStyle.OU, newCertificateDto.getOrganizationalUnitName());
            builder.addRDN(BCStyle.C, newCertificateDto.getCountryCode());
            builder.addRDN(BCStyle.E, user.getUsername());
            builder.addRDN(BCStyle.UID, user.getId().toString());

            return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateSerialNumber() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }


    private IssuerData generateIssuerData(NewCertificateDto newCertificateDto) throws CertificateEncodingException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        CertificateData certificateData = certificateDataRepository.findById(newCertificateDto.getIssuerCertificateId()).get();
        Certificate issuerCertificate = keyStoreRepository.readCertificate(certificateData.getCertificateType(), certificateData.getSerialNumber());
        X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) issuerCertificate).getSubject();
        PrivateKey issuerKey = keyStoreRepository.getPrivateKeyForKeyStore(((X509Certificate) issuerCertificate).getSerialNumber().toString(16), certificateData.getCertificateType());
        //Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new IssuerData(issuerKey, issuerName);
    }


    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


}
