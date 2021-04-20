# Getting Started
Please run project with steps provide below

### 1. Compile project
```
./mvnw clean compile
```

### 2. Run test
```
./mvnw test
```

### 3. Package to jar file
```
./mvnw package
```

### 4. Build docker image
```
docker build . --tag book
```

### 5. Run built docker image
```
docker run --publish 8080:8080 book
```
* Once docker up, use the given postman collection to call APIs
* Here is the swagger endpoint once service is up http://localhost:8080/swagger-ui.html


### Reference Documentation
For further reference, please consider the following sections:
* [Postman collection] is in folder path below
```
/postman/BOOK.postman_collection.json
```