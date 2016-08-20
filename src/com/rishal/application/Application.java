package com.rishal.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.rishal.exception.InvalidSqlException;
/**
 * Main Class used to launch the application.
 * @author Rishal
 *
 */
public class Application {
	
	public static void main(String[] args) throws IOException, InvalidSqlException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		//Taking Query input from user
		LoadData a=LoadData.getInstance();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("CommandLine SQL- Enter the SQl query to get the result");
		String inputQuery=br.readLine();
		QueryType query=new QueryType();
		QueryType.Type typeOfQuery = query.getLineType(inputQuery);
		if(!typeOfQuery.equals(QueryType.Type.NOMATCH))
		{
			Class<?> c= Class.forName("com.rishal.queryOperation"+"."+typeOfQuery.toString());
			Object t = c.newInstance();
			Method method = c.getDeclaredMethod("display", String.class);
			method.invoke(t, inputQuery);
		}
		else
		{
			throw new InvalidSqlException("Invalid Sql! Please enter valid SQL query.");
		}

	}

}