package SPLIT

import io.gatling.core.Predef._
import io.gatling.http.Predef._


import java.util.UUID.randomUUID

object ObjectOmsSplit {





  ////////////////////////////////////////////////////////////////////////
  //////////////// Test 1.1 - Mode Express avec Réservation et sans Génération d'ID /////////////
  /////////////////////////////////////////////////////////////////////////////////


  val scnTest1 = scenario("Test 1.1 - Mode Express avec Réservation et sans Génération d'ID")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(http("post test 1")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION","true")
      .header("X-GGL-CONTEXT-GENERATION-ID","false")
      .body(StringBody(
        """
          |{
          |    "typology": "ECOM",
          |    "commercial_cart_id": "0c0914b1-a2a5-435d-8425-f195793d6b38",
          |    "shop_code":"0000",
          |    "products_details": [
          |        {
          |            "uga": "24529917",
          |            "quantity": 5,
          |            "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |        },
          |        {
          |            "uga": "29167445",
          |            "quantity": 5,
          |            "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |        },
          |        {
          |            "uga": "44038490",
          |            "quantity": 5,
          |            "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |        }
          |    ]
          |}
          |""".stripMargin)).asJson
    .check(status is 201))
    .pause(1)



  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// Test 2 - Mode Express avec Réservation & Génération d'ID ////////////:
  /////////////////////////////////////////////////////////////////////////

