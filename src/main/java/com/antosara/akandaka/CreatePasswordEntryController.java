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
import com.antosara.akandaka.model.PasswordEntry;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Vinay Penmatsa
 */
public class CreatePasswordEntryController implements Initializable {

    @FXML
    private TextField title;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private TextField url;

    @FXML
    private TextArea misc;

    @FXML
    private Button save;

    @FXML
    private Button clear;

    private Crypt crypt;

    private IO io;

    void init(Crypt crypt, IO io) {
        this.crypt = crypt;
        this.io = io;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void save(ActionEvent event) {
        if (io == null || crypt == null || !App.valid) {
            Dialogs.create().owner(null).title("Error")
                    .message("Cannot save. Go to settings and verify database/password is correct")
                    .showError();
            return;
        }
        PasswordEntry pEntry = new PasswordEntry();

        try {
            pEntry.setTitle(crypt.encrypt(title.getText().getBytes()));
            pEntry.setUserName(crypt.encrypt(userName.getText().getBytes()));
            pEntry.setPassword(crypt.encrypt(password.getText().getBytes()));
            pEntry.setUrl(crypt.encrypt(url.getText().getBytes()));
            pEntry.setMisc(crypt.encrypt(misc.getText().getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException ex) {
            Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            Dialogs.create().owner(null).title("Error")
                    .message("Could not encrypt. Go to settings and select database file with correct password")
                    .showError();
            return;
        }
        pEntry.setId(UUID.randomUUID().toString());

        EncryptedDatabase db;
        try {
            db = io.read();
        } catch (IOException ex) {
            Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            Dialogs.create().owner(null).title("Error")
                    .message("Could not read database file. Go to settings and verify it is correct")
                    .showError();
            return;
        }
        List<PasswordEntry> pEntries = db.getPasswordEntries();
        if (pEntries == null || pEntries.isEmpty()) {
            pEntries = new ArrayList();
            db.setPasswordEntries(pEntries);
        }

        pEntries.add(pEntry);
        try {
            io.write(db);
        } catch (IOException ex) {
            Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            Dialogs.create().owner(null).title("Error")
                    .message("Could not write to database file. Go to settings and verify it is correct")
                    .showError();
            return;
        }
        clearFields();

        Dialogs.create().owner(null).title("Message")
                .message("Password entry saved")
                .showInformation();
    }

    public void clear(ActionEvent ae) {
        clearFields();
    }

    private void clearFields() {
        title.setText("");
        userName.setText("");
        password.setText("");
        url.setText("");
        misc.setText("");
    }
}
