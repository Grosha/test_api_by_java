package api;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matcher;
import pojo.car.Car;
import pojo.car.Message;
import pojo.carlist.CarListResponce;

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

    public static Car requestGetUpdatedCarWith(String paramName, String paramValue, Car car) {
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

    public static void requestUpdateCarWith(String paramName, String paramValue, String carModel) {
        given()
                .param(paramName, paramValue)
                .when()
                .patch(EndPoints.updateCar, carModel)
                .then()
                .log().body()
                .statusCode(200);
    }

    public static Car requestUpdateCarWithoutParameters(Car car) {
        return given()
                .when()
                .patch(EndPoints.updateCar, car.getModel())
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();
    }

    public static Car requestGetAnyCar() {
        return given()
                .when()
                .get(EndPoints.car)
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();
    }

    public static Car requestGetCar(String paramName, String paramValue) {
        return given()
                .param(paramName, paramValue)
                .when()
                .get(EndPoints.car)
                .then()
                .log().body()
                .extract()
                .body()
                .as(Message.class).getMessage();
    }

    public static CarListResponce requestGetCarList() {
        return given()
                .when()
                .get(EndPoints.carList)
                .then()
                .log().body()
                .extract()
                .body()
                .as(CarListResponce.class);
    }

    public static void requestDeleteCarModel(String carModel) {
        String deleteCarMessage = String.format("Car model %s removed", carModel);
        given()
                .when()
                .delete(EndPoints.deleteCar, carModel)
                .then()
                .spec(assertResponseMessage(equalTo(deleteCarMessage)));
    }

    public static void requestAddNewCar(Car car) {
        given()
                .spec(requestSpecWithBody(new Gson().toJson(car)))
                .when()
                .post(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(ResponseMessages.NEW_CAR_ADDED)));
    }
}
