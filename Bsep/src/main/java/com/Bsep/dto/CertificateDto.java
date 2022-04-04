package com.Bsep.dto;

import com.Bsep.model.CertificatePurposeType;
import com.Bsep.model.CertificateType;

public class CertificateDto {

    private String serialNumber;
    private String username;
    private String organization;
    private String organizationalUnitName;
    private String countryCode;
    private String issuerCommonName;
    private String endDate;
    private boolean valid;
    private CertificateType certificateType;

    public CertificateDto() {
    }

    public CertificateDto(String serialNumber, String username, String organization, String organizationalUnitName, String countryCode, String issuerCommonName, String endDate, boolean valid, CertificateType certificateType) {
        this.serialNumber = serialNumber;
        this.username = username;
        this.organization = organization;
        this.organizationalUnitName = organizationalUnitName;
        this.countryCode = countryCode;
        this.issuerCommonName = issuerCommonName;
        this.endDate = endDate;
        this.valid = valid;
        this.certificateType = certificateType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getOrganization() {
        return organization;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getIssuerCommonName() {
        return issuerCommonName;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isValid() {
        return valid;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }
}
