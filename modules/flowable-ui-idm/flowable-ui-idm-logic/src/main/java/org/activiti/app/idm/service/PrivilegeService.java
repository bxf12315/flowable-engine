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

import java.util.List;

import org.activiti.idm.api.Group;
import org.activiti.idm.api.User;

/**
 * Service for retrieving and changing privilege information.
 * 
 * @author Joram Barrez
 */
public interface PrivilegeService {

  List<String> findPrivilegeNames();
  
  List<User> findUsersWithPrivilege(String privilegeName);
  
  void addUserPrivilege(String privilegeName, String userId);
  
  void deleteUserPrivilege(String privilegeName, String userId);
  
  List<Group> findGroupsWithPrivilege(String privilegeName);
  
 void addGroupPrivilege(String privilegeName, String groupId);
  
  void deleteGroupPrivilege(String privilegeName, String groupId);
  
}
