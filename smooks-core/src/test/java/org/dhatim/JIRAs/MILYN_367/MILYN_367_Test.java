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
package org.dhatim.JIRAs.MILYN_367;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.dhatim.FilterSettings;
import org.dhatim.Smooks;
import org.dhatim.SmooksException;
import org.dhatim.StreamFilterType;
import org.dhatim.container.ExecutionContext;
import org.dhatim.delivery.dom.DOMVisitAfter;
import org.dhatim.delivery.dom.DOMVisitBefore;
import org.dhatim.delivery.dom.serialize.DefaultSerializationUnit;
import org.dhatim.delivery.sax.DefaultSAXElementSerializer;
import org.dhatim.payload.StringResult;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class MILYN_367_Test {

	@Test
	public void test_SAX_01() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSAXElementSerializer(), "#document");
		smooks.addVisitor(new DefaultSAXElementSerializer(), "#document/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.SAX));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_01.xml", result);        
	}

	@Test
	public void test_SAX_02() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSAXElementSerializer(), "customer");
		smooks.addVisitor(new DefaultSAXElementSerializer(), "customer/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.SAX));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_02.xml", result);        
	}

	@Test
	public void test_SAX_03() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSAXElementSerializer(), "items");
		smooks.addVisitor(new DefaultSAXElementSerializer(), "items/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.SAX));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_03.xml", result);        
	}

	@Test
	public void test_DOM_01() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSerializationUnit(), "#document");
		smooks.addVisitor(new DefaultSerializationUnit(), "#document/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.DOM));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_01.xml", result);        
	}

	@Test
	public void test_DOM_02() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSerializationUnit(), "customer");
		smooks.addVisitor(new DefaultSerializationUnit(), "customer/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.DOM));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_02.xml", result);        
	}

	@Test
	public void test_DOM_03() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSerializationUnit(), "items");
		smooks.addVisitor(new DefaultSerializationUnit(), "items/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.DOM));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_03.xml", result);        
	}

	@Test
	public void test_DOM_04() throws SAXException, IOException {
		Smooks smooks = new Smooks();		
		DOMVBefore customerVisitor = new DOMVBefore();
		DOMVBefore itemsVisitor = new DOMVBefore();
		
		smooks.addVisitor(customerVisitor, "customer");
		smooks.addVisitor(customerVisitor, "customer/**");
		smooks.addVisitor(itemsVisitor, "items");
		smooks.addVisitor(itemsVisitor, "items/**");
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")));
		
		assertEquals("customer-user-fname-x-lname-", customerVisitor.stringBuilder.toString());
		assertEquals("items-item-units-name-price-item-units-name-price-", itemsVisitor.stringBuilder.toString());
	}

	@Test
	public void test_DOM_05() throws SAXException, IOException {
		Smooks smooks = new Smooks();		
		DOMVAfter customerVisitor = new DOMVAfter();
		DOMVAfter itemsVisitor = new DOMVAfter();
		
		smooks.addVisitor(customerVisitor, "customer");
		smooks.addVisitor(customerVisitor, "customer/**");
		smooks.addVisitor(itemsVisitor, "items");
		smooks.addVisitor(itemsVisitor, "items/**");
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")));
		
		assertEquals("user-x-fname-lname-customer-", customerVisitor.stringBuilder.toString());
		assertEquals("units-name-price-item-units-name-price-item-items-", itemsVisitor.stringBuilder.toString());
	}

	private void assertOK(String resName, StringResult result) throws SAXException, IOException {
		XMLUnit.setIgnoreWhitespace(true);
		XMLAssert.assertXMLEqual(getRes(resName), new StringReader(result.getResult()));
	}	
	
	private Reader getRes(String name) {
		return new InputStreamReader(getClass().getResourceAsStream(name));
	}
	
	private class DOMVBefore implements DOMVisitBefore {
		
		private StringBuilder stringBuilder = new StringBuilder();
		
		public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
			stringBuilder.append(element.getLocalName() + "-");		
		}		
	}
	
	private class DOMVAfter implements DOMVisitAfter {
		
		private StringBuilder stringBuilder = new StringBuilder();
		
		public void visitAfter(Element element, ExecutionContext executionContext) throws SmooksException {
			stringBuilder.append(element.getLocalName() + "-");		
		}		
	}
}
