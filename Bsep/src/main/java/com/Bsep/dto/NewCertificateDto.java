package com.Bsep.dto;

import java.util.List;

import com.Bsep.model.CertificatePurposeType;
import com.Bsep.model.CertificateType;

public class NewCertificateDto {

    private Long subjectUID;
    private String organization;
    private String organizationalUnitName;
    private String organizationEmail;
    private String countryCode;
    private Long issuerCertificateId;
    private String endDate;
    private CertificateType certificateType;
    private List<String> keyUsages;

    public NewCertificateDto() {
    }

   

    public NewCertificateDto(Long subjectUID, String organization, String organizationalUnitName,
			String organizationEmail, String countryCode, Long issuerCertificateId, String endDate,
			CertificateType certificateType, List<String> keyUsages) {
		super();
		this.subjectUID = subjectUID;
		this.organization = organization;
		this.organizationalUnitName = organizationalUnitName;
		this.organizationEmail = organizationEmail;
		this.countryCode = countryCode;
		this.issuerCertificateId = issuerCertificateId;
		this.endDate = endDate;
		this.certificateType = certificateType;
		this.keyUsages = keyUsages;
	}



	public Long getSubjectUID() {
        return subjectUID;
    }

    public String getOrganization() {
        return organization;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Long getIssuerCertificateId() {
        return issuerCertificateId;
    }

    public String getEndDate() {
        return endDate;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }



	public List<String> getKeyUsages() {
		return keyUsages;
	}


    
}
