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
package org.activiti.idm.engine.test.api.identity;

import java.util.List;

import org.activiti.idm.api.Privilege;
import org.activiti.idm.api.Group;
import org.activiti.idm.api.User;
import org.activiti.idm.engine.impl.persistence.entity.PrivilegeEntity;
import org.activiti.idm.engine.test.PluggableActivitiIdmTestCase;

/**
 * @author Joram Barrez
 */
public class PrivilegesTest extends PluggableActivitiIdmTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
    createGroup("admins", "Admins", "user");
    createGroup("sales", "Sales", "user");
    createGroup("engineering", "Engineering" , "user");

    idmIdentityService.saveUser(idmIdentityService.newUser("kermit"));
    idmIdentityService.saveUser(idmIdentityService.newUser("fozzie"));
    idmIdentityService.saveUser(idmIdentityService.newUser("mispiggy"));

    idmIdentityService.createMembership("kermit", "admins");
    idmIdentityService.createMembership("kermit", "sales");
    idmIdentityService.createMembership("kermit", "engineering");
    idmIdentityService.createMembership("fozzie", "sales");
    idmIdentityService.createMembership("mispiggy", "engineering");

    String adminPrivilege = "access admin application";
    idmIdentityService.createPrivilege(adminPrivilege, null, "admins");
    idmIdentityService.createPrivilege(adminPrivilege, "mispiggy", null);
    
    String modelerPrivilege = "access modeler application";
    idmIdentityService.createPrivilege(modelerPrivilege, null, "admins");
    idmIdentityService.createPrivilege(modelerPrivilege, null, "engineering");
    idmIdentityService.createPrivilege(modelerPrivilege, "kermit", null);
    
    String startProcessesPrivilege = "start processes";
    idmIdentityService.createPrivilege(startProcessesPrivilege, null, "sales");
  }
  
  @Override
  protected void tearDown() throws Exception {
    clearAllUsersAndGroups();
    super.tearDown();
  }
  
  public void testGetUsers() {
    List<User> users = idmIdentityService.getUsersWithPrivilege("access admin application");
    assertEquals(1, users.size());
    assertEquals("mispiggy", users.get(0).getId());
    
    assertEquals(0, idmIdentityService.getUsersWithPrivilege("does not exist").size());
    
    try {
      idmIdentityService.getUsersWithPrivilege(null); 
      fail();
    } catch (Exception e) { }
  }
  
  public void testGetGroups() {
    List<Group> groups = idmIdentityService.getGroupsWithPrivilege("access modeler application");
    assertEquals(2, groups.size());
    assertEquals("admins", groups.get(0).getId());
    assertEquals("engineering", groups.get(1).getId());
    
    assertEquals(0, idmIdentityService.getGroupsWithPrivilege("does not exist").size());
    
    try {
      idmIdentityService.getGroupsWithPrivilege(null);
      fail();
    } catch (Exception e) {}
  }
  
  public void testQueryAll() {
    List<Privilege> privileges = idmIdentityService.createPrivilegeQuery().list();
    assertEquals(6, privileges.size());
    assertEquals(6L, idmIdentityService.createPrivilegeQuery().count());
    
    int nrOfUserPrivileges = 0;
    int nrOfGroupPrivileges = 0;
    for (Privilege privilege : privileges) {
      assertNotNull(privilege.getName());
      if (privilege.getUserId() != null) {
        nrOfUserPrivileges++;
      }
      if (privilege.getGroupId() != null) {
        nrOfGroupPrivileges++;
      }
    }
    
    assertEquals(2, nrOfUserPrivileges);
    assertEquals(4, nrOfGroupPrivileges);
  }
  
  public void testQueryByName() {
    List<Privilege> privileges = idmIdentityService.createPrivilegeQuery().privilegeName("access admin application").list();
    assertEquals(2, privileges.size());
    
    boolean groupFound = false;
    boolean userFound = false;
    for (Privilege privilege : privileges) {
      if ("admins".equals(privilege.getGroupId())) {
        groupFound = true;
      } 
      if ("mispiggy".equals(privilege.getUserId())) {
        userFound = true;
      }
    }
    
    assertTrue(userFound);
    assertTrue(groupFound);
  }
  
 public void testQueryByInvalidName() {
    assertEquals(0, idmIdentityService.createPrivilegeQuery().privilegeName("does not exist").list().size());
  }
  
  public void testQueryByUserId() {
    List<Privilege> privileges = idmIdentityService.createPrivilegeQuery().userId("kermit").list();
    assertEquals(1, privileges.size());
    
    Privilege privilege = privileges.get(0);
    assertEquals("access modeler application", privilege.getName());
  }
  
  public void testQueryByInvalidUserId() {
    assertEquals(0, idmIdentityService.createPrivilegeQuery().userId("does not exist").list().size());
  }
  
  public void testQueryByGroupId() {
    List<Privilege> privileges = idmIdentityService.createPrivilegeQuery().groupId("admins").list();
    assertEquals(2, privileges.size());
  }
  
  public void testQueryByInvalidGroupId() {
    assertEquals(0, idmIdentityService.createPrivilegeQuery().groupId("does not exist").list().size());
  }
  
  public void testNativeQuery() {
    assertEquals("ACT_ID_PRIVILEGE", idmManagementService.getTableName(Privilege.class));
    assertEquals("ACT_ID_PRIVILEGE", idmManagementService.getTableName(PrivilegeEntity.class));
    
    String tableName = idmManagementService.getTableName(PrivilegeEntity.class);
    String baseQuerySql = "SELECT * FROM " + tableName + " where USER_ID_ = #{userId}";

    assertEquals(1, idmIdentityService.createNativeUserQuery().sql(baseQuerySql).parameter("userId", "kermit").list().size());
  }
  
}
