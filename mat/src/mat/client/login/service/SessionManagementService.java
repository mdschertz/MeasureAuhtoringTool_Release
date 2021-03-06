package mat.client.login.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface SessionManagementService.
 */
@RemoteServiceRelativePath("sessionService")
public interface SessionManagementService extends RemoteService{
	
	/**
	 * The Class Result.
	 */
	public class Result implements IsSerializable {
		
		/** The user id. */
		public String userId;
		
		/** The user email. */
		public String userEmail;
		
		/** The user role. */
		public String userRole;
		
		/** The sign in date. */
		public Date signInDate;
		
		/** The sign out date. */
		public Date signOutDate;
		
		/** The login id. */
		public String loginId;
	}
	
	/**
	 * Gets the current user role.
	 * 
	 * @return the current user role
	 */
	public SessionManagementService.Result getCurrentUserRole();
	
	/**
	 * Renew session.
	 */
	public void renewSession();
}
