# Setup

## Resolve Maven pom dependencies
- This involves resolving class related errors
- Maven pom project should run free of errors
- generated-sources should exist in ./target

## Hosts File

- add: "127.0.0.1 postgres" to hosts file. C:\Windows\System32\drivers\etc

## Install Postgresql

- See \webapi\src\main\util\CommandsFile
- Port: 5432
- Needs user: "postgres" with password: "root"
- Create DB: "SampleDB" CREATE DATABASE sampledb;


## Migrate Flyway

- Open Maven project widget in intelliJ
- Make sure that the maven settings used are the ones found in .github/workflows/maven-settings.xml
- Run Baseline
- Run Migrate
- Should all be green as long as this is a proper clean install etc.

## Add JAVA_HOME environment variable to the location of your jdk
- Might look like: C:\Users\chef\.jdks\corretto-19.0.1
- Restart cmdline or the whole of intelliJ to pick up new env var

## Setup maven encryption
- run ./mvnw  --encrypt-master-password root
- save the results into settings.security
- Copy settings-security.xml to C:\Users\<user>\.m2\settings-security.xml
- Make sure that the maven settings used are the ones found in .github/workflows/maven-settings.xml
- Change maven settings in IntelliJ to use .github/workflows/maven-settings.xml



#TroubleShooting:
- You might see an error "java.lang.IllegalArgumentException: Password cannot be set empty"
- this means you are missing the environment variable: JASYPT_ENCRYPTOR_PASSWORD=secretkey