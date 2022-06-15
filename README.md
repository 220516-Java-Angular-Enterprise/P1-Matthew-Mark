# P1: Reimbursement System
### Mark Quercioli: Full Stack JAVA Developer at Revature
### Matthew Berkheimer: Full Stack JAVA Developer at Revature
## Java Enterprise Foundations Project

### Contents
- Project Description
- Technologies
- HTTPS Process and Request Structure
- ER Diagram
### Project Description

For our project, we were tasked with building an API that 
will support a new internal expense reimbursement system. 
Our goal was to have our system manage the process of reimbursing employees for expenses incurred 
on company time. All registered employees in the company can log in, submit 
requests for reimbursement, and view their past tickets and pending requests. 
Finance managers can log in, view all reimbursement requests, and past histories for 
all employees in the company. They are also authorized to approve and deny 
requests for expense reimbursement.

### Technologies 
**Persistence Tier**
- Postgresql (Database storage)

**Application Tier**
- Java 8 (Programming language)
- Apache Maven (Dependency manager)
- JDBC (Database Connectivity)
- Java EE Servlets (Creates servlets for program and testing)
- JSON Web Tokens (For authentication)
- Apache Tomcat (Deploy webservlets)
- Jenkins (Deploy WAR package to Apache)
- Postman (Used for testing HTTPS requests)
- Mockito (Mock Database Testing)
- JUnit (Testing)
- AWS (connect to EC2 machine for deployment)

### Relational Data Model
![Relational Model](https://github.com/220207-java-enterprise/assignments/blob/main/foundations-project/imgs/ERS%20Relational%20Model.png)

##### System Use Case Diagrams
![System Use Case Diagrams](https://raw.githubusercontent.com/220207-java-enterprise/assignments/main/foundations-project/imgs/ERS%20Use%20Case%20Diagram.png)

##### Reimbursment Status State Flow
![Reimbursment Status State Flow](https://raw.githubusercontent.com/220207-java-enterprise/assignments/main/foundations-project/imgs/ERS%20State%20Flow%20Diagram.png)
##### Reimbursement Types
- LODGING
- TRAVEL
- FOOD
- OTHER
### HTTPS Processes and JSON structure
- Follows RESTful architecture (level 2) using GET, POST, PUT, and DELETE methods. 
- Leverages Postman for testing backend API requests using JSON objects
### HTTPS POST Requests
#### Creating new user
*localhost:8080/shoe/users*\
{"username" : "testuser123",\
"password" : "P@ssword",\
"email" : "something@gmail.com",\
"given_name" : "firstname",\
"surname" : "lastname"}

#### Authenticate
*localhost:8080/shoe/users/auth*\
{"username" : "testuser123",\
"password" : "P@ssword"}

#### Search and retrieve all users like username
*localhost:8080/shoe/users*\
{"username" : "test"} *would retrieve all users that have 'test' in their name*

#### Retrieve user details by username
*localhost:8080/shoe/user*\
{"username" : "testusername123"} *needs to be an actual username*

#### New Reimbursement 
*localhost:8080/shoe/reimbursement*\
{"type": "travel",\
"description": "Mugged again and again",\
"paymentID": "1111222233334444", *Has to be sixteen digits long*\
"amount": "$2000"} *No commas or decimals*

### HTTPS PUT Requests
#### Activate or Inactivate a current user 
*localhost:8080/shoe/users/active or* **/users/inactive*\
{"username" : "testuser123"}

#### Reset a current user's password 
*localhost:8080/shoe/users/password*\
{"username" : "testuser123",\
"password": "newP@ssw0rd"}

#### Sort Reimbursements
*localhost:8080/shoe/users/reimbursement/history/sort or* **/pending/sort* 

{"columnName":"submitted", *column name has to exist in table*\
"ascending":"ASC"} *must be ASC or DESC*

#### Filter Reimbursements
*localhost:8080/shoe/reimbursement/pending/filter*\
{"submitted": "2022",\
"resolved":"2022",\
"description":"description",\
"username":"user",\
"payment_id" : "1111222233334444"} *can filter by all columns*
#### Reimbursement approval or denial
*localhost:8080/shoe/reimbursement*\
{"reimbID" : "61b37c36-1588-4c75-8e00-ab22662e6b7c",
"approval": "YES"} *approval is YES or NO*

### HTTPS GET Requests
#### Get active or inactive users
*localhost:8080/shoe/users/active or localhost:8080/shoe/users/inactive*

#### Get Reimbursements
*localhost:8080/shoe/reimbursement*

### HTTPS Delete Requests
#### Delete Reimbursement
*localhost:8080/shoe/reimbursement*\
{"reimbID" : "61b37c36-1588-4c75-8e00-ab22662e6b7c"} *has to be a pending reimbursement*

### Functional Requirements

- A new employee or new finance manager can request registration with the system *(Creating New User)*
- An admin user can approve or deny new registration requests *(Activate User)*
- A registered employee can authenticate with the system by providing valid credentials *(Authenticate)*
- An admin user can deactivate user accounts, making them unable to log into the system *(Put Inactivate User)*
- An admin user can reset a registered user's password *(Put Password)*
- An authenticated employee can view and manage their pending reimbursement requests *(Get Reimbursements pending and history)*
- An authenticated employee can view their reimbursement request history (sortable and filterable) *(Post Reimbursement history)*
- An authenticated employee can submit a new reimbursement request *(Post Reimbursement)*
- An authenticated finance manager can view a list of all pending reimbursement requests *(Post Reimbursement pending as finance manager)*
- An authenticated finance manager can view a history of requests that they have approved/denied *(Post Reimbursement history as finance manager)*
- An authenticated finance manager can approve/deny reimbursement requests *(Put Reimbursement)*

### Non-Functional Requirements

- Basic validation is enforced to ensure that invalid/improper data is not persisted *(Show DB)*
- User passwords will be encrypted by the system before persisting them to the data source *(Show DB)*
- Users are unable to recall reimbursement requests once they have been processed (only pending allowed) *(Cannot delete non-pending)*
- Sensitive endpoints are protected from unauthenticated and unauthorized requesters using JWTs *(Complete)*
- Errors and exceptions are handled properly and their details are obfuscated from the user 
- The system conforms to RESTful architecture constraints *(PUT, POST, GET, DELETE)*
- The system's is tested with at least 80% line coverage in all service and utility classes
- The system's data schema and component design is documented and diagrammed *(ER Diagram)*
- The system keeps detailed logs on info, error, and fatal events that occur *(Catalina log)*

### Bonus Features
- Application can be deployed with Jenkins container *(Pending)*