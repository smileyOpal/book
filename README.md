# Getting Started
Please run project with steps provide below

#### 1. Compile project
```
./mvnw clean compile
```

#### 2. Run test
```
./mvnw test
```

#### 3. Package to jar file
```
./mvnw package
```

#### 4. Build docker image
```
docker build . --tag book
```

#### 5. Run built docker image
```
docker run --publish 8080:8080 book
```
* Once docker up, use the given postman collection to call APIs
* Here is the swagger endpoint once service is up http://localhost:8080/swagger-ui.html

## API Specification

#### Create new user
```
curl --location --request POST 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "userA",
  "surname": "userA_lastname",
  "username": "userA.last",
  "password": "1234",
  "date_of_birth": "1988-04-20"
}'
```

#### Login
```
curl --location --request POST 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "userA.last",
    "password": "1234"
}'
```

#### Get user detail
```
curl --location --request GET 'http://localhost:8080/api/users' \
--header 'Authorization: Bearer {{id_token}}'
```
`id_token` get token when call login API

#### Get all books
```
curl --location --request GET 'http://localhost:8080/api/books' \
--header 'Authorization: Bearer {{id_token}}'
```
`id_token` get token when call login API

#### Create user's order
```
curl --location --request POST 'http://localhost:8080/api/users/orders' \
--header 'Authorization: Bearer {{id_token}}'
--header 'Content-Type: application/json' \
--data-raw '{
  "orders": [
    {
      "bookId": 1,
      "amount": 1
    },
    {
      "bookId": 2,
      "amount": 2
    }
  ]
}'
```
`id_token` get token when call login API

## Reference Documentation
For further reference, please consider the following sections:
* [Postman collection] is in folder path below
```
/postman/BOOK.postman_collection.json
```
* [Diagram] is in folder path below 
```
/diagram
```