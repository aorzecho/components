/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.switchyard.component.soap.config.model;

import javax.xml.namespace.QName;

import org.switchyard.common.net.SocketAddr;
import org.switchyard.component.soap.PortName;
import org.switchyard.config.Configuration;
import org.switchyard.config.model.composite.BindingModel;

/**
 * A SOAPBinding Model.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; &copy; 2013 Red Hat Inc.
 */
public interface SOAPBindingModel extends BindingModel {

    /**
     *  Prefix for SOAP Gateway Configuration.
     */
    public static final String SOAP = "soap";

    /**
     * Default namespace for SOAP Gateway Configuration.
     */
    public static final String DEFAULT_NAMESPACE =  "urn:switchyard-component-soap:config:1.0";

    /**
     * Gets the SOAPContextMapperModel.
     * @return the SOAPContextMapperModel
     */
    public SOAPContextMapperModel getSOAPContextMapper();

    /**
     * Gets the SOAPMessageComposerModel.
     * @return the SOAPMessageComposerModel
     */
    public SOAPMessageComposerModel getSOAPMessageComposer();

    /**
     * Gets the global environment configuration.
     * @return  global environment configuration
     */
    public Configuration getEnvironment();

    /**
     * Sets the global environment configuration.
     * @param environment the global environment configuration
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setEnvironment(Configuration environment);

    /**
     * Returns the WebService Service name.
     * @return the serviceName
     */
    public QName getServiceName();

    /**
     * Sets the WebService Service name.
     * @param serviceName the serviceName to set
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setServiceName(QName serviceName);

    /**
     * Returns the WebService WSDL.
     * @return the wsdl
     */
    public String getWsdl();

    /**
     * Sets the WebService WSDL.
     * @param wsdl the wsdl to set
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setWsdl(String wsdl);

    /**
     * Returns the WebService port.
     * @return the port
     */
    public PortName getPort();

    /**
     * Sets the WebService port.
     * @param port the port to set
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setPort(PortName port);

    /**
     * Returns the IP Socket Address where the WebService will be hosted.
     * @return the IP Socket Address
     */
    public SocketAddr getSocketAddr();

    /**
     * Sets the IP Socket Address where the WebService will be hosted.
     * @param socketAddr the IP Socket Address to set
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setSocketAddr(SocketAddr socketAddr);

    /**
     * Gets the extra context path of the WebService.
     * @return the contextPath
     */
    public String getContextPath();

    /**
     * Sets the extra context path of the WebService.
     * @param contextPath the contextPath to set
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setContextPath(String contextPath);

    /**
     * Gets the target endpoint address of the WebService.
     * @return the endpoint address
     */
    public String getEndpointAddress();

    /**
     * Sets the target endpoint address of the WebService.
     * This overrides the address set inside the WSDL.
     * @param endpointAddress the endpoint address to set
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setEndpointAddress(String endpointAddress);

    /**
     * Gets the endpointConfig.
     * @return the endpointConfig
     */
    public EndpointConfigModel getEndpointConfig();

    /**
     * Sets the endpointConfig.
     * @param endpointConfig the endpointConfig
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setEndpointConfig(EndpointConfigModel endpointConfig);

    /**
     * Gets the inInterceptors.
     * @return the inInterceptors
     */
    public InterceptorsModel getInInterceptors();

    /**
     * Sets the inInterceptors.
     * @param inInterceptors the inInterceptors
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setInInterceptors(InterceptorsModel inInterceptors);

    /**
     * Gets the outInterceptors.
     * @return the outInterceptors
     */
    public InterceptorsModel getOutInterceptors();

    /**
     * Sets the outInterceptors.
     * @param outInterceptors the outInterceptors
     * @return this SOAPBindingModel (useful for chaining)
     */
    public SOAPBindingModel setOutInterceptors(InterceptorsModel outInterceptors);

}
