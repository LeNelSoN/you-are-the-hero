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

## HATEOAS
This API incorporates the HATEOAS principle, meaning that each response includes hypermedia links to other possible actions. This allows clients to interact with the API dynamically, without needing to know all available URLs in advance. For example, when a user starts a new adventure, the response will include links to available choices, progress tracking, and other relevant features. This approach promotes intuitive navigation and facilitates the onboarding of new users by making documentation less critical.

## Contributing

We welcome contributions to enhance the project! Please read our [Contributing Guide](CONTRIBUTING.md) for instructions on how to get involved.

## License

This project is licensed under the GNU General Public License. See the [LICENSE](LICENSE) file for details.
