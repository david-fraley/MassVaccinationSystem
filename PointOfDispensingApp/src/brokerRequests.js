import axios from "axios";
import store from './store/store';

// Return an object with error property containing data
// from Axios error object
function toResponse(error) {
  console.log(store);
  if (error.response) {
    return { error: error.response.data };
  } else if (error.request) {
    return { error: error.request.data };
  } else {
    return { error: error.message ? error.message : error };
  }
}

function getKeycloakToken() {
  return store.state.keycloak.token;
}

function getHeaders() {
  return {
    headers: {
      'Authorization': 'bearer ' + getKeycloakToken()
    }
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
      .post(`/broker/SearchPatients`, data, getHeaders())
      .then((response) => {
        return { patients: response.data.patients };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  // Get patient from QR code for patient retrieval
  getPatientFromQrCode: (qrCode) => {
    if(!qrCode) {
      return new Promise((resolve) => {
        resolve({error: "error message"});
      });
    }
    else{
    // If we successfully got a Qr Code, hit the patient GET endpoint
      return axios
      .get(`/broker/Patient/${qrCode}`, getHeaders())
      .then((response) => {
        console.log(response);
        return { patient: response.data };
      })
      .catch((e) => {
        return toResponse(e);
      });
    }
  },

  getAppointment: (patID) => {
    return axios
      .get("/broker/Appointment", { params: { actor: patID } }, getHeaders())
      .then((response) => {
        let result;
        if (response.data.length == 0) result = { data: {} };
        else result = { data: response.data[0] };
        return result;
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  getEncounter: (patID) => {
    return axios
      .get("/broker/Encounter", { params: { subject: patID, _sort: "-date" } }, getHeaders())
      .then((response) => {
        let result;
        if (response.data.length == 0) result = { data: {} };
        else result = { data: response.data[0] };
        return result;
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  getImmunization:(patID) => {
    return axios
    .get('/broker/Immunization', {params: { patient: patID, status: "completed" }}, getHeaders())
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
  //   appointment: AppointmentID
  //   patient: PatientID
  //   location : LocationID
  // }
  checkIn: (data) => {
    return axios
      .post(`/broker/check-in`, data, getHeaders())
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
      .post("/broker/Immunization", { Immunization: data }, getHeaders())
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
        { params: { appointment: data.apptID, encounter: data.encounterID } },
        getHeaders()
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
