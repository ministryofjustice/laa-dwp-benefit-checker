package uk.gov.justice.laa.bc.validator;

/**
 * ReturnCodes.
 */
public class ReturnCodes {
  // LSCBC1nn series : ** Not used at present **

  // LSCBC2nn series : Public api faults
  //
  // Request content validation
  public static final String MSG_CODE_VALIDATION_BLANK = "LSCBC205";
  // a mandatory request parameter is missing...
  public static final String MSG_CODE_VALIDATION_SIZE = "LSCBC210";
  // request parameter has invalid size - e.g. too long.
  public static final String MSG_CODE_VALIDATION_FORMAT = "LSCBC215";
  // request parameter has invalid structure - e.g. bad date format,
  // or NIno does not match regex expr.
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
