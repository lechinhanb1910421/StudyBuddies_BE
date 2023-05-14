# StudyBuddies_BE

Back end side of StudyBuddies Project

**Author:** Nhan Le Nguyen Chi \
**Nickname:** Everett Le \
**Student Code:** B1910421 \
**Course Code:** CT466-07 

## **This project is deployed and runs on Docker environment**

**Please change the values in [example.env](example.env) file and rename it to ".env" before startup**

## **Other configs:**
1. [Postgres Adapter](postgresql-42.5.0.jar) can be downloaded from [Postgres official download site](https://jdbc.postgresql.org/download/#older-versions)  and make sure to put the correct jar file name in this line <br>
`COPY postgresql-42.5.0.jar $JBOSS_HOME/modules/system/layers/base/org/postgresql/main/` <br>
in [Dockerfile](Dockerfile) (at line 13).
2. **Firebase Cloud Messaging for Push Notification** \
Register a new Firebase App at [Firebase](https://firebase.google.com/) and paste the configuration file into [firebaseAccountKey file](keys/firebaseAccountKey.example.json) and **rename it to "firebaseAccountKey.json"**
3. Feel free to change configs in [Log4j Config File for Runtime](src/main/resources/log4j2.xml) and [Log4j Config File for Build time](src/test/resources/log4j2.xml) as you pleased.
Change sth