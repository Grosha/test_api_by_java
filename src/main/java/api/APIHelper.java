package api;

import api.EndPoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matcher;
import pojo.car.Car;
import pojo.car.Message;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class APIHelper {

    public static RequestSpecification requestSpecWithBody(Object body) {
        return new RequestSpecBuilder()
                .log(LogDetail.BODY)
                .setBody(body)
                .build();
    }

    public static ResponseSpecification assertResponseMessage(Matcher<?> matcher) {

        return new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .expectBody("message", matcher)
                .build();
    }

//    public static Car updateCarWith(String paramName, String paramValue, Car car) {
//        return given()
//                .param(paramName, paramValue)
//                .when()
//                .patch(EndPoints.updateCar, car.getModel())
//                .then()
//                .log().body()
//                .extract()
//                .body()
//                .as(Message.class).getMessage();
//    }

        public static Car updateCarWith(String paramName, String paramValue, Car car) {
        return given()
                .param(paramName, paramValue)
                .when()
                .patch(EndPoints.updateCar, car.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();
    }
}
