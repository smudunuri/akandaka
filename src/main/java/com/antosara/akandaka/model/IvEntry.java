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

/**
 *
 * @author i816361
 */
public class IvEntry {
	private byte[] entry;
	private byte[] iv;

	public IvEntry() {
		
	}
	
	public IvEntry(byte[] entry, byte[] iv) {
		this.entry = entry;
		this.iv = iv;
	}

	public byte[] getEntry() {
		return entry;
	}

	public void setEntry(byte[] entry) {
		this.entry = entry;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
	
}
