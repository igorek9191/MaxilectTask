package ourLitleTask;

import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.testng.Assert.assertEquals;

public class MaxilectRestServicePositiveTest {

    private static String BASE_URL = "http://localhost:28080";
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";

    private static String USER_NAME = "Igor";
    private static String USER_LAST_NAME = "Vlasyuk";

    String personId;

    @Test(priority = 1)
    public void checkPost(){

        String responce = given()
            .headers(FIRST_NAME, USER_NAME,LAST_NAME, USER_LAST_NAME)
            .when().post(BASE_URL+"/rs/users")
            .then()
            .log().all()
            .statusCode(200)
            .assertThat().header(FIRST_NAME, USER_NAME)
            .assertThat().header(LAST_NAME, USER_LAST_NAME)
            .extract().body().asString();

        Matcher matcher = Pattern.compile("(ID=)(\\d*)(,)").matcher(responce);
        matcher.find();
        personId = matcher.group(2);
    }

    @Test(priority = 2)
    public void checkGetAll(){
        given()
            .when()
            .get(BASE_URL+"/rs/users")
            .then().log().all()//ifError()
            .statusCode(200)
            .assertThat().header("Content-Length", Integer::parseInt, greaterThan(0));
    }

    @Test(priority = 3)
    public void checkPut(){

        given()
            .headers(FIRST_NAME, "Orlando",LAST_NAME, "Bloom")
            .when().put(BASE_URL+"/rs/users/" + personId)
            .then()
            .log().all()
            .statusCode(200)
            .assertThat().header("userId", personId)
            .assertThat().header(FIRST_NAME, "Orlando")
            .assertThat().header(LAST_NAME, "Bloom");
    }

    @Test(priority = 4)
    public void checkGetByID(){
        given()
            .when()
            .get(BASE_URL+"/rs/users/" + personId)
            .then().log().all()//ifError()
            .statusCode(200)
            .assertThat().header("userId", personId)
            .assertThat().header("Content-Length", Integer::parseInt, greaterThan(0));
    }


    @Test(priority = 5)
    public void checkDelete(){
        given()
            .when()
            .delete(BASE_URL+"/rs/users/" + personId)
            .then().log().all()//ifError()
            .statusCode(200)
            .assertThat().header("userId", personId)
            .assertThat().header("Content-Length", Integer::parseInt, equalTo(0));
    }

    @Test(priority = 6)
    public void checkAfterDelete(){
        String body = given()
            .when()
            .get(BASE_URL+"/rs/users")
            .then().log().all()//ifError()
            .statusCode(200)
            .assertThat().header("Content-Length", Integer::parseInt, equalTo(2))
            .extract().body().asString();
        assertEquals(body,"[]");
    }

}