import axios from "axios";

// Return an object with error property containing data
// from Axios error object
function toResponse(error) {
  if (error.response) {
    return { error: error.response.data };
  } else if (error.request) {
    return { error: error.request.data };
  } else {
    return { error: error.message ? error.message : error };
  }
}

// define functions for API requests here
export default {
  // Search patient for patient retrieval
  // param data:
  // {
  //   lastName: String
  //   firstName: String
  //   birthDate: String YYY-MM-DD
  //   postalcode: String
  // }
  searchPatient: (data) => {
    return axios
      .post(`/broker/SearchPatients`, data)
      .then((response) => {
        return { patients: response.data.patients };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  // Get patient from QR code for patient retrieval
  getPatientFromQrCode: (qrCode) => {
    
    // If we didn't get a Qr Code, return David's patient data
    if(!qrCode) {
      const patient = {
        id: qrCode,
        family: "Fraley",
        given: "David",
        birthDate: "1950-01-01",
        gender: "Male",
        address: {
          line: "1234 Main Street",
          city: "Waukesha",
          state: "WI",
          postalCode: "53072",
        },
        language: "English",
      };
      return new Promise((resolve) => {
        resolve({patient: patient});
      });
    }

    // If we got a Qr Code, hit the patient GET endpoint
    else {
      return axios
      .get(`/broker/Patient/${qrCode}`)
      .then((response) => {
        console.log(response);
        return { patient: response.data };
      })
      .catch((e) => {
        return toResponse(e);
      });
    }
  },

  getImmunization:(patID) => {
    return axios
    .get('/broker/Immunization', {params: { patient: patID}})
    .then((response) => {
      return {data: response.data};
    })
    .catch((e) => {
      return toResponse(e);
    });
  },

  // Check-in patient
  // param data:
  // {
  //   status: String
  //   patient: PatientID
  //   location : LocationID
  // }
  checkIn: (data) => {
    return axios
      .post(`/broker/check-in`, data)
      .then((response) => {
        return {
          data: {
            Encounter: response.data.Encounter,
            Appointment: response.data.Appointment,
          },
        };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  // Submit vaccination
  submitVaccination: (data) => {
    return axios
      .post("/broker/Immunization", { Immunization: data })
      .then((response) => {
        console.log(`/Immunization/${response.data.id}`);
        return { data: response.data };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  // Discharge
  // param data:
  // {
  //   apptID: appointment ID,
  //   encounterID: encounter ID
  // }
  discharge: (data) => {
    return axios
      .post(
        `/broker/discharge`,
        {},
        { params: { appointment: data.apptID, encounter: data.encounterID } }
      )
      .then((response) => {
        return {
          data: {
            Encounter: response.data.Encounter,
            Appointment: response.data.Appointment,
          },
        };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },
};
