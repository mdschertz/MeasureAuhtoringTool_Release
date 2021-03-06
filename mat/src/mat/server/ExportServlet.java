package mat.server;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mat.model.MeasureNotes;
import mat.model.User;
import mat.server.service.MeasureNotesService;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.server.service.UserService;
import mat.shared.FileNameUtility;
import mat.shared.InCorrectUserRoleException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The Class ExportServlet.
 */
public class ExportServlet extends HttpServlet {
	
	/** The Constant EXPORT_MEASURE_NOTES_FOR_MEASURE. */
	private static final String EXPORT_MEASURE_NOTES_FOR_MEASURE = "exportMeasureNotesForMeasure";
	
	/** The Constant EXPORT_ACTIVE_NON_ADMIN_USERS_CSV. */
	private static final String EXPORT_ACTIVE_NON_ADMIN_USERS_CSV = "exportActiveNonAdminUsersCSV";
	
	/** The Constant VALUESET. */
	private static final String VALUESET = "valueset";
	
	/** The Constant ZIP. */
	private static final String ZIP = "zip";
	
	/** The Constant CODELIST. */
	private static final String CODELIST = "codelist";
	
	/** The Constant SAVE. */
	private static final String SAVE = "save";
	
	/** The Constant ATTACHMENT_FILENAME. */
	private static final String ATTACHMENT_FILENAME = "attachment; filename=";
	
	/** The Constant CONTENT_DISPOSITION. */
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	
	/** The Constant TEXT_XML. */
	private static final String TEXT_XML = "text/xml";
	
	/** The Constant TEXT_HTML. */
	private static final String TEXT_HTML = "text/html";
	
	/** The Constant CONTENT_TYPE. */
	private static final String CONTENT_TYPE = "Content-Type";
	
	/** The Constant EMEASURE. */
	private static final String EMEASURE = "emeasure";
	
	/** The Constant SIMPLEXML. */
	private static final String SIMPLEXML = "simplexml";
	
	/** The Constant TYPE_PARAM. */
	private static final String TYPE_PARAM = "type";
	
	/** The Constant FORMAT_PARAM. */
	private static final String FORMAT_PARAM = "format";
	
	/** The Constant ID_PARAM. */
	private static final String ID_PARAM = "id";
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(ExportServlet.class);
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4539514145289378238L;
	
	/** The context. */
	protected ApplicationContext context;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		String id = req.getParameter(ID_PARAM);
		String format = req.getParameter(FORMAT_PARAM);
		String type = req.getParameter(TYPE_PARAM);

		ExportResult export = null;

