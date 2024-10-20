# Cyclone - Two-Wheeler E-commerce Platform

## Description
Cyclone is an e-commerce web application specializing in motorcycles and bicycles. It provides a platform for users to browse, purchase, and manage two-wheeler products.

## Objective
The main objective of Cyclone is to offer a user-friendly interface for customers to explore and buy two-wheeler products while providing administrators with tools to manage products, users, and orders efficiently.

## Technologies Used
- Java EE
- Servlets
- Thymeleaf
- Hibernate
- PostgreSQL
- Maven
- Tailwind CSS
- JUnit and Mockito for testing

## Project Structure
The project follows a standard Maven web application structure:
- `src/main/java`: Java source files
- `src/main/resources`: Configuration files
- `src/main/webapp`: Web resources (HTML, CSS, JS)
- `src/test`: Test files

## Architecture
Cyclone adopts a Model-View-Controller (MVC) architecture:
- Model: JPA entities and DAO classes
- View: Thymeleaf templates
- Controller: Servlet classes

## Installation and Usage

### Prerequisites
- Java JDK 8 or higher
- Maven
- PostgreSQL
- Apache Tomcat 9.x

### Database Configuration
1. Create a PostgreSQL database named `cyclone`
2. Copy `src/main/resources/config.properties.example` to `src/main/resources/config.properties`
3. Edit `config.properties` with your database credentials:

    ```Properties
    db.url=jdbc:postgresql://localhost:5432/cyclone
    db.username=your_username
    db.password=your_password
    ```


### Building the Project
1. Clone the repository:
      ```
        git clone https://github.com/JavaAura/ Lamiaa_Mokhlis_Mostafa_S2_B3_Cyclone.git
        cd Lamiaa_Mokhlis_Mostafa_S2_B3_Cyclone
     ```
2. Build the project using Maven:
   ```
   mvn clean install
   ```

### Running the Application
1. Deploy the generated WAR file to Tomcat
2. Access the application at `http://localhost:8080/cyclone/Page`

## Features
- User registration and authentication
- Product browsing and searching
- Order placement and management
- Admin panel for product and user management

## Contributing
We welcome contributions to Cyclone! Please follow these steps:
1. Fork the repository
2. Create a new branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request




## Screenshots
[Add screenshots of key pages/features here]

## Future Improvements
- Implement a review and rating system for products
- Add a wishlist feature for users
- Integrate a recommendation engine based on user preferences
- Implement real-time order tracking
- Add support for multiple languages
- Integrate with popular payment gateways
- Implement a mobile app version of the platform

## Authors
This project was developed by a team of students:

- [Lamia Termoussi](https://github.com/TERMOUSSI-LAMIAA)
- [Mostafa El Belhaj](https://github.com/MesVortex)
- [Mokhlis Belhaj](https://github.com/BelhajMokhlis)



