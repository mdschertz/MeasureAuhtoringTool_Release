package mat.client.myAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.util.ClientConstants;
import mat.shared.MyAccountModelValidator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PersonalInformationPresenter.
 */
public class PersonalInformationPresenter implements MatPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = (LoginServiceAsync) MatContext.get().getLoginService();
	
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Gets the first name.
		 * 
		 * @return the first name
		 */
		HasValue<String> getFirstName();
		
		/**
		 * Gets the middle initial.
		 * 
		 * @return the middle initial
		 */
		HasValue<String> getMiddleInitial();
		
		/**
		 * Gets the last name.
		 * 
		 * @return the last name
		 */
		HasValue<String> getLastName();
		
		/**
		 * Gets the title.
		 * 
		 * @return the title
		 */
		HasValue<String> getTitle();
		
		/**
		 * Gets the email address.
		 * 
		 * @return the email address
		 */
		HasValue<String> getEmailAddress();
		
		/**
		 * Gets the login id.
		 * 
		 * @return the login id
		 */
		Label getLoginId();
		
		/**
		 * Gets the phone number.
		 * 
		 * @return the phone number
		 */
		HasValue<String> getPhoneNumber();
		
		/**
		 * Gets the organisation.
		 * 
		 * @return the organisation
		 */
		HasValue<String> getOrganisation();
		
		/**
		 * Gets the oid.
		 * 
		 * @return the oid
		 */
		HasValue<String> getOID();
		//HasValue<String> getRootOID();
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		HasValue<String> getPassword();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the password edit info widget.
		 * 
		 * @return the password edit info widget
		 */
		PasswordEditInfoWidget getPasswordEditInfoWidget();
	}
	
	/** The display. */
	private Display display;
	
	/** The current model. */
	private MyAccountModel currentModel;
	


	/*private HibernateUserDetailService  getHibernateUserService(){
		return (HibernateUserDetailService)context.getBean("hibernateUserDetailService");
	}
	
	private UserService  getUserService(){
		return (UserService)context.getBean("userService");
	}
		*/
	
	/**
	 * Instantiates a new personal information presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public PersonalInformationPresenter(Display displayArg) {
		this.display = displayArg;
		
		display.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				beforeDisplay();
				//if(currentModel != null) {
				//	setValues(currentModel);
				//}
			}
		});
		display.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				savePersonalInformation(display.getPassword().getValue());
				display.getPassword().setValue("");
			}
		});
	
	}
	
	/**
	 * Save personal information.
	 * 
	 * @param password
	 *            the password
	 */
	private void savePersonalInformation(String password){
		
		    loginService.validatePassword(MatContext.get().getLoggedinLoginId(), password, new AsyncCallback<HashMap<String,String>>(){ 
			@Override
			public void onSuccess(HashMap<String,String> resultMap) {
				String result = (String)resultMap.get("result");
		    	if(result.equals("SUCCESS")){
		    		currentModel = getValues();
			    	if(isValid(currentModel)) {	
		    			MatContext.get().getMyAccountService().saveMyAccount(currentModel, new AsyncCallback<SaveMyAccountResult>() {
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}
		
							@Override
							public void onSuccess(SaveMyAccountResult result) {
								if(result.isSuccess()){
									display.getErrorMessageDisplay().clear();
									display.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPersonalInfoUpdatedMessage());
								}else{
									List<String> messages = new ArrayList<String>();
									switch(result.getFailureReason()) {
										case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
											messages = result.getMessages();
											break;
										default:
											messages.add(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
									}
									display.getErrorMessageDisplay().setMessages(messages);
								}
							}
						});
			    	}	
				}else{
					display.getErrorMessageDisplay().clear();
					display.getSuccessMessageDisplay().clear();
					String displayErrorMsg= (String)resultMap.get("message");
					if(displayErrorMsg.equals("REDIRECT")){
						MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
					}else{
						display.getErrorMessageDisplay().setMessage(displayErrorMsg);
					}
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
		});
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		
		display.getErrorMessageDisplay().clear();
		display.getSuccessMessageDisplay().clear();
		MatContext.get().getMyAccountService().getMyAccount(new AsyncCallback<MyAccountModel>() {
			
			public void onSuccess(MyAccountModel result) {
				currentModel = result;
				setValues(currentModel);
			}
			
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});	
		Mat.focusSkipLists("PersonalInfo");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override 
	public void beforeClosingDisplay() {
	}

	/**
	 * Sets the values.
	 * 
	 * @param model
	 *            the new values
	 */
	private void setValues(MyAccountModel model) {
		display.getFirstName().setValue(model.getFirstName());
		display.getMiddleInitial().setValue(model.getMiddleInitial());
		display.getLastName().setValue(model.getLastName());
		display.getTitle().setValue(model.getTitle());
		display.getEmailAddress().setValue(model.getEmailAddress());
		display.getLoginId().setText(model.getLoginId());
		display.getPhoneNumber().setValue(model.getPhoneNumber());
		display.getOrganisation().setValue(model.getOrganisation());
		display.getOID().setValue(model.getOid());
		//display.getRootOID().setValue(model.getRootoid());
		display.getPassword().setValue("");
	}
	
	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	private MyAccountModel getValues() {
		MyAccountModel model = new MyAccountModel();
		model.setFirstName(display.getFirstName().getValue());
		model.setMiddleInitial(display.getMiddleInitial().getValue());
		model.setLastName(display.getLastName().getValue());
		model.setTitle(display.getTitle().getValue());
		model.setEmailAddress(display.getEmailAddress().getValue());
		model.setPhoneNumber(display.getPhoneNumber().getValue());
		model.setOrganisation(display.getOrganisation().getValue());
		model.setOid(display.getOID().getValue());
		//model.setRootoid(display.getRootOID().getValue());
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	public Widget getWidget() {
		return display.asWidget();
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @param model
	 *            the model
	 * @return true, if is valid
	 */
	private boolean isValid(MyAccountModel model) {
		
		MyAccountModelValidator test = new MyAccountModelValidator();
		List<String>  message= test.validate(model);
		
		boolean valid = message.size() == 0;
		if(!valid) {
			display.getErrorMessageDisplay().setMessages(message);
		}
		else {
			display.getErrorMessageDisplay().clear();
		}
		return valid;
	}
}
