package be.nabu.glue.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.utils.excel.MatrixUtils;

@MethodProviderClass(namespace = "matrix")
public class MatrixMethods {
	
	public static Object[][] rotate(Object[][] matrix) {
		return MatrixUtils.rotate(matrix);
	}
	
	public static String[][] stringifyMatrix(Object [][] objects) {
		return stringifyMatrix(objects, "yyyy-MM-dd'T'HH:mm:ss.SSS");
	}
	
	public static String[][] stringifyMatrix(Object [][] objects, String dateFormat) {
		return stringifyMatrix(objects, dateFormat, null, null);
	}
	
	public static String[][] stringifyMatrix(Object [][] objects, String dateFormat, TimeZone timezone) {
		return stringifyMatrix(objects, dateFormat, timezone, null);
	}
	
	public static String[][] stringifyMatrix(Object [][] objects, String dateFormat, TimeZone timezone, Integer precision) {
		return MatrixUtils.toString(objects, dateFormat, timezone, precision);
	}

	public static Object[] merge(Object...matrices) {
		List<Object> merged = new ArrayList<Object>();
		for (Object matrix : matrices) {
			if (matrix instanceof Object[]) {
				for (Object single : (Object[]) matrix) {
					merged.add(single);
				}
			}
			else if (matrix instanceof Iterable) {
				for (Object single : (Iterable<?>) matrix) {
					merged.add(single);
				}
			}
			else {
				merged.add(matrix);
			}
		}
		return merged.toArray();
	}
}
