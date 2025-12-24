# HGS (Hospital Grievance System)

The **Hospital Grievance System (HGS)** is a full-stack application designed to streamline grievance submission, resolution tracking, and feedback collection in healthcare environments.  
Backend is built with **Java 25 (JDK 25)** and **Spring Boot 4.0.0**, while the frontend uses **HTML, CSS, and JavaScript** for a lightweight, responsive interface.

---

## 🚀 Features

- **User Authentication**
  - Registration with secure password hashing (BCrypt).
  - Login with role-based redirection (Patient, Staff, Admin).
  - JWT-based authentication for secure API calls.

- **Grievance Management**
  - Patients can submit complaints with category and description.
  - Staff/Admin can update status: *Submitted → In Progress → Resolved*.
  - Attachments support (optional).

- **Feedback Collection**
  - Patients can rate resolution speed and fairness.
  - Comments for qualitative feedback.

- **Admin Dashboard**
  - View all grievances.
  - Filter by category/status.
  - Export reports (CSV/Excel).

---

## 🛠️ Tech Stack

### Backend
- **Java 25 (JDK 25)**
- **Spring Boot 4.0.0**
- **Spring Web** (REST APIs)
- **Spring Data JPA** (ORM)
- **Spring Security** (Authentication & Authorization)
- **MySQL** (Persistent database)
- **H2** (Optional in-memory DB for testing)

### Frontend
- **HTML5** → structure of pages
- **CSS3** → styling and responsive design
- **JavaScript (ES6+)** → form validation, API calls, dynamic UI updates
- **Fetch API** → communicate with backend REST APIs

---

## ⚙️ Project Setup

### Prerequisites
- Install **JDK 25**
- Install **Maven**
- Install **MySQL** (or use H2 for testing)

### Database
```sql
CREATE DATABASE hgs_db;
```
### Configuration (application.properties)
```
  spring.datasource.url=jdbc:mysql://localhost:3306/grievance_system
  spring.datasource.username=root
  spring.datasource.password=yourpassword
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
```
### Run Application
```
mvn spring-boot:run
```

### 📂 Project Structure
```
backend/
│
├── src/main/java/com/info_intern/hgs
│   ├── HgsApplication.java        # Main entry point
│   ├── entity/                    # JPA entities (User, Grievance, Feedback)
│   ├── repository/                # JPA repositories
│   ├── controller/                # REST controllers
│   ├── service/                   # Business logic
│   └── config/                    # Security configuration
│
frontend/
│
├── index.html                     # Landing page
├── login.html                     # Login form
├── register.html                  # Registration form
├── patient-dashboard.html          # Patient dashboard
├── admin-dashboard.html            # Admin dashboard
├── css/                           # Stylesheets
│   └── style.css
└── js/                            # Scripts
    ├── auth.js                    # Login/Register API calls
    ├── grievance.js               # Grievance submission logic
    └── feedback.js                # Feedback form logic

```

### 📌 Future Enhancements
- SLA timers and escalation workflows.
- Analytics dashboard for admins.
- Integration with hospital HIS/EMR systems.
- Multilingual support.
- Notification system (email/SMS).

### 👨‍💻 Author
 ** Prerak **
