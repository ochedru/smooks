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
package org.dhatim.event.report;

import org.dhatim.cdr.SmooksResourceConfiguration;
import org.dhatim.delivery.VisitSequence;
import org.dhatim.event.types.*;
import org.dhatim.event.ExecutionEvent;
import org.dhatim.event.report.model.Report;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Flat Execution Report generating {@link org.dhatim.event.ExecutionEventListener}.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class FlatReportGenerator extends AbstractReportGenerator {

    public FlatReportGenerator(Writer outputWriter) {
        this(new ReportConfiguration(outputWriter));
    }

    public FlatReportGenerator(ReportConfiguration reportConfiguration) {
        super(reportConfiguration);
    }

    public void applyTemplate(Report report) throws IOException {
    }
}