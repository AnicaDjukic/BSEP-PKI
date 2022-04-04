package com.Bsep.model;

import java.security.cert.X509Certificate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class CertificateData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificateIdSeqGen")
    @SequenceGenerator(name = "certificateIdSeqGen", sequenceName = "certificateIdSeq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "serialNumber", unique = true, nullable = false)
    private String serialNumber;

    @Column(name = "subjectUsername", unique = false, nullable = false)
    private String subjectUsername;

    @Column(name="organization", unique = false, nullable = true)
    private String organization;

    @Column(name="organizationalUnitName", unique = false, nullable = true)
    private String organizationalUnitName;

    @Column(name="countryCode", unique = false, nullable = true)
    private String countryCode;

    @Column(name = "issuerSerialNumber", unique = false, nullable = false)
    private String issuerSerialNumber;

    @Enumerated(EnumType.ORDINAL)
    private CertificateStatus certificateStatus;

    @Enumerated(EnumType.ORDINAL)
    private CertificateType certificateType;

    @Enumerated(EnumType.ORDINAL)
    private CertificatePurposeType certificatePurposeType;


    public CertificateData() {
    }

    public CertificateData(String serialNumber, String subjectUsername, String organization, String organizationalUnitName, String countryCode, String issuerSerialNumber, CertificateStatus certificateStatus, CertificateType certificateType, CertificatePurposeType certificatePurposeType) {
        this.serialNumber = serialNumber;
        this.subjectUsername = subjectUsername;
        this.organization = organization;
        this.organizationalUnitName = organizationalUnitName;
        this.countryCode = countryCode;
        this.issuerSerialNumber = issuerSerialNumber;
        this.certificateStatus = certificateStatus;
        this.certificateType = certificateType;
        this.certificatePurposeType = certificatePurposeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public String getSubjectUsername() {
		return subjectUsername;
	}

	public void setSubjectUsername(String subjectUsername) {
		this.subjectUsername = subjectUsername;
	}

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public void setOrganizationalUnitName(String organizationalUnitName) {
        this.organizationalUnitName = organizationalUnitName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public CertificateStatus getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(CertificateStatus certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public CertificatePurposeType getCertificatePurposeType() {
        return certificatePurposeType;
    }

    public void setCertificatePurposeType(CertificatePurposeType certificatePurposeType) {
        this.certificatePurposeType = certificatePurposeType;
    }
}