  val scnTest2 = scenario("Test 2 - Mode Express avec Réservation & Génération d'ID")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(session => {
      val uniqueID = randomUUID()
      session.set("commercial_cart_id", uniqueID)
    })
    .exec{session =>
      println("commercial_cart_id : " + session("commercial_cart_id").as[String])
      session
    }
    .exec(http("post test 2")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "true")
      .body(StringBody(
        """
          |{
          |    "typology": "ECOM",
          |    "commercial_cart_id": "#{commercial_cart_id}",
          |    "shop_code":"0000",
          |    "products_details": [
          |        {
          |            "uga": "24529917",
          |            "quantity": 5,
          |            "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |        },
          |        {
          |            "uga": "29167445",
          |            "quantity": 5,
          |            "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |        },
          |        {
          |            "uga": "44038490",
          |            "quantity": 5,
          |            "delivery_mode": "EXPRESS_HOME_DELIVERY"
          |        }
          |    ]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)


  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// Test 3 - Mode Standard sans réservation avec Génération d'ID ////////////:
  /////////////////////////////////////////////////////////////////////////

  val scnTest3 = scenario("Test 3 - Mode Standard sans réservation avec Génération d'ID")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(session => {
      val uniqueID = randomUUID()
      session.set("commercial_cart_id", uniqueID)
    })
    .exec { session =>
      println("commercial_cart_id : " + session("commercial_cart_id").as[String])
      session
    }
    .exec(http("post test 3")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "true")
      .body(StringBody("""
          |{
          |    "typology": "ECOM",
          |    "commercial_cart_id": "#{commercial_cart_id}",
          |    "shop_code":"0000",
          |    "products_details": [
          |        {
          |            "uga": "24529917",
          |            "quantity": 5,
          |            "delivery_mode": "STANDARD_DELIVERY"
          |        },
          |        {
          |            "uga": "22460065",
          |            "quantity": 5,
          |            "delivery_mode": "STANDARD_DELIVERY"
          |        },
          |        {
          |            "uga": "44038490",
          |            "quantity": 5,
          |            "delivery_mode": "STANDARD_DELIVERY"
          |        }
          |    ]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)


  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// Test 4 - Modes de Livraison Multiples  ////////////:
  /////////////////////////////////////////////////////////////////////////
  val scnTest4 = scenario("Test 4 - Modes de Livraison Multiples")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(session => {
      val uniqueID = randomUUID()
      session.set("commercial_cart_id", uniqueID)
    })
    .exec { session =>
      println("commercial_cart_id : " + session("commercial_cart_id").as[String])
      session
    }
    .exec(http("post test 4")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "true")
      .body(StringBody(
        """
          |{
          |    "typology": "ECOM",
          |    "commercial_cart_id": "#{commercial_cart_id}",
          |    "available_deliveries_modes": ["EXPRESS_HOME_DELIVERY", "STANDARD_DELIVERY", "PICKUP_IN_STORE", "HOME_DELIVERY","RELAY_POINT_DELIVERY"],
          |    "shop_code":"0000",
          |    "products_details": [
          |        {
          |            "uga": "24529917",
          |            "quantity": 5
          |        },
          |        {
          |            "uga": "22460065",
          |            "quantity": 5
          |        },
          |        {
          |            "uga": "44038490",
          |            "quantity": 5
          |        }
          |    ]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)



  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// Test 5 - Article Éligible au Luxe (Non-Luxe)  ////////////:
  /////////////////////////////////////////////////////////////////////////

  val scnTest5 = scenario("Test 5 - Article Éligible au Luxe (Non-Luxe)")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(session => {
      val uniqueID = randomUUID()
      session.set("commercial_cart_id", uniqueID)
    })
    .exec { session =>
      println("commercial_cart_id : " + session("commercial_cart_id").as[String])
      session
    }
    .exec(http("post test 5")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "true")
      .body(StringBody(
        """
          |{
          |"typology": "ECOM",
          |"commercial_cart_id": "#{commercial_cart_id}",
          |"shop_code":"0000",
          |"products_details": [
          |{
          |"uga": "24529917",
          |"quantity": 5,
          |"rms_family": "2700",
          |"rms_group": "RG15",
          |"delivery_mode": "EXPRESS_HOME_DELIVERY"
          |}
          |]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)


  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// Test 6 - Article Éligible au Luxe (Luxe)  ////////////:
  /////////////////////////////////////////////////////////////////////////

  val scnTest6 = scenario("Test 6 - Article Éligible au Luxe (Luxe)")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(session => {
      val uniqueID = randomUUID()
      session.set("commercial_cart_id", uniqueID)
    })
    .exec { session =>
      println("commercial_cart_id : " + session("commercial_cart_id").as[String])
      session
    }
    .exec(http("post test 6")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "true")
      .body(StringBody(
        """
          |{
          |"typology": "ECOM",
          |"commercial_cart_id": "#{commercial_cart_id}",
          |"shop_code":"0000",
          |"products_details": [
          |{
          |"uga": "24529917",
          |"quantity": 5,
          |"rms_family": "2597"
          |"rms_group": "RG15",
          |"delivery_mode": "EXPRESS_HOME_DELIVERY"
          |}
          |]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)


  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// test 7 ECOM - Article Éligible au Luxe ( luxe spécifique)  ////////////:
  /////////////////////////////////////////////////////////////////////////

  val scnTest7 = scenario("test 7 ECOM - Article Éligible au Luxe ( luxe spécifique)")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(session => {
      val uniqueID = randomUUID()
      session.set("commercial_cart_id", uniqueID)
    })
    .exec { session =>
      println("commercial_cart_id : " + session("commercial_cart_id").as[String])
      session
    }
    .exec(http("post test 7")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "true")
      .body(StringBody(
        """
          |{
          |"typology": "ECOM",
          |"commercial_cart_id": "#{commercial_cart_id}",
          |"shop_code":"0000",
          |"products_details": [
          |{
          |"uga": "24529917",
          |"quantity": 5,
          |"rms_family": "2797"
          |"delivery_mode": "EXPRESS_HOME_DELIVERY",
          |"rms_group": "RG16"
          |}
          |]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)



  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// test 8 ECOM - livraison en magasin non éligible au transport interne:  ////////////:
  /////////////////////////////////////////////////////////////////////////
  val scnTest8 = scenario("ECOM - livraison en magasin non éligible au transport interne:")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(http("post test 8")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "false")
      .body(StringBody(
        """
          |{
          |"typology": "ECOM",
          |"commercial_cart_id": "0c0914b1-a2a5-435d-8425-f195793d6b38",
          |"shop_code":"4602",
          |"products_details": [
          |{
          |"uga": "24529917",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |},
          |{
          |"uga": "29167445",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |},
          |{
          |"uga": "44038490",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |}
          |]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)



  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// test 9 ECOM - livraison magasin éligible transport interne:  ////////////:
  /////////////////////////////////////////////////////////////////////////
  val scnTest9 = scenario("ECOM - livraison magasin éligible transport interne:")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(http("post test 9")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "false")
      .body(StringBody(
        """
          |{
          |"typology": "ECOM",
          |"commercial_cart_id": "0c0914b1-a2a5-435d-8425-f195793d6b38",
          |"shop_code":"3050",
          |"products_details": [
          |{
          |"uga": "24529917",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |},
          |{
          |"uga": "29167445",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |},
          |{
          |"uga": "44038490",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |}
          |]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)



  ///////////////////////////////////////////////////////////////////////////////////
  ///////////// test 10  ECOM - Mode livraison multiple: ( à domicile, en magasin, point relais) ////////////:
  /////////////////////////////////////////////////////////////////////////
  val scnTest10 = scenario("ECOM - Mode livraison multiple: ( à domicile, en magasin, point relais)")
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
    .exec(http("post test 10")
      .post("/order-split-api/api/v1/split")
      .header("X-GGL-CONTEXT-TENANT", "gl")
      .header("X-GGL-CONTEXT-RESERVATION", "true")
      .header("X-GGL-CONTEXT-GENERATION-ID", "false")
      .body(StringBody(
        """
          |{
          |"typology": "ECOM",
          |"commercial_cart_id": "0c0914b1-a2a5-435d-8425-f195793d6b38",
          |"shop_code":"3050",
          |"products_details": [
          |{
          |"uga": "24529917",
          |"quantity": 5,
          |"delivery_mode": "PICKUP_IN_STORE"
          |},
          |{
          |"uga": "29167445",
          |"quantity": 5,
          |"delivery_mode": "HOME_DELIVERY"
          |},
          |{
          |"uga": "44038490",
          |"quantity": 5,
          |"delivery_mode": "RELAY_POINT_DELIVERY"
          |}
          |]
          |}
          |""".stripMargin)).asJson
      .check(status is 201))
    .pause(1)









}
