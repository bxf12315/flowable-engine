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
package org.activiti.app.idm.service;

import java.util.ArrayList;
import java.util.List;

import org.activiti.app.idm.constant.DefaultPrivileges;
import org.activiti.idm.api.Privilege;
import org.activiti.idm.api.Group;
import org.activiti.idm.api.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Joram Barrez
 */
@Service
@Transactional
public class PrivilegeServiceImpl extends AbstractIdmService implements PrivilegeService {

  protected List<String> privilegeNames = new ArrayList<String>();
  
  public PrivilegeServiceImpl() {
    initDefaultprivilegeNames();
  }
  
  protected void initDefaultprivilegeNames() {
    privilegeNames.add(DefaultPrivileges.ACCESS_ADMIN);
    privilegeNames.add(DefaultPrivileges.ACCESS_MODELER);
  }

  public List<String> findPrivilegeNames() {
    return privilegeNames;
  }

  public void setprivilegeNames(List<String> privilegeNames) {
    this.privilegeNames = privilegeNames;
  }

  @Override
  public List<User> findUsersWithPrivilege(String privilegeName) {
    validateAdminRole();
    return identityService.getUsersWithPrivilege(privilegeName);
  }
  
  @Override
  public void addUserPrivilege(String privilegeName, String userId) {
    validateAdminRole();
    validateIsKnownPrivilege(privilegeName);
    
    if (!isUserPrivilege(privilegeName, userId)) {
      identityService.createPrivilege(privilegeName, userId, null);
    }
  }
  
  @Override
  public void deleteUserPrivilege(String privilegeName, String userId) {
    validateAdminRole();
    validateIsKnownPrivilege(privilegeName);
    
    if (isUserPrivilege(privilegeName, userId)) {
      Privilege privilege = identityService.createPrivilegeQuery()
          .privilegeName(privilegeName)
          .userId(userId)
          .singleResult();
      if (privilege != null) {
        identityService.deletePrivilege(privilege.getId());
      }
    }
  }
  
  @Override
  public List<Group> findGroupsWithPrivilege(String privilegeName) {
    validateAdminRole();
    return identityService.getGroupsWithPrivilege(privilegeName);
  }
  
  @Override
  public void addGroupPrivilege(String privilegeName, String groupId) {
    validateAdminRole();
    validateIsKnownPrivilege(privilegeName);
    
    if (!isGroupPrivilege(privilegeName, groupId)) {
      identityService.createPrivilege(privilegeName, null, groupId);
    }
  }
  
  @Override
  public void deleteGroupPrivilege(String privilegeName, String groupId) {
    validateAdminRole();
    validateIsKnownPrivilege(privilegeName);
    
    if (isGroupPrivilege(privilegeName, groupId)) {
      Privilege privilege = identityService.createPrivilegeQuery()
          .privilegeName(privilegeName)
          .groupId(groupId)
          .singleResult();
      if (privilege != null) {
        identityService.deletePrivilege(privilege.getId());
      }
    }
  }
  
  protected void validateIsKnownPrivilege(String privilegeName) {
    if (!privilegeNames.contains(privilegeName)) {
      throw new IllegalArgumentException("Invalid privilege name");
    }
  }
  
  protected boolean isUserPrivilege(String privilegeName, String userId) {
    User user = identityService.createUserQuery().userId(userId).singleResult();
    if (user == null) {
      throw new IllegalArgumentException("Invalid user id");
    }
    
    return identityService.createPrivilegeQuery().privilegeName(privilegeName).userId(userId).count() > 0;
  }
  
  protected boolean isGroupPrivilege(String privilegeName, String groupId) {
    Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
    if (group == null) {
      throw new IllegalArgumentException("Invalid group id");
    }
    
    return identityService.createPrivilegeQuery().privilegeName(privilegeName).groupId(groupId).count() > 0;
  }
  
}
