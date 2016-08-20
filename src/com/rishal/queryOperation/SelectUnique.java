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

public class SelectUnique {
	private String nameToDisplay;
	private String selectedColumn;
	private String criteriaColumn;
	private String criteriaCompareSign;
	private String criteriaColumnVal;
	
	public void display(String query) throws InvalidSqlException {
		String patternForSelectCol="(\\s*)SELECT((\\s*)UNIQ\\((.*?)\\))";
		Pattern pattern=Pattern.compile(patternForSelectCol);
		Matcher matcher=pattern.matcher(query);
		if(matcher.find())
		{
			this.nameToDisplay=matcher.group(2).trim();
			this.selectedColumn=matcher.group(4).trim();
		}
		String patternForCriteria = "(\\w+\\s*)(>|<|>=|<=|!=|=)(\\s*\\w+|\\d+)";
		Pattern r = Pattern.compile(patternForCriteria);
		Matcher m = r.matcher(query.trim());
		if (m.find()) {
			this.criteriaColumn=m.group(1).trim();
			this.criteriaCompareSign=m.group(2).trim();
			this.criteriaColumnVal=m.group(3).trim();
		}
		
		if (!LoadData.headerString.contains(selectedColumn)) {
			throw new InvalidSqlException("Column name is not valid: "
					+ selectedColumn);
		}
		if (!LoadData.headerString.contains(criteriaColumn)) {
			throw new InvalidSqlException("Column name is not valid: "
					+ criteriaColumn);
		}
		else
		{
			try {
				process();
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void process() throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Object f = null;
		Method getter = null;
		Method getterSelectColumn = null;
		String header[]=LoadData.headerString.split(",");
		ArrayList<TableData> list = LoadData.myData;
		ArrayList result = new ArrayList();
		PropertyDescriptor pdForSelectCol = new PropertyDescriptor(
				selectedColumn, TableData.class);
		getterSelectColumn = pdForSelectCol.getReadMethod();
		
		for (int i = 0; i < header.length; i++) {

			if (criteriaColumn.equals(header[i])) {
				PropertyDescriptor pd = new PropertyDescriptor(criteriaColumn,
						TableData.class);
				getter = pd.getReadMethod();
			}
		}
		String type = String.valueOf(getter.getReturnType());
		System.out.println(this.nameToDisplay);
		
		for (int i = 0; i < list.size(); i++) {
			f = getter.invoke((TableData) list.get(i));
			if (EvalCondtion.evaluate(f, criteriaColumnVal, criteriaCompareSign, type)) {
				if(!result.contains(getterSelectColumn.invoke((TableData) list
						.get(i))))
					result.add(getterSelectColumn.invoke((TableData) list
							.get(i)));
				/*System.out.println(getterSelectColumn.invoke((TableData) list
						.get(i)));*/
			}
		}
		for(int i=0;i<result.size();i++)
		{
			System.out.println(result.get(i));
		}
	}
}
