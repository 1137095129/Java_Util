package com.wjf.github.sec;

import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public enum KeyPairGeneratorKind {

	RSA("RSA", RSAPublicKeySpec.class, RSAPrivateKeySpec.class);

	private final String name;

	private final Class<? extends KeySpec> publicClazz;

	private final Class<? extends KeySpec> privateClazz;

	KeyPairGeneratorKind(String name, Class<? extends KeySpec> publicClazz, Class<? extends KeySpec> privateClazz) {
		this.name = name;
		this.publicClazz = publicClazz;
		this.privateClazz = privateClazz;
	}

	public String getName() {
		return name;
	}

	public Class<? extends KeySpec> getPublicClazz() {
		return publicClazz;
	}

	public Class<? extends KeySpec> getPrivateClazz() {
		return privateClazz;
	}
}
