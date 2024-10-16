# Assignment-Submission-Portal-Backend-System



This is a fully functional backend system built with Java, Spring Boot, GraphQL and MongoDB. 
It facilitates user registration, assignment uploads, and administrative management of assignments. 
Users can register, log in, fetch admin details and upload assignments, while admins can register, log in, view assignments, and accept or reject them.



Features:

User registration and login.
Admin registration and login.
Assignment upload by users and users can fetch admin details.
Admins can view, accept, and reject assignments.
Proper input validation and error handling.
Modular code structure for easy maintenance and readability.



Technologies:

Java (version 21)
Spring Boot
MongoDB
GraphQL for API interaction
Postman for testing endpoints



Setup:

Prerequisites: Ensure you have the following installed:
Java 21
Maven
MongoDB
IntelliJ Community Edition (recommended for development)



Installation Steps:

1- Clone the repository.
2- Build the project using Maven.
3- Ensure MongoDB is running on your local machine or configure the application.properties to connect to your MongoDB instance.



Configuration:

1- Configure the database connection in src/main/resources/application.properties
   You can adjust the MongoDB connection URI based on your setup.



Running the Application:

1- Run the application and the server will start at http://localhost:8080.



Authentication & Authorization:

Currently, the project is in progress for adding authentication and authorization:
1- OAuth2 Integration: Work is ongoing to add OAuth2-based authentication and authorization using Auth0.
2- Access Control: Proper access control will be implemented to restrict endpoint usage based on roles (Admin/User).



Future Enhancements:

1- Complete the implementation of OAuth2-based authentication.
2- Role-based access control for endpoints.
3- Enhance security, including encryption for sensitive data.
