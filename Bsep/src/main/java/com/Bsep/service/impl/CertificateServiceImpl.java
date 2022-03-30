package com.Bsep.service.impl;

import com.Bsep.certificate.CertificateGenerator;
import com.Bsep.dto.NewCertificateDto;
import com.Bsep.model.CertificateData;
import com.Bsep.model.IssuerData;
import com.Bsep.model.SubjectData;
import com.Bsep.model.User;
import com.Bsep.service.CerificateService;
import com.Bsep.service.UserService;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CerificateService {

    private final UserService userService;

    public CertificateServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CertificateData createCertificate(NewCertificateDto newCertificateDto) {

        SubjectData subjectData = generateSubjectData(newCertificateDto);

        KeyPair keyPairIssuer = generateKeyPair();
        IssuerData issuerData = generateIssuerData(keyPairIssuer.getPrivate());

        //Generise se sertifikat za subjekta, potpisan od strane issuer-a
        CertificateGenerator cg = new CertificateGenerator();
        X509Certificate cert = cg.generateCertificate(subjectData, issuerData);

        return new CertificateData();
    }

    private SubjectData generateSubjectData(NewCertificateDto newCertificateDto) {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(String.valueOf(new Date()));
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


    private IssuerData generateIssuerData(PrivateKey issuerKey) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "Nikola Luburic");
        builder.addRDN(BCStyle.SURNAME, "Luburic");
        builder.addRDN(BCStyle.GIVENNAME, "Nikola");
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "nikola.luburic@uns.ac.rs");
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, "654321");

        //Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new IssuerData(issuerKey, builder.build());
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
