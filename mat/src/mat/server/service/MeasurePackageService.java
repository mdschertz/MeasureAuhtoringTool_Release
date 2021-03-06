package mat.server.service;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.model.DataType;
import mat.model.MatValueSet;
import mat.model.QualityDataSet;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
/**MeasurePackageService.java.**/
public interface MeasurePackageService {
	
	/**
	 * Count.
	 * 
	 * @return long *
	 */
	long count();
	
	/**
	 * Count.
	 * 
	 * @param searchText
	 *            - String.
	 * @return {@link Long#} *
	 */
	long count(String searchText);
	
	/**
	 * Search.
	 * 
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param numResults
	 *            - {@link Integer}.
	 * @return {@link List} of {@link MeasureShareDTO}. *
	 */
	List<MeasureShareDTO> search(int startIndex, int numResults);
	
	/**
	 * Search measures for version.
	 * 
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param numResults
	 *            - {@link Integer}.
	 * @return {@link List} of {@link MeasureShareDTO}. *
	 */
	List<MeasureShareDTO> searchMeasuresForVersion(int startIndex,
			int numResults);
	
	/**
	 * Count measures for version.
	 * 
	 * @return {@link Long}. *
	 */
	long countMeasuresForVersion();
	
	/**
	 * Count measures for draft.
	 * 
	 * @return {@link Long}. *
	 */
	long countMeasuresForDraft();
	
	/**
	 * Search measures for draft.
	 * 
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param numResults
	 *            - {@link Integer}.
	 * @return {@link List} {@link MeasureShareDTO}. *
	 */
	List<MeasureShareDTO> searchMeasuresForDraft(int startIndex,
			int numResults);
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            - {@link String}.
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param numResults
	 *            - {@link Integer}.
	 * @return {@link List} {@link MeasureShareDTO}. *
	 */
	List<MeasureShareDTO> search(String searchText, int startIndex,
			int numResults);
	
	/**
	 * Save.
	 * 
	 * @param measurePackage
	 *            the measure package
	 */
	void save(Measure measurePackage);
	
	/**
	 * Gets the by id.
	 * 
	 * @param id
	 *            - {@link String}.
	 * @return {@link Measure}. *
	 */
	Measure getById(String id);
	
	/**
	 * Find out maximum version number.
	 * 
	 * @param measureSetId
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	String findOutMaximumVersionNumber(String measureSetId);
	
	/**
	 * Find out version number.
	 * 
	 * @param measureId
	 *            - {@link String}.
	 * @param measureSetId
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	String findOutVersionNumber(String measureId, String measureSetId);
	
	/**
	 * Gets the users for share.
	 * 
	 * @param measureId
	 *            - {@link String}.
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param pageSize
	 *            - {@link Integer}.
	 * @return {@link List} {@link MeasureShareDTO}.
	 */
	List<MeasureShareDTO> getUsersForShare(String measureId,
			int startIndex, int pageSize);
	
	/**
	 * Count users for measure share.
	 * 
	 * @return {@link Integer}. *
	 */
	int countUsersForMeasureShare();
	
	/**
	 * Update users share.
	 * 
	 * @param model
	 *            - {@link ManageMeasureShareModel}. *
	 */
	void updateUsersShare(ManageMeasureShareModel model);
	
	/**
	 * Update locked out date.
	 * 
	 * @param m
	 *            - {@link Measure}. *
	 */
	void updateLockedOutDate(Measure m);
	
	/**
	 * Validate measure for export.
	 * 
	 * @param key
	 *            - {@link String}.
	 * @param matValueSetList
	 *            - {@link ArrayList} of {@link MatValueSet}.
	 * @return {@link ValidateMeasureResult}.
	 * @throws Exception
	 *             - {@link Exception}.
	 */
	ValidateMeasureResult validateMeasureForExport(String key,
			ArrayList<MatValueSet> matValueSetList) throws Exception;
	
	/**
	 * Find measure set.
	 * 
	 * @param id
	 *            - {@link String}.
	 * @return {@link MeasureSet}. *
	 */
	MeasureSet findMeasureSet(String id);
	
	/**
	 * Save.
	 * 
	 * @param measureSet
	 *            - {@link MeasureSet}. *
	 */
	void save(MeasureSet measureSet);
	
	/**
	 * Gets the unique oid.
	 * 
	 * @return {@link String}. *
	 */
	String getUniqueOid();
	
	/**
	 * Find data type for supplimental code list.
	 * 
	 * @param dataTypeName
	 *            - {@link String}.
	 * @param categoryId
	 *            - {@link String}.
	 * @return {@link DataType}. *
	 */
	DataType findDataTypeForSupplimentalCodeList(String dataTypeName,
			String categoryId);
	
	/**
	 * Delete existing packages.
	 * 
	 * @param measureId
	 *            - {@link String}. *
	 */
	void deleteExistingPackages(String measureId);
	
	/**
	 * Save supplimental qdm.
	 * 
	 * @param qds
	 *            - {@link QualityDataSet}. *
	 */
	void saveSupplimentalQDM(QualityDataSet qds);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            - {@link String}
	 * @return {@link Boolean}. *
	 */
	boolean isMeasureLocked(String id);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return {@link Integer}. *
	 */
	int getMaxEMeasureId();
	
	/**
	 * Save and return max e measure id.
	 * 
	 * @param measure
	 *            - {@link Measure}
	 * @return {@link Integer}. *
	 */
	int saveAndReturnMaxEMeasureId(Measure measure);
	
	/**
	 * Transfer measure owner ship to user.
	 * 
	 * @param list
	 *            - {@link List} of {@link String}.
	 * @param toEmail
	 *            - {@link String}.
	 * 
	 *            *
	 */
	void transferMeasureOwnerShipToUser(List<String> list, String toEmail);
	
	/**
	 * Search with filter.
	 * 
	 * @param searchText
	 *            - {@link String}.
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param numResults
	 *            - {@link Integer}.
	 * @param filter
	 *            - {@link Integer}.
	 * @return {@link List} of {@link MeasureShareDTO}. *
	 */
	List<MeasureShareDTO> searchWithFilter(String searchText, int startIndex,
			int numResults, int filter);
	
	/**
	 * Search for admin with filter.
	 * 
	 * @param searchText
	 *            - {@link String}.
	 * @param startIndex
	 *            - {@link Integer}.
	 * @param numResults
	 *            - {@link Integer}.
	 * @param filter
	 *            - {@link Integer}.
	 * @return {@link List} of {@link MeasureShareDTO}. *
	 */
	List<MeasureShareDTO> searchForAdminWithFilter(String searchText, int startIndex,
			int numResults, int filter);
	
	/**
	 * Count.
	 * 
	 * @param filter
	 *            - {@link Integer}.
	 * @return {@link Long} *
	 */
	long count(int filter);
	
	/**
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            - {@link String}.
	 * @return {@link MeasureXmlModel}. *
	 */
	MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	
	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            - {@link MeasureXmlModel}. *
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	
	/**
	 * Retrieve steward oid.
	 * 
	 * @param stewardName
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	String retrieveStewardOID(String stewardName);
	
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            - {@link String}.
	 * @param isPrivate
	 *            - {@link Boolean}. *
	 */
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
}
