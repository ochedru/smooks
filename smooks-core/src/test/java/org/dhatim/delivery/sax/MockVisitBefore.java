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
package org.dhatim.delivery.sax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dhatim.SmooksException;
import org.dhatim.container.ExecutionContext;
import org.dhatim.delivery.dom.DOMVisitBefore;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class MockVisitBefore implements SAXVisitBefore, DOMVisitBefore {

	private List<String> elements = new ArrayList<String>();
	
	/* (non-Javadoc)
	 * @see org.dhatim.delivery.sax.SAXVisitBefore#visitBefore(org.dhatim.delivery.sax.SAXElement, org.dhatim.container.ExecutionContext)
	 */
	public void visitBefore(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
		elements.add(element.getName().getLocalPart());
	}
	
	/* (non-Javadoc)
	 * @see org.dhatim.delivery.dom.DOMVisitBefore#visitBefore(org.w3c.dom.Element, org.dhatim.container.ExecutionContext)
	 */
	public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
		elements.add(element.getLocalName());
	}

	/**
	 * @return the elements
	 */
	public List<String> getElements() {
		return elements;
	}
}
