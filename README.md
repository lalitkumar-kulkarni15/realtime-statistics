#Realtime-Statistics-API
================================================================================================================

- Steps to run the project :- 

================================================================================================================

Step 1 : Git Clone - Please clone the project at a local directory using the above mentioned url.

Step 2 : Build / compile - Please open the command prompt and 'cd' to the directory location where the code is cloned ( step 1 )

Step 3 : Run the maven command - "mvn clean install". ( Please ensure maven is already installed before executing this command ) 

Step 4 : After step 3 is successfully completed, please run the maven command "mvn spring-boot:run" to run the application.

		- (After step 4 is successfully completed, the embedded tomcat web server will be up and running. ( Kindly note the port number on which the application is up .
		  This can be seen in the console logs. ) It should be 9082 as configured in the application.yaml file.)

Step 5 : After the tomcat server is successfully up without any errors in the console, then please open the browser and hit the below swagger URL -

        Swagger URL : http://localhost:9082/swagger-ui.html
		
- Swagger UI page will be opened which has all the details of the 3 API.
- This swagger UI can also be used to test the 3 API endpoints by passing appropriate data in the request.

===================================================================================================================

- How to set up workspace in IDE ? 

====================================================================================================================

1) The code can be cloned in any IDE of your choice ex :- Eclipse / Intellij idea etc.
2) Please note that Lombok plugin is used in the application for getting rid of the boiler plate code. Thus
   the lombok plugin needs to be installed in any IDE that we want to clone this project in order for the code to
   successfully compile in the IDE. ( Maven dependency of lombok is already added in the pom.xml thus it will successfully compile 
   when running maven lifecycle commands )

===================================================================================================================
 
- Design and approach of the solution - 		

==================================================================================================================

1) The requirement of the application is to store the incomming instrument statistics in the in-memory data store
   such that the get calls should be executed in O(1) time and space complexity.

{
"instrument": "IBM.N",
"price": 143.82,
"timestamp": 1478192204000
}

2) In order to have the get calls executed within O(1) time and space complexity the incomming data should be aggregated
   during the post calls itself. Which means that at any given point in time the aggregated statistics should be ready 
   in the in-memory data store , so that the get calls can just collect those, validate if they are still valid as per
   the sliding time window and then return.
   
3) Hence, since we want to maintain the statistics of the last 60 seconds, we can have an array of aggregated transaction sttaistics.
   This array will be of fixed size i.e. 60 ( If we assume the time slicing to be 1 second ).
   So all the incomming instrument transaction withing 1 sec will be aggregated and kept in that particular index of the array.
   What this means is we now have the aggregated statistics of every second in the array ready. ( Afcourse validations are done for time ,
   and all expired partitions will be reset )   

3) The most important part to understand is in order to keep the time and space complexity O(1) i.e. constant, the number of  
   iterations of the loop should be constant in the get calls.
   
4) Now since we have an array of fixed size ( assumed time slicing as 1 sec - so an array of size 60  - since we want to maintain the 
   statistics of last 60 seconds. ) , the loop iteration will be constant always i.e. 60 times for any incomming GET request. Hence O(1).  

5) In order to implement this, a singleton class is implemented which has the transaction statistics properties and a map.

6) The transaction statistics properties are updated per incomming post call after sliding time validations. Also they are 
   put in the map data structure where the instrument name is the key and value is the corresponding transaction statistics object.

7) Thus when we receive a get request without the instrument name as a path variable then this array is looped and only partitions which are valid 
   with respect to time are considered for aggregation and then the statistics are returned back.

8) The reason of choosing the map data structure - We have a third API which needs to return the aggregated sttatistics of a specific instrument 
   which is mentioned in the incomming get request as a path variable. Hence we need to have a data structure which can hold a instrument name and corresponding
   statistics in it.  This map which is present inside every partition of the fixed size array which holds the key value pair of the instruments and its statistics 
   received for that particular time slicing interval.
   
9) Thus when a GET call with the instrument name is received we iterate this fixed size array and find if the record of this instrument is present in this map
   for that particular index. If  yes we do a map.get operation and consider it for statistics aggregation if it satisfies the time validity criteria.

=======================================================================================================================================================

- Below are the urls of the 3 API :- 

=======================================================================================================================================================   

1) POST : http://localhost:9082/tick

2) GET  : http://localhost:9082/statistics/

3) GET  : localhost:9082/statistics/IBM ( example - we can replace the name of instrument in place of IBM )

==================================================================================================================

- Assumptions

===================================================================================================================

1) Have assumed the time slicing interval to be 1 sec. This is configurable in the property file.

===================================================================================================================

- Possible enhancements - 

1) Spring security - JWT. - If I had more time I would like to have this API secured using spring security.

===================================================================================================================

- Code quality :- 

===================================================================================================================

1) Code coverage - Have uploaded the code coverage report of the application alongwith, which comes out to be around 97.5 %
2) SONAR         - The source code is staticaly scanned using SONAR with no significant issues seen. 

=====================================================================================================================

- Attachments - 

======================================================================================================================

1) Code coverage report.
2) Swagger UI snapshots.

======================================================================================================================

Thank you,

Lalit Kulkarni


======================================================================================================================
