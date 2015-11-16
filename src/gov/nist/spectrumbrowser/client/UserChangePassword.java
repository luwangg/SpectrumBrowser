package gov.nist.spectrumbrowser.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nist.spectrumbrowser.client.SpectrumBrowser;
import gov.nist.spectrumbrowser.common.Defines;
import gov.nist.spectrumbrowser.common.SpectrumBrowserCallback;
import gov.nist.spectrumbrowser.common.SpectrumBrowserScreen;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Screen for user to change their password when they remember their old password
 * @author Julie Kub
 *
 */
public class UserChangePassword implements SpectrumBrowserCallback<String> , SpectrumBrowserScreen {
	
	private VerticalPanel verticalPanel;
	private SpectrumBrowser spectrumBrowser;
	private PasswordTextBox oldPasswordEntry;
	private PasswordTextBox passwordEntry;
	private PasswordTextBox passwordEntryConfirm;
	private TextBox emailEntry;
	private final SpectrumBrowserScreen loginScreen;
	private static Logger logger = Logger.getLogger("SpectrumBrowser");
	public static final String LABEL = "Change Password";

	
	public UserChangePassword(
			VerticalPanel verticalPanel, SpectrumBrowser spectrumBrowser, 
			SpectrumBrowserScreen loginScreen) {
		logger.finer("UserChangePassword");
		this.verticalPanel = verticalPanel;
		this.spectrumBrowser = spectrumBrowser;
		this.loginScreen = loginScreen;
				
	}
	
	class SubmitChangePassword implements ClickHandler {


			@Override
			public void onClick(ClickEvent event) {
				String password = "";
				String oldPassword = "";
				String passwordConfirm = "";
				String emailAddress = "";
				try {
					oldPassword = oldPasswordEntry.getValue();
					password = passwordEntry.getValue();
					passwordConfirm = passwordEntryConfirm.getValue();
					emailAddress = emailEntry.getValue().trim();					
				}
				catch (Throwable th) {
					//not a problem, since we will check for null's below.
				}

				logger.finer("SubmitNewAccount: " + emailAddress);
				if (emailAddress == null || emailAddress.length() == 0) {
					Window.alert("Email is required.");
					return;
				}
				if (oldPassword == null || oldPassword.length() == 0) {
					Window.alert("Current password is required.");
					return;
				}
				if (password == null || password.length() == 0) {
					Window.alert("Password is required.");
					return;
				}
				if (passwordConfirm == null || passwordConfirm.length() == 0) {
					Window.alert("Re-typed password is required.");
					return;
				}
				if (password != passwordConfirm)
				{
					Window.alert("Password entries must match.");
					return;					
				}
				
				JSONObject jsonObject  = new JSONObject();
				jsonObject.put(Defines.ACCOUNT_EMAIL_ADDRESS, new JSONString(emailAddress));
				jsonObject.put(Defines.ACCOUNT_OLD_PASSWORD, new JSONString(oldPassword));
				jsonObject.put(Defines.ACCOUNT_NEW_PASSWORD, new JSONString(password));
				jsonObject.put(Defines.ACCOUNT_PRIVILEGE, new JSONString(Defines.USER_PRIVILEGE));

				spectrumBrowser.getSpectrumBrowserService().changePassword(jsonObject.toString(), UserChangePassword.this);
				verticalPanel.clear();
				loginScreen.draw();
			
			};
		

	}


	public void draw() {
		

		verticalPanel.clear();
		HTML title = new HTML("<h2>Change Password</h2>");
		verticalPanel.add(title);
		
		Grid grid = new Grid(4,2);
		grid.setText(0, 0, "Email Address");
		emailEntry = new TextBox();
		emailEntry.setWidth("250px");
		grid.setWidget(0, 1, emailEntry);
		grid.setText(1,0, "Current Password");
		oldPasswordEntry = new PasswordTextBox();
		grid.setWidget(1, 1, oldPasswordEntry);
		grid.setText(2,0, "New Password");
		passwordEntry = new PasswordTextBox();
		grid.setWidget(2, 1, passwordEntry);
		grid.setText(3,0, "Re-type New Password");
		passwordEntryConfirm = new PasswordTextBox();
		grid.setWidget(3, 1, passwordEntryConfirm);
		verticalPanel.add(grid);
	
		Grid buttonGrid = new Grid(1,2);
		verticalPanel.add(buttonGrid);
		
		Button buttonSubmit = new Button("Submit");
		buttonSubmit.addStyleName("sendButton");
		buttonGrid.setWidget(0,0,buttonSubmit);
		buttonSubmit.addClickHandler( new SubmitChangePassword());
		
		Button cancelButton = new Button("Cancel");
		buttonGrid.setWidget(0, 1, cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				verticalPanel.clear();
				UserChangePassword.this.loginScreen.draw();
			}});
	}

	@Override
	public void onSuccess(String result) {
		JSONObject jsonObject = JSONParser.parseLenient(result).isObject();
		String statusMessage = jsonObject.get(Defines.STATUS_MESSAGE).isString().stringValue();
		Window.alert(statusMessage);
	}

	@Override
	public void onFailure(Throwable throwable) {
		logger.log(Level.SEVERE, "Error occured when contacting server in UserChangePassword",throwable);
		Window.alert("Error occured contacting server in UserChangePassword.");
		
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public String getEndLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
