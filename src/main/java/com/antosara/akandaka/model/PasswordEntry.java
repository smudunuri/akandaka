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
 * @author Vinay Penmatsa
 */
public class PasswordEntry {

	private String id;
	private IvEntry title;
	private IvEntry userName;
	private IvEntry password;
	private IvEntry url;
	private IvEntry misc;
	
	public IvEntry getUserName() {
		return userName;
	}

	public void setUserName(IvEntry userName) {
		this.userName = userName;
	}

	public IvEntry getPassword() {
		return password;
	}

	public void setPassword(IvEntry password) {
		this.password = password;
	}

	public IvEntry getUrl() {
		return url;
	}

	public void setUrl(IvEntry url) {
		this.url = url;
	}

	public IvEntry getMisc() {
		return misc;
	}

	public void setMisc(IvEntry misc) {
		this.misc = misc;
	}

	public IvEntry getTitle() {
		return title;
	}

	public void setTitle(IvEntry title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
