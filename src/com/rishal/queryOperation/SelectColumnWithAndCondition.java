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

public class SelectColumnWithAndCondition {
	private String selectCol;
	private String colNameFirstCond;
	private String colValFirstCond;
	private String compareFirstCond;
	private String colNameSecondCond;
	private String colValSecondCond;
	private String compareSecondCond;
	private String conditionBetCriteria;
	
	public void display(String query) throws InvalidSqlException {
		String headerarr[] = LoadData.headerString.split(",");
		String patternForColumnName = "(SELECT\\s*)(\\w+)";
		String patternForCriteria = "(\\w+\\s*)(>|<|>=|<=|!=|=)(\\s*\\d+|\\w+)(\\s*AND\\s*)(\\w+\\s*)(>|<|>=|<=|!=|=)(\\s*\\d+|\\w+)";
		Pattern r = Pattern.compile(patternForColumnName);
		Matcher m = r.matcher(query.trim());

		Pattern criteriaP = Pattern.compile(patternForCriteria);
		Matcher criteriaM = criteriaP.matcher(query.trim());
		if (m.find()) {
			this.selectCol = m.group(2).trim();
		}
		if (criteriaM.find()) {
			this.colNameFirstCond = criteriaM.group(1).trim();
			this.compareFirstCond = criteriaM.group(2).trim();
			this.colValFirstCond = criteriaM.group(3).trim();
			this.conditionBetCriteria = criteriaM.group(4).trim();
			this.colNameSecondCond = criteriaM.group(5).trim();
			this.compareSecondCond = criteriaM.group(6).trim();
			this.colValSecondCond = criteriaM.group(7).trim();
		}

		if (!LoadData.headerString.contains(colNameFirstCond)) {
			throw new InvalidSqlException("Invalid Column name: "
					+ colNameFirstCond);
		} else if (!LoadData.headerString.contains(colNameSecondCond)) {
			throw new InvalidSqlException("Invalid Column name: "
					+ colNameSecondCond);
		} else if (!LoadData.headerString.contains(selectCol)) {
			throw new InvalidSqlException("Invalid Column name: " + selectCol);
		} else {
			try {
				process(colNameFirstCond, compareFirstCond, colValFirstCond,
						colNameSecondCond, compareSecondCond, colValSecondCond,
						selectCol, LoadData.myData, headerarr,
						conditionBetCriteria);
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void process(String colNameFirstCond, String compareFirstCond,
			String colValFirstCond, String colNameSecondCond,
			String compareSecondCond, String colValSecondCond,
			String selectCol, ArrayList<TableData> myData, String[] headerarr,
			String conditionBetCriteria) throws IntrospectionException {

		Method getterFirstCond = null;
		Method getterSecondCond = null;
		Method getterSelectColumn = null;

		PropertyDescriptor pdForSelectCol = new PropertyDescriptor(selectCol,
				TableData.class);
		getterSelectColumn = pdForSelectCol.getReadMethod();

		for (int i = 0; i < headerarr.length; i++) {
			if (colNameFirstCond.equals(headerarr[i])) {
				PropertyDescriptor pdFirstCond = new PropertyDescriptor(
						colNameFirstCond, TableData.class);
				getterFirstCond = pdFirstCond.getReadMethod();
			}
			if (colNameSecondCond.equals(headerarr[i])) {
				PropertyDescriptor pdSecondCond = new PropertyDescriptor(
						colNameSecondCond, TableData.class);
				getterSecondCond = pdSecondCond.getReadMethod();
			}
		}
		String returnTypeFirstCond = String.valueOf(getterFirstCond
				.getReturnType());
		String returnTypeSecondCond = String.valueOf(getterSecondCond
				.getReturnType());
		System.out.println(selectCol);
		ArrayList<TableData> dataList=LoadData.myData;
		Object firstCriteriaRetVal = null;
		Object secondCriteriaRetVal = null;
			for (int i = 0; i < dataList.size(); i++) {
				try {
					firstCriteriaRetVal = getterFirstCond
							.invoke((TableData) dataList.get(i));
					secondCriteriaRetVal = getterSecondCond
							.invoke((TableData) dataList.get(i));
					switch (conditionBetCriteria) {
					case "AND":
						if(processAndCondition(firstCriteriaRetVal,secondCriteriaRetVal,colValFirstCond,colValSecondCond,compareFirstCond,compareSecondCond,returnTypeFirstCond,returnTypeSecondCond))
							System.out.println(getterSelectColumn
									.invoke((TableData) dataList.get(i)));
						break;
					case "OR":
						if(processAndCondition(firstCriteriaRetVal,secondCriteriaRetVal,colValFirstCond,colValSecondCond,compareFirstCond,compareSecondCond,returnTypeFirstCond,returnTypeSecondCond))
						System.out.println(getterSelectColumn
								.invoke((TableData) dataList.get(i)));
						break;
					default:
						break;
					}

				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


	}

	private boolean processAndCondition(Object firstCriteriaRetVal,
			Object secondCriteriaRetVal, String colValFirstCond,
			String colValSecondCond, String compareFirstCond,
			String compareSecondCond,String returnTypeFirstCond,String returnTypeSecondCond) {
		boolean firstCondtionResult=EvalCondtion.evaluate(firstCriteriaRetVal, colValFirstCond, compareFirstCond, returnTypeFirstCond);
		boolean secondContionResult=EvalCondtion.evaluate(secondCriteriaRetVal, colValSecondCond, compareSecondCond, returnTypeSecondCond);
		if(firstCondtionResult && secondContionResult)
			return true;
		else
			return false;
	}
	
	private boolean processOrCondition(Object firstCriteriaRetVal,
			Object secondCriteriaRetVal, String colValFirstCond,
			String colValSecondCond, String compareFirstCond,
			String compareSecondCond,String returnTypeFirstCond,String returnTypeSecondCond) {
		boolean firstCondtionResult=EvalCondtion.evaluate(firstCriteriaRetVal, colValFirstCond, compareFirstCond, returnTypeFirstCond);
		boolean secondContionResult=EvalCondtion.evaluate(secondCriteriaRetVal, colValSecondCond, compareSecondCond, returnTypeSecondCond);
		if(firstCondtionResult || secondContionResult)
			return true;
		else
			return false;
	}
}
