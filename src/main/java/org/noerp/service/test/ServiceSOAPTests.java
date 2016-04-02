/*
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
 */
package org.noerp.service.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.noerp.base.util.UtilDateTime;
import org.noerp.base.util.UtilGenerics;
import org.noerp.entity.GenericValue;
import org.noerp.service.testtools.ServiceTestCase;

public class ServiceSOAPTests extends ServiceTestCase {

    public final static String module = ServiceSOAPTests.class.getName();

    public ServiceSOAPTests(String name) {
        // TODO Auto-generated constructor stub
        super(name);
    }

    public void testSOAPSimpleService() throws Exception {
        Map<String, Object> serviceContext = new HashMap<String, Object>();
        serviceContext.put("defaultValue", new Double("123.4567"));
        serviceContext.put("message", "Test Message !!!");
        dispatcher.runSync("testSoapSimple", serviceContext);
    }

    public void testSOAPService() throws Exception {
        Map<String, Object> serviceContext = new HashMap<String, Object>();
        GenericValue testing = delegator.makeValue("Testing");
        testing.put("testingId", "COMPLEX_TYPE_TEST");
        testing.put("testingTypeId", "SOAP_TEST");
        testing.put("testingName", "Complex Type Test");
        testing.put("createdStamp", UtilDateTime.nowTimestamp());
        serviceContext.put("testing", testing);
        Map<String, Object> results = dispatcher.runSync("testSoap", serviceContext);
        List<GenericValue> testingNodes = UtilGenerics.cast(results.get("testingNodes"));
        assertNotNull(testingNodes);
    }
}
