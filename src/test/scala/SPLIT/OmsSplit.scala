package SPLIT

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class OmsSplit extends Simulation{

  private val host: String = System.getProperty("urlCible", "https://ws-oms-pp.galerieslafayette.com")

  val httpProtocol = http
    .baseUrl(host)
    .header("content-type","application/json")
    .header("Authorization","Basic c3BsaXQ6c3BsaXQ=")


  val scnTest1 = scenario("Test 1 - Mode Express avec Réservation & SGID").exec(ObjectOmsSplit.scnTest1)
  val scnTest2 = scenario("Test 2 - Mode Express avec Réservation & Génération d'ID").exec(ObjectOmsSplit.scnTest2)
  val scnTest3 = scenario("Test 3").exec(ObjectOmsSplit.scnTest3)
  val scnTest4 = scenario("Test 4").exec(ObjectOmsSplit.scnTest4)
  val scnTest5 = scenario("Test 5").exec(ObjectOmsSplit.scnTest5)
  val scnTest6 = scenario("Test 6").exec(ObjectOmsSplit.scnTest6)
  val scnTest7 = scenario("Test 7").exec(ObjectOmsSplit.scnTest7)
  val scnTest8 = scenario("Test 8").exec(ObjectOmsSplit.scnTest8)
  val scnTest9 = scenario("Test 9").exec(ObjectOmsSplit.scnTest9)
  val scnTest10 = scenario("Test 10").exec(ObjectOmsSplit.scnTest10)



  setUp(
    scnTest1.inject(atOnceUsers(1)),
    scnTest2.inject(atOnceUsers (1)),
    scnTest3.inject(atOnceUsers (1)),
    scnTest4.inject(atOnceUsers (1)),
    scnTest5.inject(atOnceUsers (1)),
    scnTest6.inject(atOnceUsers (1)),
    scnTest7.inject(atOnceUsers (1)),
    scnTest8.inject(atOnceUsers (1)),
    scnTest9.inject(atOnceUsers (1)),
    scnTest10.inject(atOnceUsers (1)
  ).protocols(httpProtocol))


}
