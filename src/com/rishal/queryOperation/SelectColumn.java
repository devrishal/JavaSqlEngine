package com.rishal.queryOperation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rishal.application.LoadData;
import com.rishal.condtionEval.EvalCondtion;
import com.rishal.exception.InvalidSqlException;
import com.rishal.valueObjects.TableData;

public class SelectColumn {
	public void display(String query) throws InvalidSqlException {
		String selectCol = null;
		String criteriaCol = null;
		String compareSign = null;
		String criteriaColValue = null;
		String headerarr[] = LoadData.headerString.split(",");

		String patternForColumnName = "(SELECT\\s*)(\\w+)";
		String patternForCriteriaColumnNVal = "(\\w+\\s*)(>|<|>=|<=|!=|=)(\\s*\\d+)";
		Pattern r = Pattern.compile(patternForColumnName);
		Pattern r1 = Pattern.compile(patternForCriteriaColumnNVal);
		Matcher m = r.matcher(query.trim());
		Matcher m1 = r1.matcher(query.trim());
		if (m.find()) {
			selectCol = m.group(2).trim();
		}
		if (m1.find()) {
			criteriaCol = m1.group(1).trim();
			compareSign = m1.group(2).trim();
			criteriaColValue = m1.group(3).trim();
		}
		if (!LoadData.headerString.contains(selectCol)) {
			throw new InvalidSqlException("Column name is not valid: "
					+ selectCol);
		}
		if (!LoadData.headerString.contains(criteriaCol)) {
			throw new InvalidSqlException("Column name is not valid: "
					+ criteriaCol);

		} else {
			try {
				process(criteriaCol, criteriaColValue, LoadData.myData,
						headerarr, compareSign, selectCol);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | IntrospectionException
					| InvalidSqlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void process(String columnName, String columnValue,
			ArrayList<TableData> list, String header[], String CompareSign,
			String selectColumn) throws IntrospectionException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InvalidSqlException {
		Object f = null;
		Method getter = null;
		Method getterSelectColumn = null;
		PropertyDescriptor pdForSelectCol = new PropertyDescriptor(
				selectColumn, TableData.class);
		getterSelectColumn = pdForSelectCol.getReadMethod();

		for (int i = 0; i < header.length; i++) {

			if (columnName.equals(header[i])) {
				PropertyDescriptor pd = new PropertyDescriptor(columnName,
						TableData.class);
				getter = pd.getReadMethod();
			}
		}
		String type = String.valueOf(getter.getReturnType());
		System.out.println(selectColumn);

		for (int i = 0; i < list.size(); i++) {
			f = getter.invoke((TableData) list.get(i));
			if (EvalCondtion.evaluate(f, columnValue, CompareSign, type)) {
				System.out.println(getterSelectColumn.invoke((TableData) list
						.get(i)));
			}
		}
	}
}