		FileNameUtility fnu = new FileNameUtility();
		try {
			if (SIMPLEXML.equals(format)) {
				export = getService().getSimpleXML(id);
				if (SAVE.equals(type)) {
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getSimpleXMLName(export.measureName));
				} else {
					resp.setHeader(CONTENT_TYPE, TEXT_XML);
				}
			} else if (EMEASURE.equals(format)) {
				if ("open".equals(type)) {
					export = getService().getEMeasureHTML(id);
					resp.setHeader(CONTENT_TYPE, TEXT_HTML);
				} else if (SAVE.equals(type)) {
					export = getService().getEMeasureXML(id);
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getEmeasureXMLName(export.measureName));
				}
			} else if (CODELIST.equals(format)) {
				export = getService().getEMeasureXLS(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getEmeasureXLSName(export.measureName, export.packageDate));
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			} else if (ZIP.equals(format)) {
				export = getService().getEMeasureZIP(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName));
				resp.setContentType("application/zip");
				resp.getOutputStream().write(export.zipbarr);
				resp.getOutputStream().close();
				export.zipbarr = null;
			} else if (VALUESET.equals(format)) {
				export = getService().getValueSetXLS(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getValueSetXLSName(export.valueSetName, export.lastModifiedDate));
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			} else if (EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)) {
				String userRole = LoggedInUserUtil.getLoggedInUserRole();
				if ("Administrator".equalsIgnoreCase(userRole)) {
					String csvFileString = generateCSVOfActiveUserEmails();
					Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String activeUserCSVDate = formatter.format(new Date());
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getCSVFileName("activeUsers", activeUserCSVDate) + ";");
					resp.setContentType("text/csv");
					resp.getOutputStream().write(csvFileString.getBytes());
					resp.getOutputStream().close();
				}
			} else if ("exportMeasureNotesForMeasure".equals(format)) {
				String csvFileString = generateCSVToExportMeasureNotes(id);
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String measureNoteDate = formatter.format(new Date());
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getCSVFileName("MeasureNotes", measureNoteDate) + ";");
				resp.setContentType("text/csv");
				resp.getOutputStream().write(csvFileString.getBytes());
				resp.getOutputStream().close();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if (!CODELIST.equals(format) && !EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)
				&& !EXPORT_MEASURE_NOTES_FOR_MEASURE.equals(format)) {
			resp.getOutputStream().println(export.export);
		}
	}

	/**
	 * Generate csv to export measure notes.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the string
	 */
	private String generateCSVToExportMeasureNotes(final String measureId) {
		logger.info("Generating CSV of Measure Notes...");
		List<MeasureNotes> allMeasureNotes = getMeasureNoteService().getAllMeasureNotesByMeasureID(measureId);

		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Title,Description,LastModifiedDate,Created By,Modified By");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for (MeasureNotes measureNotes:allMeasureNotes) {
			if (measureNotes.getModifyUser() != null) {
				csvStringBuilder.append("\"" + measureNotes.getNoteTitle() + "\",\""
						+ measureNotes.getNoteDesc() + "\",\""
						+ convertDateToString(measureNotes.getLastModifiedDate()) + "\",\""
						+ measureNotes.getCreateUser().getEmailAddress() + "\",\""
						+ measureNotes.getModifyUser().getEmailAddress() + "\"");
			} else {
				csvStringBuilder.append("\"" + measureNotes.getNoteTitle() + "\",\""
						+ measureNotes.getNoteDesc() + "\",\""
						+ convertDateToString(measureNotes.getLastModifiedDate()) + "\",\""
						+ measureNotes.getCreateUser().getEmailAddress() + "\",\"" + "" + "\"");
			}
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}

	/**
	 * Converts a date into a date time string of "MM/dd/yyyy hh:mm:ss a z" format.
	 * @param date - date to be formated into a string.
	 * @return the formated date string.
	 */
	private String convertDateToString(final Date date) {
		String dateString = StringUtils.EMPTY;
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
			dateString = format.format(date);
		}
		return dateString;
	}

	/**
	 * Generate csv of active user emails.
	 * 
	 * @return the string
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	private String generateCSVOfActiveUserEmails() throws InCorrectUserRoleException {
		logger.info("Generating CSV of email addrs for all Active Users...");
		//Get all the active users
		List<User> allNonAdminActiveUsersList = getUserService().getAllNonAdminActiveUsers();

		//Iterate through the 'allNonAdminActiveUsersList' and generate a csv
		return createCSVOfAllNonAdminActiveUsers(allNonAdminActiveUsersList);
	}

	/**
	 * Creates the csv of all non admin active users.
	 * 
	 * @param allNonAdminActiveUsersList
	 *            the all non admin active users list
	 * @return the string
	 */
	private String createCSVOfAllNonAdminActiveUsers(
			final List<User> allNonAdminActiveUsersList) {

		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Last Name,First Name,Email Address,Organization,Organization Id");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for (User user:allNonAdminActiveUsersList) {
			csvStringBuilder.append("\"" + user.getLastName() + "\",\"" + user.getFirstName()
					+ "\",\"" + user.getEmailAddress() + "\",\"" + user.getOrganizationName()
					+ "\",\"" + user.getOrgOID() + "\"");
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private SimpleEMeasureService getService() {
		SimpleEMeasureService service = (SimpleEMeasureService) context.getBean("eMeasureService");
		return service;
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService) context.getBean("userService");
	}

	/**
	 * Gets the measure note service.
	 * 
	 * @return the measure note service
	 */
	private MeasureNotesService getMeasureNoteService() {
		return (MeasureNotesService) context.getBean("measureNotesService");
	}
}
