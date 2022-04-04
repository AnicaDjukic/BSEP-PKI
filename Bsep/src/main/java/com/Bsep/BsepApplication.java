package com.Bsep;

import org.bouncycastle.asn1.DERBitString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BsepApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsepApplication.class, args);
		String inputString = "digitalSignature";
		byte[] byteArrray = inputString.getBytes();
		System.out.println(new DERBitString(byteArrray));
	}

}
