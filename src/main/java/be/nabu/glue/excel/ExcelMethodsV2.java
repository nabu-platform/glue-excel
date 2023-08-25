package be.nabu.glue.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nabu.glue.annotations.GlueMethod;
import be.nabu.glue.core.impl.methods.ScriptMethods;
import be.nabu.libs.evaluator.ContextAccessorFactory;
import be.nabu.libs.evaluator.EvaluationException;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.libs.evaluator.api.ContextAccessor;
import be.nabu.libs.evaluator.api.ListableContextAccessor;
import be.nabu.utils.excel.ExcelParser;
import be.nabu.utils.excel.FileType;
import be.nabu.utils.excel.Template;
import be.nabu.utils.excel.Template.Direction;

@MethodProviderClass(namespace = "excel")
public class ExcelMethodsV2 {
	
	public static byte [] templateExcel(Object template, Object variables, Boolean duplicateAll, Direction direction, Boolean removeNonExistent, String fileType) throws IOException, EvaluationException {
		if (fileType == null) {
			fileType = FileType.XLSX.name();
		}
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(template)), FileType.valueOf(fileType.toUpperCase()), null);
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		Map<String, Object> input = new HashMap<String, Object>();
		if (variables != null) {
			ContextAccessor accessor = ContextAccessorFactory.getInstance().getAccessor(variables.getClass());
			if (accessor instanceof ListableContextAccessor) {
				for (Object entry : ((ListableContextAccessor) accessor).list(variables)) {
					input.put(entry.toString(), accessor.get(variables, entry.toString()));
				}
			}
			else {
				throw new IllegalArgumentException("Can not use this data type for variables: " + variables.getClass());
			}
		}
		Template.substitute(
			parser.getWorkbook(), 
			target, 
			input, 
			duplicateAll != null && duplicateAll, 
			direction == null ? Direction.VERTICAL : direction, 
			removeNonExistent != null && removeNonExistent
		);
		target.flush();
		return target.toByteArray();
	}
	
	@GlueMethod(version = 2)
	public static Map<String, List<List<Object>>> objectify(Object content, String...sheets) throws IOException, ParseException {
		String fileType = "xlsx";
		if (content instanceof String && ((String) content).endsWith(".xls")) {
			fileType = "xls";
		}
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(content)), FileType.valueOf(fileType.toUpperCase()), null);
		try {
			Map<String, List<List<Object>>> matrix = new HashMap<String, List<List<Object>>>();
			List<String> whitelist = sheets == null || sheets.length == 0 ? new ArrayList<String>() : Arrays.asList(sheets);
			for (int i = 0; i < parser.getWorkbook().getNumberOfSheets(); i++) {
				String sheetName = parser.getWorkbook().getSheetName(i);
				if (whitelist.isEmpty() || whitelist.contains(sheetName)) {
					matrix.put(sheetName, parser.matrix(parser.getWorkbook().getSheetAt(i)));
				}
			}
			return matrix;
		}
		finally {
			parser.close();
		}
	}
	
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
	@SuppressWarnings({ "resource", "unchecked" })
	public static List<List<?>> sheet(Object content, String sheetName, String fileType) throws ParseException, IOException {
		ExcelParser parser = new ExcelParser(new ByteArrayInputStream(ScriptMethods.bytes(content)), fileType == null ? FileType.XLSX : FileType.valueOf(fileType.toUpperCase()), null);
		List matrix = parser.matrix(parser.getSheet(sheetName, true));
		return matrix;
	}
}
