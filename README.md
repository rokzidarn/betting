# BETTING PROJECT
__________________

#### Client:
    Paurus
#### Owner:
    Rok Zidarn

#### Tech stack:

    - Java 11
    - Spring FW
    - PostgreSQL
    - Gradle

#### Prerequisites:

    1. Install PostgreSQL (optionally pgAdmin) and create DB, check application.properties for
        credentials (username, password, DB name)

    2. Run application, BettingApplication.java in IntelliJ

    3. Use Postman to test, endpoints documented in Swagger,
        also Postman collection provided in /docs folder

    4. Taxation rules found in application.properties

    5. To run persist tasks, again check Swagger to see which endpoints to call, data found in /data folder

#### Results:
    Lexorank + multithreading: 302536 entries
    min(date_insert): 2021-08-13 18:21:54.407
    max(date_insert): 2021-08-13 18:22:20.521
    file: data/results.csv