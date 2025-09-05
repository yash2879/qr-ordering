#  टेबलSE (TableSE) - QR-Powered Restaurant Ordering System

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white) ![WebSocket](https://img.shields.io/badge/WebSocket-realtime-blue?style=for-the-badge&logo=socket.io&logoColor=white)

**TableSE** (a play on "table-side") is a modern, real-time QR code-based ordering system designed to streamline the dining experience. It empowers customers to view menus and place orders directly from their smartphones, while providing kitchens with an instantaneous, dynamic order management hub.

Say goodbye to paper menus and manual order-taking. Say hello to efficiency and a seamless digital workflow.

---

## ✨ Core Features

*   📱 **Contactless QR Ordering:** Customers scan a table-specific QR code to access the menu and place orders.
*   ⚡ **Real-Time Kitchen Hub:** New orders are pushed instantly to the kitchen dashboard via WebSockets. No refreshing needed!
*   👨‍🍳 **Dynamic Order Management:** Kitchen staff can view, manage, and update the status of incoming orders (e.g., "Preparing," "Ready," "Completed").
*   📝 **Easy Menu Management:** A secure admin panel allows restaurant staff to add, update, and remove menu items.
*   🔐 **Secure by Default:** Built with Spring Security to protect sensitive routes and administrative functions.
*   🛠️ **RESTful API:** A well-defined API for clean separation between frontend and backend logic.

---

## 🚀 How It Works: The Order Flow

The entire process is designed to be smooth and intuitive for both customers and staff.

1.  **Customer Scans QR Code**
    *   A unique QR code at each table directs the customer to the restaurant's digital menu.
    *   The URL automatically includes the table number.

2.  **Places Order**
    *   The customer browses the interactive menu and adds items to their cart.
    *   Once submitted, the order is sent to the backend.

3.  **Kitchen Hub is Instantly Notified**
    *   The backend receives the order and immediately pushes a notification to the Kitchen Hub UI using WebSockets.
    *   The new order appears at the top of the order queue without any manual intervention.

4.  **Kitchen Prepares the Order**
    *   The kitchen staff views the order details and updates its status (e.g., from `PENDING` to `PREPARING`).
    *   Status changes are broadcasted, allowing for potential future integration with a customer-facing order tracking page.

5.  **Order is Completed**
    *   The kitchen marks the order as `COMPLETED`, moving it off the active queue.

---

## 🛠️ Tech Stack

This project leverages a powerful and modern set of technologies:

| Category      | Technology                                                                                                  |
| ------------- | ----------------------------------------------------------------------------------------------------------- |
| **Backend**   | **Java 17**, **Spring Boot** (Web, Data JPA, Security, WebSocket)                                             |
| **Frontend**  | **HTML**, **CSS**, **Thymeleaf** (for server-side rendering)                                                  |
| **Database**  | **JPA-compliant database** (like H2, PostgreSQL, MySQL - configurable in `application.properties`)            |
| **Build**     | **Apache Maven**                                                                                            |
| **Real-time** | **Spring WebSocket** with **STOMP** messaging protocol                                                        |

---

## 🏁 Getting Started

Follow these steps to get the project running on your local machine.

### Prerequisites

*   **JDK 17** or later
*   **Apache Maven**
*   A running instance of your chosen database (or use the default in-memory H2 database).

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/yash2879/qr-ordering.git
    cd qr-ordering
    ```

2.  **Configure the application:**
    *   Open `src/main/resources/application.properties`.
    *   Update the `spring.datasource.*` properties to point to your local or remote database. If you leave it as is, it will use an in-memory H2 database, which is great for testing.

3.  **Build the project using the Maven wrapper:**
    *   On macOS/Linux:
        ```sh
        ./mvnw clean install
        ```
    *   On Windows:
        ```sh
        ./mvnw.cmd clean install
        ```

4.  **Run the application:**
    ```sh
    java -jar target/tablese_core-0.0.1-SNAPSHOT.jar
    ```

5.  **You're all set!**
    *   The application should now be running on `http://localhost:8080`.
    *   **Admin Login:** `http://localhost:8080/login` (User: `admin`, Password: `password` - as configured in `SecurityConfig.java`)
    *   **Customer Menu (Example):** `http://localhost:8080/menu/1/1` (Restaurant ID 1, Table 1)
    *   **Kitchen Hub:** `http://localhost:8080/kitchen/hub/1` (Restaurant ID 1)

---

## 💡 Contributing

Contributions are welcome! If you have ideas for new features, bug fixes, or improvements, please feel free to:

1.  Fork the repository.
2.  Create a new feature branch (`git checkout -b feature/YourAwesomeFeature`).
3.  Commit your changes (`git commit -m 'Add some YourAwesomeFeature'`).
4.  Push to the branch (`git push origin feature/YourAwesomeFeature`).
5.  Open a Pull Request.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
