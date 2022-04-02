package com.Bsep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bsep.model.CertificateData;


public interface CertificateDataRepository extends JpaRepository<CertificateData, Long>{

	CertificateData findBySerialNumber(Long issuerCertificateId);

}
