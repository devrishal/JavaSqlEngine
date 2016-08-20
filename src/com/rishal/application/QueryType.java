package com.rishal.application;

public class QueryType {
	public static enum Type{
		 SelectAll, 
		 SelectColumn, 
		 SelectMax, 
		 SelectUnique, 
		 SelectColumnWithAndCondition,
		 SelectColumnWithOrInsideAndCondition,
		 NOMATCH
	}
	
	public class QueryFilter
	{
		private QueryType.Type type;
		private String pattern;
		public QueryFilter(QueryType.Type type,String pattern)
		{
			this.type = type;
			this.pattern = pattern;
		}
		
		public String getPattern()
		{
			return this.pattern;
					
		}
		
		public QueryType.Type getType()
		{
			return this.type;
		}
	}
	
	public static String selectStar="(\\s*)SELECT(\\s*)[*](\\s*)FROM(\\s*)[a-z]+(\\s*)WHERE(\\s*)[a-z]+(\\s*)(>|<|>=|<=|!=|=)(\\s*)[0-9]+";
	public static String selectColumn="(\\s*)SELECT(\\s*)[a-z]+(\\s*)FROM(\\s*)[a-z]+(\\s*)WHERE(\\s*)[a-z]+(\\s*)(>|<|=|>=|<=)(\\s*)[0-9]+";
	public static String selectMax="(\\s*)SELECT(\\s*)MAX\\((.*?)\\)(\\s*)FROM(\\s*)[a-z]+";
	public static String selectUnique="(\\s*)SELECT(\\s*)UNIQ\\((.*?)\\)(\\s*)FROM(\\s*)[a-z]+(\\s*)WHERE(\\s*)([a-z]+(?:_[a-z]+)*)=[a-z]+";
	public static String selectColumnWithAnd="(\\s*)SELECT(\\s*)[a-z]+(\\s*)FROM(\\s*)[a-z]+(\\s*)WHERE(\\s*)([a-z]+(?:_[a-z]+)*)(>|<|>=|<=|!=|=)[a-z]+(\\s*)AND(\\s*)[a-z]+(>|<|>=|<=|!=|=)[0-9]+";
	public static String selectColumnWithAndOr="(\\s*)SELECT(\\s*)[a-z]+(\\s*)FROM(\\s*)[a-z]+(\\s*)WHERE(\\s*)([a-z]+(?:_[a-z]+)*)(>|<|>=|<=|!=|=)[a-z]+(\\s*)AND(\\s*)\\(.[a-z]+(>|<|>=|<=|!=|=)[0-9]+(\\s*)OR(\\s*)[a-z]+(>|<|>=|<=|!=|=)[0-9]+(\\s*)\\)";
	private QueryFilter[] queryFilter;
	
	public QueryType()
	{
		// Since we have have four type of lines
		this.queryFilter = new QueryFilter[6];
		this.queryFilter[0] = new QueryFilter(QueryType.Type.SelectAll, selectStar);
		this.queryFilter[1] = new QueryFilter(QueryType.Type.SelectColumn, selectColumn);
		this.queryFilter[2] = new QueryFilter(QueryType.Type.SelectMax, selectMax);
		this.queryFilter[3] = new QueryFilter(QueryType.Type.SelectUnique, selectUnique);
		this.queryFilter[4] = new QueryFilter(QueryType.Type.SelectColumnWithAndCondition, selectColumnWithAnd);
		this.queryFilter[5] = new QueryFilter(QueryType.Type.SelectColumnWithOrInsideAndCondition, selectColumnWithAndOr);
		
	}
	
	public QueryType.Type getLineType(String line)
	{
		line = line.trim();
		QueryType.Type result = Type.NOMATCH;
		
		boolean matched = false;
			
		for(int i =0;i<queryFilter.length && !matched ;i++)
		{
			if( line.matches(queryFilter[i].getPattern()) )
			{
				matched = true;
				result = queryFilter[i].getType();
			}
			
		}
		
		return result;
		
	}
	
}
