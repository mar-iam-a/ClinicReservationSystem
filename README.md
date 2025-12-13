# 🏥 Clinic Reservation System

A **Java Desktop Application** built with **JavaFX** and **SQL** that manages clinic reservations for both **doctors** and **patients**. The system provides full scheduling, booking, reporting, and communication features to streamline clinic operations.

---

## 🛠️ Tech Stack

* **Language:** Java
* **UI:** JavaFX (FXML)
* **Database:** SQL (JDBC)
* **Architecture:** MVC + DAO Pattern
* **Reporting:** PDF & Excel Export
* **Maps Integration:** Google Maps
* **Email Service:** Automated Email Notifications

---

## 💬 Real-Time Chat System

The system includes a **professional in-app chat** between doctors and patients, designed for secure and organized communication.

### Chat Features

* One-to-one chat between **doctor and patient**
* Accessible directly from the dashboard
* Chat available:

  * Before the appointment (inquiries & follow-ups)
  * After booking confirmation
* Messages are stored in the database
* Clear separation between different conversations
* Simple and intuitive chat UI inside the JavaFX application

---

## 👥 User Roles

### 👨‍⚕️ Doctor

Each doctor can manage **one clinic** with full control over its data.

#### Clinic Management

* Create and edit clinic details
* Set clinic location and view it on **Google Maps**
* Enable or disable **online consultation**
* Update clinic information at any time

#### Schedule & Slots

* Define working schedule (days & times)
* System automatically **generates time slots** based on:

  * Schedule start & end time
  * Consultation duration
* Schedule updates apply **starting from the next month** (existing data remains unchanged)

#### Reservations

* View all bookings
* Cancel reservations
* After appointment time:

  * Mark patient as **Attended** or **Absent**
  * If attended → patient can submit a **rating** (only after completion)

#### Waiting List

* View waiting list when a day is fully booked
* Accept or reject additional patients

#### Reviews & Reports

* View clinic reviews and ratings
* Generate detailed reports including:

  * Confirmed appointments
  * Cancellations
  * Absences
  * Reviews
  * Booking dates
  * Patient details
* Export reports as **PDF** or **Excel**

#### Account Settings

* Update profile information
* Change username and password

---

### 🧑‍💼 Patient

#### Dashboard Features

* Search for doctors
* View doctor profiles and clinic information
* See clinic location on map

#### Booking

* View available time slots
* Book appointments based on doctor’s schedule
* Cancel bookings

#### Waiting List

* Join waiting list when no slots are available
* Receive automatic email notification if a slot becomes available

#### Rating & Reviews

* Submit rating **only if appointment is marked as attended** by the doctor

#### Communication

* Chat with the doctor

#### Account Management

* Edit personal information

---

## 🔁 Waiting List Automation

When a booking is canceled:

1. The system selects the **first patient** in the waiting list
2. Sends an **email notification** with the available slot
3. If the patient does not book within **10 minutes**:

   * The request is marked as **Expired**
   * The system notifies the next patient automatically

---

## 🔐 Authentication

* Secure **Login & Registration** for doctors and patients
* Input validation and role-based access

---

## 📊 Key Features Summary

* Automatic slot generation
* Real-time booking & cancellation handling
* Waiting list with timed email notifications
* Doctor attendance tracking
* Rating system with completion validation
* Full statistical reports
* PDF & Excel export
* Google Maps integration
* Chat system

---

## 🚀 Future Improvements

* Online payment integration
* Mobile application version
* Admin dashboard
* Advanced analytics

---

## 📌 Project Purpose

This project was developed as a **desktop clinic management system** to demonstrate:

* JavaFX UI design
* Database-driven applications
* Clean architecture (MVC & DAO)
* Real-world scheduling and reservation logic

---

## 🧩 UML & Database Design

This project includes clear design documentation to support maintainability and scalability.

### UML Diagrams

* Use Case Diagram
* Class Diagram

![UML Class Diagram](docs/uml/class-diagram.png)


### Database Design

* Relational database schema
* Clear relationships and foreign keys

![Database ERD](docs/database/erd.png)

---

## 👥 Contributors

* **Nour Sameh**
* **Nermen Ramadan**
* **Mariem Tarek**
* **Mariem Ali**

