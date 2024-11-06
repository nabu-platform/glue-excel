/*
* Copyright (C) 2016 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.glue.excel;

import be.nabu.glue.annotations.GlueMethod;
import be.nabu.glue.core.impl.methods.ScriptMethods;
import be.nabu.libs.converter.ConverterFactory;
import be.nabu.libs.converter.api.Converter;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;

@MethodProviderClass(namespace = "excel", caseSensitive = false)
public class ExcelFunctions {
	
	@GlueMethod(version = 1)
	public static Number SUM(Object...objects) {
		if (objects == null) {
			return null;
		}
		Double sumDouble = 0d;
		Long sumLong = 0l;
		boolean useDecimal = false;
		Converter converter = ConverterFactory.getInstance().getConverter();
		for (Object object : ScriptMethods.array(objects)) {
			if (object == null) {
				continue;
			}
			if (!useDecimal && isDecimal(object)) {
				useDecimal = true;
			}
			Double converted = converter.convert(object, Double.class);
			if (converted == null) {
				throw new IllegalArgumentException("Not a number: " + object);
			}
			sumDouble += converted;
			if (!useDecimal) {
				sumLong += converter.convert(object, Long.class);
			}
		}
		if (useDecimal) {
			return sumDouble;
		}
		else {
			return sumLong;
		}
	}
	
	private static boolean isDecimal(Object object) {
		return object instanceof Double || object.getClass().equals(double.class) || object instanceof Float || object.getClass().equals(float.class) || (object instanceof String && ((String) object).contains("."));
	}
	
	@GlueMethod(version = 1)
	public static Number MAX(Object...objects) {
		if (objects == null) {
			return null;
		}
		Converter converter = ConverterFactory.getInstance().getConverter();
		Number max = null;
		boolean useDecimal = false;
		for (Object object : ScriptMethods.array(objects)) {
			if (object == null) {
				continue;
			}
			if (!useDecimal && isDecimal(object)) {
				useDecimal = true;
				max = converter.convert(max, Double.class);
			}
			Number converted = converter.convert(object, useDecimal ? Double.class : Long.class);
			if (converted == null) {
				throw new IllegalArgumentException("Not a number: " + object);
			}
			if (max == null || (useDecimal && ((Double) converted).compareTo((Double) max) > 0) || (!useDecimal && ((Long) converted).compareTo((Long) max) > 0)) {
				max = converted;
			}
		}
		return max;
	}
	
	@GlueMethod(version = 1)
	public static Object IF(Boolean useFirst, Object first, Object second) {
		return useFirst != null && useFirst ? first : second;
	}
}
