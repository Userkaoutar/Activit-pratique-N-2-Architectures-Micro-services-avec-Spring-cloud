# Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud
<img width="652" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/d301ccb7-9aeb-43d2-814c-c477a3f8c60b">


## Première partie : (Customer-Service, Inventory-Service, Spring Cloud Gateway, Eureka Discovery)

### Customer-Service


architecture:

<img width="330" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/8b04ee97-6725-4566-870d-cc503e7cfb0d">

dépandances:
- spring-boot-starter-actuator.
- spring-boot-starter-data-jpa.
- spring-boot-starter-data-rest.
- spring-boot-starter-web.
- spring-cloud-starter-netflix-eureka-client.
- spring-boot-devtools.
- h2.
- lombok.

 application.properties:
 ```bash

server.port=8081
spring.application.name=customer-service
spring.datasource.url=jdbc:h2:mem:customer-db
spring.cloud.discovery.enabled=true
#management.endpoints.web.exposure.include=*

```
CustomerServiceApplication:
```bash
@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository, RepositoryRestConfiguration restConfiguration) {
		restConfiguration.exposeIdsFor(Customer.class);
		return args -> {
			customerRepository.saveAll(List.of(
					Customer.builder().name("kaoutar").email("k@gmail.com").build(),
					Customer.builder().name("laila").email("l@gmail.com").build(),
					Customer.builder().name("nour").email("n@gmail.com").build()
			));
			customerRepository.findAll().forEach(System.out::println);
		};
	}

```
le test sur:http://localhost:8081/customers:

<img width="476" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/cdbe3b20-46f7-4d54-8f88-87bb4e475081">


### Inventory service
Gestion des produits

architecture:

<img width="341" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/9d2c7c2e-2cb5-4988-97c1-e9f206a97274">

dépandances:
- spring-boot-starter-actuator.
- spring-boot-starter-data-jpa.
- spring-boot-starter-data-rest.
- spring-boot-starter-web.
- spring-cloud-starter-netflix-eureka-client.
- spring-boot-devtools.
- h2.
- lombok.

 application.properties:
 ```bash

server.port=8082
spring.application.name=inventory-service
spring.datasource.url=jdbc:h2:mem:products-db
spring.cloud.discovery.enabled=true
#management.endpoints.web.exposure.include=*
spring.jpa.hibernate.ddl-auto=update

```
CustomerServiceApplication:
```bash
@Bean
	CommandLineRunner start(ProductRepository productRepository,
							RepositoryRestConfiguration restConfiguration) {
		restConfiguration.exposeIdsFor(Product.class);
		return args -> {
				productRepository.save(new Product(null,"ordi",600,12));
			productRepository.save(new Product(null,"fax",1000,4));
			productRepository.save(new Product(null,"Impre",800,8));

			productRepository.findAll().forEach(p->{
				System.out.println(p.getName());
			});

		};
	}
```
le test sur:http://localhost:8082/products:

<img width="447" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/8ec48166-ec3b-4ad6-92eb-f553e35e22f4">

### Gateway service
 passerelle entre les microservices


dépandances:
 -spring-boot-starter-actuator
 -spring-cloud-starter-gateway
 -spring-cloud-starter-netflix-eureka-client


 application.properties:
 ```bash

server.port=8083
spring.application.name=gateway-service
spring.cloud.discovery.enabled=true



```
application.yml:
 ```bash

spring:
  cloud:
    gateway:
      routes:
        - id: r1
          uri: http://localhost:8081/
          predicates:
            - Path=/customers/**

        - id: r2
          uri: http://localhost:8082/
          predicates:
            - Path=/products/**

      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedHeaders: "*"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE



```
 GatewayServiceApplication:
```bash
@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}


	/*RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route((r) -> r.path("/customers/**").uri("lb://CUSTOMER-SERVICE"))
				.route((r) -> r.path("/products/**").uri("lb://INVENTORY-SERVICE"))
				.build();
	}*/

	@Bean
	public DiscoveryClientRouteDefinitionLocator definitionLocator(
			ReactiveDiscoveryClient rdc,
			DiscoveryLocatorProperties properties) {
		return new DiscoveryClientRouteDefinitionLocator(rdc, properties);
	}


}


```
le test sur:http://localhost:8083/Micro-SERVICE/*:

<img width="513" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/45669497-fc9f-4a6f-80fb-6d0120275219">


### Eureka Discovery
Eureka est un serveur d'enregistrement et de découverte de services couramment utilisé en conjonction avec Spring Boot pour la création de microservices. Il permet aux microservices de se trouver et de communiquer entre eux dans un environnement dynamique et évolutif.


dépandances:
- spring-cloud-starter-netflix-eureka-server.


 application.properties:
 ```bash

server.port=8761
eureka.client.fetch-registry=false
eureka.client.register-with-eureka=false


```
 EurikaDiscoveryApplication:
```bash
@Bean
@SpringBootApplication
@EnableEurekaServer
public class EurikaDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurikaDiscoveryApplication.class, args);
	}

}

```
le test sur:http://localhost:8761/:

<img width="941" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/22ec347d-913f-4d21-bdb8-25d3cbe41a97">



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
