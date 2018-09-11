package ourLitleTask;

import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.restassured.RestAssured.given;

public class MaxilectRestServiceNegativeTest {

    private static String BASE_URL = "http://localhost:28080";
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";

    private static String USER_NAME = "Igor";
    private static String USER_LAST_NAME = "Vlasyuk";

    String personId;

    @Test(priority = 1)
    public void postEmptyFirstName(){

        given()
            .headers(FIRST_NAME, "",LAST_NAME, USER_LAST_NAME)
            .when().post(BASE_URL+"/rs/users")
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 2)
    public void postEmptyLastName(){

        given()
            .headers(FIRST_NAME, USER_NAME,LAST_NAME, "")
            .when().post(BASE_URL+"/rs/users")
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 3)
    public void postNotStringFirstName(){

        given()
            .headers(FIRST_NAME, 1,LAST_NAME, USER_LAST_NAME)
            .when().post(BASE_URL+"/rs/users")
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 4)
    public void postNotStringLastName(){

        given()
            .headers(FIRST_NAME, USER_NAME,LAST_NAME, 1)
            .when().post(BASE_URL+"/rs/users")
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 5)
    public void putWithoutID(){

        given()
            .headers(FIRST_NAME, "Orlando",LAST_NAME, "Bloom")
            .when().put(BASE_URL+"/rs/users/")
            .then()
            .log().all()
            .statusCode(405);
    }

    @Test(priority = 6)
    public void putWithEmptyFirstName(){

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

        given()
            .headers(FIRST_NAME, "",LAST_NAME, "Bloom")
            .when().put(BASE_URL+"/rs/users/"+personId)
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 7)
    public void putWithEmptyLastName(){

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

        given()
            .headers(FIRST_NAME, "Orlando",LAST_NAME, "")
            .when().put(BASE_URL+"/rs/users/"+personId)
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 8)
    public void putNotStringFirstName(){

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

        given()
            .headers(FIRST_NAME, 1,LAST_NAME, "Bloom")
            .when().put(BASE_URL+"/rs/users/"+personId)
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 9)
    public void putNotStringLastName(){

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

        given()
            .headers(FIRST_NAME, "Orlando",LAST_NAME, 1)
            .when().put(BASE_URL+"/rs/users/"+personId)
            .then()
            .log().all()
            .statusCode(406);
    }

    @Test(priority = 10)
    public void getWithNullInsteadOfID(){
        given().
            when().
            get(BASE_URL+"/rs/users/" + null)
            .then().log().all()//ifError()
            .statusCode(400);
    }

    @Test(priority = 11)
    public void deleteWithNullInsteadOfID(){
        given()
            .when()
            .delete(BASE_URL+"/rs/users/" + null)
            .then().log().all()//ifError()
            .statusCode(400);
    }

    @Test(priority = 12)
    public void deleteWithoutID(){
        given()
            .when()
            .delete(BASE_URL+"/rs/users/")
            .then().log().all()//ifError()
            .statusCode(405);
    }


}
