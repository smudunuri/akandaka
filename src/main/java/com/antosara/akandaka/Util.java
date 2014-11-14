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

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Vinay Penmatsa
 */
public class Util {

	private static FileHandler LOG_FILE_HANDLER;
	private static final Logger globalLogger = Logger.getGlobal();

	static {
		try {
			LOG_FILE_HANDLER = new FileHandler(System.getProperty("user.home")
					+ File.separator + "antosara_ss.log");
			LOG_FILE_HANDLER.setFormatter(new SimpleFormatter());
			globalLogger.setUseParentHandlers(false);
			globalLogger.addHandler(LOG_FILE_HANDLER);

		} catch (IOException | SecurityException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static boolean hasValue(String str) {
		return str != null && !"".equals(str.trim());
	}

	public static void log(Level level, String message, Throwable excp) {
		globalLogger.log(level, message, excp);
	}
}
