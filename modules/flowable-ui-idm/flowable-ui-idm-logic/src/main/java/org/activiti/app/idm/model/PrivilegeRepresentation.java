/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.app.idm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joram Barrez
 */
public class PrivilegeRepresentation extends AbstractRepresentation {
  
  protected String name;
  protected List<UserRepresentation> users;
  protected List<GroupRepresentation> groups;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public List<UserRepresentation> getUsers() {
    return users;
  }
  public void setUsers(List<UserRepresentation> users) {
    this.users = users;
  }
  public void addUser(UserRepresentation userRepresentation) {
    if (users == null) {
      users = new ArrayList<UserRepresentation>();
    }
    users.add(userRepresentation);
  }
  public List<GroupRepresentation> getGroups() {
    return groups;
  }
  public void setGroups(List<GroupRepresentation> groups) {
    this.groups = groups;
  }
  public void addGroup(GroupRepresentation groupRepresentation) {
    if (groups == null) {
      groups = new ArrayList<GroupRepresentation>();
    }
    groups.add(groupRepresentation);
  }
  
}
