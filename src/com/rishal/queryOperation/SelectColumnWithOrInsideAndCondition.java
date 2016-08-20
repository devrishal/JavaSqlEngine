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

public class SelectColumnWithOrInsideAndCondition {
	// Column name selection
	private String columnName;

	// criteria condition prepration
	private String criteriaCol1;
	private String criteriaCompareSign;
	private String criteriaColVal1;
	private String logicalOperators1; 
	private String criteriaCol2;
	private String criteriaCompareSign1;
	private String criteriaColVal2;
	private String logicalOperators2;
	private String criteriaCol3;
	private String criteriaCompareSign2;
	private String criteriaColVal3;

	public void display(String query) throws InvalidSqlException {

		// patterns for criteria and column name
		String patternForColumnName = "(SELECT\\s*)(\\w+)";
		String patternForCriteria = "(WHERE\\s*)(\\w+)(>|<|>=|<=|!=|=)(\\w+|\\d+)\\s*(AND|OR)\\s*(\\((\\s*\\w+)(>|<|>=|<=|!=|=)(\\w+|\\d+)\\s*(AND|OR)\\s*(\\w+)(>|<|>=|<=|!=|=)(\\w|\\d)\\s*\\))";

		Pattern r = Pattern.compile(patternForCriteria);
		Matcher m = r.matcher(query.trim());

		Pattern r1 = Pattern.compile(patternForColumnName);
		Matcher m1 = r1.matcher(query.trim());
		if (m1.find()) {
			this.columnName = m1.group(2).trim();
		}
		if (m.find()) {
			this.criteriaCol1 = m.group(2).trim();
			this.criteriaCompareSign = m.group(3).trim();
			this.criteriaColVal1 = m.group(4).trim();
			this.logicalOperators1=m.group(5).trim();
			this.criteriaCol2 = m.group(7).trim();
			this.criteriaCompareSign1 = m.group(8).trim();
			this.criteriaColVal2 = m.group(9).trim();
			this.logicalOperators2=m.group(10).trim();
			this.criteriaCol3 = m.group(11).trim();
			this.criteriaCompareSign2 = m.group(12).trim();
			this.criteriaColVal3 = m.group(13).trim();
		}
		String arr[]={logicalOperators1,logicalOperators2}; 
		if (!LoadData.headerString.contains(criteriaCol1)) {
			throw new InvalidSqlException("Invalid Column name: "
					+ criteriaCol1);
		} else if (!LoadData.headerString.contains(criteriaCol2)) {
			throw new InvalidSqlException("Invalid Column name: "
					+ criteriaCol2);
		} else if (!LoadData.headerString.contains(criteriaCol3)) {
			throw new InvalidSqlException("Invalid Column name: "
					+ criteriaCol3);
		} else if (!LoadData.headerString.contains(columnName)) {
			throw new InvalidSqlException("Invalid Column name: " + columnName);
		}
		else 
		{
			String headerarr[]=LoadData.headerString.split(",");
			try {
				process(columnName, headerarr, arr);
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void process(String selectCol,String[] headerarr,String [] arr) throws IntrospectionException
	{
		
		Method getterFirstCond = null;
		Method getterSecondCond = null;
		Method getterThirdCond = null;
		Method getterSelectColumn = null;
		PropertyDescriptor pdForSelectCol = new PropertyDescriptor(selectCol,TableData.class);
		getterSelectColumn = pdForSelectCol.getReadMethod();
		
		for (int i = 0; i < headerarr.length; i++) {
			if (this.criteriaCol1.equals(headerarr[i])) {
				PropertyDescriptor pdFirstCond = new PropertyDescriptor(
						criteriaCol1, TableData.class);
				getterFirstCond = pdFirstCond.getReadMethod();
			}
			if (this.criteriaCol2.equals(headerarr[i])) {
				PropertyDescriptor pdSecondCond = new PropertyDescriptor(
						this.criteriaCol2, TableData.class);
				getterSecondCond = pdSecondCond.getReadMethod();
			}
			if (this.criteriaCol3.equals(headerarr[i])) {
				PropertyDescriptor pdThirdCond = new PropertyDescriptor(
						this.criteriaCol3, TableData.class);
				getterThirdCond = pdThirdCond.getReadMethod();
			}
		}
		String returnTypeFirstCond = String.valueOf(getterFirstCond
				.getReturnType());
		String returnTypeSecondCond = String.valueOf(getterSecondCond
				.getReturnType());
		String returnTypeThirdCond = String.valueOf(getterThirdCond
				.getReturnType());
		System.out.println(selectCol);
		ArrayList<TableData> dataList=LoadData.myData;
		Object firstCriteriaRetVal = null;
		Object secondCriteriaRetVal = null;
		Object thirdCriteriaRetVal = null;
		for (int i = 0; i < dataList.size(); i++) {
			try {
				firstCriteriaRetVal = getterFirstCond
						.invoke((TableData) dataList.get(i));
				secondCriteriaRetVal = getterSecondCond
						.invoke((TableData) dataList.get(i));
				thirdCriteriaRetVal = getterThirdCond
						.invoke((TableData) dataList.get(i));
				boolean resultAfterBracketCriteria=false;
				boolean resultAfterCompleteCriteriaEval=false;
				switch (arr[1]) {
					case "AND":
						if(processAndCondition(secondCriteriaRetVal,thirdCriteriaRetVal,criteriaColVal2,criteriaColVal3,criteriaCompareSign2,criteriaCompareSign1,returnTypeSecondCond,returnTypeThirdCond))
							resultAfterBracketCriteria=true;
						break;
					case "OR":
						if(processOrCondition(secondCriteriaRetVal,thirdCriteriaRetVal,criteriaColVal2,criteriaColVal3,criteriaCompareSign2,criteriaCompareSign1,returnTypeSecondCond,returnTypeThirdCond))
							resultAfterBracketCriteria=true;
						break;
					default:
						break;
					}
				switch (arr[0]) {
				case "AND":
					if(resultAfterBracketCriteria && EvalCondtion.evaluate(firstCriteriaRetVal, criteriaColVal1, criteriaCompareSign, returnTypeFirstCond))
						resultAfterCompleteCriteriaEval=true;
					break;
				case "OR":
					if(resultAfterBracketCriteria && EvalCondtion.evaluate(firstCriteriaRetVal, criteriaColVal1, criteriaCompareSign, returnTypeFirstCond))
						resultAfterCompleteCriteriaEval=true;
					break;
				default:
					break;
				}
				if(resultAfterCompleteCriteriaEval)
					System.out.println(getterSelectColumn.invoke((TableData) dataList.get(i)));
			
				
			}
			catch(Exception e)
			{
				
			}
		}
		
	}

	private boolean processAndCondition(Object firstCriteriaRetVal,
			Object secondCriteriaRetVal, String colValFirstCond,
			String colValSecondCond, String compareFirstCond,
			String compareSecondCond, String returnTypeFirstCond,
			String returnTypeSecondCond) {
		boolean firstCondtionResult = EvalCondtion.evaluate(
				firstCriteriaRetVal, colValFirstCond, compareFirstCond,
				returnTypeFirstCond);
		boolean secondContionResult = EvalCondtion.evaluate(
				secondCriteriaRetVal, colValSecondCond, compareSecondCond,
				returnTypeSecondCond);
		if (firstCondtionResult && secondContionResult)
			return true;
		else
			return false;
	}

	private boolean processOrCondition(Object firstCriteriaRetVal,
			Object secondCriteriaRetVal, String colValFirstCond,
			String colValSecondCond, String compareFirstCond,
			String compareSecondCond, String returnTypeFirstCond,
			String returnTypeSecondCond) {
		boolean firstCondtionResult = EvalCondtion.evaluate(
				firstCriteriaRetVal, colValFirstCond, compareFirstCond,
				returnTypeFirstCond);
		boolean secondContionResult = EvalCondtion.evaluate(
				secondCriteriaRetVal, colValSecondCond, compareSecondCond,
				returnTypeSecondCond);
		if (firstCondtionResult || secondContionResult)
			return true;
		else
			return false;
	}

}
