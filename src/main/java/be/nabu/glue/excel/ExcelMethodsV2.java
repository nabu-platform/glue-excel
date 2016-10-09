package be.nabu.glue.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.nabu.glue.annotations.GlueMethod;
import be.nabu.glue.core.impl.methods.ScriptMethods;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.utils.excel.ExcelParser;
import be.nabu.utils.excel.FileType;

@MethodProviderClass(namespace = "excel")
public class ExcelMethodsV2 {
	
	@GlueMethod(version = 2)
	public static List<String> sheets(Object content) throws IOException {
		String fileType = "xlsx";
		if (content instanceof String && ((String) content).endsWith(".xls")) {
			fileType = "xls";
		}
		return sheets(content, fileType);
	}
	
	@GlueMethod(version = 2)
	@SuppressWarnings("resource")
	public static List<String> sheets(Object content, String fileType) throws IOException {
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(content)), FileType.valueOf(fileType.toUpperCase()), null);
		List<String> sheets = new ArrayList<String>();
		for (int i = 0; i < parser.getWorkbook().getNumberOfSheets(); i++) {
			sheets.add(parser.getWorkbook().getSheetName(i));
		}
		return sheets;
	}
	
	@GlueMethod(version = 2)
	public static List<List<?>> sheet(Object content, String sheetName) throws ParseException, IOException {
		String fileType = "xlsx";
		if (content instanceof String && ((String) content).endsWith(".xls")) {
			fileType = "xls";
		}
		return sheet(content, sheetName, fileType);
	}
	
	@GlueMethod(version = 2)
	@SuppressWarnings("resource")
	public static List<List<?>> sheet(Object content, String sheetName, String fileType) throws ParseException, IOException {
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(content)), fileType == null ? FileType.XLSX : FileType.valueOf(fileType.toUpperCase()), null);
		return parser.matrix(parser.getSheet(sheetName, true));
	}
}
