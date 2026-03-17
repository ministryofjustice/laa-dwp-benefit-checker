/*
 * BenefiteCheckerConstants.java
 *
 * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 * Version History<p>
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 * Version     Date             Author      SIR     Description<p>
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 *    1.0     27 Mar 2008       LING-G      147992  initial version
 *    1.5	  14 Oct 2008       LEO		    148865	Added additional response from DWP (Deceased,Deleted and Superseded)
 *    1.6     23 Oct 2008       ling-g      148937  Pen test message credentials - obfustication
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 */

package uk.gov.justice.laa.bc.validator;


/**
 * Benefit Checker application wide Constants interface.
 *
 * @author LING-G
 */
public class BenefiteCheckerConstants {
  // BenefitChecker config properties.

  public static final String CONFIG_SVR_CONTEXT = "CREDENTIALS_SERVICE_CONTEXT";
  public static final String CONFIG_CLIENT_IDS = "CLIENT_IDS";

  public static final int REQUEST = 10;
  public static final int RESPONSE = 20;
  public static final int EXCEPTION = 30;

  public static final boolean TOLERATE_NULLS = true;


  // Configuration property values
  public static final String PROP_NAME_DWP_URL = "DWP_SERVICE_NAME";
  public static final String DWP_CALL_FLAG = "DWP_CALL_FLAG";

  public static final String DWP_SERVICE_TIMEOUT = "lsc.benefitchecker.dwp.service.timeout";
  public static final String DWP_SERVICE_TESTMODE = "lsc.benefitchecker.dwp.service.test";
  // DWP service response status values....
  // Note. Why are these not provided by the DWPObj.jar (DWP Client)
  // WARNING - These values are presented on the public interface
  //         - consider the impact to clients (MAAT, SLAG, NILSC etc) if changed.
  public static final String DWP_STATUS_ERROR = "Technical fault";
  public static final String DWP_STATUS_YES = "Yes";
  public static final String DWP_STATUS_NO = "No";
  public static final String DWP_STATUS_UNDETERMINED = "Undetermined";
  public static final String DWP_STATUS_DECEASED = "Deceased";
  public static final String DWP_STATUS_DELETED = "Deleted";
  public static final String DWP_STATUS_SUPERSEDED = "Superseded";


  public static final String MSG_DESC_VALIDATION_CREDENTIALS = "Invalid request credentials.";
  public static final String MSG_DESC_REQUEST_TIMED_OUT = "Request timed out.";
  public static final String MSG_DESC_REQUEST_TIMEOUT = "DWP Service has timed out";

  public static final int MIN_LEN = 3;
  public static final int MAX_LEN_NINO = 9;
  public static final int MIN_LEN_SURNAME = 2;
  public static final int MAX_LEN_SURNAME = 50;
  public static final int MAX_LEN_DATE = 8;

  public static final int MAX_LEN_SERVICE_CONTEXT = 7;
  public static final int MAX_LEN_CLIENT_ORG_ID = 13;
  public static final int MAX_LEN_CLIENT_USER_ID = 15;
  public static final int MAX_LEN_CLIENT_REF = 50;


  // used in the event that DWP response status handling error
  public static final String DWP_SERVICE_UNAVAILABLE_MSG =
      "lsc.benefitchecker.dwp.service.exception.message";

  // LSCBC1nn series : ** Not used at present **

  // LSCBC2nn series : Public api faults
  //
  // Request content validation
  public static final String MSG_CODE_VALIDATION_BLANK = "LSCBC205";
      // a mandatory request parameter is missing...
  public static final String MSG_CODE_VALIDATION_SIZE = "LSCBC210";
      // request parameter has invalid size - e.g. too long.
  public static final String MSG_CODE_VALIDATION_FORMAT = "LSCBC215";
      // request parameter has invalid structure - e.g. bad date format, or NIno does not match regex expr.
  //
  // Request credaential issues
  public static final String MSG_CODE_VALIDATION_SVRNAME = "LSCBC250";
      // the Requests embedded service name cedential does not match that of the service.
  public static final String MSG_CODE_VALIDATION_CLIENTORG = "LSCBC252";
      // the Requests embedded clientOrg Id cedential does not match those registered for the service.
  public static final String MSG_CODE_VALIDATION_USERID = "LSCBC254";
      // the Requests embedded user Id cedential does not match those registered for the service.
  // Request general.
  public static final String MSG_CODE_DWP_TIMED_OUT = "LSCBC299";
      // the Requests timed out waiting for response from remote DWP service... (> 90seconds)

  // Property Cache messages
  // LSCBC9nn series : Internal faults
  //
  // config related:
  public static final String MSG_CODE = "LSCBC900";    // general undefined message
  public static final String MSG_CODE_PROP_CACHE = "LSCBC910";
      // Object retrieved from cache not of expected type (Class Cast)
  public static final String MSG_CODE_PROP_CACHE_CONVERSION = "LSCBC920";
      // A cache conversion operation raised a fault. e.g  invalid String to Int.;
  //
  // DWP related:
  public static final String MSG_CODE_DWP_RESP = "LSCBC950";
      // The underlying DWP service response is not as expected... unsupported response type.
  public static final String MSG_CODE_DWP_JNDI_URL = "LSCBC951";
      // Unable to retrieve the DWP URL (Jndi env/ ) from the container.
  public static final String MSG_CODE_DWP_FAILURE_SERVICE = "LSCBC958";
      // The underlying DWP service Client has malformed URL - check dwpURL in app server config.
  public static final String MSG_CODE_DWP_FAILURE_REMOTE = "LSCBC959";
      // The underlying DWP service has failed or can't be contacted with configured URL

  //
  // General BenefitChecker catch all...
  public static final String MSG_CODE_FATAL_RUNTIME = "LSCBC998";
      // Internal Runtime failure occured
  public static final String MSG_CODE_FATAL_THROWABLE = "LSCBC999";
      // Internal Throwable detected..

}
