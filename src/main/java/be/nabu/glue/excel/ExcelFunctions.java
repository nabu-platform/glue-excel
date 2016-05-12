package be.nabu.glue.excel;

import be.nabu.glue.impl.methods.ScriptMethods;
import be.nabu.libs.converter.ConverterFactory;
import be.nabu.libs.converter.api.Converter;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;

@MethodProviderClass(namespace = "excel", caseSensitive = false)
public class ExcelFunctions {
	
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
	
	public static Object IF(Boolean useFirst, Object first, Object second) {
		return useFirst != null && useFirst ? first : second;
	}
}
