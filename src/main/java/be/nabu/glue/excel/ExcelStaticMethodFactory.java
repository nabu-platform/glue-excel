package be.nabu.glue.excel;

import java.util.ArrayList;
import java.util.List;

import be.nabu.glue.api.StaticMethodFactory;

public class ExcelStaticMethodFactory implements StaticMethodFactory {

	@Override
	public List<Class<?>> getStaticMethodClasses() {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(ExcelMethods.class);
		classes.add(ExcelFunctions.class);
		classes.add(MatrixMethods.class);
		return classes;
	}
	
}
