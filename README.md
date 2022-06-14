# P1: Matthew-Mark
## Java Enterprise Foundations Project

### Contents
- Project Description
- Technologies
- HTTPS Process (How people interact with program?)
- ER Diagram
### Project Description

For our project, we were tasked with building an API that 
will support a new internal, expense reimbursement system. 
Our goal was to have our system manage the process of reimbursing employees for expenses incurred 
while on company time. All registered employees in the company can log in, submit 
requests for reimbursement and view their past tickets and pending requests. 
Finance managers can log in, view all reimbursement requests, and past history for 
all employees in the company. They are also authorized to approve and deny 
requests for expense reimbursement.

### Technologies 
**Persistence Tier**
- Postgresql

**Application Tier**
- Java 8
- Apache Maven
- JDBC
- Java EE Servlets
- JSON Web Tokens
- JUnit
- Mockito

### HTTPS Processes
- Follows RESTful architecture (level 2) using GET, POST, PUT, and DELETE methods. 
- Leverages Postman for testing backend API requests using JSON objects

##### Relational Data Model
![Relational Model](https://github.com/220207-java-enterprise/assignments/blob/main/foundations-project/imgs/ERS%20Relational%20Model.png)

##### Reimbursement Types
- LODGING
- TRAVEL
- FOOD
- OTHER
##### System Use Case Diagrams
![System Use Case Diagrams](https://raw.githubusercontent.com/220207-java-enterprise/assignments/main/foundations-project/imgs/ERS%20Use%20Case%20Diagram.png)

##### Reimbursment Status State Flow
![Reimbursment Status State Flow](https://raw.githubusercontent.com/220207-java-enterprise/assignments/main/foundations-project/imgs/ERS%20State%20Flow%20Diagram.png)
### Functional Requirements

- A new employee or new finance manager can request registration with the system
- An admin user can approve or deny new registration requests
- The system will register the user's information for payment processing
- A registered employee can authenticate with the system by providing valid credentials
- An authenticated employee can view and manage their pending reimbursement requests
- An authenticated employee can view their reimbursement request history (sortable and filterable)
- An authenticated employee can submit a new reimbursement request
- An authenticated finance manager can view a list of all pending reimbursement requests
- An authenticated finance manager can view a history of requests that they have approved/denied
- An authenticated finance manager can approve/deny reimbursement requests
- The system will send a payment request when a reimbursement request is approved
- An admin user can deactivate user accounts, making them unable to log into the system
- An admin user can reset a registered user's password

### Non-Functional Requirements

- Basic validation is enforced to ensure that invalid/improper data is not persisted
- User passwords will be encrypted by the system before persisting them to the data source
- Users are unable to recall reimbursement requests once they have been processed (only pending allowed)
- Sensitive endpoints are protected from unauthenticated and unauthorized requesters using JWTs
- Errors and exceptions are handled properly and their details are obfuscated from the user
- The system conforms to RESTful architecture constraints
- The system's is tested with at least 80% line coverage in all service and utility classes
- The system's data schema and component design is documented and diagrammed
- The system keeps detailed logs on info, error, and fatal events that occur

### Bonus Features
- Application can be run within a Docker container
- Authenticated employees are able to upload a receipt image along with their reimbursement request