/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.api.services;


import org.apache.ambari.api.handlers.RequestHandler;
import org.apache.ambari.api.resource.ResourceDefinition;
import org.apache.ambari.api.services.formatters.ResultFormatter;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 *
 */
public class ComponentServiceTest {

  @Test
  public void testGetComponent() {
    ResourceDefinition resourceDef = createStrictMock(ResourceDefinition.class);
    ResultFormatter resultFormatter = createStrictMock(ResultFormatter.class);
    Object formattedResult = new Object();
    Serializer serializer = createStrictMock(Serializer.class);
    Object serializedResult = new Object();
    RequestFactory requestFactory = createStrictMock(RequestFactory.class);
    ResponseFactory responseFactory = createStrictMock(ResponseFactory.class);
    Request request = createNiceMock(Request.class);
    RequestHandler requestHandler = createStrictMock(RequestHandler.class);
    Result result = createStrictMock(Result.class);
    Response response = createStrictMock(Response.class);

    HttpHeaders httpHeaders = createNiceMock(HttpHeaders.class);
    UriInfo uriInfo = createNiceMock(UriInfo.class);

    String clusterName = "clusterName";
    String serviceName = "serviceName";
    String componentName = "componentName";

    // expectations
    expect(requestFactory.createRequest(eq(httpHeaders), eq(uriInfo), eq(Request.RequestType.GET),
        eq(resourceDef))).andReturn(request);

    expect(requestHandler.handleRequest(request)).andReturn(result);
    expect(resourceDef.getResultFormatter()).andReturn(resultFormatter);
    expect(resultFormatter.format(result, uriInfo)).andReturn(formattedResult);
    expect(request.getSerializer()).andReturn(serializer);
    expect(serializer.serialize(formattedResult)).andReturn(serializedResult);

    expect(responseFactory.createResponse(serializedResult)).andReturn(response);

    replay(resourceDef, resultFormatter, serializer, requestFactory, responseFactory, request, requestHandler,
        result, response, httpHeaders, uriInfo);

    //test
    ComponentService componentService = new TestComponentService(resourceDef, clusterName, serviceName, componentName,
        requestFactory, responseFactory, requestHandler);
    assertSame(response, componentService.getComponent(httpHeaders, uriInfo, componentName));

    verify(resourceDef, resultFormatter, serializer, requestFactory, responseFactory, request, requestHandler,
        result, response, httpHeaders, uriInfo);
  }

  @Test
  public void testGetComponents() {
    ResourceDefinition resourceDef = createStrictMock(ResourceDefinition.class);
    ResultFormatter resultFormatter = createStrictMock(ResultFormatter.class);
    Object formattedResult = new Object();
    Serializer serializer = createStrictMock(Serializer.class);
    Object serializedResult = new Object();
    RequestFactory requestFactory = createStrictMock(RequestFactory.class);
    ResponseFactory responseFactory = createStrictMock(ResponseFactory.class);
    Request request = createNiceMock(Request.class);
    RequestHandler requestHandler = createStrictMock(RequestHandler.class);
    Result result = createStrictMock(Result.class);
    Response response = createStrictMock(Response.class);

    HttpHeaders httpHeaders = createNiceMock(HttpHeaders.class);
    UriInfo uriInfo = createNiceMock(UriInfo.class);

    String clusterName = "clusterName";
    String serviceName = "serviceName";

    // expectations
    expect(requestFactory.createRequest(eq(httpHeaders), eq(uriInfo), eq(Request.RequestType.GET),
        eq(resourceDef))).andReturn(request);

    expect(requestHandler.handleRequest(request)).andReturn(result);
    expect(resourceDef.getResultFormatter()).andReturn(resultFormatter);
    expect(resultFormatter.format(result, uriInfo)).andReturn(formattedResult);
    expect(request.getSerializer()).andReturn(serializer);
    expect(serializer.serialize(formattedResult)).andReturn(serializedResult);

    expect(responseFactory.createResponse(serializedResult)).andReturn(response);
    replay(resourceDef, resultFormatter, serializer, requestFactory, responseFactory, request, requestHandler,
        result, response, httpHeaders, uriInfo);

    //test
    ComponentService componentService = new TestComponentService(resourceDef, clusterName, serviceName, null, requestFactory, responseFactory, requestHandler);
    assertSame(response, componentService.getComponents(httpHeaders, uriInfo));

    verify(resourceDef, resultFormatter, serializer, requestFactory, responseFactory, request, requestHandler,
        result, response, httpHeaders, uriInfo);
  }

  private class TestComponentService extends ComponentService {
    private RequestFactory m_requestFactory;
    private ResponseFactory m_responseFactory;
    private RequestHandler m_requestHandler;
    private ResourceDefinition m_resourceDef;
    private String m_clusterId;
    private String m_serviceId;
    private String m_componentId;

    private TestComponentService(ResourceDefinition resourceDef, String clusterId, String serviceId, String componentId, RequestFactory requestFactory, ResponseFactory responseFactory, RequestHandler handler) {
      super(clusterId, serviceId);
      m_requestFactory = requestFactory;
      m_responseFactory = responseFactory;
      m_requestHandler = handler;
      m_resourceDef = resourceDef;
      m_clusterId = clusterId;
      m_serviceId = serviceId;
      m_componentId = componentId;
    }

    @Override
    ResourceDefinition createResourceDefinition(String componentName, String clusterName, String serviceName) {
      assertEquals(m_clusterId, clusterName);
      assertEquals(m_serviceId, serviceName);
      assertEquals(m_componentId, componentName);
      return m_resourceDef;
    }

    @Override
    RequestFactory getRequestFactory() {
      return m_requestFactory;
    }

    @Override
    ResponseFactory getResponseFactory() {
      return m_responseFactory;
    }

    @Override
    RequestHandler getRequestHandler() {
      return m_requestHandler;
    }
  }
}
