import api.EndPoints;
import api.ResponseMessages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pojo.carlist.CarListResponce;

import static api.APIHelper.assertResponseMessage;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestGetCarWhenAllCarRented extends TestBaseAPI {

    private CarListResponce carListResponce = null;

    @BeforeEach
    void rentAllCar() {
        carListResponce = given()
                .when()
                .get(EndPoints.carList)
                .then()
                .log().body()
                .extract()
                .body()
                .as(CarListResponce.class);

        carListResponce.getMessage().forEach(car -> given()
                .param("status", 0)
                .when()
                .patch(EndPoints.updateCar, car.getModel())
                .then()
                .log().body()
                .statusCode(200));
    }

    @Test
    void testGetCarWhenCarNoAvailable() {

        given()
                .when()
                .get(EndPoints.car)
                .then()
                .spec(assertResponseMessage(equalTo(ResponseMessages.ERROR_NO_FREE_CAR_AVAILABLE)));
    }

    @AfterEach
    void returnAllCar() {
        carListResponce.getMessage().forEach(car -> given()
                .param("status", 1)
                .when()
                .patch(EndPoints.updateCar, car.getModel())
                .then()
                .log().body()
                .statusCode(200));
    }
}
