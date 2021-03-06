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
package org.switchyard.component.rules.config.model;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.builder.ReleaseId;
import org.kie.event.rule.DebugWorkingMemoryEventListener;
import org.kie.runtime.Channel;
import org.switchyard.Scope;
import org.switchyard.common.io.pull.StringPuller;
import org.switchyard.common.io.resource.ResourceType;
import org.switchyard.common.type.Classes;
import org.switchyard.component.common.knowledge.LoggerType;
import org.switchyard.component.common.knowledge.config.model.ActionModel;
import org.switchyard.component.common.knowledge.config.model.ChannelModel;
import org.switchyard.component.common.knowledge.config.model.ContainerModel;
import org.switchyard.component.common.knowledge.config.model.ListenerModel;
import org.switchyard.component.common.knowledge.config.model.LoggerModel;
import org.switchyard.component.common.knowledge.config.model.ManifestModel;
import org.switchyard.component.common.knowledge.config.model.MappingModel;
import org.switchyard.component.common.knowledge.expression.Expression;
import org.switchyard.component.common.knowledge.expression.ExpressionFactory;
import org.switchyard.component.common.knowledge.expression.ExpressionType;
import org.switchyard.component.common.knowledge.util.Containers;
import org.switchyard.component.rules.RulesActionType;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.Scanner;
import org.switchyard.config.model.ScannerInput;
import org.switchyard.config.model.ScannerOutput;
import org.switchyard.config.model.composite.ComponentImplementationModel;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.config.model.composite.CompositeModel;
import org.switchyard.config.model.property.PropertyModel;
import org.switchyard.config.model.resource.ResourceModel;
import org.switchyard.config.model.resource.ResourcesModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * Tests Rules models.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; &copy; 2012 Red Hat Inc.
 */
public class RulesModelTests {

    private static final String CONTAINER_XML = "/org/switchyard/component/rules/config/model/RulesModelTests-Container.xml";
    private static final String RESOURCES_XML = "/org/switchyard/component/rules/config/model/RulesModelTests-Resources.xml";

    private ModelPuller<SwitchYardModel> _puller;

    @Before
    public void before() throws Exception {
        _puller = new ModelPuller<SwitchYardModel>();
    }

    @After
    public void after() throws Exception {
        _puller = null;
    }

    @Test
    public void testReadContainer() throws Exception {
        doTestRead(CONTAINER_XML);
    }

    @Test
    public void testReadResources() throws Exception {
        doTestRead(RESOURCES_XML);
    }

    private void doTestRead(String xml) throws Exception {
        ClassLoader loader = getClass().getClassLoader();
        doTestModel(_puller.pull(xml, loader), xml, loader);
    }

