# JavaSqlEngine
This is basic implementation of SQL engine without any external libraries it support only some SELECT operations

As per the requirement given in the problem description, I have tried implementing it using inbuilt functionality of JAVA without using any third party library.
I have used following methodology in my project which are-
1) Regex Patterns
2) Java Reflection 
3) Pojo classes
4) Enumeration datatypes

Problem solving approach is defined as below-
Whenever we do operation with Queries we have to check the Syntax and Semantics.For these things i have used the regex patterns. Validations of all input query are done first if there is NOMATCH then error is thrown saying invalid SQL exception.

I have devided the complete problem set into following parts:
1)Syntax check.
2)Semantics check.
3)Criteria Evaluation.
4)Storing (if required).
5)Printing the results on to the console.

The classes present inside the folder Application are related to taking inputs from user, Loading data into the application and also checking the syntax of query given by the user.

The classes present inside the ConditionEval is used to Evaluate the criteria.

The classes present inside the QueryOperation folder is for doing the complete operation and returning the results to the end user. The queries given in problem set is divided into six groups which is SelectAll, SelectColumn, SelectMax, SelectUnique, SelectColumnWithAndCondition, SelectColumnWithOrInsideAndCondition, and NOMATCH if there is any error in the Query provided.

Exception is also passed from all layers and while doing the processing wherever required.

There is simple POJO class also present in the folder ValueObjects which is used to read the data from the CSV file and prepare the object which will be available throughout the application for the processing.

<b>To Run the application </b>

Starting point of application is Application.java, The File should be placed in the class path.


