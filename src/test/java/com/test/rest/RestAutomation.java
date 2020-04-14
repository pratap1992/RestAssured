package com.test.rest;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class RestAutomation {
	public String response;
	public String deck_id;
	public int remaining;
	
	@BeforeClass
		public void setup() {
		RestAssured.baseURI="https://deckofcardsapi.com";
		
	}
	@Test
	//Get request for creating new Deck
	public void GET_Deck()
	{
		
		response=given().log().all().
				header("Content-type","application/json").
		when().get("api/deck/new/").
		then().assertThat().statusCode(200).extract().response().asString();
		System.out.println("Response from the GET methode "+response);
		JsonPath js=new JsonPath(response);
		deck_id=js.getString("deck_id");
		remaining=js.getInt("remaining");
		System.out.println("Deck id = " + deck_id);
		System.out.println("Remaining card in the Deck =" + remaining);
		//since its a new deck total number of card should be 52
		assertEquals(remaining,52);
	}
	@Test
	//Draw  a card from the Same Deck , created in earlier Method
	public void GET_drawFromDeck() {
		response=given().log().all().header("Content-type","application/json").
		when().get("api/deck/"+deck_id+"/draw/").
		then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js=new JsonPath(response);
		System.out.println(response);
		remaining=js.getInt("remaining");
		System.out.println("Remaining card in the Deck =" + remaining);
		//Since we draw a card from the deck, then number of the cards should be 52-1=51
		assertEquals(remaining,51);
	}
	@Test
	//Draw the 2 card from same deck , from where we draw 1 card earlier
	public void Get_drawFromDeckCount() {
		response=given().log().all().header("Content-type","application/json").queryParams("count","2").
		when().get("api/deck/"+deck_id+"/draw/").
		then().assertThat().statusCode(200).extract().response().asString();
		System.out.println(response);
		JsonPath js=new JsonPath(response);
		remaining=js.getInt("remaining");
		System.out.println("Remaining card in the Deck =" + remaining);
		
		// form the same deck we are drawing 2 more cards from deck now total cards should be [52-1-2=49]
		
		assertEquals(remaining, 49);
	}
	
	@Test
	//Adding 2 Joker in the new Deck (newly Created )
	public void Get_Add_JokerEnabled() {
		response=given().log().all().header("Content-type","application/json").queryParams("jokers_enabled","true").
				when().get("api/deck/new/").
				then().assertThat().statusCode(200).extract().response().asString();
				System.out.println(response);
				JsonPath js=new JsonPath(response);
				remaining=js.getInt("remaining");
				System.out.println("Remaining card in the Deck =" + remaining);
				//since 2 joker added in new deck,now total card in deck should be 52+2= 54
				assertEquals(remaining,54);
			}
	}
			
	



