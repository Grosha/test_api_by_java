import api.APIHelper;
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
        carListResponce = APIHelper.requestGetCarList();
        carListResponce.getMessage()
                .forEach(car -> APIHelper.requestUpdateCarWith("status", "0", car.getModel()));
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
        carListResponce.getMessage()
                .forEach(car -> APIHelper.requestUpdateCarWith("status", "1", car.getModel()));
    }
}
