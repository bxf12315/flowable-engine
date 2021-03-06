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
package org.activiti.content.engine.impl;

import org.activiti.content.api.ContentManagementService;
import org.activiti.content.api.ContentService;
import org.activiti.content.engine.ContentEngine;
import org.activiti.content.engine.ContentEngineConfiguration;
import org.activiti.content.engine.ContentEngines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tijs Rademakers
 */
public class ContentEngineImpl implements ContentEngine {

  private static Logger log = LoggerFactory.getLogger(ContentEngineImpl.class);

  protected String name;
  protected ContentManagementService managementService;
  protected ContentService contentService;
  protected ContentEngineConfiguration engineConfiguration;

  public ContentEngineImpl(ContentEngineConfiguration engineConfiguration) {
    this.engineConfiguration = engineConfiguration;
    this.name = engineConfiguration.getEngineName();
    this.managementService = engineConfiguration.getContentManagementService();
    this.contentService = engineConfiguration.getContentService();

    if (name == null) {
      log.info("default activiti ContentEngine created");
    } else {
      log.info("ContentEngine {} created", name);
    }

    ContentEngines.registerContentEngine(this);
  }

  public void close() {
    ContentEngines.unregister(this);
  }

  // getters and setters
  // //////////////////////////////////////////////////////

  public String getName() {
    return name;
  }
  
  public ContentManagementService getContentManagementService() {
    return managementService;
  }

  public ContentService getContentService() {
    return contentService;
  }

  public ContentEngineConfiguration getContentEngineConfiguration() {
    return engineConfiguration;
  }
}
