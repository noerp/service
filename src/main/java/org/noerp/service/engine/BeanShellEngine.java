/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.noerp.service.engine;

import static org.noerp.base.util.UtilGenerics.cast;

import java.util.Map;

import org.noerp.base.util.BshUtil;
import org.noerp.base.util.Debug;
import org.noerp.base.util.GeneralException;
import org.noerp.base.util.UtilValidate;
import org.noerp.service.GenericServiceException;
import org.noerp.service.ModelService;
import org.noerp.service.ServiceDispatcher;
import org.noerp.service.ServiceUtil;

/**
 * BeanShell Script Service Engine
 */
public final class BeanShellEngine extends GenericAsyncEngine {

    public BeanShellEngine(ServiceDispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * @see org.noerp.service.engine.GenericEngine#runSyncIgnore(java.lang.String, org.noerp.service.ModelService, java.util.Map)
     */
    @Override
    public void runSyncIgnore(String localName, ModelService modelService, Map<String, Object> context) throws GenericServiceException {
        runSync(localName, modelService, context);
    }

    /**
     * @see org.noerp.service.engine.GenericEngine#runSync(java.lang.String, org.noerp.service.ModelService, java.util.Map)
     */
    @Override
    public Map<String, Object> runSync(String localName, ModelService modelService, Map<String, Object> context) throws GenericServiceException {
        return serviceInvoker(localName, modelService, context);
    }

    // Invoke the BeanShell Script.
    private Map<String, Object> serviceInvoker(String localName, ModelService modelService, Map<String, Object> context) throws GenericServiceException {
        if (UtilValidate.isEmpty(modelService.location)) {
            throw new GenericServiceException("Cannot run Beanshell service with empty location");
        }

        String location = this.getLocation(modelService);
        context.put("dctx", dispatcher.getLocalContext(localName));

        try {
            Object resultObj = BshUtil.runBshAtLocation(location, context);

            if (resultObj != null && resultObj instanceof Map<?, ?>) {
                Debug.logInfo("Got result Map from script return: " + resultObj, module);
                return cast(resultObj);
            } else if (context.get("result") != null && context.get("result") instanceof Map<?, ?>) {
                Debug.logInfo("Got result Map from context: " + resultObj, module);
                return cast(context.get("result"));
            }
        } catch (GeneralException e) {
            throw new GenericServiceException(e);
        }

        return ServiceUtil.returnSuccess();
    }
}
