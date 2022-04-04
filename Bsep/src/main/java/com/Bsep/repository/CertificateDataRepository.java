package com.Bsep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bsep.model.CertificateData;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CertificateDataRepository extends JpaRepository<CertificateData, Long>{

	CertificateData findBySerialNumber(Long issuerCertificateId);


}
