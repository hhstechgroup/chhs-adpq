# Technical approach

## 1. Project goals, objectives and task definition

EngagePoint understands the design, development, and implementation activities required to successfully develop a prototype (using open source technologies) in which foster parents perform of case-related functions. This prototype is an automated modern web application which scales horizontally and can be deployed to an Infrastructure as a Service (IAAS) or Platform as a Service (PAAS) provider.

## 2. User-centric design approach

EngagePoint applied user-centric design principles in creating the prototype by following the US Digital Services Playbook. See the responses in [Playbook (Play 1 - Play 3)](../../C:/wiki/display/APQD/U.S.+Digital+Services+Playbook+checklist+and+data+collected) and [Requirements C - F](../../C:/wiki/pages/viewpage.action%3FpageId=2949131) for additional information.

## 3. Agile Methodology

To implement the prototype, we modified our Scrum methodology to align with the user-centric design approach. Additional details regarding our approach can be found in our response to [Requirement G](../../C:/wiki/pages/viewpage.action%3FpageId=2949131) and [US Digital Services Playbook Play 4](../../C:/wiki/display/APQD/U.S.+Digital+Services+Playbook+checklist+and+data+collected).

## 4. Technical architecture

EngagePoint selected the Java Virtual Machine (JVM) platform and Java 1.8 as the prototype's programming language. The JVM platform is commonly used in high-load web applications, such as Google. However, small prototype web applications can be created quickly and then transitioned to production usage on the same technology platform without having to entirely rewrite the application.

Prototype development using the JVM platform is the correct choice due to availability of qualified developers, low development and support cost, and low risk in the short- and long- term.

Maximizing our development team's performance is crucial, so we adhere to the following key factors that lead to higher productivity:

- Leverage a generic application platform for prototype construction
- Use code generators rather than boilerplate code
- Minimize time for the code-compile-test-deploy cycle

To minimize risk, we considered these factors for each system architecture component:

- Maturity level
- Success stories
- Documentation quality
- Cloud readiness
- Performance metrics
- Maintenance costs

### 4.1. Application Platform

