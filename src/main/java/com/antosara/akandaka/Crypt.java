/*
 Copyright (c) 2014 Vinay Penmatsa

 This file is part of Akandaka.

 Akandaka is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Akandaka is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Akandaka.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.antosara.akandaka;

import com.antosara.akandaka.model.IvEntry;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Vinay Penmatsa
 */
public class Crypt {

	private final SecretKey key;

	public Crypt(byte[] salt, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
		SecretKey secret = factory.generateSecret(spec);
		this.key = new SecretKeySpec(secret.getEncoded(), "AES");
	}

	public IvEntry encrypt(byte[] str) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, this.key);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		return new IvEntry(cipher.doFinal(str), iv);
	}

	public byte[] decrypt(IvEntry ivEntry) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(ivEntry.getIv()));
		return cipher.doFinal(ivEntry.getEntry());
	}

	public static byte[] generateSalt() {
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[128];
		sr.nextBytes(salt);
		return salt;
	}
}
