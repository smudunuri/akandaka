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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Vinay Penmatsa
 */
public class AppController implements Initializable {

	@FXML
	private Button createDbFile;

	@FXML
	private Button selectDbFile;

	@FXML
	private TextField dbFile;

	@FXML
	private WebView webView;

	@FXML
	private GridPane createPasswordPane;

	@FXML
	private CreatePasswordEntryController createPasswordEntryController;

	@FXML
	private ListPasswordEntriesController listPasswordEntriesController;

	private Properties settings;

	private File settingsFile;

	private IO io;

	private Crypt crypt;

	@FXML
	private void createDbFile(MouseEvent event) {
		App.valid = true;
		Window window = createDbFile.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(window);
		if (file != null) {
			try {
				file.createNewFile();
				IO ioNew = new IO(file);
				EncryptedDatabase eDbNew = new EncryptedDatabase();
				eDbNew.setSalt(Crypt.generateSalt());
				ioNew.write(eDbNew);
				dbFile.setText(file.getAbsolutePath());
				settings.setProperty("DATABASE_FILE", file.getAbsolutePath());
				settings.store(new FileWriter(getSettingsFile()), null);
				loadCrypt();
			} catch (IOException ex) {
				Util.log(Level.SEVERE, "AppController.createDbFile", ex);
				App.valid = false;
			}

		}

	}

	@FXML
	private void selectDbFile(MouseEvent event) {
		App.valid = true;
		Window window = selectDbFile.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(window);
		if (file != null) {
			try {
				file.createNewFile();
				dbFile.setText(file.getAbsolutePath());
				settings.setProperty("DATABASE_FILE", file.getAbsolutePath());
				settings.store(new FileWriter(getSettingsFile()), null);
				loadCrypt();
			} catch (IOException ex) {
				Util.log(Level.SEVERE, "AppController.selectDbFile", ex);
				App.valid = false;
			}
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		settings = new Properties();

		loadHelp();
		// Look for previously used database file from settings
		// If found load from that.
		try {
			settings.load(new FileInputStream(getSettingsFile()));
			dbFile.setText(settings.getProperty("DATABASE_FILE", ""));
		} catch (IOException ex) {
			Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			Dialogs.create().owner(null).title("Message")
					.message("Unable to access " + settingsFile.getAbsolutePath())
					.showError();
		}

		// If database is found, ask for password
		if (Util.hasValue(dbFile.getText())) {
			loadCrypt();
		}

	}

	private void loadCrypt() {
		Optional<String> pw = PasswordDialog.showPasswordInput();
		if (pw.isPresent() && Util.hasValue(pw.get())) {
			try {
				io = new IO(new File(dbFile.getText()));
			} catch (IOException ex) {
				Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
				Dialogs.create().owner(null).title("Error")
						.message("Unable to read database file: " + dbFile.getText())
						.showError();
				App.valid = false;
				return;
			}
			EncryptedDatabase eDb = null;
			byte[] salt = null;
			try {
				eDb = io.read();
				salt = eDb.getSalt();
			} catch (IOException e) {
				Util.log(Level.SEVERE, e.getLocalizedMessage(), e);
				Dialogs.create().owner(null).title("Error")
						.message("Invalid database file: " + dbFile.getText())
						.showError();
				App.valid = false;
				return;
			}
			try {
				crypt = new Crypt(salt, pw.get().toCharArray());
				createPasswordEntryController.init(crypt, io);
				listPasswordEntriesController.init(crypt, io);
				listPasswordEntriesController.load();
			} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
				Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
				App.valid = false;
				Dialogs.create().owner(null).title("Error")
						.message("Crypto error: " + ex.getLocalizedMessage())
						.showError();
			}
		}
	}

	private File getSettingsFile() throws IOException {
		if (settingsFile != null) {
			return settingsFile;
		}
		String userHome = System.getProperty("user.home");
		settingsFile = new File(userHome + File.separator + ".vinpass");
		if (!settingsFile.exists()) {
			settingsFile.createNewFile();
		}

		return settingsFile;
	}

	public void loadHelp() {
		final WebEngine webEngine = webView.getEngine();
		webEngine.load("http://pvinay.github.io/akandaka/help.html");
	}
}
