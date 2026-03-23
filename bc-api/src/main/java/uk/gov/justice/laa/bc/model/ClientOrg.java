package uk.gov.justice.laa.bc.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * ClientOrg.
 */
public class ClientOrg {

  private final Collection<String> userIds;

  private final String orgId;

  ClientOrg(String orgId, Collection<String> userIds) {
    this.orgId = orgId;
    this.userIds = userIds;
  }

  /**
   * parse string to ClientOrg.
   *
   * @param client String
   * @return ClientOrg
   */
  public static ClientOrg parse(String client) {
    String[] parts = client.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException(
          "client definition should be clientId:userId1[|userId2...]");
    }

    String[] userIds = parts[1].split(",");
    return new ClientOrg(parts[0], Arrays.asList(userIds));
  }

  String getOrgId() {
    return this.orgId;
  }

  public Collection<String> getUserIds() {
    return Collections.unmodifiableCollection(this.userIds);
  }

}
