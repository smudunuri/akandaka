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
package com.antosara.akandaka.model;

import java.util.List;

/**
 *
 * @author Vinay Penmatsa
 */
public class EncryptedDatabase {

	private byte[] salt;
	private List<PasswordEntry> passwordEntries;

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public List<PasswordEntry> getPasswordEntries() {
		return passwordEntries;
	}

	public void setPasswordEntries(List<PasswordEntry> passwordEntries) {
		this.passwordEntries = passwordEntries;
	}

}
