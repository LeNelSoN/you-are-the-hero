![Couverture](https://i.imgur.com/PhHGK8G.jpg)

# L'API dont vous êtes le héros

## Description
This project is a Java API that revives the magic of old "choose your own adventure" books, which can sometimes be found in flea markets and second-hand bookstores. These iconic books shaped our childhood by allowing us to choose our own adventure on each page. With this API, I aim to recreate this immersive and interactive experience, where users can navigate through captivating stories and make decisions that influence the course of their adventure. Dive into a world of choices and imagination while rediscovering the nostalgia of tales that have marked generations!

## Features
- Start a new adventure.
- Choose actions at each step.
- Create stories and add scenes.
- Authentication with JWT for session management.

## Technologies Used
This project relies on several modern technologies to ensure optimal performance and security:

- **Java**: The programming language used to develop the API, offering robustness and scalability.
- **Spring Boot**: A Java framework that simplifies application development by reducing necessary configuration and providing powerful tools for API creation.
- **JWT (JSON Web Token)**: Used for secure user authentication, allowing session management without storing sensitive information on the server.
- **MongoDB**: A NoSQL database chosen for its flexibility and ability to dynamically manage documents, which perfectly suits the evolving nature of interactive stories.

## Prerequisites
- **JDK** Version 17 or higher.
- **Maven** Version 3.6.3 or higher.
- **Docker** If you want to run MongoDB via Docker.
- **Postman** For testing the API endpoints.

## HATEOAS
This API incorporates the HATEOAS principle, meaning that each response includes hypermedia links to other possible actions. This allows clients to interact with the API dynamically, without needing to know all available URLs in advance. For example, when a user starts a new adventure, the response will include links to available choices, progress tracking, and other relevant features. This approach promotes intuitive navigation and facilitates the onboarding of new users by making documentation less critical.

## Dependencies
Here are the main dependencies used in the project:

- Spring Boot: `3.3.2`
- spring-boot-starter-web: For creating RESTful web applications.
- spring-boot-starter-data-mongodb: For integration with MongoDB.
- spring-boot-starter-security: For application security.
- spring-boot-starter-hateoas: For implementing HATEOAS principles.
- spring-boot-devtools: For live development (optional).
- spring-boot-starter-test: For unit and integration testing.
- Mockito: `5.0.0` (or another stable version) for unit testing.
- JJWT: For managing JSON Web Tokens (JWT).

## Getting Started

### Installation Instructions
1. Clone the repository:
    ```bash
   git clone https://github.com/your-username/you-are-the-hero.git
    cd you-are-the-hero
   ```

2. Install the dependencies:
    ```bash
    mvn install
   ```
3. Start MongoDB using Docker:
    ```bash
    docker run --name mongodb -d -p 27017:27017 mongo
   ```
   or build the Docker image for your Spring Boot application (make sure you're in the project's root directory where the Dockerfile is located):
    ```bash
    docker build -t you-are-the-hero .
   ```
   Start the application using Docker Compose:
    ```bash
    docker-compose up -d
   ```
4. Access the application:

- The Spring Boot application will be accessible at `http://localhost:8080`.
- MongoDB will be accessible at `mongodb://localhost:27017`.

To stop the services, you can use:
```bash
docker-compose down
   ```

## Contributing

We welcome contributions to enhance the project! Please read our [Contributing Guide](CONTRIBUTING.md) for instructions on how to get involved.

## License

This project is licensed under the GNU General Public License. See the [LICENSE](LICENSE) file for details.
