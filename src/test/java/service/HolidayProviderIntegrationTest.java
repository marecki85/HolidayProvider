package service;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HolidayProviderIntegrationTest {

	private final String contextPath = "/HolidayAPI/webapi/holidayprovider";

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	public void getCommmonHolidaySomeDaysBeforeHoliday() {
		Response response = given().queryParam("country1", "PL").queryParam("country2", "CN")
				.queryParam("date", "2014-04-02").when().get(contextPath).then().statusCode(200)
				.contentType("application/json").extract().response();

		String name1 = response.jsonPath().getString("name1");
		String name2 = response.jsonPath().getString("name2");
		String date = response.jsonPath().getString("date");

		assertEquals("Święto Pracy", name1);
		assertEquals("May Day", name2);
		assertEquals("2014-05-01", date);
	}

	@Test
	public void getCommmonHolidayInTheDayOfHoliday() {
		Response response = given().queryParam("country1", "ES").queryParam("country2", "DE")
				.queryParam("date", "2011-12-25").when().get(contextPath).then().statusCode(200)
				.contentType("application/json").extract().response();

		String name1 = response.jsonPath().getString("name1");
		String name2 = response.jsonPath().getString("name2");
		String date = response.jsonPath().getString("date");

		assertEquals("Navidad", name1);
		assertEquals("Weihnachtstag", name2);
		assertEquals("2011-12-25", date);
	}

	@Test
	public void getCommmonHolidayForCountryHavingTwoHolidaysAtOneDay() {
		Response response = given().queryParam("country1", "US").queryParam("country2", "ST")
				.queryParam("date", "2011-12-31").when().get(contextPath).then().statusCode(200)
				.contentType("application/json").extract().response();

		String name1 = response.jsonPath().getString("name1");
		String name2 = response.jsonPath().getString("name2");
		String date = response.jsonPath().getString("date");

		assertEquals("Last Day of Kwanzaa; New Year's Day", name1);
		assertEquals("New Year", name2);
		assertEquals("2012-01-01", date);
	}
}
