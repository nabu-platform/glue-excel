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
