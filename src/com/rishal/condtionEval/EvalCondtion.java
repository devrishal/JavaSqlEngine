package com.rishal.condtionEval;

public class EvalCondtion {
	public static boolean evaluate(Object value,String valueInColumn,String compareSign,String returnTypeOfCond)
	{
		boolean conditionResult=false;
		if (returnTypeOfCond.equals("int")) {
			switch (compareSign) {
			case "=":
				if (Integer.valueOf(valueInColumn) == (int) value)
					conditionResult=true;
				break;
			case "!=":
				if (Integer.valueOf(valueInColumn) != (int) value)
					conditionResult=true;
				break;
			case ">=":
				if ((int) value>=Integer.valueOf(valueInColumn) )
					conditionResult=true;
				break;
			case "<=":
				if ((int) value<= Integer.valueOf(valueInColumn) )
					conditionResult=true;
				break;
			case "<":
				if ((int) value<Integer.valueOf(valueInColumn))
					conditionResult=true;
				break;
			case ">":
				if ((int) value>Integer.valueOf(valueInColumn) )
					conditionResult=true;
				break;
			default:
				break;
			}
		}
		if (returnTypeOfCond.equals("boolean")) {
			switch (compareSign) {
			case "=":
				if (Boolean.valueOf(valueInColumn)==(boolean)value)
					conditionResult=true;
				break;
			case "!=":
				if (Boolean.valueOf(valueInColumn)!=(boolean)value)
					conditionResult=true;
				break;
			default:
				break;
			}
		}
		if (returnTypeOfCond.equals("String")) {
			switch (compareSign) {
			case "=":
				if (String.valueOf(valueInColumn).equals(String.valueOf(value)))
					conditionResult=true;
				break;
			case "!=":
				if (!String.valueOf(valueInColumn).equals(String.valueOf(value)))
					conditionResult=true;
				break;
			default:
				break;
			}
		}
		if (returnTypeOfCond.equals("double")) {
			switch (compareSign) {
			case "=":
				if (Double.valueOf(valueInColumn) == (double) value)
					conditionResult=true;
				break;
			case "!=":
				if (Double.valueOf(valueInColumn) != (double) value)
					conditionResult=true;
				break;
			case ">=":
				if ((double) value>=Double.valueOf(valueInColumn) )
					conditionResult=true;
				break;
			case "<=":
				if ((double) value<=Double.valueOf(valueInColumn))
					conditionResult=true;
				break;
			case "<":
				if ((double) value<Double.valueOf(valueInColumn))
					conditionResult=true;
				break;
			case ">":
				if ((double) value>Double.valueOf(valueInColumn))
					conditionResult=true;
				break;
			default:
				break;
			}
		}
		return conditionResult;
	}
}