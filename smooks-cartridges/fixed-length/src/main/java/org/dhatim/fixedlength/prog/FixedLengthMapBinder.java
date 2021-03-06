/*
 * Milyn - Copyright (C) 2006 - 2010
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (version 2.1) as published by the Free Software
 * Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details:
 * http://www.gnu.org/licenses/lgpl.txt
 */
package org.dhatim.fixedlength.prog;

import org.dhatim.FilterSettings;
import org.dhatim.Smooks;
import org.dhatim.assertion.AssertArgument;
import org.dhatim.fixedlength.FixedLengthBinding;
import org.dhatim.fixedlength.FixedLengthBindingType;
import org.dhatim.fixedlength.FixedLengthReaderConfigurator;
import org.dhatim.payload.JavaResult;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.UUID;

/**
 * Fixed Length {@link java.util.Map} Binder class.
 * <p/>
 * Simple Fixed Length records to Object {@link java.util.Map} binding class.
 * <p/>
 * Exmaple usage:
 * <pre>
 * public class PeopleBinder {
 *     // Create and cache the binder instance..
 *     private FixedLengthMapBinder binder = new FixedLengthMapBinder("firstname[10],lastname[10],gender[1],age[3],country[2]", Person.class, "firstname");
 *
 *     public Map&lt;String, Person&gt; bind(Reader fixedLengthStream) {
 *         return binder.bind(fixedLengthStream);
 *     }
 * }
 * </pre>
 *
 * @author <a href="mailto:maurice.zeijen@smies.com">maurice.zeijen@smies.com</a>
 */
public class FixedLengthMapBinder {

    private String beanId = UUID.randomUUID().toString();
    private Smooks smooks;

    public FixedLengthMapBinder(String fields, Class recordType, String keyField) {
        AssertArgument.isNotNullAndNotEmpty(fields, "fields");
        AssertArgument.isNotNull(recordType, "recordType");
        AssertArgument.isNotNullAndNotEmpty(keyField, "keyField");

        smooks = new Smooks();
        smooks.setFilterSettings(FilterSettings.DEFAULT_SAX);
        smooks.setReaderConfig(new FixedLengthReaderConfigurator(fields)
                .setBinding(new FixedLengthBinding(beanId, recordType, FixedLengthBindingType.MAP).setKeyField(keyField)));
    }

    public Map bind(Reader fixedLengthStream) {
        AssertArgument.isNotNull(fixedLengthStream, "fixedLengthStream");

        JavaResult javaResult = new JavaResult();

        smooks.filterSource(new StreamSource(fixedLengthStream), javaResult);

        return (Map) javaResult.getBean(beanId);
    }

    public Map bind(InputStream fixedLengthStream) {
        return bind(new InputStreamReader(fixedLengthStream));
    }
}