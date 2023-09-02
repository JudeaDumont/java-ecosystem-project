# Checkout the repo here: https://github.com/JudeaDumont/jersey-servlet-scaffolding
- I have setup the jersey servlet in there with apache tomcat

- if github actions supports multiple container testing scenarios, I will need to use that
- next I will need to setup github actions for the "jersey servlet" repo, with apache tomcat as an apt-get install
- then I will need to find a way to run the unit tests for it, both locally and in github actions
- then I will need to introduce other components of the system, either on the same container
- but maybe splitting them up into multiple containers for github actions to test is a good future