package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.HasVisible;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.search.SearchView;
import mat.model.Author;
import mat.model.MeasureType;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MetaDataPresenter.
 */
public class MetaDataPresenter extends BaseMetaDataPresenter implements MatPresenter{
	
	
	/**
	 * The Interface MetaDataDetailDisplay.
	 */
	public static interface MetaDataDetailDisplay extends BaseMetaDataDisplay{
		
		/**
		 * Gets the measure name.
		 * 
		 * @return the measure name
		 */
		public Label getMeasureName();
		
		/**
		 * Gets the short name.
		 * 
		 * @return the short name
		 */
		public Label getShortName();
		
		//US 421. Measure scoring choice is now part of measure creation process. So, this module just displays the choice.
		/**
		 * Gets the measure scoring.
		 * 
		 * @return the measure scoring
		 */
		public Label getMeasureScoring();
		
		/**
		 * Gets the edits the authors button.
		 * 
		 * @return the edits the authors button
		 */
		public HasClickHandlers getEditAuthorsButton();
		
		/**
		 * Gets the edits the measure type button.
		 * 
		 * @return the edits the measure type button
		 */
		public HasClickHandlers getEditMeasureTypeButton();
		
		/**
		 * Gets the focus panel.
		 * 
		 * @return the focus panel
		 */
		public HasKeyDownHandlers getFocusPanel();
		
		/**
		 * Gets the version number.
		 * 
		 * @return the version number
		 */
		public Label getVersionNumber();
		//public HasValue<String> getMeasureId();
		/**
		 * Gets the sets the name.
		 * 
		 * @return the sets the name
		 */
		public HasValue<String> getSetName();
		
		/**
		 * Gets the e measure identifier.
		 * 
		 * @return the e measure identifier
		 */
		public Label geteMeasureIdentifier();
		
		/**
		 * Gets the nqf id.
		 * 
		 * @return the nqf id
		 */
		public HasValue<String> getNqfId();
		
		/**
		 * Gets the finalized date.
		 * 
		 * @return the finalized date
		 */
		public Label getFinalizedDate();
		
		/**
		 * Gets the measurement from period.
		 * 
		 * @return the measurement from period
		 */
		public String getMeasurementFromPeriod();
		
		/**
		 * Gets the measurement from period input box.
		 * 
		 * @return the measurement from period input box
		 */
		public DateBoxWithCalendar getMeasurementFromPeriodInputBox();
		
		/**
		 * Gets the measurement to period.
		 * 
		 * @return the measurement to period
		 */
		public String getMeasurementToPeriod();
		
		/**
		 * Gets the measurement to period input box.
		 * 
		 * @return the measurement to period input box
		 */
		public DateBoxWithCalendar getMeasurementToPeriodInputBox();
		
		/**
		 * Gets the measure type.
		 * 
		 * @return the measure type
		 */
		public String getMeasureType();
		
		/**
		 * Gets the measure steward.
		 * 
		 * @return the measure steward
		 */
		public ListBoxMVP getMeasureSteward();

		//US 413. Introduced Measure Steward Other option.
		/**
		 * Gets the measure steward list box.
		 * 
		 * @return the measure steward list box
		 */
		public HasValue<String> getMeasureStewardListBox();		
		
		/**
		 * Gets the measure steward value.
		 * 
		 * @return the measure steward value
		 */
		public String getMeasureStewardValue();
		
		/**
		 * Gets the measure steward other.
		 * 
		 * @return the measure steward other
		 */
		public TextBox getMeasureStewardOther();
		
		/**
		 * Gets the measure steward other value.
		 * 
		 * @return the measure steward other value
		 */
		public String getMeasureStewardOtherValue();
		
		/**
		 * Gets the endorseby nqf.
		 * 
		 * @return the endorseby nqf
		 */
		public HasValue<Boolean> getEndorsebyNQF();
		
		/**
		 * Gets the not endorseby nqf.
		 * 
		 * @return the not endorseby nqf
		 */
		public HasValue<Boolean> getNotEndorsebyNQF();
		
		/**
		 * Gets the measure status.
		 * 
		 * @return the measure status
		 */
		public ListBoxMVP getMeasureStatus();
		
		/**
		 * Gets the measure status value.
		 * 
		 * @return the measure status value
		 */
		public String getMeasureStatusValue();
		
		/**
		 * Gets the author.
		 * 
		 * @return the author
		 */
		public String getAuthor();
		
		/**
		 * Sets the authors list.
		 * 
		 * @param author
		 *            the new authors list
		 */
		public void setAuthorsList(List<Author> author);
		
		/**
		 * Sets the measure type list.
		 * 
		 * @param measureType
		 *            the new measure type list
		 */
		public void setMeasureTypeList(List<MeasureType> measureType);
		
		/**
		 * Gets the description.
		 * 
		 * @return the description
		 */
		public HasValue<String> getDescription();
		
		/**
		 * Gets the copyright.
		 * 
		 * @return the copyright
		 */
		public HasValue<String> getCopyright();
		
		/**
		 * Gets the clinical recommendation.
		 * 
		 * @return the clinical recommendation
		 */
		public HasValue<String> getClinicalRecommendation();
		
		/**
		 * Gets the definitions.
		 * 
		 * @return the definitions
		 */
		public HasValue<String> getDefinitions();
		
		/**
		 * Gets the guidance.
		 * 
		 * @return the guidance
		 */
		public HasValue<String> getGuidance();
		
		/**
		 * Gets the transmission format.
		 * 
		 * @return the transmission format
		 */
		public HasValue<String> getTransmissionFormat();
		
		/**
		 * Gets the rationale.
		 * 
		 * @return the rationale
		 */
		public HasValue<String> getRationale();
		
		/**
		 * Gets the improvement notation.
		 * 
		 * @return the improvement notation
		 */
		public HasValue<String> getImprovementNotation();
		
		/**
		 * Gets the stratification.
		 * 
		 * @return the stratification
		 */
		public HasValue<String> getStratification();
		
		/**
		 * Gets the risk adjustment.
		 * 
		 * @return the risk adjustment
		 */
		public HasValue<String> getRiskAdjustment();
		
