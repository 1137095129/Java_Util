package com.wjf.github.sec;

public enum CipherKind {

	RSA_ECB_NOPADDING("Rsa/Ecb/NoPadding");

	private final String name;

	CipherKind(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
