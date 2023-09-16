# Checkout the repo here: https://github.com/JudeaDumont/jersey-servlet-scaffolding
- I have setup the jersey servlet in there with apache tomcat
- I have also setup github actions that is now building the jersey servlet using jdk20

- I installed a VM for ubuntu so I have another avenue of developing github actions with my software.

- I setup github actions for the "jersey servlet" repo, with apache tomcat as an apt-get install

- I deployed the simple-service project using tomcat, and smart-tomcat plugin, 

- I told tomcat to host on a different port,

- installed and hosted postgres on a github actions container, 
- not sure if I can o multi-container deployments onto github actions, 
- but could not find an implementation for it so.

- I found a way to run the unit tests for it, both locally and in github actions

- Next I will need to make sure flyway and hibernate are working with the postgres installation, 
- then I can really start moving over the java components.

- noticed that the servlet repo is in a private repo, so I need to make that public, and also accept; that
- might be the sole reason why fb says no, was because they are not going to be able to see the project
- I sold myself with
- would not have been able to figure that out unless I had attempted to pull it on a different laptop

