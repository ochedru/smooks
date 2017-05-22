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
package org.dhatim.delivery;

import org.dhatim.SmooksException;
import org.dhatim.container.ExecutionContext;
import org.dhatim.delivery.dom.DOMElementVisitor;
import org.dhatim.delivery.sax.SAXElement;
import org.dhatim.delivery.sax.SAXElementVisitor;
import org.dhatim.delivery.sax.SAXText;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SAXAndDOMVisitor implements DOMElementVisitor, SAXElementVisitor {

    public static boolean visited = false;
    
    public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
        visited = true;
    }

    public void visitAfter(Element element, ExecutionContext executionContext) throws SmooksException {
        visited = true;
    }

    public void visitBefore(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
        visited = true;
    }

    public void onChildText(SAXElement element, SAXText text, ExecutionContext executionContext) throws SmooksException, IOException {
        visited = true;
    }

    public void onChildElement(SAXElement element, SAXElement childElement, ExecutionContext executionContext) throws SmooksException, IOException {
        visited = true;
    }

    public void visitAfter(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
        visited = true;
    }
}
