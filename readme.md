* ********Swasthyain*******
* This is the script for checking selected apis,load and stress
* THey are :
* 1.Login API( CHO,Doc,patient)
  We need a static OTP (Everywhere and if API is included in this) for testing.
  Need a CSV file-
  1.Available CHO and Doctors
  2.free doctor Ids available for consultation

2.Doctor Online/busy-/api/Consultation/CheckAvailabilityStatus- Doctor panel-1 hour

3.Create consultatioN-/api/CHOPatient/ConsultationOnlineDoctor-CHO( consult now)-8-12 hours,Free doctor IDs

4.Doc app/web-/api/UnAuth/DoctorLoginRequestOTP,/api/UnAuth/DoctorLoginVerifyOTP- Doctor-4 hours
5.CHO sign up-/api/UnAuth/SignInCHO-CHO-4 hours

6.Sign up patient-/api/UnAuth/SendOTP-https://dev-app.swasthyaingit.in/#/login-2 hours
7.Patient get list-/api/CHOPatient/GetCHOPatientList-CHO (MY SK),Other SK(more data)-4 hours
8.Cases listing-/api/Consultation/ReferredList-Cases Drafted and Consulted-4 hours
9.Recieved list-/api/Consultation/RecievedList-Doctor-cases recieved-credentials-7973124636- 4 hours
10.Get list-/api/Consultation/{id}-Doctor-cases recieved-complete/inprocess buttons -->view details- 4 hours
11.Admin login-/api/Auth/SignInAdmin-2 hours
12.Doctor Register-api/Doctors/InsertDoctor-2 hours

Total days-6 working days.

Note:Disable sms and email on beta.