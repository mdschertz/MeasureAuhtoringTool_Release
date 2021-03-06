package mat.server;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.server.service.MeasureLibraryService;

/**
 * The Class MeasureServiceImpl.
 */
public class MeasureServiceImpl extends SpringRemoteServiceServlet implements
		MeasureService {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2280421300224680146L;
	
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	private MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) this.context.getBean("measureLibraryService");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMeasure(java.lang.String)
	 */
	@Override
	public ManageMeasureDetailModel getMeasure(String key) {
		return this.getMeasureLibraryService().getMeasure(key);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#save(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult save(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().save(model);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveFinalizedVersion(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public SaveMeasureResult saveFinalizedVersion(String measureId,
			boolean isMajor, String version) {
		return this.getMeasureLibraryService().saveFinalizedVersion(measureId, isMajor, version);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureDetails(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveMeasureDetails(model);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#search(java.lang.String, int, int, int)
	 */
	@Override
	public ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter) {
		return this.getMeasureLibraryService().search(searchText, startIndex, pageSize, filter);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getUsersForShare(java.lang.String, int, int)
	 */
	@Override
	public ManageMeasureShareModel getUsersForShare(String measureId,
			int startIndex, int pageSize) {
		return this.getMeasureLibraryService().getUsersForShare(measureId, startIndex, pageSize);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateUsersShare(mat.client.measure.ManageMeasureShareModel)
	 */
	@Override
	public void updateUsersShare(ManageMeasureShareModel model) {
		this.getMeasureLibraryService().updateUsersShare(model);		
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#validateMeasureForExport(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public ValidateMeasureResult validateMeasureForExport(String key , ArrayList<MatValueSet> matValueSetList)
			throws MatException {
		return this.getMeasureLibraryService().validateMeasureForExport(key, matValueSetList);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateLockedDate(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveMeasureResult updateLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().updateLockedDate(measureId, userId);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#resetLockedDate(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveMeasureResult resetLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().resetLockedDate(measureId, userId);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#searchMeasuresForVersion(int, int)
	 */
	@Override
	public ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchMeasuresForVersion(startIndex, pageSize);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#searchMeasuresForDraft(int, int)
	 */
	@Override
	public ManageMeasureSearchModel searchMeasuresForDraft(int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchMeasuresForDraft(startIndex, pageSize);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#isMeasureLocked(java.lang.String)
	 */
	@Override
	public boolean isMeasureLocked(String id) {
		return this.getMeasureLibraryService().isMeasureLocked(id);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMaxEMeasureId()
	 */
	@Override
	public int getMaxEMeasureId() {
		return this.getMeasureLibraryService().getMaxEMeasureId();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#generateAndSaveMaxEmeasureId(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId) {
		return this.getMeasureLibraryService().generateAndSaveMaxEmeasureId(measureId);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#searchUsers(int, int)
	 */
	@Override
	public TransferMeasureOwnerShipModel searchUsers(int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchUsers(startIndex, pageSize);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#transferOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail) {
		this.getMeasureLibraryService().transferOwnerShipToUser(list, toEmail);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMeasureXmlForMeasure(java.lang.String)
	 */
	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
		return this.getMeasureLibraryService().getMeasureXmlForMeasure(measureId);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public void saveMeasureXml(MeasureXmlModel measureXmlModel) {
		this.getMeasureLibraryService().saveMeasureXml(measureXmlModel);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#cloneMeasureXml(boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId) {
		this.getMeasureLibraryService().cloneMeasureXml(creatingDraft, oldMeasureId, clonedMeasureId);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#appendAndSaveNode(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String)
	 */
	@Override
	public void appendAndSaveNode(MeasureXmlModel measureXmlModel,
			String nodeName) {
		this.getMeasureLibraryService().appendAndSaveNode(measureXmlModel, nodeName);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#createAndSaveElementLookUp(java.util.ArrayList, java.lang.String)
	 */
	@Override
	public void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID) {
		this.getMeasureLibraryService().createAndSaveElementLookUp(list, measureID);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveAndDeleteMeasure(java.lang.String)
	 */
	@Override
	public void saveAndDeleteMeasure(String measureID) {
		this.getMeasureLibraryService().saveAndDeleteMeasure(measureID);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updatePrivateColumnInMeasure(java.lang.String, boolean)
	 */
	@Override
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
		this.getMeasureLibraryService().updatePrivateColumnInMeasure(measureId, isPrivate);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateMeasureXML(mat.model.QualityDataSetDTO, mat.model.QualityDataSetDTO, java.lang.String)
	 */
	@Override
	public void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId) {
		this.getMeasureLibraryService().updateMeasureXML(modifyWithDTO, modifyDTO, measureId);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureNote(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2) {
		this.getMeasureLibraryService().saveMeasureNote(noteTitle, noteDescription, string, string2);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getAllMeasureNotesByMeasureID(java.lang.String)
	 */
	@Override
	public MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID) {
		return this.getMeasureLibraryService().getAllMeasureNotesByMeasureID(measureID);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#deleteMeasureNotes(mat.DTO.MeasureNoteDTO)
	 */
	@Override
	public void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO) {
		this.getMeasureLibraryService().deleteMeasureNotes(measureNoteDTO);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateMeasureNotes(mat.DTO.MeasureNoteDTO, java.lang.String)
	 */
	@Override
	public void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId) {
		this.getMeasureLibraryService().updateMeasureNotes(measureNoteDTO, userId);

	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getAppliedQDMFromMeasureXml(java.lang.String, boolean)
	 */
	@Override
	public ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(
			String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	}

}
