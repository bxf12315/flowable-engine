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

package org.activiti.content.engine.impl.interceptor;

import org.activiti.content.engine.impl.cfg.TransactionContext;
import org.activiti.content.engine.impl.cfg.TransactionListener;
import org.activiti.content.engine.impl.context.Context;
import org.activiti.engine.impl.cfg.TransactionContextFactory;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.activiti.engine.impl.interceptor.TransactionCommandContextCloseListener;

/**
 * @author Joram Barrez
 */
public class TransactionContextInterceptor extends AbstractCommandInterceptor {
  
  protected TransactionContextFactory<TransactionListener, CommandContext> transactionContextFactory;

  public TransactionContextInterceptor() {
  }

  public TransactionContextInterceptor(TransactionContextFactory<TransactionListener, CommandContext> transactionContextFactory) {
    this.transactionContextFactory = transactionContextFactory;
  }

  public <T> T execute(CommandConfig config, Command<T> command) {
    
    CommandContext commandContext = Context.getCommandContext();
    // Storing it in a variable, to reference later (it can change during command execution)
    boolean isReused = commandContext.isReused();
    
    try {
      
      if (transactionContextFactory != null && !isReused) {
        TransactionContext transactionContext = (TransactionContext) transactionContextFactory.openTransactionContext(commandContext);
        Context.setTransactionContext(transactionContext);
        commandContext.addCloseListener(new TransactionCommandContextCloseListener(transactionContext));
      }
      
      return next.execute(config, command);
      
    } finally {
      if (transactionContextFactory != null && !isReused) {
        Context.removeTransactionContext();
      }
    }

  }

  public TransactionContextFactory<TransactionListener, CommandContext> getTransactionContextFactory() {
    return transactionContextFactory;
  }

  public void setTransactionContextFactory(TransactionContextFactory<TransactionListener, CommandContext> transactionContextFactory) {
    this.transactionContextFactory = transactionContextFactory;
  }

}
