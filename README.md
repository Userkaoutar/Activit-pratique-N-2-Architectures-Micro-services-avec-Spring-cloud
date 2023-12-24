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
le test sur: http://localhost:8081/customers:

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
le test sur: http://localhost:8082/products:

<img width="447" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/8ec48166-ec3b-4ad6-92eb-f553e35e22f4">

### Gateway service
 passerelle entre les microservices


dépandances:
  - spring-boot-starter-actuator
  - spring-cloud-starter-gateway
  - spring-cloud-starter-netflix-eureka-client


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
le test sur: http://localhost:8083/Micro-SERVICE/*:

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


##Deuxième Partie : Billing Service avec Open Feign Rest Client:

architecture:

<img width="344" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/f4ce867e-e392-4da4-a712-0be378b6b7f8">

dépandances:
- spring-boot-starter-actuator.
- spring-boot-starter-data-jpa.
- spring-boot-starter-data-rest.
- spring-boot-starter-web.
- spring-cloud-starter-netflix-eureka-client.
- spring-boot-devtools.
- h2.
- lombok.
- spring-cloud-starter-openfeign

 application.properties:
 ```bash
server.port=9999
spring.application.name=billing-service
spring.datasource.url=jdbc:h2:mem:billing-db
spring.cloud.discovery.enabled=true
management.endpoints.web.exposure.include=*
# Enable H2 Console
spring.h2.console.enabled=true

# Configure H2 Console URL
spring.h2.console.path=/h2-console

```

BillingServiceApplication:
```bash

	@Bean
	CommandLineRunner start(BillRepository billRepository,
							ProductItemRepository productItemRepository,
							CustomerRestClient customerRestClient,
							ProductItemRestClient productItemRestClient) {
		return args -> {
			Customer customer = customerRestClient.getCustomerById(1L);
			Bill bill = billRepository.save(new Bill(null, new Date(), null, customer.getId(), null));
				PagedModel<Product> productPagedModel = productItemRestClient.pageProduct(0,10);
				productPagedModel.forEach(p -> {
					ProductItem productItem = new ProductItem();
					productItem.setPrice(p.getPrice());
					productItem.setQuantity(1+new Random().nextInt(100));
					productItem.setBill(bill);
					productItem.setProductID(p.getId());
					productItemRepository.save(productItem);
				});




		};
	}

```
le test sur eureka: 


<img width="844" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/b12dd473-fab5-4257-a184-277f70bf34b6">

le test sur: http://localhost:8083/BILLING-SERVICE/fullBill/1

<img width="376" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/146445d3-458f-411d-ab64-fb4218b45510">



## Troisième Partie : Angular
- Création d'un projet
```bash
 ng new ecom-web
 ```
![image](https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/bb35d96e-61e3-47ff-be0a-62d98b62ec5b)

![image](https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/96da9251-9fa2-401e-9aa1-7e3d3308eb10)


   -les composantes créés:


<img width="287" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/38eb2266-01c5-4643-86be-b955d92d9a9b">


app-routing.module.ts:

```bash
 const routes: Routes = [

  {
    path:"products", component: ProductsComponent,

  },
  {
    path:"customers", component: CustomersComponent,
  },
  {
    path:"orders/:customerId", component: OrdersComponent,
  },
];

 ```



intefaces:

![image](https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/0d552c3e-eb07-4624-9612-107c6dd72269)

<img width="848" alt="image" src="https://github.com/Userkaoutar/Activit-pratique-N-2-Architectures-Micro-services-avec-Spring-cloud/assets/101696114/b4789e49-9634-48ed-bd58-f1b105809cbf">


