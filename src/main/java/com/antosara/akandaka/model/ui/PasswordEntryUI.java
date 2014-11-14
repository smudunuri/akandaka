/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antosara.akandaka.model.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Vinay Penmatsa
 */
public class PasswordEntryUI {

	private String id;
	private StringProperty title;
	private StringProperty userName;
	private StringProperty password;
	private StringProperty url;
	private StringProperty misc;

	public PasswordEntryUI(String id, String title, String userName, String url, String misc) {
		this.id = id;
		this.title = new SimpleStringProperty(this, "title", title);
		this.userName = new SimpleStringProperty(this, "userName", userName);
		this.password = new SimpleStringProperty(this, "password", "*****");
		this.url = new SimpleStringProperty(this, "url", url);
		this.misc = new SimpleStringProperty(this, "misc", misc);
	}

	public String getTitle() {
		return titleProperty().get();
	}

	public void setTitle(StringProperty title) {
		this.title = title;
	}

	public StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title");
		}
		return title;
	}

	public String getUrl() {
		return urlProperty().get();
	}

	public void setUrl(StringProperty url) {
		this.url = url;
	}

	public StringProperty urlProperty() {
		if (url == null) {
			url = new SimpleStringProperty(this, "url");
		}
		return url;
	}

	public String getUserName() {
		return userNameProperty().get();
	}

	public void setUserName(StringProperty userName) {
		this.userName = userName;
	}

	public StringProperty userNameProperty() {
		if (userName == null) {
			userName = new SimpleStringProperty(this, "userName");
		}
		return userName;
	}

	public String getPassword() {
		return passwordProperty().get();
	}

	public void setPassword(StringProperty password) {
		this.password = password;
	}

	public StringProperty passwordProperty() {
		if (password == null) {
			password = new SimpleStringProperty(this, "password");
		}
		return password;
	}

	public String getMisc() {
		return miscProperty().get();
	}

	public void setMisc(StringProperty misc) {
		this.misc = misc;
	}

	public StringProperty miscProperty() {
		if (misc == null) {
			misc = new SimpleStringProperty(this, "misc");
		}
		return misc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
