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
import com.antosara.akandaka.model.ui.PasswordEntryUI;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Vinay Penmatsa
 */
public class ListPasswordEntriesController implements Initializable {

    @FXML
    private TextField search;

    @FXML
    private TableView<PasswordEntryUI> passwordEntriesList;

    @FXML
    private TableColumn<PasswordEntryUI, String> titleColumn;

    @FXML
    private TableColumn<PasswordEntryUI, String> userNameColumn;

    @FXML
    private TableColumn<PasswordEntryUI, String> passwordColumn;

    @FXML
    private TableColumn<PasswordEntryUI, String> urlColumn;

    @FXML
    private TableColumn<PasswordEntryUI, String> notesColumn;

    private ObservableList<PasswordEntryUI> passwordEntryData;

    private Crypt crypt;

    private IO io;

    private Map<String, PasswordEntry> entryMap;

    void init(Crypt crypt, IO io) {
        this.crypt = crypt;
        this.io = io;
        entryMap = new HashMap();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordEntryData = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        userNameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        urlColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().miscProperty());

        passwordEntriesList.getSelectionModel().setCellSelectionEnabled(true);
        passwordEntriesList.setOnKeyReleased((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
                int idx = passwordEntriesList.getSelectionModel().getSelectedIndex();
                Action response = Dialogs.create().owner(null).title("Delete?")
                        .message("Are you sure you want to delete record?")
                        .showConfirm();

                if (response != Dialog.Actions.YES) {
                    return;
                }
                PasswordEntryUI deleted = passwordEntryData.remove(idx);
                entryMap.remove(deleted.getId());
                Collection<PasswordEntry> newCol = entryMap.values();

                try {
                    EncryptedDatabase db = io.read();
                    List<PasswordEntry> ents = db.getPasswordEntries();
                    ents.clear();
                    ents.addAll(newCol);
                    io.write(db);
                    load();
                } catch (IOException ex) {
                    Util.log(Level.SEVERE, null, ex);
                    Dialogs.create().owner(null).title("Error")
                            .message("Could not save")
                            .showError();
                }
            }
        });
        passwordEntriesList.setOnMouseClicked((MouseEvent e) -> {
            int count = e.getClickCount();
            int idx = passwordEntriesList.getSelectionModel().getSelectedIndex();
            switch (count) {
                case 1:
                    break;
                case 2:
                    ObservableList<TableColumn<PasswordEntryUI, ?>> columns = passwordEntriesList.getColumns();

                    for (TableColumn<PasswordEntryUI, ?> col : columns) {
                        if (passwordEntriesList.getSelectionModel().isSelected(idx, col)) {
                            Clipboard cb = Clipboard.getSystemClipboard();
                            ClipboardContent cc = new ClipboardContent();
                            if ("Password".equals(col.getText())) {
                                PasswordEntry pe = entryMap.get(passwordEntriesList.getSelectionModel().selectedItemProperty().get().getId());
                                try {
                                    cc.putString(new String(crypt.decrypt(pe.getPassword())));
                                } catch (Exception ex) {
                                    Util.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                    Dialogs.create().owner(null).title("Error")
                                            .message("Could not decrypt. Go to settings and select database file with correct password")
                                            .showError();
                                    return;
                                }
                            } else {
                                cc.putString(String.valueOf(col.getCellObservableValue(idx).getValue()));
                            }
                            cb.setContent(cc);
                            Task<Void> cbKillTask = new Task<Void>() {

                                @Override
                                protected Void call() throws Exception {
                                    Platform.runLater(() -> {
                                        cc.putString("");
                                        cb.setContent(cc);
                                        cb.clear();
                                    });
                                    return null;
                                }
                            };

                            Timer t = new Timer("Clipboard Killer", true);
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    cbKillTask.run();
                                }
                            }, 5000);
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }

        });

    }

    public void load() {
        if (io == null || crypt == null) {
            Dialogs.create().owner(null).title("Error")
                    .message("Could not load data. Go to settings and verify database file is correct")
                    .showError();
            return;
        }
        passwordEntryData.clear();
        entryMap.clear();
        passwordEntriesList.setItems(passwordEntryData);

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
        List<PasswordEntry> entries = db.getPasswordEntries();
        if (entries == null) {
            return;
        }

        try {
            for (PasswordEntry entry : entries) {
                entryMap.put(entry.getId(), entry);
                passwordEntryData.add(
                        new PasswordEntryUI(
                                entry.getId(),
                                new String(crypt.decrypt(entry.getTitle())),
                                new String(crypt.decrypt(entry.getUserName())),
                                new String(crypt.decrypt(entry.getUrl())),
                                new String(crypt.decrypt(entry.getMisc()))
                        ));

            }
            passwordEntriesList.setItems(passwordEntryData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            Util.log(Level.SEVERE, e.getLocalizedMessage(), e);
			// Password might be wrong. We should disable to prevent corruption
            // TODO: implement a better way
            App.valid = false;

            Dialogs.create().owner(null).title("Error")
                    .message("Could not decrypt. Go to settings and select database file with correct password")
                    .showError();
        }
    }

    public void filter(KeyEvent ae) {
        passwordEntriesList.setItems(passwordEntryData.filtered((p) -> p.getUserName().contains(search.getCharacters())));
    }

}
