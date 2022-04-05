package com.Bsep.service.impl;

import com.Bsep.certificate.CertificateGenerator;
import com.Bsep.dto.CertificateDto;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.mapper.CertificateMapper;
import com.Bsep.model.CertificateData;
import com.Bsep.model.CertificatePurposeType;
import com.Bsep.model.CertificateStatus;
import com.Bsep.model.CertificateType;
import com.Bsep.model.IssuerData;
import com.Bsep.model.SubjectData;
import com.Bsep.model.User;
import com.Bsep.repository.CertificateDataRepository;
import com.Bsep.repository.KeyStoreRepository;
import com.Bsep.service.CerificateService;
import com.Bsep.service.UserService;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
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

    private final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private final String END_CERT = "-----END CERTIFICATE-----";
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");


    public CertificateServiceImpl(UserService userService, CertificateDataRepository certificateDataRepository, KeyStoreRepository keyStoreRepository, CertificateMapper certificateMapper) {
        this.userService = userService;
        this.certificateDataRepository = certificateDataRepository;
        this.keyStoreRepository = keyStoreRepository;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public CertificateData createCertificate(NewCertificateDto newCertificateDto) throws UnrecoverableKeyException, CertificateEncodingException, KeyStoreException, NoSuchAlgorithmException, ParseException {

        KeyPair keyPairSubject = generateKeyPair();
        SubjectData subjectData = generateSubjectData(newCertificateDto, keyPairSubject.getPublic());
        String issuerSerialNumber = getIssuerSerialNumber(subjectData, newCertificateDto);
        IssuerData issuerData = getIssuerData(newCertificateDto, keyPairSubject, subjectData);

        CertificateData certificateData = new CertificateData(subjectData.getSerialNumber(),
                getRDNValueFromSubjectData(subjectData, BCStyle.CN),
                getRDNValueFromSubjectData(subjectData, BCStyle.O),
                getRDNValueFromSubjectData(subjectData, BCStyle.OU),
                getRDNValueFromSubjectData(subjectData, BCStyle.C),
                issuerSerialNumber,
                iso8601Formater.parse(newCertificateDto.getEndDate()),
                CertificateStatus.VALID,
                newCertificateDto.getCertificateType(),
                getCertificatePurposeBasedOnType(newCertificateDto.getCertificateType()));

        CertificateData savedCertificateData = certificateDataRepository.save(certificateData);

        X509Certificate x509certificate = new CertificateGenerator().generateCertificate(subjectData, issuerData, newCertificateDto.getCertificateType());
        Certificate[] certificateChain = getCertificateChain(savedCertificateData, x509certificate);

        keyStoreRepository.saveCertificate(keyPairSubject.getPrivate(), x509certificate, newCertificateDto.getCertificateType(), certificateChain);

        return savedCertificateData;
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

    @Override
    public void createCertificateFile(Long id) throws Exception {
        Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());
        CertificateData certificateData = certificateDataRepository.findById(id).get();
        if (isCertificateValid(certificateData)) {
            throw new Exception();
        }
        byte[] bytes = keyStoreRepository.readCertificate(certificateData.getCertificateType(), certificateData.getSerialNumber()).getEncoded();

        String certificate = BEGIN_CERT + LINE_SEPARATOR + new String(encoder.encode(bytes)) + LINE_SEPARATOR
                + END_CERT;

        writeBytesToFile(certificateData.getSerialNumber() + ".cer", certificate.getBytes());
    }

    private void writeBytesToFile(String fileOutput, byte[] bytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileOutput)) {
            fos.write(bytes);
        }
    }

    private boolean isCertificateValid(CertificateData certificate) {
        return certificate.getCertificateStatus() != CertificateStatus.VALID;
    }

    private CertificatePurposeType getCertificatePurposeBasedOnType(CertificateType certificateType) {
        if (certificateType.equals(CertificateType.ROOT)) return CertificatePurposeType.SERVICE;
        if (certificateType.equals(CertificateType.INTERMEDIATE)) return CertificatePurposeType.SUBSYSTEM;
        return CertificatePurposeType.USER;
    }

    private SubjectData generateSubjectData(NewCertificateDto newCertificateDto, PublicKey publicKey) {
        try {
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

            return new SubjectData(publicKey, builder.build(), sn, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Certificate[] getCertificateChain(CertificateData certificateData, Certificate currentCertificate) {
        if (certificateData.getCertificateType() == CertificateType.ROOT)
            return new Certificate[]{currentCertificate};

        List<Certificate> certificates = new ArrayList<>();
        certificates.add(currentCertificate);
        CertificateData issuerCertificateData = certificateDataRepository.findBySerialNumber(certificateData.getIssuerSerialNumber());
        Certificate issuerCertificate = keyStoreRepository.readCertificate(issuerCertificateData.getCertificateType(), issuerCertificateData.getSerialNumber());
        certificates.add(issuerCertificate);
        while (!issuerCertificateData.getSerialNumber().equals(issuerCertificateData.getIssuerSerialNumber())) {
            issuerCertificateData = certificateDataRepository.findBySerialNumber(issuerCertificateData.getIssuerSerialNumber());
            issuerCertificate = keyStoreRepository.readCertificate(issuerCertificateData.getCertificateType(), issuerCertificateData.getIssuerSerialNumber());
            certificates.add(issuerCertificate);
        }
        return certificates.toArray(new Certificate[0]);
    }


    public static String generateSerialNumber() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        while (uuid.startsWith("0"))
            uuid = UUID.randomUUID().toString().replaceAll("-", ""); //uuid starting with 0 creates problems with bigint parsing
        return uuid;
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

    private IssuerData getIssuerData(NewCertificateDto newCertificateDto, KeyPair keyPairSubject, SubjectData subjectData) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateEncodingException {

        if (newCertificateDto.getCertificateType() == CertificateType.ROOT) {
            return new IssuerData(keyPairSubject.getPrivate(), subjectData.getX500name());
        }
        CertificateData issuerCertificateData = certificateDataRepository.findById(newCertificateDto.getIssuerCertificateId()).get();
        Certificate issuerCertificate = keyStoreRepository.readCertificate(issuerCertificateData.getCertificateType(), issuerCertificateData.getSerialNumber());
        PrivateKey issuerKey = keyStoreRepository.getPrivateKeyForKeyStore(((X509Certificate) issuerCertificate).getSerialNumber().toString(16), issuerCertificateData.getCertificateType());
        X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) issuerCertificate).getSubject();
        return new IssuerData(issuerKey, issuerName);
    }

    private String getIssuerSerialNumber(SubjectData subjectData, NewCertificateDto certificateDto) {
        if (certificateDto.getCertificateType() == CertificateType.ROOT)
            return subjectData.getSerialNumber();
        return certificateDataRepository.findById(certificateDto.getIssuerCertificateId()).get().getSerialNumber();
    }

    private String getRDNValueFromSubjectData(SubjectData subjectData, ASN1ObjectIdentifier asn1ObjectIdentifier) {
        RDN rdn = subjectData.getX500name().getRDNs(asn1ObjectIdentifier)[0];
        return IETFUtils.valueToString(rdn.getFirst().getValue());
    }


}
