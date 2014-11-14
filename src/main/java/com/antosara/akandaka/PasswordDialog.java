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

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.controlsfx.dialog.Dialog;

/**
 *
 * @author Vinay Penmatsa
 */
public class PasswordDialog {

	public static Optional<String> showPasswordInput() {
		Dialog dialog = new Dialog(null, "Enter Password");
		PasswordField pwField = new PasswordField();
		GridPane content = new GridPane();
		content.setHgap(10);
		content.setVgap(10);

		content.add(pwField, 0, 0);
		GridPane.setHgrow(pwField, Priority.ALWAYS);
		dialog.setResizable(false);
		dialog.setIconifiable(false);
		dialog.setContent(content);

		dialog.getActions().addAll(Dialog.Actions.OK, Dialog.Actions.CANCEL);
		Platform.runLater(() -> {
			pwField.requestFocus();
		});

		return Optional.ofNullable(dialog.show() == Dialog.Actions.OK ? pwField.getText() : null);
	}
}
