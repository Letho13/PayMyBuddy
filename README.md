# PayMyBuddy
## A solution to transfer money easily with friends

<p align="center">
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://seeklogo.com/images/S/spring-boot-logo-9D6125D4E7-seeklogo.com.png" alt="Spring Boot" width="100"/>
  </a>
  <a href="https://www.thymeleaf.org/">
    <img src="https://www.thymeleaf.org/images/thymeleaf.png" alt="Thymeleaf" width="100"/>
  </a>
  <a href="https://www.postgresql.org/">
    <img src="https://upload.wikimedia.org/wikipedia/commons/2/29/Postgresql_elephant.svg" alt="PostgreSQL" width="100"/>
  </a>
</p>

![Build Status](https://github.com/Letho13/PayMyBuddy/actions/workflows/ci.yml/badge.svg)


Pay My Buddy is a website that allows users to transfer money between people by simply adding their email address.

## Features

- Sign in with an email address, username, and a protected password.
- Add friends already registered with their email.
- Add money or withdraw from your bank.
- Transfer the chosen amount or receive it from a friend.
- Consult the list of past transactions.
- Modify your personal information if needed.
- Each transaction is charged a 0.5% fee, paid by the sender.

## MPD

![MPD Diagram](mpd-diagram.png)

## Tech

PayMyBuddy uses several open-source technologies:

- [Spring Boot](https://spring.io/) - A secure and robust Java framework.
- [Thymeleaf](https://www.thymeleaf.org/) - A modern server-side Java template engine.
- [PostgreSQL](https://www.postgresql.org/) - A powerful relational database system.
- [Bootstrap](https://getbootstrap.com/) - A great UI boilerplate for modern web apps.

## Prerequisites

- Java 21 (download [here](https://openjdk.org/projects/jdk/21/))
- Maven installed
- PostgreSQL database running

## Installation

1. **Clone the repository**
   ```sh
   git clone https://github.com/Letho13/PayMyBuddy.git
   cd PayMyBuddy
