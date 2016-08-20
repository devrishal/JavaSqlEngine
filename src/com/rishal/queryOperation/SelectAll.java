package com.rishal.queryOperation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rishal.application.LoadData;
import com.rishal.condtionEval.EvalCondtion;
import com.rishal.exception.InvalidSqlException;
import com.rishal.valueObjects.TableData;

public class SelectAll {
	public void display(String query) {
		String criteriaCol = null;
		String compareSign = null;
		String criteriaColValue = null;
		String headerarr[] = LoadData.headerString.split(",");

		String pattern = "(\\w+\\s*)(>|<|>=|<=|!=|=)(\\s*\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(query.trim());
		if (m.find()) {
			criteriaCol = m.group(1);
			compareSign = m.group(2);
			criteriaColValue = m.group(3);
		}

		try {
			process(criteriaCol, criteriaColValue, LoadData.myData, headerarr,
					compareSign);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IntrospectionException
				| InvalidSqlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void process(String columnName, String columnValue,
			ArrayList<TableData> list, String header[], String CompareSign)
			throws IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InvalidSqlException {
		Object f = null;
		Method getter = null;
		for (int i = 0; i < header.length; i++) {

			if (columnName.equals(header[i])) {
				PropertyDescriptor pd = new PropertyDescriptor(columnName,
						TableData.class);
				getter = pd.getReadMethod();

			}
		}
		System.out.println(LoadData.headerString);

		String type = String.valueOf(getter.getReturnType());

		for (int i = 0; i < list.size(); i++) {
			f = getter.invoke((TableData) list.get(i));
			if (EvalCondtion.evaluate(f, columnValue, CompareSign, type)) {
				System.out.println(list.get(i).selectStar());
			}
		}

		
	}
}