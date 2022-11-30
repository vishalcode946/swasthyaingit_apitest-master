package com.swasthyaingit
import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

import scala.util.Random

class Doctorflow extends Simulation{

  val httpProtocol=http.baseUrl("https://beta-api.swasthyaingit.in")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
  val rnd = new Random()

  def Random_mobile_num(length: Int) = {
    rnd.alphanumeric.filter(_.isDigit).take(length).mkString
  }
  val csvFeeder=csv("data/doc_users.csv").circular
  val customFeeder = Iterator.continually(Map(
    "mobile1" -> Random_mobile_num(10)))

  def authenticate()={
    feed(customFeeder)
    .exec(http("Login doctor")
    .post("/api/UnAuth/SignInDoctor")
    .body(ElFileBody("com/swasthyaingit/Doctorflowjson/DoctorLogin.json")).asJson
    .check(jsonPath("$.token").saveAs("OTPreceived"))
      .check(jsonPath("$..institutionId").saveAs("institutionId"))
     .check(jsonPath("$..mobile").saveAs("mobile"))
      .check(status is (200))
    .check(bodyString.saveAs("login response")))
      .exec{session => println(session("login response").as[String]);session}

  }

  def authenticate_csv() = {
    feed(csvFeeder)
      .exec(http("Login doctor")
        .post("/api/UnAuth/SignInDoctor")
        .body(ElFileBody("com/swasthyaingit/Doctorflowjson/DoctorLogincsv.json")).asJson
        .check(jsonPath("$.token").saveAs("OTPreceived"))
        .check(jsonPath("$..institutionId").saveAs("institutionId"))
        .check(jsonPath("$..mobile").saveAs("mobile"))
        .check(status is(200))
        .check(bodyString.saveAs("login response")))
      .exec { session => println(session("login response").as[String]); session }

  }
  def verify_doctor_otp()={
    exec(http("Verfiy Doctor OTP")
      .post("/api/UnAuth/DoctorLoginVerifyOTP")
      .body(ElFileBody("com/swasthyaingit/Doctorflowjson/verifydoctorOTP.json")).asJson
      .check(jsonPath("$.token").saveAs("jwttoken"))
      .check(bodyString.saveAs("verify otp response")))
      .exec { session => println(session("verify otp response").as[String]); session }

  }

  def man_verify_doctor_otp() = {
    exec(http("manual request Verfiy Doctor OTP")
      .post("/api/UnAuth/DoctorLoginVerifyOTP")
      .body(ElFileBody("com/swasthyaingit/Doctorflowjson/DoctorLoginVerifyOTP_man.json")).asJson
      .check(jsonPath("$.token").saveAs("jwttoken"))
      .check(bodyString.saveAs("verify otp response")))
      .exec { session => println(session("verify otp response").as[String]); session }

  }

    def Check_avaliabilty() = {
    exec(http("Check doctor avaliabilty")
      .get("/api/Consultation/CheckAvailability")
      .header("authorization", "Bearer ${jwttoken}")
      .check(status is(200))
      .check(bodyString.saveAs("Response CheckAvailability")))
      .exec { session => println(session("Response CheckAvailability").as[String]); session }
      .pause(2)

    }
    def Case_recevied_complete() ={
      exec(http("Check case received on complete tab")
        .post("/api/Consultation/ReceivedList")
        .header("authorization", "Bearer ${jwttoken}")
        .body(ElFileBody("com/swasthyaingit/Doctorflowjson/casereceviedcompleted.json"))
        .check(status is(200))
        .check(bodyString.saveAs("Response Check case received on complete tab")))
        .exec { session => println(session("Response Check case received on complete tab").as[String]); session }
        .pause(2)
    }

  def Case_recevied_inprogress() = {
    exec(http("Check case received on complete tab")
      .post("/api/Consultation/ReceivedList")
      .header("authorization", "Bearer ${jwttoken}")
      .body(ElFileBody("com/swasthyaingit/Doctorflowjson/casereceviedinprogress.json"))
      .check(status is (200))
      .check(bodyString.saveAs("Response Check case received on complete tab")))
      .exec { session => println(session("Response Check case received on complete tab").as[String]); session }
      .pause(2)
  }




  val scn1= scenario("login doctor")
    .exec(authenticate())
   // .exec(authenticate_csv())
    //.pause(2)
   //.exec(verify_doctor_otp())
   // .exec(man_verify_doctor_otp())
//    .exec(Check_avaliabilty())
//    .exec(Case_recevied_complete())
//    .exec(Case_recevied_inprogress())


  setUp(
    scn1.inject(nothingFor(5),
      constantUsersPerSec(6).during(40),
  ).protocols(httpProtocol)
  )
}
