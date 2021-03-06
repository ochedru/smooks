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
package org.dhatim.visitors.remove;

import org.dhatim.SmooksException;
import org.dhatim.cdr.annotation.ConfigParam;
import org.dhatim.container.ExecutionContext;
import org.dhatim.delivery.Filter;
import org.dhatim.delivery.dom.DOMVisitAfter;
import org.dhatim.delivery.sax.DefaultSAXElementSerializer;
import org.dhatim.delivery.sax.SAXElement;
import org.dhatim.delivery.sax.SAXVisitAfter;
import org.dhatim.delivery.sax.SAXVisitBefore;
import org.dhatim.io.NullWriter;
import org.dhatim.util.FreeMarkerTemplate;
import org.dhatim.xml.DomUtils;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Remove Element.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class RemoveElement implements SAXVisitBefore, SAXVisitAfter, DOMVisitAfter {

    private boolean keepChildren;

    @ConfigParam(defaultVal = "false")
    public RemoveElement setKeepChildren(boolean keepChildren) {
        this.keepChildren = keepChildren;
        return this;
    }

    public void visitBefore(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
        // Claim ownership of the writer for this fragment element...
        Writer writer = element.getWriter(this);

        if(!keepChildren) {
            // Swap in a NullWriter instance for the whole fragment...
            element.setWriter(new NullWriter(), this);
            // Stash the real writer instance on the element so we can reset it at the end...
            element.setCache(this, writer);
        } else {
            // Just don't write this element, but write the child elements...
        }
    }

    public void visitAfter(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
        if(!keepChildren) {
            // Reset the writer...
            element.setWriter((Writer) element.getCache(this), this);
        }
    }

    public void visitAfter(Element element, ExecutionContext executionContext) throws SmooksException {
        DomUtils.removeElement(element, keepChildren);
    }
}
 