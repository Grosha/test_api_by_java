# API tests for service 'Rent car service'

## Set up
What you need:
1.installed gradle
2.JAVA_HOME veriable
3.installed docker
4.download car service from https://github.com/Grosha/testing_api_service/archive/car_service_for_java.zip
5.unzip it and enter to that folder via terminal
6.set up server by command: docker-compose up -d (for reset service you need to stop it -> command docker-compose down)
7.one test will be always failed and report you can see at file, which gradle'll generate

## Run tests
```
$ gradle test
$ gradle test --tests "TestUpdateCar"

```