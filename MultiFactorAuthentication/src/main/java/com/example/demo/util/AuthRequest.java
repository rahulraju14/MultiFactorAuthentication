package com.example.demo.util;

public class AuthRequest {

	private String userName;
	private String password;
	private String actionUrl;
	private boolean showVerficationField;
	private boolean displayOtpLink;
	private boolean displayPass;
	private boolean displayEmailField;
	private boolean displayErrorMessage;
	private String qrCodeUrl;
	private boolean mobileView;
	private String generatedCode;
	private String otpLabel;

	public AuthRequest() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public boolean isShowVerficationField() {
		return showVerficationField;
	}

	public void setShowVerficationField(boolean showVerficationField) {
		this.showVerficationField = showVerficationField;
	}

	public boolean isDisplayOtpLink() {
		return displayOtpLink;
	}

	public void setDisplayOtpLink(boolean displayOtpLink) {
		this.displayOtpLink = displayOtpLink;
	}

	public boolean isDisplayPass() {
		return displayPass;
	}

	public void setDisplayPass(boolean displayPass) {
		this.displayPass = displayPass;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public boolean isDisplayEmailField() {
		return displayEmailField;
	}

	public void setDisplayEmailField(boolean displayEmailField) {
		this.displayEmailField = displayEmailField;
	}

	public boolean isDisplayErrorMessage() {
		return displayErrorMessage;
	}

	public void setDisplayErrorMessage(boolean displayErrorMessage) {
		this.displayErrorMessage = displayErrorMessage;
	}

	public boolean isMobileView() {
		return mobileView;
	}

	public void setMobileView(boolean mobileView) {
		this.mobileView = mobileView;
	}

	public String getGeneratedCode() {
		return generatedCode;
	}

	public void setGeneratedCode(String generatedCode) {
		this.generatedCode = generatedCode;
	}

	public String getOtpLabel() {
		return otpLabel;
	}

	public void setOtpLabel(String otpLabel) {
		this.otpLabel = otpLabel;
	}

}
