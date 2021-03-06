/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
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
package org.switchyard.component.common.knowledge.config.model.v1;

import static org.switchyard.component.common.knowledge.config.model.ActionsModel.ACTIONS;
import static org.switchyard.component.common.knowledge.config.model.ChannelModel.CHANNEL;
import static org.switchyard.component.common.knowledge.config.model.ChannelsModel.CHANNELS;
import static org.switchyard.component.common.knowledge.config.model.ContainerModel.CONTAINER;
import static org.switchyard.component.common.knowledge.config.model.ListenerModel.LISTENER;
import static org.switchyard.component.common.knowledge.config.model.ListenersModel.LISTENERS;
import static org.switchyard.component.common.knowledge.config.model.LoggerModel.LOGGER;
import static org.switchyard.component.common.knowledge.config.model.LoggersModel.LOGGERS;
import static org.switchyard.component.common.knowledge.config.model.ManifestModel.MANIFEST;
import static org.switchyard.component.common.knowledge.config.model.MappingModel.MAPPING;
import static org.switchyard.component.common.knowledge.config.model.MappingsModel.GLOBALS;
import static org.switchyard.component.common.knowledge.config.model.MappingsModel.INPUTS;
import static org.switchyard.component.common.knowledge.config.model.MappingsModel.MAPPINGS;
import static org.switchyard.component.common.knowledge.config.model.MappingsModel.OUTPUTS;
import static org.switchyard.config.model.property.PropertiesModel.PROPERTIES;
import static org.switchyard.config.model.property.PropertyModel.PROPERTY;
import static org.switchyard.config.model.resource.ResourceModel.RESOURCE;
import static org.switchyard.config.model.resource.ResourcesModel.RESOURCES;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.Model;
import org.switchyard.config.model.composite.v1.V1CompositeMarshaller;
import org.switchyard.config.model.property.v1.V1PropertiesModel;
import org.switchyard.config.model.property.v1.V1PropertyModel;
import org.switchyard.config.model.resource.v1.V1ResourceModel;
import org.switchyard.config.model.resource.v1.V1ResourcesModel;

/**
 * A CompositeMarshaller which can also create knowledge models.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; &copy; 2012 Red Hat Inc.
 */
public class V1KnowledgeMarshaller extends V1CompositeMarshaller {

    /**
     * Required constructor called via reflection.
     *
     * @param desc the Descriptor
     */
    public V1KnowledgeMarshaller(Descriptor desc) {
        super(desc);
    }

    /**
     * Reads in the Configuration, looking for various knowledge models.
     * If not found, it falls back to the super class (V1CompositeMarshaller).
     *
     * @param config the Configuration
     * @return the Model
     */
    @Override
    public Model read(Configuration config) {
        String name = config.getName();
        Descriptor desc = getDescriptor();
        if (ACTIONS.equals(name)) {
            return new V1ActionsModel(config, desc);
        } else if (CHANNELS.equals(name)) {
            return new V1ChannelsModel(config, desc);
        } else if (CHANNEL.equals(name)) {
            return new V1ChannelModel(config, desc);
        } else if (CONTAINER.equals(name)) {
            return new V1ContainerModel(config, desc);
        } else if (LISTENERS.equals(name)) {
            return new V1ListenersModel(config, desc);
        } else if (LISTENER.equals(name)) {
            return new V1ListenerModel(config, desc);
        } else if (LOGGERS.equals(name)) {
            return new V1LoggersModel(config, desc);
        } else if (LOGGER.equals(name)) {
            return new V1LoggerModel(config, desc);
        } else if (MANIFEST.equals(name)) {
            return new V1ManifestModel(config, desc);
        } else if (MAPPINGS.equals(name) || GLOBALS.equals(name) || INPUTS.equals(name) || OUTPUTS.equals(name)) {
            return new V1MappingsModel(config, desc);
        } else if (MAPPING.equals(name)) {
            return new V1MappingModel(config, desc);
        } else if (PROPERTIES.equals(name)) {
            return new V1PropertiesModel(config, desc);
        } else if (PROPERTY.equals(name)) {
            return new V1PropertyModel(config, desc);
        } else if (RESOURCES.equals(name)) {
            return new V1ResourcesModel(config, desc);
        } else if (RESOURCE.equals(name)) {
            return new V1ResourceModel(config, desc);
        }
        return super.read(config);
    }

}
