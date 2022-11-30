package com.swasthyaingit
import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

import scala.util.Random
class createpatientcases extends Simulation {

  val httpProtocol = http.baseUrl("https://beta-api.swasthyaingit.in")
    .acceptHeader("application/json")
    .contentTypeHeader(" application/json")

  val rnd = new Random()

  def Random_String(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def Random_num(length: Int) = {
    rnd.alphanumeric.filter(_.isDigit).take(length).mkString
  }

  val customFeeder = Iterator.continually(Map(
    "FirstName" -> Random_String(10),
    "LastName" -> Random_String(10),
    "guardian_Name" -> Random_String(10),
    "num" -> Random_num(10),
    //username-load, 123456
    "Authtoken1" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEso74C4N5wZZubvtiVgcE9vrpkvbBAh500j93LvZcdF8lGyELp8J7KPKpa5ZS3NJEsqANX7dEm1Uniz6d23LsV354OlLPW46I2WSep0NvVvltyNekxLTh/z/ghQTNc3q8TZYVlft5wPwx8nXjCnxT/d5OuOeohPdvw58O3hhtuvMlHW1QME1PWbAeur8bp9DhczY81p+C3GNOEIkihcqKqrhSoV/18bugiIwHCsB4cX9b4Epg8na9JYHkPP6wk5gTu4+VQP+kNLcCCSiaHVcsmNvM6JH5/7SPXV4iO8t/oOINbSc5vRB1PfQbexWOHFjzvuGZQSQcIdA0UG3JnJMNnj41FIwKbJX6JAAokzniQIbVQFs5ILW2I1y4yMSRNEHZQApXCQNEOaL6P+7tVntTatrqvKrAeZofJEkomh1XLJjbxGtFIEh+l92Qo/a8vPPW74komh1XLJjb/mPbOBHAa2wWZmQ8uQEDK0K2qkbcoSYl+Zv4mhIpyJZbcpCsb/r4yCN6h8FTf/ma9Go3Ecm0EVwF850ePncSRarJnKVCsWGkt92AMGKVNEvJKJodVyyY2/yS6eriW0VPQejHL8Be9SS7lxhlD4vn/bwdoDmbfw/1XYe0fn0urzWDlm7FqN47+LYVJjX29QLsOmv5tFCx656poUKYKDPLfXlnRRIsZ5mM6wNLmtczEkBpBqm/d4SPKWlNbEMNUejoAFHuocXLoldG22kwJ3f71a7tge8MntUY33G5b2da298DtKjipoc9I0=",
 //Doctorload,123456
  "authdoc" -> "4r3AvshjduIbagybY7IflveWvVY7vZoxCyoHjhYrYzAMZCdWgJ89zhYzjPrF90bnpa5ZS3NJEsrvYh/GL40arWx4P2pPtoj1HF0L/eB8s5r1tfkoUoyZQTkjB32GCsqJRpoLmBEOAM3rrHR7M+aNf8B9Zb6LDbSsy69MKwG0Nm0KaUa9fV2WKs+G7ZYE4xPXf7KNyJdTe95A2MUKeQ6LAvZBIt2p6XGWKSNrd8H3gQJHdyhdqYGXWFB0UhubWzJNA7i0Bh4tF1/tMlGPIB7Fap+pQN7+4uQBz1n0acEo0yliFpSM80wVaqjxVNH0QlycjeofBU3/5msliWBnb/AU5O9FkyeJdgO7vTLUSzan3jgQDh9mybuD0KqYvtrRMxytWhdLPtnszLcH4VNnqV/gBBmCgo9ptnpngidjkrRi/23CVYS18cKQROWqSja+jzG0TmuZXgc328dAVolNUzyaXHA6U9PRzXt4Ea0UgSH6X3ZCj9ry889bviSiaHVcsmNv5m/iaEinIlltykKxv+vjII3qHwVN/+ZrlJ/WEzw1jkzt326GfmwEaB3s++1RwAXDr8ogiwG9u3iASrOlNreJM+wR8rT6T88aqBMKS536tk0Txa4ZAs/NArkVa9sPi3z/wztkzAyaG3w15HS24bjkh230ED3x5hfOtqOwe7oa3TmNIv1A5h9PmwX+hY1cBxmH980PRyhzYsTFp4acAAypbNlbRSUF6xHu"

  ))

  def login_cho() = {
    exec(http("Login with CHO")
      .post("/api/UnAuth/SignInCHO")
      .body(ElFileBody("com/swasthyaingit/choflow1/CHOlogin.json")).asJson
      .check(jsonPath("$.token").saveAs("Authtoken"))
      .check(status is (200))
      .check(bodyString.saveAs("Login with CHO")))
      .exec { session => println(session("Login with CHO").as[String]); session }

  }

  def create_patient_via_CHO() = {
    repeat(1) {
      feed(customFeeder)
        .exec(http("Create Patient CHO")
          .post("/api/CHOPatient/AddPatient_Optim")
          .body(ElFileBody("com/swasthyaingit/Createpatient/CreatepatientCHO.json")).asJson
          .header("authorization", "Bearer ${Authtoken1}")
          .check(status is (200))
          .check(bodyString.saveAs("Create Patient CHO")))
        .exec { session => println(session("Create Patient CHO").as[String]); session }
        .pause(1)
    }
  }

  def Registered_Patients_mysk_with_draft() = {
    repeat(1, "i") {
      feed(customFeeder)
        .exec(http("Registered Patients my sk")
          .get("/api/CHOPatient/GetCHOPatientList?SkipRecords=0&LimitRecords=100&LoadAll=false")
          .header("authorization", "Bearer ${Authtoken1}")
          .check(jsonPath("$.lstModel[${i}].patientInfoId").saveAs("Patientid"))
          .check(jsonPath("$.lstModel[${i}].firstName").saveAs("firstname"))
          .check(jsonPath("$.lstModel[${i}].lastName").saveAs("lastname"))
          .check(bodyString.saveAs("Registered Patients my sk")))
        .pause(1)

        .exec(http("Draftcase")
          .post("/api/Consultation/DraftConsultation_Optim")
          .header("authorization", "Bearer ${Authtoken1}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/Draftcase.json")).asJson
          .check(status is (200))
          .check(jsonPath("$.model.consultationModel.consultationId").saveAs("consultationId"))
          .check(bodyString.saveAs("Draftcase")))
        .exec { session => println(session("Draftcase").as[String]); session }
    }
  }

