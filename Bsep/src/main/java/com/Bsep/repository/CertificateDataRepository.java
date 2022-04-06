package com.Bsep.repository;

import com.Bsep.model.CertificateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface CertificateDataRepository extends JpaRepository<CertificateData, Long> {

    CertificateData findBySerialNumber(String serialNumber);

    @Query("SELECT c FROM CertificateData c WHERE (:isCa is null or (:isCa = TRUE and (c.certificateType = 0 or c.certificateType = 1)) or (:isCa = FALSE and c.certificateType = 2))")
    Set<CertificateData> findAll(@Param("isCa") Boolean isCa);

    List<CertificateData> findBySubjectUsername(String username);

    List<CertificateData> findByIssuerSerialNumber(String issuerSerialNumber);
}
