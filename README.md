## SPM-2023-LES
This project, SPM-2023-LES, is a platform developed for an association called _"Prodotti Nobili Marchigiani e Italiani"_ with the aim of facilitating communication and the promotion of products between clients and farmers belonging to this association. 
***
## Let's start!
### Frontend
* Clone the repository: `git clone https://github.com/FabrizioFornari/SPM-2023-LES-Frontend.git`
* Install Angular: `npm install -g @angular/cli@17`
* Run the project: `ng serve`
### Backend
* Clone the repository: `git clone https://github.com/FabrizioFornari/SPM-2023-LES-Backend.git`
* Modify the _application.properties_ file adding details of your database
* Build the project : `mvn clean install`
* Run the application : `mvn spring-boot:run`
### Docker
After cloning the frontend and backend and making sure we have both projects in the same folder that we might call _SPM_ , open the terminal and run the following command: `docker-compose -p SPM up -d`
### Help
If you need more information, consult the [wiki](https://github.com/FabrizioFornari/SPM-2023-LES-Frontend/wiki/Getting-started).