		/**
		 * Gets the reference.
		 * 
		 * @return the reference
		 */
		public HasValue<String> getReference();
		
		/**
		 * Gets the supplemental data.
		 * 
		 * @return the supplemental data
		 */
		public HasValue<String> getSupplementalData();
		
		/**
		 * Gets the disclaimer.
		 * 
		 * @return the disclaimer
		 */
		public HasValue<String> getDisclaimer();
		
		/**
		 * Gets the initial patient pop.
		 * 
		 * @return the initial patient pop
		 */
		public HasValue<String> getInitialPatientPop();
		
		/**
		 * Gets the denominator.
		 * 
		 * @return the denominator
		 */
		public HasValue<String> getDenominator();
		
		/**
		 * Gets the denominator exclusions.
		 * 
		 * @return the denominator exclusions
		 */
		public HasValue<String> getDenominatorExclusions();
		
		/**
		 * Gets the numerator.
		 * 
		 * @return the numerator
		 */
		public HasValue<String> getNumerator();
		
		/**
		 * Gets the numerator exclusions.
		 * 
		 * @return the numerator exclusions
		 */
		public HasValue<String> getNumeratorExclusions();
		
		/**
		 * Gets the denominator exceptions.
		 * 
		 * @return the denominator exceptions
		 */
		public HasValue<String> getDenominatorExceptions();
		
		/**
		 * Gets the measure population.
		 * 
		 * @return the measure population
		 */
		public HasValue<String> getMeasurePopulation();
		
		/**
		 * Gets the measure observations.
		 * 
		 * @return the measure observations
		 */
		public HasValue<String> getMeasureObservations();
		
		/**
		 * Gets the rate aggregation.
		 * 
		 * @return the rate aggregation
		 */
		public HasValue<String> getRateAggregation();
		
		/**
		 * Gets the emeasure id.
		 * 
		 * @return the emeasure id
		 */
		public HasValue<String> getEmeasureId();
		
		/**
		 * Gets the generate emeasure id button.
		 * 
		 * @return the generate emeasure id button
		 */
		public HasClickHandlers getGenerateEmeasureIdButton();
		
		/**
		 * Sets the generate emeasure id button enabled.
		 * 
		 * @param b
		 *            the new generate emeasure id button enabled
		 */
		public void setGenerateEmeasureIdButtonEnabled(boolean b);
		
		/**
		 * Gets the reference values.
		 * 
		 * @return the reference values
		 */
		public List<String> getReferenceValues();
		
		/**
		 * Sets the reference values.
		 * 
		 * @param values
		 *            the values
		 * @param editable
		 *            the editable
		 */
		public void setReferenceValues(List<String> values, boolean editable);
		
		/**
		 * Sets the adds the edit buttons visible.
		 * 
		 * @param b
		 *            the new adds the edit buttons visible
		 */
		public void setAddEditButtonsVisible(boolean b);
		
		/**
		 * Enable endorse by radio buttons.
		 * 
		 * @param b
		 *            the b
		 */
		public void enableEndorseByRadioButtons(boolean b);
		
		/**
		 * Sets the save button enabled.
		 * 
		 * @param b
		 *            the new save button enabled
		 */
		public void setSaveButtonEnabled(boolean b);
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		//US 413. Interfaces to show or clear out Steward Other text boxes.
		/**
		 * Show other text box.
		 */
		public void showOtherTextBox();
		
		/**
		 * Hide other text box.
		 */
		public void hideOtherTextBox();
		
		/**
		 * Gets the save btn.
		 * 
		 * @return the save btn
		 */
		public Button getSaveBtn();
		
		/**
		 * Gets the delete measure.
		 * 
		 * @return the delete measure
		 */
		public Button getDeleteMeasure();	
		
	}
	
	/**
	 * The Interface AddEditAuthorsDisplay.
	 */
	public static interface AddEditAuthorsDisplay extends BaseAddEditDisplay<Author> {
		
		/**
		 * Gets the author.
		 * 
		 * @return the author
		 */
		public String getAuthor();
		
		/**
		 * Gets the author input box.
		 * 
		 * @return the author input box
		 */
		public HasValue<String> getAuthorInputBox();
		
		/**
		 * Gets the other author.
		 * 
		 * @return the other author
		 */
		public HasValue<String> getOtherAuthor();
	}
	
	/**
	 * The Interface AddEditMeasureTypeDisplay.
	 */
	public static interface AddEditMeasureTypeDisplay extends BaseAddEditDisplay<MeasureType>{
		
		/**
		 * Gets the measure type.
		 * 
		 * @return the measure type
		 */
		public String getMeasureType();
		
		/**
		 * Gets the measure type input box.
		 * 
		 * @return the measure type input box
		 */
		public HasValue<String> getMeasureTypeInputBox();
		
		/**
		 * Gets the other measure type.
		 * 
		 * @return the other measure type
		 */
		public HasValue<String> getOtherMeasureType();
	}
	
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The meta data display. */
	private MetaDataDetailDisplay metaDataDisplay;
	
	/** The add edit authors display. */
	private AddEditAuthorsDisplay addEditAuthorsDisplay;
	
	/** The add edit measure type display. */
	private AddEditMeasureTypeDisplay addEditMeasureTypeDisplay;
	
	/** The current measure detail. */
	private ManageMeasureDetailModel currentMeasureDetail;
	
	/** The current authors list. */
	private ManageAuthorsModel currentAuthorsList;
	
	/** The current measure type list. */
	private ManageMeasureTypeModel currentMeasureTypeList;
	
	/** The author list. */
	private List<Author> authorList = new ArrayList<Author>();
	
	/** The measure type list. */
	private List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
	
	/** The db author list. */
	private List<Author> dbAuthorList = new ArrayList<Author>();
	
	/** The db measure type list. */
	private List<MeasureType> dbMeasureTypeList = new ArrayList<MeasureType>();
	
	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The previous continue buttons. */
	private HasVisible previousContinueButtons;
	
	/** The last request time. */
	private long lastRequestTime;
	
	/** The max emeasure id. */
	private int maxEmeasureId;
	
	/** The editable. */
	private boolean editable = false;
	
