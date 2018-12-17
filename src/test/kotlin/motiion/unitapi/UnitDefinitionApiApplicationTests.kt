package motiion.unitapi

import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.specification.RequestSpecification
import motiion.unitapi.controller.DefinitionModelConverter.convertToDTO
import motiion.unitapi.model.UnitDefinition
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [motiion.unitapi.UnitApiApplication::class])
class UnitDefinitionApiApplicationTests {

    @LocalServerPort
    var port: Int = 0

    var spec: RequestSpecification? = null

    @Before
    fun prepare() {
        spec = RequestSpecBuilder().setBaseUri("http://localhost:$port").log(LogDetail.ALL).build();
    }

    /**
     * Happy path cases
     */

    @Test
    fun `Demo 1`() {

        //Create first definition
        val (unitDef, unit) = TestDataGenerator.buildUnitAndDefinitionPair()
        var response = given(spec)
            .contentType("application/json")
            .body(unitDef)
            .post("/v1/units/definitions")
            .then().log().all()
            .assertThat()
            .statusCode(200)
            .body("name", `is`(unitDef.name))
            .extract().response()

        val id = response.jsonPath().getLong("id")

        //Publish it
        given(spec)
            .contentType("application/json")
            .pathParam("id", id)
            .post("/v1/units/definitions/{id}/publish")
            .then().log().all()
            .assertThat()
            .statusCode(200)
            .body("id", not(equals(id)))
            .extract()
            .response()

        //Look at publish
        given(spec)
            .contentType("application/json")
            .get("/v1/units/definitions/published/current")
            .then().log().all().assertThat()
            .statusCode(200)

        //Create unit
        given(spec)
            .contentType("application/json")
            .body(unit)
            .post("/v1/units")
            .then().log().all()
            .assertThat()
            .statusCode(200)
            .extract()
            .response()

        //Make changes to original definition

        //Publish new

        //Get previous unit and check it hasn't changed

        //Add Location Events to unit
    }

    @Test
    fun `Create a good UnitDefinition`() {
        val unitDef = TestDataGenerator.buildValidUnitDefinition()
        val unitDefDTO = convertToDTO(unitDef)
        given(spec)
            .contentType("application/json")
            .body(unitDefDTO)
            .post("/v1/units/definitions")
            .then().log().all().assertThat()
            .statusCode(200)
            .body("name", `is`(unitDefDTO.name))
    }

    @Test
    fun `Get Published UnitDefinitions`() {
        val unitDef = TestDataGenerator.buildValidUnitDefinition()
        val unitDefDTO = convertToDTO(unitDef)
        given(spec)
            .contentType("application/json")
            .body(unitDefDTO)
            .post("/v1/units/definitions")
            .then().log().all().assertThat()
            .statusCode(200)
            .body("name", `is`(unitDefDTO.name))
    }

//    @Test
    fun `Create unit by name`() {
        val unitDef = TestDataGenerator.buildValidUnitDefinition()
        val unitDefDTO = convertToDTO(unitDef)
        given(spec)
            .contentType("application/json")
            .body(unitDefDTO)
            .post("/v1/units/definitions")
            .then().log().all().assertThat()
            .statusCode(200)
            .body("name", `is`(unitDefDTO.name))
    }

    /**
     * Error cases
     */
    @Test
    fun `Create a bad UnitDefinition`() {
        val unitDef = buildInvalidDefinition()
        val unitDefDTO = convertToDTO(unitDef)
        given(spec)
            .contentType("application/json")
            .body(unitDefDTO)
            .post("/v1/units/definitions")
            .then().log().all().assertThat()
            .statusCode(400)
            .body("error", `is`("Bad Request"))
    }

    @Test
    fun `Hit an unknown resource`() {
        val unitDef = buildInvalidDefinition()
        val unitDefDTO = convertToDTO(unitDef)
        given(spec)
            .contentType("application/json")
            .body(unitDefDTO)
            .post("/foobar")
            .then().log().all().assertThat()
            .statusCode(404)
    }

    @Test
    fun `Creating definition with dupe name fails`() {

        //Create first definition
        val dto = TestDataGenerator.buildUnitDefWithDupeAttributes()
        given(spec)
            .contentType("application/json")
            .body(dto)
            .post("/v1/units/definitions")
            .then().log().all()
            .assertThat()
            .statusCode(400)
            .body("error", equalTo("Bad Request"))
            .extract().response()
    }

    private fun buildInvalidDefinition(): UnitDefinition {
        return TestDataGenerator.buildInvalidUnitDefinition()
    }
}

