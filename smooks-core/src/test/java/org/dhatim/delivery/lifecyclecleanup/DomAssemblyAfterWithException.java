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
package org.dhatim.delivery.lifecyclecleanup;

import org.dhatim.SmooksException;
import org.dhatim.container.ExecutionContext;
import org.dhatim.delivery.ExecutionLifecycleCleanable;
import org.dhatim.delivery.dom.DOMVisitAfter;
import org.dhatim.delivery.dom.Phase;
import org.dhatim.delivery.dom.VisitPhase;
import org.w3c.dom.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@Phase(value = VisitPhase.ASSEMBLY)
public class DomAssemblyAfterWithException implements DOMVisitAfter, ExecutionLifecycleCleanable {

    public static boolean cleaned;

    public void visitAfter(Element element, ExecutionContext executionContext) throws SmooksException {
        if(cleaned) {
            fail("Resource shouldn't be clened yet!");
        }
    }

    public void executeExecutionLifecycleCleanup(ExecutionContext executionContext) {
        cleaned = true;
        throw new RuntimeException("Blah");
    }
}