EngagePoint used the open source code generator [JHipster](https://jhipster.github.io/), which let us generate a generic application based on  [Spring Boot](http://projects.spring.io/spring-boot/),  [AnglarJS](https://angularjs.org/) with incorporated minimal for production usage functionality, such as user management, user roles, configuration, monitoring.

JHipster let us choose technologies based on project requirements. For the prototype, EngagePoint chose this technology stack:

## 4.2. Rationale

The table below lists EngagePoint's architectural design decisions and their alignment with the functional and non-functional prototype requirements.

| Application Requirements | Technologies | Motivation and rationale |
| --- | --- | --- |
| Modern Web Application | HTML5, CSS3, [AngularJS](https://angularjs.org/) | AngularJS is a mature MVC JavaScript framework for web application development with decent documentation and an active GitHub community |
| Responsive UI | [Bootstrap](http://getbootstrap.com/) | Bootstrap is a popular framework for responsive UI, simplifying responsive UI implementation complexity |
| Allow foster parents to communicate with the case worker via a private inbox | [Elasticsearch](https://www.elastic.co/products/elasticsearch), [PostgreSQL](https://www.postgresql.org/), [Websockets](https://en.wikipedia.org/wiki/WebSocket) | - Elasticsearch implements search capabilities needed for inbox functionality like full-text search, relevancy, ranking, and fuzzy search. <br/>- PostgreSQL is production ready full-featured relational database used in an application for the persistence of messages in the private inbox. <br/>- Websockets technology is used for real-time notifications for new messages |
| Allow foster parents to view children's residential facilities in their zip code | [Leaflet](http://leafletjs.com/) [Mapzen Search](https://mapzen.com/projects/search/?lng=-76.67925&lat=39.01412&zoom=12) | - Leaflet is the leading open-source JavaScript library for mobile-friendly interactive maps. <br/>- Mapzen Search is an open source geocoding tool used for address lookup capabilities on the facilities page. |
| Allow foster parents to establish and manage their profile | [JHipster](https://jhipster.github.io/) [Hazelcast](http://hazelcast.org/) | - Generic JHipster application has built-in login, registration, and user profile functionality. EngagePoint has customized the generic implementation. <br/>- Hazelcast is a distributed data grid used for distributed cache capabilities and performance improvement of the application. For the prototype, we are storing user sessions as well as L2 Hibernate cache. |
| Application prototype shall be deployable to IAAS or PAAS environment | [Spring Boot](http://projects.spring.io/spring-boot/), [Docker](https://www.docker.com/), [Jenkins](https://jenkins.io/) | - Spring Boot provides DevOps tools like externalized configuration, monitoring, and logging. Spring Boot eliminates the need to use external application containers, simplifying cloud deployment. <br/>- Docker containers are used for all components of application infrastructure (application, Elasticsearch server, PostgreSQL server). Containerization helps with automated deployment and makes application environments agnostic. <br/>- Jenkins is open source tool used to implement continuous integration and delivery. |

### 4.3. Development Tools

JHipster provides tools which accelerate development and reduce the need for custom coding. Entity Generator supports application prototyping, allowing the Technical Architect to describe the standard Entity Relational Diagram using JDL (a domain specific language). Based on JDL, JHipster generates boilerplate code needed for simple CRUD operations with these entities:  [Liquibase](http://www.liquibase.org/) scripts for database objects, Hibernate entities, repository classes, Java REST resources, AngularJS controllers, REST client services, routers, unit tests for Java and JavaScript, and sample administrative UI.

The prototype has two maven profiles: DEV and PROD. The DEV profile is used on the local development environment and incorporates in-memory H2 and Elasticsearch engines. Spring Boot provides an embedded light-weight application container, Tomcat, which runs the prototype. We used  [Browsersync](https://www.browsersync.io/) and  [Spring Boot Devtools](http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html) for automated reload of front-end and back-end code. These techniques reduce development time to seconds as the developer views updates in real-time. The PROD profile builds the prototype's production version, which is optimized for production use.

### 4.4. Automated testing

- These automated testing tools ensure that the prototype meets California's requirements:
  - [Junit](http://junit.org/junit4/) - Java unit tests
  - [Spring Boot integration testing tools](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) - Java integration tests
  - [Karma JS](https://karma-runner.github.io/0.13/index.html) - JavaScript unit tests
  - [Gatling](http://gatling.io/#/) - Load testing
  - BDD framework  [Cucumber](https://cucumber.io/) - Acceptance testing

### 4.5. Deployment Approach

EngagePoint has experience in implementing automated Continuous Integration and Delivery for web applications. For the prototype, we used  [Jenkins](https://jenkins.io/) as a continuous integration and continuous delivery tool. Containerization using  [Docker](https://www.docker.com/) is the best approach for application delivery to target environments. Please refer to [Requirements J, L-O](https://chhs-apqd.atlassian.net/wiki/pages/viewpage.action?pageId=2949131) for additional information.

## 5. Summary

EngagePoint conducted these steps to implement the prototype:

1. Performed 1:1 interviews and documented results
2. Developed an interactive wireframe and performed usability testing with users
3. Developed user interface mock-ups using  [U.S. Web Design Standards](https://standards.usa.gov/)
4. Generated a generic web application using JHipster with an appropriate technology stack
5. Configured continuous integration and automated testing using Jenkins
6. Designed a data model and generated code artifacts using JHipster's entity generator
7. Developed custom user interfaces according to design mockups and integrated them into a generic application
8. Modified the front-end and back-end code to support ADPQ functionality

For each prototype development stage, our team created automated tests (along with performance and acceptance tests) for Java and JavaScript code. Code quality was controlled using  [SonarQube](http://www.sonarqube.org/), incorporated in an automated continuous delivery workflow that was implemented in  [Jenkins](https://jenkins.io/). We also used manual code review in order to ensure code quality.

Using this process, we have a modern single-page web application for foster parents which is cloud ready.

#  References

| **#** | **Requirement** | **Description** |
| --- | --- | --- |
| 1. | a. Assigned one leader and gave that person authority and responsibility and held that person accountable for the quality of the prototype submitted.  [Margreta Silverstone](../../C:%5Cwiki%5Cdisplay%5C~margreta.silverstone) |  Margreta | | 
2. | b. Assembled a multidisciplinary and collaborative team that includes, at a minimum, five of the labor categories as identified in Attachment C - ADPQ Vendor Pool Labor Category Descriptions.  [Margreta Silverstone](../../C:%5Cwiki%5Cdisplay%5C~margreta.silverstone) | <br/>- Agile ADPQ Product Manager (Margreta)<br/>- Delivery Manager / Technical Architect (Leonid)<br/>- Interaction Designer (Dariia)<br/>- Business Analyst / User Researcher (Pavel)<br/>- Security Engineer (Dmitry)<br/>- DevOps Engineer (Dmytro)<br/>- Front End Designer (Yevhen, Oleksander)<br/>- Backend Web Developer (Oleg, Alexander, Alexander, Serge)  |
| 3. |c. Understood what people needed, by including people in the prototype development and design process -   [Pavel Khozhainov](../../C:%5Cwiki%5Cdisplay%5C~pavel.khozhainov),  [Dariia Iarmuratii](../../C:%5Cwiki%5Cdisplay%5C~dariia.iarmuratii), People were Shelly & Kacie & Others | Interviewed Users ( [Shelly](../../C:%5Cwiki%5Cpages%5Cviewpage.action%3FpageId=1212557) & [Kacie](../../C:%5Cwiki%5Cpages%5Cviewpage.action%3FpageId=1212559)) for initial needs. Solicited feedback in the prototype development ( [Shelly](../../C:%5Cwiki%5Cpages%5Cviewpage.action%3FpageId=2523151)) |
| 4. | d. Used at least three "human-centered design" techniques or tools -  [Pavel Khozhainov](../../C:%5Cwiki%5Cdisplay%5C~pavel.khozhainov),  [Dariia Iarmuratii](../../C:%5Cwiki%5Cdisplay%5C~dariia.iarmuratii) | During application prototype development we have used following "human-centered design" technics and tools. Follow the link to see materials collected using each of listed technics.<br/>- [User Interview](https://chhs-apqd.atlassian.net/wiki/display/APQD/01.+Interviews)<br/>- [Expert Interview](https://chhs-apqd.atlassian.net/wiki/pages/viewpage.action?<br/>pageId=1212557)<br/>- [User Needs](https://chhs-apqd.atlassian.net/wiki/display/APQD/02.+User+Needs)<br/>- [User Personas](https://chhs-apqd.atlassian.net/wiki/display/APQD/03.+User+Personas)<br/>- [User Stories & Scenarios](https://chhs-apqd.atlassian.net/wiki/pages/viewpage.action?pageId=1212431)<br/>- [Wireframing](https://chhs-apqd.atlassian.net/wiki/display/APQD/05.+List+of+Axure+wireframe+versions)<br/>- [Usability Testing](https://chhs-apqd.atlassian.net/wiki/display/APQD/06.+Scenarios+for+usability+testing)<br/>- [Design mockups](https://chhs-apqd.atlassian.net/wiki/display/APQD/08.+Pages+in+Design)<br/>In our opinion, interactive wireframes play an important part in human-centric design approach because it provides ability quickly prototype and validate design ideas with users. The interactive wireframe is an input for application development team. In such way, properly organized design stage minimizes the need to rework actual application on later stages when the cost of changes will be much more expensive for the customer.  Additional information within the [UX Design Process](../../C:%5Cwiki%5Cdisplay%5CAPQD%5CUX+Design+Process)  |
| 5. | e. Created or used a design style guide and/or a pattern library -  [Dariia Iarmuratii](../../C:%5Cwiki%5Cdisplay%5C~dariia.iarmuratii) |  Please see within the [UX Design Process](../../C:%5Cwiki%5Cdisplay%5CAPQD%5CUX+Design+Process) |
| 6. | f. Performed usability tests with people -  [Pavel Khozhainov](../../C:%5Cwiki%5Cdisplay%5C~pavel.khozhainov),  [Dariia Iarmuratii](../../C:%5Cwiki%5Cdisplay%5C~dariia.iarmuratii) | Please see within the [UX Design Process](../../C:%5Cwiki%5Cdisplay%5CAPQD%5CUX+Design+Process)the artifacts for user feedback, including  [local users](../../C:%5Cwiki%5Cdisplay%5CAPQD%5C04.+Local+user+interviews) input. |
| 7. | g. Used an iterative approach, where feedback informed subsequent work or versions of the prototype -  [Dariia Iarmuratii](../../C:%5Cwiki%5Cdisplay%5C~dariia.iarmuratii);  [Leonid Marushevskiy](../../C:%5Cwiki%5Cdisplay%5C~admin) | Please see the [Agile Approach Overview](../../C:%5Cwiki%5Cdisplay%5CAPQD%5CAgile+Approach+Overview)for the iterative process used. According to the methodology, we have used a short 2 days iteration for Design Group and 1-week iteration for application prototype development. Three times a week we had Design sessions to review wireframes, design mockups and gather feedback from team members on design artifacts. Examples of Recorded Design sessions and [meeting notes](https://chhs-apqd.atlassian.net/wiki/display/APQD/Design+Sessions).Every week we have Sprint Demo meeting in order to demonstrate application prototype to Product Manager and other interested stakeholders. [M](https://chhs-apqd.atlassian.net/wiki/display/APQD/Sprint+Demos) [eeting notes](https://chhs-apqd.atlassian.net/wiki/display/APQD/Design+Sessions) [from Sprint.](https://chhs-apqd.atlassian.net/wiki/display/APQD/Design+Sessions) According to user-centric design approach, we were focused on users input and feedback. We conducted regular interviews with defined in c. people as well as usability testing with other users. [Transcripts of interviews](https://chhs-apqd.atlassian.net/wiki/display/APQD/01.+Interviews) and recordings of interviews can be found in GitHub repository. Based on user's feedback we [iteratively evolve wireframes](https://chhs-apqd.atlassian.net/wiki/display/APQD/05.+List+of+Axure+wireframe+versions).<br/>During 3 weeks of the development team have implemented tree versions of application prototype 0.1, 0.2 and 1.0. |
| 8. | h. Created a prototype that works on multiple devices, and presents a responsive design -  [Leonid Marushevskiy](../../C:%5Cwiki%5Cdisplay%5C~admin),  [Dariia Iarmuratii](../../C:%5Cwiki%5Cdisplay%5C~dariia.iarmuratii) | Along with wireframes for the desktop version, we also maintain wireframe for mobile version [Version 01(cell) - 05/26/2016 for Cell Phone](../../C:%5Cwiki%5Cpages%5Cviewpage.action%3FpageId=1900630). Base on wireframe we produced design mockups for the mobile version. Both wireframe and design mockups become requirements for final application.In order to implement responsive UI in the application prototype, we have used [Bootstrap](http://getbootstrap.com/) framework. Bootstrap is HTML, CSS, and JS framework for developing responsive, mobile first projects on the web. It provides needed mechanisms to implement applications with user interfaces which are different depends on device resolution.   Please see the [UX Design Process](../../C:%5Cwiki%5Cdisplay%5CAPQD%5CUX+Design+Process)the  [list of Axure mockups](../../C:%5Cwiki%5Cdisplay%5CAPQD%5C05.+List+of+Axure+wireframe+versions). |
| 9. | i. Used at least five modern (see Note #2) and open-source technologies, regardless of architecturallayer(frontend, backend, etc.) - [Leonid Marushevskiy](../../C:%5Cwiki%5Cdisplay%5C~admin) | Below some of used in application prototype technologies, their versions and links to the website with release notes or proof of date of release.Frontend (files [package.json](https://mdc-lad-git.engagepoint.us/cws/chhs-apqd/blob/develop/package.json) for NPM packages and [bower.json](https://mdc-lad-git.engagepoint.us/cws/chhs-apqd/blob/develop/bower.json) for Bower packages)<br/>- [Bootstrap](http://getbootstrap.com/) 3.3.5: [release 07/15/2015](http://blog.getbootstrap.com/2015/06/15/bootstrap-3-3-5-released/)<br/>- [UI Bootstrap](http://angular-ui.github.io/bootstrap/) 1.2.5: [release 03/20/2016](https://github.com/angular-ui/bootstrap/releases/tag/1.2.5)<br/>- [AngularJS](https://angularjs.org/) 1.4.8: [release date](https://github.com/angular/code.angularjs.org/tree/master/1.4.8) [11/20/2015](https://github.com/angular/code.angularjs.org/tree/master/1.4.8)<br/>- [Bower](http://bower.io/)1.7.9: [released 04/05/2016](https://www.versioneye.com/nodejs/bower/1.7.9)<br/>- [Grunt](http://gruntjs.com/)0.4.5: [released 05/12/2014](http://gruntjs.com/blog/2014-05-12-grunt-0.4.5-released)<br/>- [Karma JS](http://karma-runner.github.io/0.13/index.html) 0.13.19: [release 02/12/2016](http://karma-runner.github.io/0.13/about/changelog.html)<br/><br/>Backend (dependencies and versions are in Maven [pom.xml](https://mdc-lad-git.engagepoint.us/cws/chhs-apqd/blob/develop/pom.xml) file)<br/>- [Spring Boot](http://projects.spring.io/spring-boot/) 1.3.1.RELEASE: [release 12/18/2015](https://spring.io/blog/2015/12/18/spring-boot-1-3-1-and-1-2-8-available-now)<br/>- [Elasticsearch](https://www.elastic.co/products/elasticsearch) 1.5.2: [release 04/27/2015](https://www.elastic.co/blog/elasticsearch-1-5-2-and-1-4-5-released)<br/>- [Hazelcast](http://hazelcast.org/) 3.5.4: [release 11/25/2015](http://hazelcast.org/download/archives/)<br/>- [Liquibase](http://www.liquibase.org/) 3.4.2:   [release 11/24/2015](http://www.liquibase.org/2015/11/liquibase-3-4-2-released.html) |
| 10. | j. Deployed the prototype on an Infrastructure as a Service (Iaas) or Platform as Service (Paas) provider, and indicated which provider they used. -  [Dmytro Balym](../../C:%5Cwiki%5Cdisplay%5C~dmytro.balym), Grant Walker; Shane Evans |  Deployed into Amazon cloud services. |
| 11. | k. Developed automated unit tests for their code -  [Leonid Marushevskiy](../../C:%5Cwiki%5Cdisplay%5C~admin) |  In order to eliminate functional regression and ensure that application in compliance with requirements we are using different levels of automated testing:<br/>- Java unit tests using  [JUnit](http://junit.org/junit4/) - Jenkins job:  [http://mdc-apdq-inf-a1:8888/job/chhs-apqd\_test\_UT/](http://mdc-apdq-inf-a1:8888/job/chhs-apqd_test_UT/)<br/>- Java integration tests using  [Spring Boot integration testing tools](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) <br/>- Jenkins job:  [http://mdc-apdq-inf-a1:8888/job/chhs-apqd\_test\_UT/](http://mdc-apdq-inf-a1:8888/job/chhs-apqd_test_UT/)<br/>- JavaScript unit tests using  [Karma JS](https://karma-runner.github.io/0.13/index.html) - Jenkins job:  [http://mdc-apdq-inf-a1:8888/job/chhs-apqd\_test\_UI/](http://mdc-apdq-inf-a1:8888/job/chhs-apqd_test_UI/)<br/>- Load testing using  [Gatling](http://gatling.io/#/) - Jenkins job:  [http://mdc-apdq-inf-a1:8888/job/chhs-apqd\_test\_perf/](http://mdc-apdq-inf-a1:8888/job/chhs-apqd_test_perf/)<br/>- Acceptance testing using BDD framework  [Cucumber](https://cucumber.io/) - Jenkins job: [http://mdc-apdq-inf-a1:8888/job/chhs-apqd\_test\_Cucumber/](http://mdc-apdq-inf-a1:8888/job/chhs-apqd_test_Cucumber/) |
| 12. | l. Setup or used a continuous integration system to automate the running of tests and continuously deployed their code to their IaaS or PaaS provider. -  [Dmytro Balym, Grant Walker; Shane Evans](../../C:%5Cwiki%5Cdisplay%5C~dmytro.balym) | Describe continuous integration framework we use:<br/>Tools: [Jenkins](https://jenkins.io/), [Sonar](http://www.sonarqube.org/)Provide high-level description of Jenkins jobs workflow:<br/>- Build source code<br/>- Run series of tests:<br/>  - Java unit tests<br/>  - JavaScript unit tests<br/>  - Sonar code analyze<br/>- In case of all previous tests passed - deploy to Development environment<br/>- Run acceptance tests based on Cucumber<br/>- Run load tests based on Gatling<br/>- In case of all tests passed - deploy into AWS |
| 13. |m. Setup or used configuration management -  [Dmytro Balym](../../C:%5Cwiki%5Cdisplay%5C~dmytro.balym) | Configuration management implemented as Chef cookbook to install Docker infrastructure, pull all necessary images to up and run the whole solution. |
| 14. |n. Setup or used continuous monitoring -  [Dmytro Balym](../../C:%5Cwiki%5Cdisplay%5C~dmytro.balym) | Continuous monitoring implemented by the synergy of built-in AWS containers tools for hardware items and Zabbix for application specific parameters. |
| 15. |- o. Deployed their software in a container (i.e., utilized operating-system-level virtualization) -  [Dmytro Balym](../../C:%5Cwiki%5Cdisplay%5C~dmytro.balym) | The Prototype is delivered as Docker container image. It requires PostgreSQL DB and Elasticsearch as external dependencies. The Prototype can be configured to use dependencies as external services or as linked Docker containers. |
| 16. |p. Provided sufficient documentation to install and run their prototype on another machine -  [Leonid Marushevskiy](../../C:%5Cwiki%5Cdisplay%5C~admin),  [Dmytro Balym](../../C:%5Cwiki%5Cdisplay%5C~dmytro.balym) | In order to install and run prototype on another machine we provide several available options like the portable version for Windows 64x, Docker-based deployment and compilation of prototype from source code.Portable version (Windows x64 only)The fastest way to start application prototype on another computer is to download a portable version of the application prototype for Windows x64 using the link below. You should unzip the package and start run.bat file to start the application. Start of application will take couple minutes and the application will be available by URL  [http://127.0.0.1:8080/#/](http://127.0.0.1:8080/#/)<br/>In portable mode, the application prototype will use in-memory database H2 and in-memory search index Elasticsearch. In this mode every next time when you will stop the application all data will  be gone.<br/>Download Windows 64x <<<link to GitHub>>>Start application using DockerWe have published docker image with the application prototype to the [Docker Hub](https://hub.docker.com/). In order to run Docker image on another machine, you need to do following steps:<br/>1. Install [Docker Toolbox](https://www.docker.com/products/docker-toolbox) on your machine<br/>2. Start Docker Quickstart Terminal or use graphical tool Kitematic(Alpha)<br/>3. Pull Docker image of prototype from [Docker Hub](https://hub.docker.com/r/kaver79/chhs-apqd/) using command line: docker pull kaver79/chhs-apqd<br/>or pull the same image using Kitematic (Alpha)<br/>4. Start image using command line or using Kitematic (Alpha)<br/>5. Application will be available by default URL [http://127.0.0.1:8080/#/](http://127.0.0.1:8080/#/)<br/>Compile application from source code and start (any operation system) In order to be able to compile application from source code you will need to setup and configure development environment using steps below:<br/>1. Install Java Development Kit (JDK) version 8 from  [the Oracle website](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).<br/>2. Install Java dependency management tool Maven from official  [Maven website](http://maven.apache.org/)<br/>3. Install Git from  [git-scm.com](https://git-scm.com/downloads)<br/>4. Clone project source code from GitHub repository  [https://github.com/engagepoint/chhs-apqd](https://github.com/engagepoint/chhs-apqd)<br/>5. Install Node.js from  [the Node.js website](http://nodejs.org/). This will also install npm, which is the node package manager we are using in the next commands.<br/>6. Navigate to Git repository folder chhs-apqd<br/>7. Install Yeoman using command line: npm install -g yo<br/>8. Install Bower using command line: npm install -g bower<br/>9. Install  [Grunt](http://gruntjs.com/) using command line: npm install -g grunt-cli<br/>10. Install JHipster: npm install -g generator-jhipster<br/>When development environment is configured you can compile and run prototype application using steps below:<br/>1. From Git repository folder chhs-apqd, run Maven command: mvn spring-boot:run<br/>2. Application will be automatically started on  [http://127.0.0.1:8080/#/](http://127.0.0.1:8080/#/) in couple minutes |
| 17. |q. Prototype and underlying platforms used to create and run the prototype are openly licensed and free of charge -  [Leonid Marushevskiy](../../C:%5Cwiki%5Cdisplay%5C~admin) | EngagePoint has used only open source technologies and platforms for prototype creation. Key technologies with links to source code and license type are described below:<br/>1. Client side:<br/>- [Bootstrap](http://getbootstrap.com/) 3.3.5: [source code](https://github.com/twbs/bootstrap) [,  ](http://getbootstrap.com/) [MIT License](https://github.com/twbs/bootstrap/blob/master/LICENSE)<br/>- [UI Bootstrap](http://angular-ui.github.io/bootstrap/) [:](http://getbootstrap.com/) [source code](https://github.com/angular-ui/bootstrap) [,  ](http://getbootstrap.com/) [MIT License](https://github.com/angular-ui/bootstrap/blob/master/LICENSE)<br/>- [AngularJS](https://angularjs.org/): [source code](https://github.com/angular/angular.js), [MIT License](https://github.com/angular/angular.js/blob/master/LICENSE)<br/>- [Leaflet](http://leafletjs.com/): [source code](https://github.com/Leaflet/Leaflet), [BSD 2-Clause License](https://github.com/Leaflet/Leaflet/blob/master/LICENSE)<br/>- [Mapzen](https://mapzen.com/): [source code](https://github.com/mapzen/leaflet-geocoder), [MIT License](https://github.com/mapzen/leaflet-geocoder/blob/master/LICENSE)<br/>2. Server Side:<br/>- [Spring Boot](http://projects.spring.io/spring-boot/): [source code](https://github.com/spring-projects/spring-boot), [Apache License Version 2](https://github.com/spring-projects/spring-boot/blob/master/LICENSE.txt)<br/>- [Spring Framework](https://projects.spring.io/spring-framework/): [source code](https://github.com/spring-projects/spring-framework), [Apache License Version 2](https://github.com/spring-projects/spring-boot/blob/master/LICENSE.txt)<br/>- [Spring Security](http://projects.spring.io/spring-security/): [source code](https://github.com/spring-projects/spring-security), [Apache License Version 2](https://github.com/spring-projects/spring-security/blob/master/license.txt)<br/>- [Spring Data JPA](http://projects.spring.io/spring-data-jpa/): [source code](https://github.com/spring-projects/spring-data-jpa), [Apache License Version 2](http://docs.spring.io/spring-data/jpa/snapshot-site/license.html)<br/>- [Spring Data Elasticsearch](http://projects.spring.io/spring-data-elasticsearch/): [source code](https://github.com/spring-projects/spring-data-elasticsearch), [Apache License Version 2](https://github.com/spring-projects/spring-data-elasticsearch/blob/master/src/main/resources/license.txt)<br/>- [Hibernate ORM](http://hibernate.org/orm/) [:](http://hibernate.org/orm/) [source code](https://github.com/hibernate/hibernate-orm) [,](http://hibernate.org/orm/) [LGPL V2.1](http://hibernate.org/license/)<br/>- [Elasticsearch](https://www.elastic.co/products/elasticsearch): [source code](https://github.com/elastic/elasticsearch), [Apache License Version 2](https://github.com/elastic/elasticsearch/blob/master/LICENSE.txt)<br/>- [PostgreSQL](https://www.postgresql.org/): [source code](https://github.com/postgres/postgres), [PostgreSQL Licence](https://opensource.org/licenses/postgresql) open license similar to BSD or MIT<br/>- [Hazelcast](http://hazelcast.org/): [source code](https://github.com/hazelcast/hazelcast), [Apache License Version 2](https://github.com/elastic/elasticsearch/blob/master/LICENSE.txt) [     ](https://github.com/spring-projects/spring-framework) |