    private void doTestModel(SwitchYardModel switchyard, String xml, ClassLoader loader) throws Exception {
        CompositeModel composite = switchyard.getComposite();
        ComponentModel component = null;
        for (ComponentModel c : composite.getComponents()) {
            if (DoStuffRules.class.getSimpleName().equals(c.getName())) {
                component = c;
                break;
            }
        }
        ComponentImplementationModel implementation = component.getImplementation();
        Assert.assertTrue(implementation instanceof RulesComponentImplementationModel);
        RulesComponentImplementationModel rules = (RulesComponentImplementationModel)implementation;
        Assert.assertEquals("rules", rules.getType());
        ActionModel action = rules.getActions().getActions().get(0);
        Assert.assertEquals("theId", action.getId());
        Assert.assertEquals("process", action.getOperation());
        Assert.assertEquals(RulesActionType.FIRE_UNTIL_HALT, action.getType());
        MappingModel globalMapping = action.getGlobals().getMappings().get(0);
        Expression globalExpression = ExpressionFactory.instance().create(globalMapping);
        Assert.assertEquals("context['foobar']", globalExpression.getExpression());
        Assert.assertEquals(ExpressionType.MVEL, globalExpression.getType());
        Assert.assertTrue(globalMapping.getScope() == null || globalMapping.getScope().equals(Scope.EXCHANGE));
        Assert.assertEquals("exchangeVar", globalMapping.getVariable());
        MappingModel inputMapping = action.getInputs().getMappings().get(0);
        Expression inputExpression = ExpressionFactory.instance().create(inputMapping);
        Assert.assertEquals("message.content.nested", inputExpression.getExpression());
        Assert.assertEquals(ExpressionType.MVEL, inputExpression.getType());
        Assert.assertEquals(Scope.MESSAGE, inputMapping.getScope());
        Assert.assertEquals("inputVar", inputMapping.getVariable());
        MappingModel outputMapping = action.getOutputs().getMappings().get(0);
        Expression outputExpression = ExpressionFactory.instance().create(outputMapping);
        Assert.assertEquals("message.content", outputExpression.getExpression());
        Assert.assertEquals(ExpressionType.MVEL, outputExpression.getType());
        Assert.assertEquals(Scope.MESSAGE, outputMapping.getScope());
        Assert.assertEquals("outputVar", outputMapping.getVariable());
        ChannelModel channel = rules.getChannels().getChannels().get(0);
        Assert.assertEquals(TestChannel.class, channel.getClazz(loader));
        Assert.assertEquals("theName", channel.getName());
        Assert.assertEquals("theOperation", channel.getOperation());
        Assert.assertEquals("theReference", channel.getReference());
        ListenerModel listener = rules.getListeners().getListeners().get(0);
        Assert.assertEquals(DebugWorkingMemoryEventListener.class, listener.getClazz(loader));
        LoggerModel logger = rules.getLoggers().getLoggers().get(0);
        Assert.assertEquals(Integer.valueOf(2000), logger.getInterval());
        Assert.assertEquals("theLog", logger.getLog());
        Assert.assertEquals(LoggerType.CONSOLE, logger.getType());
        ManifestModel manifest = rules.getManifest();
        ContainerModel container = manifest.getContainer();
        ResourcesModel resources = manifest.getResources();
        Assert.assertTrue((container != null && resources == null) || (container == null && resources != null));
        if (CONTAINER_XML.equals(xml)) {
            ReleaseId rid = Containers.toReleaseId(container.getReleaseId());
            Assert.assertEquals("theGroupId", rid.getGroupId());
            Assert.assertEquals("theArtifactId", rid.getArtifactId());
            Assert.assertEquals("theVersion", rid.getVersion());
            Assert.assertEquals("theBase", container.getBaseName());
            Assert.assertEquals("theSession", container.getSessionName());
            Assert.assertTrue(container.isScan());
            Assert.assertEquals(Long.valueOf(1000), container.getScanInterval());
            Assert.assertNull(resources);
        } else if (RESOURCES_XML.equals(xml)) {
            Assert.assertNull(container);
            ResourceModel resource1 = resources.getResources().get(0);
            Assert.assertEquals("foo.drl", resource1.getLocation());
            Assert.assertEquals(ResourceType.valueOf("DRL"), resource1.getType());
            ResourceModel resource2 = resources.getResources().get(1);
            Assert.assertEquals("bar.dsl", resource2.getLocation());
            Assert.assertEquals(ResourceType.valueOf("DSL"), resource2.getType());
        }
        PropertyModel property = rules.getProperties().getProperties().get(0);
        Assert.assertEquals("foo", property.getName());
        Assert.assertEquals("bar", property.getValue());
    }

    @Test
    public void testWriteContainer() throws Exception {
        doTestWrite(CONTAINER_XML);
    }

    @Test
    public void testWriteResources() throws Exception {
        doTestWrite(RESOURCES_XML);
    }

    private void doTestWrite(String xml) throws Exception {
        String old_xml = new StringPuller().pull(xml, getClass());
        SwitchYardModel switchyard = _puller.pull(new StringReader(old_xml));
        String new_xml = switchyard.toString();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(old_xml, new_xml);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    @Test
    public void testValidateContainer() throws Exception {
        doTestValidate(CONTAINER_XML);
    }

    @Test
    public void testValidateResources() throws Exception {
        doTestValidate(RESOURCES_XML);
    }

    private void doTestValidate(String xml) throws Exception {
        SwitchYardModel switchyard = _puller.pull(xml, getClass());
        switchyard.assertModelValid();
    }

    /*
    @Test
    public void testScanContainer() throws Exception {
        doTestScan(CONTAINER_XML);
    }
    */

    @Test
    public void testScanResources() throws Exception {
        doTestScan(RESOURCES_XML);
    }

    private void doTestScan(String xml) throws Exception {
        ClassLoader loader = getClass().getClassLoader();
        Scanner<SwitchYardModel> scanner = new RulesSwitchYardScanner();
        ScannerInput<SwitchYardModel> input = new ScannerInput<SwitchYardModel>().setName(getClass().getSimpleName());
        List<URL> urls = new ArrayList<URL>();
        String resPath = getClass().getName().replaceAll("\\.", "/") + ".class";
        String urlPath = Classes.getResource(resPath).getPath();
        File file = new File(urlPath.substring(0, urlPath.length() - resPath.length()));
        urls.add(file.toURI().toURL());
        input.setURLs(urls);
        ScannerOutput<SwitchYardModel> output = scanner.scan(input);
        SwitchYardModel model = output.getModel();
        CompositeModel composite = model.getComposite();
        Assert.assertEquals(getClass().getSimpleName(), composite.getName());
        doTestModel(model, xml, loader);
    }

    @Test
    public void testScanEmpty() throws Exception {
        Scanner<SwitchYardModel> scanner = new RulesSwitchYardScanner();
        ScannerInput<SwitchYardModel> input = new ScannerInput<SwitchYardModel>();
        ScannerOutput<SwitchYardModel> output = scanner.scan(input);
        Assert.assertNull("Composite element should not be created if no components were found.", output.getModel().getComposite());
    }

    public static final class TestChannel implements Channel {
        @Override
        public void send(Object object) {
            System.out.println(object);
        }
    }

}