  def get_Draft_CHO_cases() = {
    repeat(1, "i") {
      feed(customFeeder)
        .exec(http("get_Draft_CHO_cases")
          .get("/api/Consultation/ReferredList")
          .header("authorization", "Bearer ${Authtoken1}")
          .body(ElFileBody("com/swasthyaingit/choflow1/CHOcasedraft.json")).asJson
          .check(jsonPath("$.lstModel[i].consultationId").saveAs("consultationId"))
          .check(jsonPath("$.lstModel[i].patientInfoID").saveAs("patientInfoID"))
          .check(bodyString.saveAs("get_Draft_CHO_cases")))
        .exec { session => println(session("get_Draft_CHO_cases").as[String]); session }

        .exec(http("Update doctor status consultation")
          .post("api/Consultation/UpdateConsultationStatus")
          .header("authorization", "Bearer ${authdoc}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/Updatestatusconsultation.json")).asJson
          .check(bodyString.saveAs("Update status consultation")))
        .exec { session => println(session("Update status consultation").as[String]); session }

        .exec(http("doctor close the consultation")
          .post("/api/Consultation/InsertResponseConsultation")
          .header("authorization", "Bearer ${authdoc}")
          .body(ElFileBody("com/swasthyaingit/Createpatient/CloseConsultion.json")).asJson
          .check(status is (200))
          .check(bodyString.saveAs("doctor close the consultation")))
        .exec { session => println(session("doctor close the consultation").as[String]); session }

    }
  }

  def create_admin_doctor() = {
    repeat(100) {
      feed(customFeeder)
        .exec(http("create doctor ")
          .post("/api/Doctors/InsertDoctor")
          .header("authorization", "Bearer 4r3AvshjduI6rBO5vK+Uq9oyvvjEGEbaNN3BEEhIQNK2XxxUEsv4abWTMiib07kjg2GEHqGKBxCuDCQAHS1SXusXP8rSxKxFuu5NzLX5pt8yK1QSFf2TXvjPMJO3mUDFJKSVG4eokYgWHp65OCY935M6qOL/RMpCK4jMI9dzOQrZHO4Ij1kFPgrquHjiBnlwBi1WWYK3p4O0AyJeFlGqjNYbSgQnS2sXdwoaWqBhvnkE8VgnNclgyzAFj6fiLeZrbs7WA7uw9d9lke2Fhe0rOy5KFHjxGZpjyU5YY2gs4v7XTrYVKCP+OSKrhRJdNk263nIb7c6u7FGlJVP3rj3NvXQqJeqZO0LgwggNY4AR4c6J33r78YDsfZ8l1TLSOkMtC8U036Me7/5jjMyeTkD4u36rGz262wBinrdLnWNt1syGFewf8giLUme3O4e5xCwmDPJz72+nDtpZHyOYRypAuOzemeJsB7fIHwRfMnXSsBcm1doSh6nsFio4QZVb/eGaAUwUYYXku8rQHYoeruNdiP8HVgCdTSIplbnZ912e+5LYCLI8ThFVCgvFNN+jHu/+YKVX7f8FZ69W2uWyTCOk/ySiaHVcsmNv8kunq4ltFT3ll/ZZTc5MEVVW6MMZhvN+8HaA5m38P9WeYpdd0yf08DESQCtyHq2M2FSY19vUC7Dpr+bRQseueqaFCmCgzy315Z0USLGeZjOsDS5rXMxJAaQapv3eEjyl6jQ+A2p3KSrKPJlF/ZDUhh5EPXPfCjXlG6GE+FCp2iIthJg42kvGWV60OeNvpfDm")
          .body(ElFileBody("com/swasthyaingit/Createpatient/createdoctor1.json")).asJson
          .check(bodyString.saveAs("create doctor")))
        .exec { session => println(session("create doctor").as[String]); session }
    }
  }

  val scn = scenario("Create patient CHO")
    .exec(create_patient_via_CHO())
   // .exec(Registered_Patients_mysk_with_draft())
  //.exec(get_Draft_CHO_cases())


  setUp(
    scn.inject(nothingFor(5),
      constantUsersPerSec(6).during(40)
  ).protocols(httpProtocol)
  )
}
