# Redomized Practical Project, Combined With Several Useful technologies

A sophisticated Java backend project that showcases a secure, containerized, and performance-optimized backend using **Spring Security with JWT**, **Redis**, **Docker**, and **Maven**. Built with modularity and production-readiness in mind.

## Features

- ✅ **Spring Boot + Java 17+** – Powerful and production-ready Java backend framework.
- 🔐 **Spring Security + JWT** – Secures endpoints with token-based authentication.
- ⚡ **Redis Integration** – Fast in-memory key-value store for caching and optimized data retrieval.
- 🐳 **Dockerized Setup** – Containerized app with Redis via Docker Compose.
- ⚙️ **Maven Build Tool** – Efficient dependency and build management.
- 📦 **Modular Codebase** – Organized architecture for scalability and clarity.
## 🧰 Tech Stack

| Layer           | Technology                      |
|------------------|----------------------------------|
| Backend          | Java, Spring Boot               |
| Security         | Spring Security, JWT            |
| Cache/Storage    | Redis                           |
| Containerization | Docker, Docker Compose          |
| Build Tool       | Maven                           |

---

## 🔐 Authentication & Authorization

> [!NOTE]
> ```
>- Initially, User strive up app with username and password to login
>- After login, User build JWT token based on Heder, Payload(body), Singature in server-side and return the signed JWT token
>- When a constructed JWT is returned, the subsequent requests must pass the token via the `Authorization` header.
>- Tokens are validated before accessing protected endpoints.
>- 
> ```

## Application Configuration
> [!NOTE]
> ```
>spring.datasource.url=jdbc:mysql://localhost:3306/quiz_db
>spring.datasource.username=root
>spring.datasource.password=yourpassword
>spring.jpa.hibernate.ddl-auto=update
>spring.jpa.show-sql=true
> ```

**For Testing-- Configure src/test/resources/application.properties** 

> [!NOTE]
> ```
> spring.datasource.url=jdbc:h2:mem:testdb
> spring.datasource.driver-class-name=org.h2.Driver
> spring.jpa.hibernate.ddl-auto=create-drop
>  ```

## 📦 Setup & Installation
### ✅ Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- Java 17+ (for running locally without Docker)

  ```bash
git clone https://github.com/YeZaw2003NeoPhenon/Randomizd_pj_with_complex_tech.git
cd Randomizd_pj_with_complex_tech
docker-compose up --build

### Contributing

## Fork the Repository:
**We welcome contributions to enhance the project! Follow these steps to contribute:
Click the "Fork" button at the top right of the repository page.**

## Create a New Branch:
```git checkout -b feature/your-feature```

## Make Your Changes:
Implement your feature or bug fix.

## Commit Your Changes:
```git commit -m "Add feature: your feature description"```

## Push To The Branch
```git push origin feature/your-feature```

## Create a Pull Request:
Open a pull request on GitHub and describe your changes.