	/** The is sub view. */
	private boolean isSubView = false;
	
	/** The measure xml model. */
	private MeasureXmlModel measureXmlModel;// will hold the measure xml. 02/2013
	
	/** The is measure details loaded. */
	private boolean isMeasureDetailsLoaded = false;
	
	
	
	/**
	 * Instantiates a new meta data presenter.
	 * 
	 * @param mDisplay
	 *            the m display
	 * @param aDisplay
	 *            the a display
	 * @param mtDisplay
	 *            the mt display
	 * @param pcButtons
	 *            the pc buttons
	 * @param lp
	 *            the lp
	 */
	public MetaDataPresenter(MetaDataDetailDisplay mDisplay,AddEditAuthorsDisplay aDisplay,AddEditMeasureTypeDisplay mtDisplay,HasVisible pcButtons,ListBoxCodeProvider lp){
		super(mDisplay,aDisplay,mtDisplay,lp);
		previousContinueButtons = pcButtons;
		this.metaDataDisplay = mDisplay;
		this.addEditAuthorsDisplay = aDisplay;
		this.addEditMeasureTypeDisplay = mtDisplay;
		HandlerManager eventBus = MatContext.get().getEventBus();
		metaDataDisplay.getEditAuthorsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getMetaDataDisplay().getSaveErrorMsg().clear();
				displayAddEditAuthors();
			}
		});
		
		metaDataDisplay.getEditMeasureTypeButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getMetaDataDisplay().getSaveErrorMsg().clear();
				displayAddEditMeasureType();
			}
		});
		metaDataDisplay.getDeleteMeasure().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				DeleteMeasureConfirmationBox.showDeletionConfimationDialog();
				DeleteMeasureConfirmationBox.getConfirm().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(final ClickEvent event) {

						checkPasswordForMeasureDeletion(DeleteMeasureConfirmationBox.getPasswordEntered());
						DeleteMeasureConfirmationBox.getDialogBox().hide();
					}
				});
			}
		});
		addEditAuthorsDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addEditAuthorsDisplay.getAuthorInputBox().setValue("");
				addEditAuthorsDisplay.hideTextBox();
			}
		});
		addEditAuthorsDisplay.getReturnButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				isSubView = false;
				backToDetail();
			}
		});

		addEditAuthorsDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (addEditAuthorsDisplay.getAuthor().equals(MatContext.PLEASE_SELECT)) {
					//do nothing
				} else if (!addEditAuthorsDisplay.getAuthor().startsWith("Other")) {
					if (!addEditAuthorsDisplay.getAuthor().equals("")) {
						addToAuthorsList(addEditAuthorsDisplay.getAuthor());
				    	addEditAuthorsDisplay.getAuthorInputBox().setValue("");
					}
				} else {
					  if (!addEditAuthorsDisplay.getOtherAuthor().getValue().equals("")) {
						  addToAuthorsList(addEditAuthorsDisplay.getOtherAuthor().getValue());
						  addEditAuthorsDisplay.getOtherAuthor().setValue("");
					  }
				}
			}
		});
		addEditAuthorsDisplay.getRemoveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(final ClickEvent event) {
			     removeSelectedAuthor();
			}
		});
		addEditMeasureTypeDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addEditMeasureTypeDisplay.getMeasureTypeInputBox().setValue("");
				addEditMeasureTypeDisplay.hideTextBox();
			}
		});
		addEditMeasureTypeDisplay.getReturnButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				backToDetail();
			}
		});

		addEditMeasureTypeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (addEditMeasureTypeDisplay.getMeasureType().equals(
						MatContext.PLEASE_SELECT)) {
					//do nothing
				} else if(!(addEditMeasureTypeDisplay.getMeasureType().startsWith("Other"))) {
					if(!addEditMeasureTypeDisplay.getMeasureType().equals("")){
						addToMeasureTypeList(addEditMeasureTypeDisplay.getMeasureType());
						addEditMeasureTypeDisplay.getMeasureTypeInputBox().setValue("");
					}
				}else{
					if(!addEditMeasureTypeDisplay.getOtherMeasureType().getValue().equals("")){
						addToMeasureTypeList(addEditMeasureTypeDisplay.getOtherMeasureType().getValue());
						addEditMeasureTypeDisplay.getOtherMeasureType().setValue("");
					}
				}
					
			}
		});
		addEditMeasureTypeDisplay.getRemoveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				removeSelectedMeasureType();
				
			}
		});
		addEditAuthorsDisplay.getAuthorInputBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String authorValue = event.getValue();
				String changedAuthorValue = addEditAuthorsDisplay.getAuthor();
				if(changedAuthorValue.startsWith("Other")){
					addEditAuthorsDisplay.showTextBox();
					}
				else{
					addEditAuthorsDisplay.hideTextBox();
				}
				
			}
		});
		addEditMeasureTypeDisplay.getMeasureTypeInputBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
			    String measureType = addEditMeasureTypeDisplay.getMeasureType();
				if(measureType.startsWith("Other")){
					addEditMeasureTypeDisplay.showTextBox();
					}
				else{
					addEditMeasureTypeDisplay.hideTextBox();
				}
				
			}
		});
		
		//US 413. Added value change listener to show or clear out Steward Other text box based on the selection.
		metaDataDisplay.getMeasureStewardListBox().addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {				
					String value = metaDataDisplay.getMeasureStewardValue();
					if(value.startsWith("Other")){
						metaDataDisplay.showOtherTextBox();
						}
					else{
						metaDataDisplay.hideOtherTextBox();
					}
					
				}
			});
		
		metaDataDisplay.getSaveButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				saveMetaDataInformation(true);
			}
			
		});
		metaDataDisplay.getFocusPanel().addKeyDownHandler(new KeyDownHandler(){
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				//control-alt-s is save
				if(event.isAltKeyDown() && event.isControlKeyDown() && event.getNativeKeyCode()==83){
					saveMetaDataInformation(true);
				}
			}
		});
		//This event will be called when measure has been selected from measureLibrary
		eventBus.addHandler(MeasureSelectedEvent.TYPE, new MeasureSelectedEvent.Handler() {
			@Override
			public void onMeasureSelected(MeasureSelectedEvent event) {
				isMeasureDetailsLoaded = false;
				if(event.getMeasureId() != null) {
					isMeasureDetailsLoaded = true;
				    getMeasureDetail();
				}
				else {
					displayEmpty();
				}
			}
		});
		
		metaDataDisplay.getGenerateEmeasureIdButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				generateAndSaveNewEmeasureid();
			}
		});
		
		
		emptyWidget.add(new Label("No Measure Selected"));
	}
	
	/**
	 * Fire successfull deletion event.
	 * 
	 * @param isSuccess
	 *            the is success
	 * @param message
	 *            the message
	 */
	private void fireSuccessfullDeletionEvent(boolean isSuccess, String message){
		MeasureDeleteEvent deleteEvent = new MeasureDeleteEvent(isSuccess, message);
		MatContext.get().getEventBus().fireEvent(deleteEvent);
	}
	
	/**
	 * Fire back to measure library event.
	 */
	private void fireBackToMeasureLibraryEvent(){
		BackToMeasureLibraryPage backToMeasureLibraryPage = new BackToMeasureLibraryPage();
		MatContext.get().getEventBus().fireEvent(backToMeasureLibraryPage);
	}
			
	
	/**
	 * Generate and save new emeasureid.
	 */
	private void generateAndSaveNewEmeasureid(){
		MeasureServiceAsync service = MatContext.get().getMeasureService();
		service.generateAndSaveMaxEmeasureId(currentMeasureDetail, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				
			}

			@Override
			public void onSuccess(Integer result) {
				maxEmeasureId = result.intValue();
				if(maxEmeasureId < 1000000){
					metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
					metaDataDisplay.getEmeasureId().setValue(maxEmeasureId+"");
					((TextBox)metaDataDisplay.getEmeasureId()).setFocus(true);
				}
			}
			
		});
	}
	
