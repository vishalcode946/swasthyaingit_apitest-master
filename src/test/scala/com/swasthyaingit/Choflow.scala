package com.swasthyaingit
import io.gatling.http.Predef._
import io.gatling.core.Predef._

class Choflow  extends Simulation{

val httpProtocol=http.baseUrl("https://beta-api.swasthyaingit.in")
  .acceptHeader("application/json")
  .contentTypeHeader("application/json")


val csvFeeder=csv("data/cho_users.csv").circular
  val customFeeder = Iterator.continually(Map(
    "Authtoken1" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEsrmCASeEAv2vuTq13cyqSmnbHg/ak+2iPUcXQv94HyzmvW1+ShSjJlBOSMHfYYKyomHWb7BssRWj/oZ5wwJRC8FwH1lvosNtKzLr0wrAbQ2bUxhIRtfABRxwHSWi3FUCaCCFBM1zerxNlhWV+3nA/DHydeMKfFP93k6456iE92/Dnw7eGG268yUdbVAwTU9ZsB66vxun0OFzNjzWn4LcY04QiSKFyoqquFB3FCOSNSF67CzCG5Fmb9hgSmDydr0lgeQ8/rCTmBO7j5VA/6Q0twIJKJodVyyY28zokfn/tI9dXiI7y3+g4g1tJzm9EHU99BKRIpRrkf2RoZlBJBwh0DRQbcmckw2ePgiP4icnPgWeQCiTOeJAhtVAWzkgtbYjXLjIxJE0QdlAClcJA0Q5ovo/7u1We1Nq2uq8qsB5mh8kSSiaHVcsmNvEa0UgSH6X3ZCj9ry889bviSiaHVcsmNv+Y9s4EcBrbBZmZDy5AQMrQraqRtyhJiX5m/iaEinIlltykKxv+vjII3qHwVN/+Zr0ajcRybQRXAXznR4+dxJFqsmcpUKxYaS33YAwYpU0S8komh1XLJjb/JLp6uJbRU9nesKzMbbkqDuBKQWsGtPfvB2gOZt/D/VVwJBi0Yg5u79GTdEV0FklNhUmNfb1Auw6a/m0ULHrnqmhQpgoM8t9eWdFEixnmYzrA0ua1zMSQGkGqb93hI8pWWh5Fj+QUVbipFEvxa8D9j/ZLNmKMMC5wGvAa1IGL1XES1TfuW0PL7+N6FwXzQbfA=="
  ))

def login_cho()={
  exec(http("Login with CHO")
  .post("/api/UnAuth/SignInCHO")
  .body(ElFileBody("com/swasthyaingit/choflow1/CHOlogin.json")).asJson
  .check(jsonPath("$.token").saveAs("Authtoken"))
  .check(status is(200))
  .check(bodyString.saveAs("Login with CHO")))
  .exec{session => println(session("Login with CHO").as[String]);session}}

  def login_cho_csv() = {
    feed(csvFeeder)
    .exec(http("Login with CHO csv")
      .post("/api/UnAuth/SignInCHO")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHologincsv.json")).asJson
      .check(jsonPath("$.token").saveAs("Authtoken"))
      .check(status is (200))
      .check(bodyString.saveAs("Login with CHO csv")))
      .exec { session => println(session("Login with CHO csv").as[String]); session }

}
def Registered_Patients_mysk()={
  feed(customFeeder)
  .exec(http("Registered Patients my sk")
  .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=false")
    .header("authorization","Bearer ${Authtoken1}")
    .check(status is (200))
  .check(bodyString.saveAs("Registered Patients my sk")))
  .exec{session => println(session("Registered Patients my sk").as[String]);session}
}
  def Registered_Patients_allsk()={
    feed(customFeeder)
    .exec(http("Registered Patients all sk")
      .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=true")
      .header("authorization","Bearer ${Authtoken1}")
      .check(bodyString.saveAs("Registered Patients my sk")))
    .exec{session => println(session("Registered Patients my sk").as[String]);session}
  }

  def get_doctor_for_consult() = {
    feed(customFeeder)
    .exec(http("get_doctor_for_consult")
      .get("/api/CHOPatient/GetOnlineDoctors?SkipRecords=0&LimitRecords=100&spl_id=0")
      .header("authorization", "Bearer ${Authtoken1}")
      .check(jsonPath("$.lstModel[0].doctor_Status").saveAs("doctorStatus"))
      .check(jsonPath("$.lstModel[0].doctor_id").saveAs("doctorid"))
      .check(bodyString.saveAs("get_doctor_for_consult")))
    .exec { session => println(session("get_doctor_for_consult").as[String]); session }
  }

  def get_Draft_CHO_cases() = {
    feed(customFeeder)
    .exec(http("get_Draft_CHO_cases")
      .get("/api/Consultation/ReferredList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOcasedraft.json"))
      .check(bodyString.saveAs("get_Draft_CHO_cases")))
    .exec { session => println(session("get_Draft_CHO_cases").as[String]); session }
  }

  def get_inprogress_CHO_cases() = {
    feed(customFeeder)
    .exec(http("get_inprogress_CHO_cases")
      .get("/api/Consultation/ReferredList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOinprogresscases.json"))
      .check(bodyString.saveAs("get_inprogress_CHO_cases")))
    .exec { session => println(session("get_inprogress_CHO_cases").as[String]); session }
  }

  def get_completed_CHO_cases() = {
    feed(customFeeder)
    .exec(http("get_completed_CHO_cases")
      .get("/api/Consultation/ReferredList")
      .header("authorization", "Bearer ${Authtoken1}")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOcompletedcases.json"))
      .check(bodyString.saveAs("get_completed_CHO_cases")))
    .exec { session => println(session("get_completed_CHO_cases").as[String]); session }
  }

  val scn1 = scenario("CHO flow")
 .exec(login_cho())
   //.exec(login_cho_csv())
//    .exec(Registered_Patients_mysk())
//    .exec(Registered_Patients_allsk())
  //.exec(get_doctor_for_consult())
//    .exec(get_Draft_CHO_cases())
//   .exec(get_inprogress_CHO_cases())
//   .exec(get_completed_CHO_cases())

  setUp(scn1.inject(nothingFor(5),
    constantUsersPerSec(6).during(40)
  )
  ).protocols(httpProtocol)
}

