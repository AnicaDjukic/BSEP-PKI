package com.Bsep.mapper;

import com.Bsep.dto.CertificateDto;
import com.Bsep.model.CertificateData;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class CertificateMapper {
    SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");

    public CertificateDto toDTO(CertificateData certificateData, String issuerCommonName) {
        return new CertificateDto(certificateData.getId(), certificateData.getSerialNumber(),
                certificateData.getSubjectUsername(),
                certificateData.getOrganization(),
                certificateData.getOrganizationalUnitName(),
                certificateData.getCountryCode(),
                issuerCommonName,
                iso8601Formater.format(certificateData.getEndDate()),
                certificateData.getCertificateStatus(),
                certificateData.getCertificateType());
    }
}