/*
 * Commenting this method as generate EmeasureIdentifier has to save and return the saved value.	
 * private void pullCurrentEmeasureId(){
		MeasureServiceAsync service = MatContext.get().getMeasureService();
		service.getMaxEMeasureId(new AsyncCallback<Integer>(){
			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			@Override
			public void onSuccess(Integer result) {
				maxEmeasureId = result.intValue()+1;
				if(maxEmeasureId < 1000000){
					metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
					metaDataDisplay.getEmeasureId().setValue(maxEmeasureId+"");
					((TextBox)metaDataDisplay.getEmeasureId()).setFocus(true);
				}
			}
		});
	}
	*/
	
	/**
 * Sets the authors list on view.
 */
private void setAuthorsListOnView() {
		Collections.sort(currentMeasureDetail.getAuthorList(), new Author.Comparator());
		if(currentMeasureDetail.getAuthorList()!= null){
			currentAuthorsList = new ManageAuthorsModel(currentMeasureDetail.getAuthorList());
			currentAuthorsList.setPageSize(SearchView.PAGE_SIZE_ALL);
			addEditAuthorsDisplay.buildDataTable(currentAuthorsList);
			
		}
		
	}
	
	/**
	 * Sets the measure type on view.
	 */
	private void setMeasureTypeOnView(){
		Collections.sort(currentMeasureDetail.getMeasureTypeList(), new MeasureType.Comparator());
		if(currentMeasureDetail.getMeasureTypeList()!= null){
			currentMeasureTypeList = new ManageMeasureTypeModel(currentMeasureDetail.getMeasureTypeList());
			currentMeasureTypeList.setPageSize(SearchView.PAGE_SIZE_ALL);
			addEditMeasureTypeDisplay.buildDataTable(currentMeasureTypeList);
		}	    
	}
	
	/**
	 * Removes the selected measure type.
	 */
	protected void removeSelectedMeasureType() {
		List<MeasureType> selectedMt = currentMeasureTypeList.getSelectedMeasureType();
		for(MeasureType mt: selectedMt){
			currentMeasureDetail.getMeasureTypeList().remove(mt);
		}
		metaDataDisplay.setMeasureTypeList(currentMeasureDetail.getMeasureTypeList());
		setMeasureTypeOnView();
	}

	/**
	 * Removes the selected author.
	 */
	protected void removeSelectedAuthor() {
		List<Author> selectedAuthor = currentAuthorsList.getSelectedAuthor();
		for(Author a: selectedAuthor){
			currentMeasureDetail.getAuthorList().remove(a);
		}
		metaDataDisplay.setAuthorsList(currentMeasureDetail.getAuthorList());
		setAuthorsListOnView();
		
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	public Widget getWidget() {
		return panel;
	}
	
	/**
	 * Display empty.
	 */
	private void displayEmpty() {
		previousContinueButtons.setVisible(false);
		panel.clear();
		panel.add(emptyWidget);
	}
	
	/**
	 * Display detail.
	 */
	public void displayDetail(){
		previousContinueButtons.setVisible(true);
		prepopulateFields();
		panel.clear();
		if(editable){
			if("0".equals(metaDataDisplay.getEmeasureId().getValue())){
				metaDataDisplay.setGenerateEmeasureIdButtonEnabled(true);
				metaDataDisplay.getEmeasureId().setValue("");
			}else if(metaDataDisplay.getEmeasureId() != null){
				metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
			}
		}else{
			metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
			if("0".equals(metaDataDisplay.getEmeasureId().getValue())){
				metaDataDisplay.getEmeasureId().setValue("");
			}
		}
		
		panel.add(metaDataDisplay.asWidget());
	}
	
	/**
	 * Back to detail.
	 */
	public void backToDetail(){
		previousContinueButtons.setVisible(true);
		panel.clear();
		panel.add(metaDataDisplay.asWidget());
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Prepopulate fields.
	 */
	private void prepopulateFields() {
		metaDataDisplay.getNqfId().setValue(currentMeasureDetail.getNqfId());
		metaDataDisplay.geteMeasureIdentifier().setText(currentMeasureDetail.getMeasureSetId());
		metaDataDisplay.getSetName().setValue(currentMeasureDetail.getGroupName());
		metaDataDisplay.getMeasureName().setText(currentMeasureDetail.getName());
		metaDataDisplay.getShortName().setText(currentMeasureDetail.getShortName());
		metaDataDisplay.getMeasureScoring().setText(currentMeasureDetail.getMeasScoring());
		metaDataDisplay.getClinicalRecommendation().setValue(currentMeasureDetail.getClinicalRecomms());
		metaDataDisplay.getDefinitions().setValue(currentMeasureDetail.getDefinitions());
		metaDataDisplay.getDescription().setValue(currentMeasureDetail.getDescription());
		
		metaDataDisplay.getDisclaimer().setValue(currentMeasureDetail.getDisclaimer());
		metaDataDisplay.getRiskAdjustment().setValue(currentMeasureDetail.getRiskAdjustment());
		metaDataDisplay.getRateAggregation().setValue(currentMeasureDetail.getRateAggregation());
		metaDataDisplay.getInitialPatientPop().setValue(currentMeasureDetail.getInitialPatientPop());
		metaDataDisplay.getDenominator().setValue(currentMeasureDetail.getDenominator());
		metaDataDisplay.getDenominatorExclusions().setValue(currentMeasureDetail.getDenominatorExclusions());
		metaDataDisplay.getNumerator().setValue(currentMeasureDetail.getNumerator());
		metaDataDisplay.getNumeratorExclusions().setValue(currentMeasureDetail.getNumeratorExclusions());
		metaDataDisplay.getDenominatorExceptions().setValue(currentMeasureDetail.getDenominatorExceptions());
		metaDataDisplay.getMeasurePopulation().setValue(currentMeasureDetail.getMeasurePopulation());
		metaDataDisplay.getMeasureObservations().setValue(currentMeasureDetail.getMeasureObservations());

				
		metaDataDisplay.getCopyright().setValue(currentMeasureDetail.getCopyright());
		if(currentMeasureDetail.getEndorseByNQF()!= null && currentMeasureDetail.getEndorseByNQF().equals(true)){
			metaDataDisplay.getEndorsebyNQF().setValue(true);
		}else{
			metaDataDisplay.getNotEndorsebyNQF().setValue(true);
		}
		
		metaDataDisplay.getMeasureStatus().setValueMetadata(currentMeasureDetail.getMeasureStatus());
		metaDataDisplay.getGuidance().setValue(currentMeasureDetail.getGuidance());
		metaDataDisplay.getTransmissionFormat().setValue(currentMeasureDetail.getTransmissionFormat());
		metaDataDisplay.getImprovementNotation().setValue(currentMeasureDetail.getImprovNotations());
		metaDataDisplay.getSupplementalData().setValue(currentMeasureDetail.getSupplementalData());
		metaDataDisplay.getFinalizedDate().setText(currentMeasureDetail.getFinalizedDate());
		metaDataDisplay.getMeasurementFromPeriodInputBox().setValue(currentMeasureDetail.getMeasFromPeriod());
		metaDataDisplay.getMeasurementToPeriodInputBox().setValue(currentMeasureDetail.getMeasToPeriod());		
		metaDataDisplay.getVersionNumber().setText(currentMeasureDetail.getVersionNumber());
		
		//US 413. Populate Steward and Steward Other value if any.
		String steward = currentMeasureDetail.getMeasSteward();
		metaDataDisplay.getMeasureSteward().setValueMetadata(currentMeasureDetail.getMeasSteward());
		if(metaDataDisplay.getMeasureSteward().getSelectedIndex() == 0 && steward != null && !steward.equals("")){
			steward = "Other";
			currentMeasureDetail.setMeasStewardOther(currentMeasureDetail.getMeasSteward());
			metaDataDisplay.getMeasureSteward().setValueMetadata(steward);
		}
		boolean setSteward = steward != null && steward.equalsIgnoreCase("Other"); 
		if(setSteward){
			metaDataDisplay.showOtherTextBox();			
			metaDataDisplay.getMeasureStewardOther().setValue(currentMeasureDetail.getMeasStewardOther());
			
		}else{
			metaDataDisplay.hideOtherTextBox();
		}
		
		metaDataDisplay.getRationale().setValue(currentMeasureDetail.getRationale());
		metaDataDisplay.getStratification().setValue(currentMeasureDetail.getStratification());
		metaDataDisplay.getRiskAdjustment().setValue(currentMeasureDetail.getRiskAdjustment());
		if(currentMeasureDetail.getAuthorList() != null){
			metaDataDisplay.setAuthorsList(currentMeasureDetail.getAuthorList());
		}else{
			List<Author> authorList = new ArrayList<Author>();
			metaDataDisplay.setAuthorsList(authorList);
			currentMeasureDetail.setAuthorList(authorList);
		}
		dbAuthorList.clear();
		dbAuthorList.addAll(currentMeasureDetail.getAuthorList());
		authorList = currentMeasureDetail.getAuthorList();
		if(currentMeasureDetail.getMeasureTypeList()!= null){
			metaDataDisplay.setMeasureTypeList(currentMeasureDetail.getMeasureTypeList());
		}else{
			List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
			metaDataDisplay.setMeasureTypeList(measureTypeList);
			currentMeasureDetail.setMeasureTypeList(measureTypeList);
		}
		dbMeasureTypeList.clear();
		dbMeasureTypeList.addAll(currentMeasureDetail.getMeasureTypeList());
		measureTypeList = currentMeasureDetail.getMeasureTypeList();
		editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		if(currentMeasureDetail.getReferencesList()!= null){
			metaDataDisplay.setReferenceValues(currentMeasureDetail.getReferencesList(), editable);
		}else{
			metaDataDisplay.setReferenceValues(new ArrayList<String>(), editable);
		}
		metaDataDisplay.setAddEditButtonsVisible(editable);
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(metaDataDisplay.asWidget(),editable);
		metaDataDisplay.enableEndorseByRadioButtons(editable);
		metaDataDisplay.setSaveButtonEnabled(editable);
		metaDataDisplay.getEmeasureId().setValue(currentMeasureDetail.geteMeasureId()+"");
		
		if(currentMeasureDetail.getMeasureOwnerId()!= null && !currentMeasureDetail.getMeasureOwnerId().equalsIgnoreCase(MatContext.get().getLoggedinUserId())){
			metaDataDisplay.getDeleteMeasure().setEnabled(false);
		}else{
			metaDataDisplay.getDeleteMeasure().setEnabled(true);
		}
		
	}

	/**
	 * Save meta data information.
	 * 
	 * @param dispSuccessMsg
	 *            the disp success msg
	 */
	public void saveMetaDataInformation(final boolean dispSuccessMsg){
		metaDataDisplay.getSaveErrorMsg().clear();
		metaDataDisplay.getErrorMessageDisplay().clear();
		metaDataDisplay.getSuccessMessageDisplay().clear();
		metaDataDisplay.getSaveBtn().setFocus(true);
		updateModelDetailsFromView();
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			Mat.showLoadingMessage();
			MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(true);
			MatContext.get().getMeasureService().saveMeasureDetails(currentMeasureDetail, new AsyncCallback<SaveMeasureResult>() {
				
				@Override
				public void onSuccess(SaveMeasureResult result) {
					
					if(result.isSuccess()) {
						Mat.hideLoadingMessage();
						if(dispSuccessMsg){
							metaDataDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getChangesSavedMessage());
						}
						
						MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(false);
						MatContext.get().getMeasureService().getMeasure(MatContext.get().getCurrentMeasureId(), 
								new AsyncCallback<ManageMeasureDetailModel>(){

									@Override
									public void onFailure(Throwable caught) {
										
									}

									@Override
									public void onSuccess(ManageMeasureDetailModel result) {
										currentMeasureDetail = result;
										displayDetail();
										
									}
							
						});
					}
					else{
						Mat.hideLoadingMessage();
						MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(false);
						metaDataDisplay.getErrorMessageDisplay().setMessage(MessageDelegate.getMeasureSaveServerErrorMessage( result.getFailureReason()));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Mat.hideLoadingMessage();
					MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(false);
					metaDataDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
			});
		}
	}
	
	/**
	 * Update model details from view.
	 */
	private void updateModelDetailsFromView(){
		updateModelDetailsFromView(currentMeasureDetail, metaDataDisplay);
	}
	
	/**
	 * Update model details from view.
	 * 
	 * @param currentMeasureDetail
	 *            the current measure detail
	 * @param metaDataDisplay
	 *            the meta data display
	 */
	public void updateModelDetailsFromView(ManageMeasureDetailModel currentMeasureDetail, MetaDataDetailDisplay metaDataDisplay){
		currentMeasureDetail.setName(metaDataDisplay.getMeasureName().getText());
		currentMeasureDetail.setShortName(metaDataDisplay.getShortName().getText());
		currentMeasureDetail.setFinalizedDate(metaDataDisplay.getFinalizedDate().getText());
		currentMeasureDetail.setClinicalRecomms(metaDataDisplay.getClinicalRecommendation().getValue());
		currentMeasureDetail.setDefinitions(metaDataDisplay.getDefinitions().getValue());
		currentMeasureDetail.setDescription(metaDataDisplay.getDescription().getValue());
		currentMeasureDetail.setDisclaimer (metaDataDisplay.getDisclaimer().getValue());
		currentMeasureDetail.setRiskAdjustment(metaDataDisplay.getRiskAdjustment().getValue());
		currentMeasureDetail.setRateAggregation(metaDataDisplay.getRateAggregation().getValue());
		currentMeasureDetail.setInitialPatientPop(metaDataDisplay.getInitialPatientPop().getValue());
		currentMeasureDetail.setDenominator(metaDataDisplay.getDenominator().getValue());
		currentMeasureDetail.setDenominatorExclusions(metaDataDisplay.getDenominatorExclusions().getValue());
		currentMeasureDetail.setNumerator(metaDataDisplay.getNumerator().getValue());
		currentMeasureDetail.setNumeratorExclusions(metaDataDisplay.getNumeratorExclusions().getValue());
		currentMeasureDetail.setDenominatorExceptions(metaDataDisplay.getDenominatorExceptions().getValue());
		currentMeasureDetail.setMeasurePopulation(metaDataDisplay.getMeasurePopulation().getValue());
		currentMeasureDetail.setMeasureObservations(metaDataDisplay.getMeasureObservations().getValue());

		currentMeasureDetail.setCopyright(metaDataDisplay.getCopyright().getValue());
		currentMeasureDetail.setEndorseByNQF(metaDataDisplay.getEndorsebyNQF().getValue());
		currentMeasureDetail.setGuidance(metaDataDisplay.getGuidance().getValue());
		currentMeasureDetail.setTransmissionFormat(metaDataDisplay.getTransmissionFormat().getValue());
		currentMeasureDetail.setImprovNotations(metaDataDisplay.getImprovementNotation().getValue());
		currentMeasureDetail.setMeasFromPeriod(metaDataDisplay.getMeasurementFromPeriod());

		//US 413. Update Steward and Steward Other values from the UI. 		
		String stewardValue = metaDataDisplay.getMeasureStewardValue();
		if(nullCheck(stewardValue)){
			currentMeasureDetail.setMeasSteward(stewardValue);			
		}else{
			currentMeasureDetail.setMeasSteward(null);
		}
		currentMeasureDetail.setMeasStewardOther(metaDataDisplay.getMeasureStewardOtherValue());		
		currentMeasureDetail.setMeasToPeriod(metaDataDisplay.getMeasurementToPeriod());
		currentMeasureDetail.setSupplementalData(metaDataDisplay.getSupplementalData().getValue());
		if(nullCheck(metaDataDisplay.getMeasureStatusValue())){
			currentMeasureDetail.setMeasureStatus(metaDataDisplay.getMeasureStatusValue());}
		currentMeasureDetail.setRationale(metaDataDisplay.getRationale().getValue());
		currentMeasureDetail.setReferencesList(metaDataDisplay.getReferenceValues());
		currentMeasureDetail.setMeasureSetId(metaDataDisplay.geteMeasureIdentifier().getText());
		currentMeasureDetail.setGroupName(metaDataDisplay.getSetName().getValue());
		currentMeasureDetail.setStratification(metaDataDisplay.getStratification().getValue());
		currentMeasureDetail.setRiskAdjustment(metaDataDisplay.getRiskAdjustment().getValue());
		currentMeasureDetail.setVersionNumber(metaDataDisplay.getVersionNumber().getText());
		currentMeasureDetail.setAuthorList(authorList);
		currentMeasureDetail.setMeasureTypeList(measureTypeList);
		currentMeasureDetail.setToCompareAuthor(dbAuthorList);
		currentMeasureDetail.setToCompareMeasure(dbMeasureTypeList);
		currentMeasureDetail.setNqfId(metaDataDisplay.getNqfId().getValue());
		if(metaDataDisplay.getEmeasureId().getValue() != null && !metaDataDisplay.getEmeasureId().getValue().equals("")){
			currentMeasureDetail.seteMeasureId(new Integer(metaDataDisplay.getEmeasureId().getValue()));
		}
	}
	
	
	
	/**
	 * Null check.
	 * 
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	private boolean nullCheck(String value){
		return  !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
	
	
	/**
	 * Display add edit authors.
	 */
	private void displayAddEditAuthors(){
		isSubView = true;
		addEditAuthorsDisplay.setReturnToLink("Return to Previous");
		currentAuthorsList = new ManageAuthorsModel(currentMeasureDetail.getAuthorList());
		currentAuthorsList.setPageSize(SearchView.PAGE_SIZE_ALL);
		addEditAuthorsDisplay.buildDataTable(currentAuthorsList);
		panel.clear();		
		panel.add(addEditAuthorsDisplay.asWidget());
		previousContinueButtons.setVisible(false);
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Display add edit measure type.
	 */
	private void displayAddEditMeasureType(){
		isSubView = true;
		addEditMeasureTypeDisplay.setReturnToLink("Return to Previous");
		currentMeasureTypeList = new ManageMeasureTypeModel(currentMeasureDetail.getMeasureTypeList());
		currentMeasureTypeList.setPageSize(SearchView.PAGE_SIZE_ALL);
		
		addEditMeasureTypeDisplay.buildDataTable(currentMeasureTypeList);
		panel.clear();
		panel.add(addEditMeasureTypeDisplay.asWidget());
		previousContinueButtons.setVisible(false);	
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Adds the to authors list.
	 * 
	 * @param selectedAuthor
	 *            the selected author
	 */
	private void addToAuthorsList(String selectedAuthor){
		Author author = new Author();
		author.setAuthorName(selectedAuthor);
		authorList.add(author);
		currentMeasureDetail.setAuthorList(authorList);
		metaDataDisplay.setAuthorsList(currentMeasureDetail.getAuthorList());
		setAuthorsListOnView();
		
	}
	
	/**
	 * Adds the to measure type list.
	 * 
	 * @param selectedMeasureType
	 *            the selected measure type
	 */
	private void addToMeasureTypeList(String selectedMeasureType){
		MeasureType mt = new MeasureType();
		mt.setDescription(selectedMeasureType);
		measureTypeList.add(mt);
		currentMeasureDetail.setMeasureTypeList(measureTypeList);
		metaDataDisplay.setMeasureTypeList(currentMeasureDetail.getMeasureTypeList());
		setMeasureTypeOnView();
	}


	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		if(MatContext.get().getCurrentMeasureId() == null ||
				MatContext.get().getCurrentMeasureId().equals("")) {
			displayEmpty();
		}
		else {
			if(!isMeasureDetailsLoaded){// this check is made so that when measure is clicked from Measure library, its not called twice.
				currentMeasureDetail = null;
				lastRequestTime = System.currentTimeMillis();
				getMeasureDetail();
			}else{
				isMeasureDetailsLoaded = false;
			}
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("MetaData");
		Mat.focusSkipLists("MeasureComposer");
		clearMessages();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override 
	public void beforeClosingDisplay() {
		/*if(currentMeasureDetail != null) {// Removed Auto Save
			saveMetaDataInformation(false);
		}*/
		//This is done to reset measure composure tab to show "No Measure Selected" as when measure is deleted,it should not show Any sub tabs under MeasureComposure. 
		//MatContext.get().getCurrentMeasureInfo().setMeasureId("");
		clearMessages();
	}
	

	/**
	 * Gets the measure detail.
	 * 
	 * @return the measure detail
	 */
	private void getMeasureDetail(){
		MatContext.get().getMeasureService().getMeasure(MatContext.get().getCurrentMeasureId(), 
				new AsyncCallback<ManageMeasureDetailModel>(){
			final long callbackRequestTime = lastRequestTime;
			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				if(callbackRequestTime == lastRequestTime) {
					currentMeasureDetail = result;
//					loadMeasureXml(result.getId());
					displayDetail();
					fireMeasureEditEvent();
				}
			}
		});
	}
	
	/**
	 * Check password for measure deletion.
	 * 
	 * @param password
	 *            the password
	 */
	private void checkPasswordForMeasureDeletion(String password){
		
		MatContext.get().getLoginService().isValidPassword(MatContext.get().getLoggedinLoginId(), password, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				fireBackToMeasureLibraryEvent();
				fireSuccessfullDeletionEvent(false,null);
			}

			@Override
			public void onSuccess(Boolean result) {
				if(result){
					deleteMeasure();
				}else{
					fireBackToMeasureLibraryEvent();
					fireSuccessfullDeletionEvent(false,MatContext.get().getMessageDelegate().getMeasureDeletionInvalidPwd());
				}
				
			}
		});
	}
	
	
	/**
	 * Delete measure.
	 */
	private void deleteMeasure(){
		MatContext.get().getMeasureService().saveAndDeleteMeasure(MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				fireBackToMeasureLibraryEvent();
				
				fireSuccessfullDeletionEvent(false,null);
			}

			@Override
			public void onSuccess(Void result) {
				MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(), null, "MEASURE_DELETE_EVENT", "Measure Successfully Deleted", ConstantMessages.DB_LOG);
				// this is set to avoid showing dirty check message if user has modified Measure details and is deleting without saving.
				currentMeasureDetail.setDeleted(true);
				MatContext.get().setMeasureDeleted(true);
				fireBackToMeasureLibraryEvent();
				fireSuccessfullDeletionEvent(true,MatContext.get().getMessageDelegate().getMeasureDeletionSuccessMgs());
				
			}
			
		});
		
		
	}
	
	/**
	 * Added on FEB 2013 Method loads the MeasureXml from Measure_XML table
	 * using the Measure ID. Sets the measureXmlModel field.
	 * 
	 * @param id
	 *            the id
	 */
	private void loadMeasureXml(String id) {
		MatContext.get().getMeasureService().getMeasureXmlForMeasure(id, new AsyncCallback<MeasureXmlModel>(){

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(MeasureXmlModel result) {
				setMeasureXmlModel(result);
			}
		});
	}
	
	/**
	 * Fire measure edit event.
	 */
	private void fireMeasureEditEvent() {
		MeasureEditEvent evt = new MeasureEditEvent();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	/**
	 * Clear messages.
	 */
	private void  clearMessages(){
		metaDataDisplay.getErrorMessageDisplay().clear();
		metaDataDisplay.getSuccessMessageDisplay().clear();
	}

	/**
	 * Gets the meta data display.
	 * 
	 * @return the metaDataDisplay
	 */
	public MetaDataDetailDisplay getMetaDataDisplay() {
		return metaDataDisplay;
	}

	/**
	 * Sets the meta data display.
	 * 
	 * @param metaDataDisplay
	 *            the metaDataDisplay to set
	 */
	public void setMetaDataDisplay(MetaDataDetailDisplay metaDataDisplay) {
		this.metaDataDisplay = metaDataDisplay;
	}

	/**
	 * Gets the current measure detail.
	 * 
	 * @return the currentMeasureDetail
	 */
	public ManageMeasureDetailModel getCurrentMeasureDetail() {
		return currentMeasureDetail;
	}

	/**
	 * Sets the current measure detail.
	 * 
	 * @param currentMeasureDetail
	 *            the currentMeasureDetail to set
	 */
	public void setCurrentMeasureDetail(
			ManageMeasureDetailModel currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
	}

	/**
	 * Gets the current authors list.
	 * 
	 * @return the currentAuthorsList
	 */
	public ManageAuthorsModel getCurrentAuthorsList() {
		return currentAuthorsList;
	}

	/**
	 * Sets the current authors list.
	 * 
	 * @param currentAuthorsList
	 *            the currentAuthorsList to set
	 */
	public void setCurrentAuthorsList(ManageAuthorsModel currentAuthorsList) {
		this.currentAuthorsList = currentAuthorsList;
	}

	/**
	 * Gets the author list.
	 * 
	 * @return the authorList
	 */
	public List<Author> getAuthorList() {
		return authorList;
	}

	/**
	 * Sets the author list.
	 * 
	 * @param authorList
	 *            the authorList to set
	 */
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}

	/**
	 * Gets the db author list.
	 * 
	 * @return the dbAuthorList
	 */
	public List<Author> getDbAuthorList() {
		return dbAuthorList;
	}

	/**
	 * Sets the db author list.
	 * 
	 * @param dbAuthorList
	 *            the dbAuthorList to set
	 */
	public void setDbAuthorList(List<Author> dbAuthorList) {
		this.dbAuthorList = dbAuthorList;
	}

	/**
	 * Gets the db measure type list.
	 * 
	 * @return the dbMeasureTypeList
	 */
	public List<MeasureType> getDbMeasureTypeList() {
		return dbMeasureTypeList;
	}

	/**
	 * Sets the db measure type list.
	 * 
	 * @param dbMeasureTypeList
	 *            the dbMeasureTypeList to set
	 */
	public void setDbMeasureTypeList(List<MeasureType> dbMeasureTypeList) {
		this.dbMeasureTypeList = dbMeasureTypeList;
	}
	
	/**
	 * Sets the focus for save.
	 */
	public void setFocusForSave(){
		getMetaDataDisplay().getSaveBtn().setFocus(true);
	}

	/**
	 * Gets the adds the edit authors display.
	 * 
	 * @return the addEditAuthorsDisplay
	 */
	public AddEditAuthorsDisplay getAddEditAuthorsDisplay() {
		return addEditAuthorsDisplay;
	}

	/**
	 * Sets the adds the edit authors display.
	 * 
	 * @param addEditAuthorsDisplay
	 *            the addEditAuthorsDisplay to set
	 */
	public void setAddEditAuthorsDisplay(AddEditAuthorsDisplay addEditAuthorsDisplay) {
		this.addEditAuthorsDisplay = addEditAuthorsDisplay;
	}

	/**
	 * Gets the adds the edit measure type display.
	 * 
	 * @return the addEditMeasureTypeDisplay
	 */
	public AddEditMeasureTypeDisplay getAddEditMeasureTypeDisplay() {
		return addEditMeasureTypeDisplay;
	}

	/**
	 * Sets the adds the edit measure type display.
	 * 
	 * @param addEditMeasureTypeDisplay
	 *            the addEditMeasureTypeDisplay to set
	 */
	public void setAddEditMeasureTypeDisplay(
			AddEditMeasureTypeDisplay addEditMeasureTypeDisplay) {
		this.addEditMeasureTypeDisplay = addEditMeasureTypeDisplay;
	}

	/**
	 * Checks if is sub view.
	 * 
	 * @return the isSubView
	 */
	public boolean isSubView() {
		return isSubView;
	}

	/**
	 * Sets the sub view.
	 * 
	 * @param isSubView
	 *            the isSubView to set
	 */
	public void setSubView(boolean isSubView) {
		this.isSubView = isSubView;
	}

	/**
	 * Gets the measure xml model.
	 * 
	 * @return the measureXmlModel
	 */
	public MeasureXmlModel getMeasureXmlModel() {
		return measureXmlModel;
	}

	/**
	 * Sets the measure xml model.
	 * 
	 * @param measureXmlModel
	 *            the measureXmlModel to set
	 */
	public void setMeasureXmlModel(MeasureXmlModel measureXmlModel) {
		this.measureXmlModel = measureXmlModel;
	}

}
