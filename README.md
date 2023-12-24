# Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud
<img width="652" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/d301ccb7-9aeb-43d2-814c-c477a3f8c60b">
#  Architectures Micro-services avec Spring cloud

## Première partie : (Customer-Service, Inventory-Service, Spring Cloud Gateway, Eureka Discovery)

### Customer-Service
Un micro service qui consiste à ajouter les clients
- Customer Entity
```bash
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}
```
- Configuration 
bash
server.port=8081
spring.application.name=customer-server
spring.datasource.url=jdbc:h2:mem:customer-db
spring.cloud.discovery.enabled=true

- Application
screen localhost

### Inventory service
Un micro service qui consiste à ajouter les produits

- Product Entity
- Configuration
bash
server.port=8082
spring.application.name=inventory-service
spring.datasource.url=jdbc:h2:mem:inventory-db
spring.cloud.discovery.enabled=true

- Application
screen localhost

### Spring Cloud Gateway
#### Les dépendances 
- Spring Cloud Gateway
- Eureka Discovery Client
- Spring Boot Actuator
#### Configuration
screen

### Eureka Discovery
Eureka est un serveur d'enregistrement et de découverte de services couramment utilisé en conjonction avec Spring Boot pour la création de microservices. Il permet aux microservices de se trouver et de communiquer entre eux dans un environnement dynamique et évolutif.
- Configuration
bash
server.port=8761

# dont register server itself as a client
eureka.client.fetch-registry=false

# does not register itself in the service registry
eureka.client.register-with-eureka=false

## Deuxième Partie : Billing Service avec Open Feign Rest Client
Pour réaliser cette partie, nous avons besoin d'exécuter les microservices dans l'ordre suivant :

- Eureka-discovery (Port : 8761)
- Customer-service (Port : 8081)
- Inventory-Service (Port:8082)
- Gateway (Port : 8888)
### Billing Service
 Un micro Service qui consiste à commander des produits
#### Architecture
screen

#### Configuration
bash
server.port=8083
spring.application.name=billing-service
spring.datasource.url=jdbc:h2:mem:billing-db
spring.cloud.discovery.enabled=true

screen

## Troisième Partie : Angular
- Création d'un projet
 bash
 ng new bill-web-app
 
 - Ajouter une composante
 bash
 ng new bill-web-app
