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
package org.activiti.app.rest.idm;

import java.util.List;

import org.activiti.app.idm.model.AddGroupPrivilegeRepresentation;
import org.activiti.app.idm.model.AddUserPrivilegeRepresentation;
import org.activiti.app.idm.model.PrivilegeRepresentation;
import org.activiti.app.idm.model.GroupRepresentation;
import org.activiti.app.idm.model.UserRepresentation;
import org.activiti.app.idm.service.PrivilegeService;
import org.activiti.idm.api.Group;
import org.activiti.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joram Barrez
 */
@RestController
public class IdmPrivilegesResource {
  
  @Autowired
  protected PrivilegeService privilegeService;
  
  @RequestMapping(value = "/rest/admin/privileges", method = RequestMethod.GET)
  public List<String> getPrivileges() {
    return privilegeService.findPrivilegeNames();
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}", method = RequestMethod.GET)
  public PrivilegeRepresentation getPrivilege(@PathVariable String privilegeName) {
    PrivilegeRepresentation privilege = new PrivilegeRepresentation();
    privilege.setName(privilegeName);
    
    List<User> users = privilegeService.findUsersWithPrivilege(privilegeName);
    for (User user : users) {
      privilege.addUser(new UserRepresentation(user));
    }
    
    List<Group> groups = privilegeService.findGroupsWithPrivilege(privilegeName);
    for (Group group : groups) {
      privilege.addGroup(new GroupRepresentation(group));
    }
    
    return privilege;
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}/users", method = RequestMethod.GET)
  public List<UserRepresentation> getUsers(@PathVariable String privilegeName) {
    return getPrivilege(privilegeName).getUsers();
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}/users", method = RequestMethod.POST)
  public void addUserPrivilege(@PathVariable String privilegeName, 
      @RequestBody AddUserPrivilegeRepresentation representation) {
    privilegeService.addUserPrivilege(privilegeName, representation.getUserId());
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}/users/{userId}", method = RequestMethod.DELETE)
  public void deleteUserPrivilege(@PathVariable String privilegeName, @PathVariable String userId) {
    privilegeService.deleteUserPrivilege(privilegeName, userId);
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}/groups", method = RequestMethod.GET)
  public List<GroupRepresentation> getGroups(@PathVariable String privilegeName) {
    return getPrivilege(privilegeName).getGroups();
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}/groups", method = RequestMethod.POST)
  public void addGroupPrivilege(@PathVariable String privilegeName, 
      @RequestBody AddGroupPrivilegeRepresentation representation) {
    privilegeService.addGroupPrivilege(privilegeName, representation.getGroupId());
  }
  
  @RequestMapping(value = "/rest/admin/privileges/{privilegeName}/groups/{groupId}", method = RequestMethod.DELETE)
  public void deleteGroupPrivilege(@PathVariable String privilegeName, @PathVariable String groupId) {
    privilegeService.deleteGroupPrivilege(privilegeName, groupId);
  }

}
