# Product Management API

## Overview
This project is a Spring Boot application for managing products. It provides CRUD operations, pagination, and sorting for products. The API is documented using Swagger for easy exploration of endpoints.

## Features
- Create, Read, Update, and Delete (CRUD) operations for products.
- Pagination and sorting for retrieving products.
- API documentation using Swagger.

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Spring Boot 2.7 or higher

## Setup Instructions

### Clone the Repository

git clone https://github.com/ZeelPatel04/ProductManagement.git
cd product-management
The application will start on `http://localhost:8080`.

# API Documentation

The API is documented using Swagger. You can access the Swagger UI to explore the API endpoints.

### Accessing Swagger UI
1. Start the application.
2. Open your browser and navigate to:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

### Available Endpoints
- **POST** `/api/products` - Create a new product.
- **GET** `/api/products/{id}` - Get a product by ID.
- **PUT** `/api/products/{id}` - Update a product by ID.
- **DELETE** `/api/products/{id}` - Delete a product by ID.
- **GET** `/api/products` - Get all products with pagination and sorting.

### Running Tests
To run the tests, execute:./mvnw test


