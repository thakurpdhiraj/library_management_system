# Library Management System

A complete suite of book lending system with role based features : 
*(Check [Snapshots](#snapshots))*
1. Admin
    * Add books with isbn validation and inventory creation with unique id & generating downloadable (pdf) barcode.
    * Search, update and delete books or inventory.
    * Add, search, update and delete users.
    * Search orders based on order id, user id or book id.
    * Mark order as collected.
    * Mark order as returned and generate downloadable invoice with late fees as applicable.
    * Search user's order history.
    * Search orders that are overdue for return by user id or book id.

2. User
    * Order a new book based on inventory availability.
    * Search self orders and order history.
    * Filter orders based on return overdue.

3. All
    * Authentication with Login and Logout.
    * Role based authorization and views (front-end).
    * Update self account details & change password.
    
## Technology Stack & Frameworks
> `Java 11`  `Spring-Boot-2.3.4.RELEASE` `Spring-Cloud-Hoxton.SR8` `Spring-Security` `Spring Data JPA` `Vue JS 2` `Vuetify 2.4` `Docker` `Docker-Compose` `JUnit 5`

## Installation and Running
- **With Docker and Docker Compose Installed**
```docker
docker compose down
docker compose build
docker compose up
-------------------------
or run the start.sh file
-------------------------
./start.sh
```
- **Without Docker** *(`npm` & `maven` required locally)*

*For Backend*
```
./build.sh
Open terminal to './server' && mvn spring-boot:run
---------------------------------------------
Once ServerApplication.java is up & running
---------------------------------------------
Open terminal to './user' && mvn spring-boot:run
Open terminal to './book' && mvn spring-boot:run
Open terminal to './auth' && mvn spring-boot:run
Open terminal to './order' && mvn spring-boot:run
Open terminal to './inventory' && mvn spring-boot:run
Open terminal to './client-backend' && mvn spring-boot:run
```

*For Frontend*
```
Open terminal to './client-frontend' && npm run serve
```
## Ports Exposed
Application | Port 
----------- | ---- 
*auth* | `8081`
*book* | `8082`
*inventory* | `8083`
*order* | `8084`
*user* | `8085`
*client-backend* | `8086`
*server* | `8761`
*client-frontend* | `8080`

## Snapshots

* Admin
    * Search Orders
    ![Admin- Search Orders](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_order_search.jpeg "Search Orders")
    * Search Order History
    ![Admin- Search Order History](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_order_history.jpeg "Search Order History")
    * Search Order Overdue
    ![Admin- Search Order Overdue](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_order_overdue.jpeg "Search Order Overdue")
    * Search Books
    ![Admin- Search Books](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_book_find.jpeg "Search Books")
    * Add Book
    ![Admin- Add Book](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_book_add.jpeg "Add Book")
    * Generated Inventory Barcode on Book Inventory Addition
    ![Admin- Inventory Barcode Generation](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_book_add_barcode.jpeg "Inventory Barcode Generation")
    * Delete Book
    ![Admin- Delete Book](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_book_delete.jpeg "Delete Book")
    * Find User
    ![Admin- Find User](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_user_find.jpeg "Find User")
    * Add User
    ![Admin- Add User](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_user_add.jpeg "Add User")
    * Delete User
    ![Admin- Delete User](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/admin_user_delete.jpeg "Delete User")
    
* User
    * Search Orders / Home
    ![User- Search Orders](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/user_home.jpeg "Search Orders")
    * Search Order History
    ![User- Search Order History](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/user_history.jpeg "Search Order History")
    * Order Book
    ![User- Order Book Step 1](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/user_new_1.jpeg "Order Book Step 1")
    ![User- Order Book Step 2](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/user_new_2.jpeg "Order Book Step 2")
    ![User- Order Book Step 3](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/user_new_3.jpeg "Order Book Step 3")
    
* All
    * Edit Account
    ![All- Edit Account](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/edit_account.jpeg "Edit Account")
    * Change Password
    ![All- Change Password](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/change_password.jpeg "Change Password")
    * Login
    ![All- Login](https://github.com/thakurpdhiraj/projectscreenshots/blob/master/Project_Screenshot/library_management_system/login.jpeg "Login")
    
