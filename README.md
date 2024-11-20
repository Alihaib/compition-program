# **Winter Competition Game**

The **Winter Competition Game** is a Java-based simulation that models a winter sports competition. It supports a variety of winter sports like skiing and snowboarding, providing a dynamic and interactive environment for managing and simulating competitions.

---

## **Project Overview**

This project focuses on applying advanced object-oriented programming concepts, such as inheritance, interfaces, and design patterns, to create a robust and extensible competition framework. It supports multi-threading for simultaneous execution of competitors and uses the Observer pattern to update the competition state in real-time.

---

## **Features**

### **1. General Competition Framework**  
- Base classes and interfaces for modeling any type of competition.  
- Extensible architecture for adding new types of competitions.  

### **2. Winter Competition Extension**  
- Specialized implementation for winter sports such as skiing and snowboarding.  
- Supports unique properties for winter competitions like terrain and weather conditions.  

### **3. Graphical User Interface (GUI)**  
- A user-friendly Java Swing GUI for:  
  - Building the arena.  
  - Creating competitions.  
  - Adding and managing competitors.  
  - Simulating competitions with real-time updates.

### **4. Multi-Threading**  
- Competitors run as separate threads, simulating simultaneous actions.  
- GUI updates every 30 milliseconds to reflect the current state of the arena.

### **5. Observer Pattern**  
- The competition acts as an **Observer**.  
- Competitors act as **Observable** objects, notifying the competition of their progress.

---

## **Technologies Used**

- **Programming Language:** Java  
- **Framework:** Java Swing for GUI  
- **Design Patterns:** Observer pattern  
- **Concurrency:** Multi-threading with Java threads  

---

## **System Requirements**

- **JDK Version**: 11 or higher  
- **IDE**: IntelliJ IDEA, Eclipse, or any compatible Java IDE  

---

## **Installation**

1. Clone the repository:  
   ```bash
   git clone <repository_url>
