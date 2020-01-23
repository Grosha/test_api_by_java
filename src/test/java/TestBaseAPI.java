import api.EndPoints;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBaseAPI {
    @BeforeAll
    public void before() {

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(EndPoints.BASE_URL)
                .setPort(EndPoints.PORT)
                .log(LogDetail.URI)
                .build();

    }

    @AfterAll
    public void after() {
        System.out.println("after");
    }
}
