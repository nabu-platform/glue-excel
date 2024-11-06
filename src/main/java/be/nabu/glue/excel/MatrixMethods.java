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

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import be.nabu.glue.annotations.GlueMethod;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.utils.excel.MatrixUtils;

@MethodProviderClass(namespace = "matrix")
public class MatrixMethods {
	
	@GlueMethod(version = 1)
	public static Object[][] rotate(Object[][] matrix) {
		return MatrixUtils.rotate(matrix);
	}
	
	@GlueMethod(version = 1)
	public static String[][] stringifyMatrix(Object [][] objects) {
		return stringifyMatrix(objects, "yyyy-MM-dd'T'HH:mm:ss.SSS");
	}
	
	@GlueMethod(version = 1)
	public static String[][] stringifyMatrix(Object [][] objects, String dateFormat) {
		return stringifyMatrix(objects, dateFormat, null, null);
	}
	
	@GlueMethod(version = 1)
	public static String[][] stringifyMatrix(Object [][] objects, String dateFormat, TimeZone timezone) {
		return stringifyMatrix(objects, dateFormat, timezone, null);
	}
	
	@GlueMethod(version = 1)
	public static String[][] stringifyMatrix(Object [][] objects, String dateFormat, TimeZone timezone, Integer precision) {
		return MatrixUtils.toString(objects, dateFormat, timezone, precision);
	}

	@GlueMethod(version = 1)
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
