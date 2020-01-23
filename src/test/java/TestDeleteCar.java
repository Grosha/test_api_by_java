import api.EndPoints;
import api.ResponseMessages;
import org.junit.jupiter.api.Test;

import static api.APIHelper.assertResponseMessage;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestDeleteCar extends TestBaseAPI {

    @Test
    void testDeleteNoExistCar() {

        given()
                .when()
                .delete(EndPoints.deleteCar, "RX350-")
                .then()
                .spec(assertResponseMessage(equalTo(ResponseMessages.ERROR_CAR_ABSENT_IN_THE_LIST)));
    }
}
