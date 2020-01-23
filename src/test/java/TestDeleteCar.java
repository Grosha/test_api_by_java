import api.EndPoints;
import api.ResponseMessages;
import org.junit.jupiter.api.Test;

import static helpers.APIHelper.responseSpecEqualsMessage;
import static io.restassured.RestAssured.given;

public class TestDeleteCar extends TestBaseAPI {

    @Test
    void testDeleteNoExistCar() {

        given()
                .when()
                .delete(EndPoints.deleteCar, "RX350-")
                .then()
                .spec(responseSpecEqualsMessage(ResponseMessages.ERROR_CAR_ABSENT_IN_THE_LIST));
    }
}
