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

import com.antosara.akandaka.model.EncryptedDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Vinay Penmatsa
 */
public class IO {

	private final File database;

	public IO(File db) throws IOException {
		this.database = db;
		if(!this.database.exists()) {
			throw new IOException("File doesn't exist");
		}
	}

	public EncryptedDatabase read() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		EncryptedDatabase db = mapper.readValue(database, EncryptedDatabase.class);
		return db;
	}

	public void write(EncryptedDatabase db) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(database, db);
	}
}
