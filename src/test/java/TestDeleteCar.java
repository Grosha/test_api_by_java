import api.EndPoints;
import org.junit.jupiter.api.Test;

import static api.APIHelper.assertResponseMessage;
import static api.ResponseMessages.ERROR_CAR_ABSENT_IN_THE_LIST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestDeleteCar extends TestBaseAPI {

    @Test
    void testDeleteNoExistCar() {

        given()
                .when()
                .delete(EndPoints.deleteCar, "RX350-")
                .then()
                .spec(assertResponseMessage(equalTo(ERROR_CAR_ABSENT_IN_THE_LIST)));
    }
}
