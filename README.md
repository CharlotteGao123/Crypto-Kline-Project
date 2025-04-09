# Crypto Kline Project
Crypto Kline Project is a Java-based application designed to fetch, store, and manage cryptocurrency Kline data (candlestick data) from Binance. The project uses Spring Boot, MySQL, and integrates with Binance's public API.

# Project Structure
```plaintext
src/
├── main/
│   ├── java/
│   └── resources/
│       └── application.yml
├── test/
└── pom.xml
```
# Object-Oriented Design (OOD)
The following diagram illustrates the project's Object-Oriented Design structure:
```markdown
![OOD Diagram](doc/CryptoKline-class.png?raw=true)
```
# Setup 
## Prerequisites
- Java 17
- Maven 3+
- MySQL 8+

## Installation
1. Clone the repository:
   ``` java
   git clone https://github.com/your-username/crypto-kline.git
   cd crypto-kline
   ```
2. Set up the MySQL database:
   ```sql
   CREATE DATABASE cryptokline_db;
   ```
3. Configure application.yml
```
   spring:
   datasource:
   url: jdbc:mysql://localhost:3306/cryptokline_db
   username: your_username
   password: your_password
```

## Running the Application
Build and run the project:
```java
mvn clean install
mvn spring-boot:run
```
## Running Tests
```java
mvn test
```

## Fearures
- Fetch real-time and historical Kline data from Binance
- Store candlestick data in MySQL
- Scheduled data fetching with configurable intervals
- REST APIs for querying Kline data
- Easy deployment and CI/CD-friendly structure

## Technologies Used
- Spring Boot: Framework for rapid application development.
- MySQL: Data persistence.
- Maven – Build and dependency management
- JUnit 5 and Mockito: Unit testing and mocking.
- Binance API: Fetching cryptocurrency data.

# Author
Charlotte Gao
Content information: charlotte.gxy@outlook.com

# License
This project is licensed under the MIT License - see the LICENSE file for details.
