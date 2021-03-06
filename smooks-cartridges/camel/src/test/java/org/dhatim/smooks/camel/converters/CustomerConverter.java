/*
 * Milyn - Copyright (C) 2006 - 2010
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (version 2.1) as published
 * by the Free Software Foundation.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License for more details:
 * http://www.gnu.org/licenses/lgpl.txt
 */
package org.dhatim.smooks.camel.converters;

import java.util.Map;

import javax.xml.transform.Source;

import org.apache.camel.Converter;
import org.dhatim.payload.JavaResult;
import org.dhatim.payload.JavaSource;
import org.dhatim.smooks.camel.dataformat.Customer;

/**
 * Converts a JavaResult to a Customer object. 
 * <p/>
 * This converter is only intended for testing purposes.
 * 
 * @author Daniel Bevenius
 *
 */
@Converter
public class CustomerConverter
{
	private CustomerConverter()
	{
	}

	@Converter
	public static Customer toCoordinate(JavaResult result)
	{
		Object singleObject = getSingleObjectFromJavaResult(result);
		return (Customer) singleObject;
	}
	
	private static Object getSingleObjectFromJavaResult(JavaResult result)
	{
		Map<String, Object> resultMap = result.getResultMap();
		if(resultMap.size() == 1) 
		{
			return resultMap.values().iterator().next();
		}
		return null;
	}
	
	@Converter
	public static Source getSource(final Customer customer)
	{
	    JavaSource javaSource = new JavaSource(customer);
	    return javaSource;
	}
}
