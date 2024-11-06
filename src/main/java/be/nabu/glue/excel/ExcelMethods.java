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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import be.nabu.glue.annotations.GlueMethod;
import be.nabu.glue.core.impl.methods.ScriptMethods;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.utils.excel.ExcelParser;
import be.nabu.utils.excel.FileType;

@MethodProviderClass(namespace = "excel")
public class ExcelMethods {
	
	@GlueMethod(version = 1)
	public static String [] sheets(Object content) throws IOException {
		String fileType = "xlsx";
		if (content instanceof String && ((String) content).endsWith(".xls")) {
			fileType = "xls";
		}
		return sheets(content, fileType);
	}
	
	@GlueMethod(version = 1)
	@SuppressWarnings("resource")
	public static String[] sheets(Object content, String fileType) throws IOException {
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(content)), FileType.valueOf(fileType.toUpperCase()), null);
		String [] sheets = new String[parser.getWorkbook().getNumberOfSheets()];
		for (int i = 0; i < sheets.length; i++) {
			sheets[i] = parser.getWorkbook().getSheetName(i);
		}
		return sheets;
	}
	
	@GlueMethod(version = 1)
	public static Object[][] excel(Object content, String sheetName) throws IOException {
		String fileType = "xlsx";
		if (content instanceof String && ((String) content).endsWith(".xls")) {
			fileType = "xls";
		}
		return excel(content, sheetName, fileType);
	}
	
	@GlueMethod(version = 1)
	@SuppressWarnings("resource")
	public static Object[][] excel(Object content, String sheetName, String fileType) throws IOException {
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(content)), fileType == null ? FileType.XLSX : FileType.valueOf(fileType.toUpperCase()), null);
		return parser.parse(parser.getSheet(sheetName, true));
	}
}
