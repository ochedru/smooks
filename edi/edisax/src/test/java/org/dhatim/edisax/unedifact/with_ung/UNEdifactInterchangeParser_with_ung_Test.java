/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package org.dhatim.edisax.unedifact.with_ung;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.dhatim.edisax.EDIConfigurationException;
import org.dhatim.edisax.EDIParseException;
import org.dhatim.edisax.EDIParser;
import org.dhatim.edisax.MockContentHandler;
import org.dhatim.edisax.MockContentHandlerNS;
import org.dhatim.edisax.model.EdifactModel;
import org.dhatim.edisax.unedifact.UNEdifactInterchangeParser;
import org.dhatim.edisax.registry.DefaultMappingsRegistry;
import org.dhatim.namespace.NamespaceAwareHandler;
import org.dhatim.namespace.NamespaceDeclarationStack;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class UNEdifactInterchangeParser_with_ung_Test {

        @Test
	public void test_with_groupref() throws IOException, SAXException, EDIConfigurationException {
		EdifactModel model1 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG1-model.xml"));
		EdifactModel model2 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG2-model.xml"));
		
		UNEdifactInterchangeParser parser = new UNEdifactInterchangeParser();
		parser.setMappingsRegistry(new DefaultMappingsRegistry(model1, model2));
		parser.ignoreNewLines(true);

		MockContentHandlerNS handler;
		
		// Test message 01 - no UNA segment...
		handler = new MockContentHandlerNS();
        NamespaceDeclarationStack namespaceDeclarationStack = new NamespaceDeclarationStack(parser);

        parser.setContentHandler(new NamespaceAwareHandler(handler, namespaceDeclarationStack));
		parser.parse(new InputSource(getClass().getResourceAsStream("unedifact-msg-01.edi")));		
		//System.out.println(handler.xmlMapping);
        XMLUnit.setIgnoreWhitespace( true );
        XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("unedifact-msg-expected-01.xml")), new StringReader(handler.xmlMapping.toString()));

		// Test message 01 - has a UNA segment...
		handler = new MockContentHandlerNS();
		parser.setContentHandler(handler);		
		parser.parse(new InputSource(getClass().getResourceAsStream("unedifact-msg-02.edi")));
		
//		System.out.println(handler.xmlMapping);
        XMLUnit.setIgnoreWhitespace( true );
        XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("unedifact-msg-expected-01.xml")), new StringReader(handler.xmlMapping.toString()));
	}

        @Test
	public void test_without_groupref() throws IOException, SAXException, EDIConfigurationException {
		EdifactModel model1 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG1-model.xml"));
		EdifactModel model2 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG2-model.xml"));
		
		UNEdifactInterchangeParser parser = new UNEdifactInterchangeParser();
		parser.setMappingsRegistry(new DefaultMappingsRegistry(model1, model2));
		parser.ignoreNewLines(true);

		MockContentHandler handler;
		
		handler = new MockContentHandler();
        NamespaceDeclarationStack namespaceDeclarationStack = new NamespaceDeclarationStack(parser);
        parser.setContentHandler(new NamespaceAwareHandler(handler, namespaceDeclarationStack));

		try {
			parser.parse(new InputSource(getClass().getResourceAsStream("unedifact-msg-03.edi")));
			fail("Expected EDIParseException");
		} catch(EDIParseException e) {
			assertEquals("EDI message processing failed [EDI Message Interchange Control Model][N/A].  Segment [UNG], field 5 (groupRef) expected to contain a value.  Currently at segment number 2.", e.getMessage());
		}
	}

        @Test
	public void test_full_group_header() throws IOException, SAXException, EDIConfigurationException {
		EdifactModel model1 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG1-model.xml"));
		EdifactModel model2 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG2-model.xml"));
		
		UNEdifactInterchangeParser parser = new UNEdifactInterchangeParser();
		parser.setMappingsRegistry(new DefaultMappingsRegistry(model1, model2));
		parser.ignoreNewLines(true);

		MockContentHandlerNS handler;
		
		// Test message 01 - no UNA segment...
		handler = new MockContentHandlerNS();
        NamespaceDeclarationStack namespaceDeclarationStack = new NamespaceDeclarationStack(parser);
        parser.setContentHandler(new NamespaceAwareHandler(handler, namespaceDeclarationStack));
		parser.parse(new InputSource(getClass().getResourceAsStream("unedifact-msg-04.edi")));
		//System.out.println(handler.xmlMapping);
        XMLUnit.setIgnoreWhitespace( true );
        XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("unedifact-msg-expected-02.xml")), new StringReader(handler.xmlMapping.toString()));
	}

        @Test
	public void test_with_unknown_ucd_segment() throws IOException, SAXException, EDIConfigurationException {
		EdifactModel model1 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG1-model.xml"));
		EdifactModel model2 = EDIParser.parseMappingModel(getClass().getResourceAsStream("../MSG2-model.xml"));
		
		UNEdifactInterchangeParser parser = new UNEdifactInterchangeParser();
		parser.setMappingsRegistry(new DefaultMappingsRegistry(model1, model2));
		parser.ignoreNewLines(true);

		MockContentHandler handler;
		
		// Test message 01 - no UNA segment...
		handler = new MockContentHandler();
        NamespaceDeclarationStack namespaceDeclarationStack = new NamespaceDeclarationStack(parser);
        parser.setContentHandler(new NamespaceAwareHandler(handler, namespaceDeclarationStack));
		parser.parse(new InputSource(getClass().getResourceAsStream("unedifact-msg-05.edi")));
//		System.out.println(handler.xmlMapping);
        XMLUnit.setIgnoreWhitespace( true );
        XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("unedifact-msg-expected-03.xml")), new StringReader(handler.xmlMapping.toString()));
	}
}
