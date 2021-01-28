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

// Returns promise to make a call to healthcheck
function healthcheckPromise() {
  return axios
    .get(`/broker/healthcheck`)
    .then((response) => {
      console.log(response.data);

      return { data: response.data };
    })
    .catch((e) => {
      return toResponse(e);
    });
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
    let config = {
      params: {
        family: data.lastName,
        given: data.firstName,
        birthdate: data.birthDate,
      },
    };
    if (data.postalCode) config.params["address-postalcode"] = data.postalCode;

    return axios
      .get(`/broker/Patient`, config)
      .then((response) => {
        return { data: response.data };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  // Get patient from QR code for patient retrieval
  // param data:
  // {
  //   id: String
  // }
  getPatient: (data) => {
    const patient = {
      id: data.id,
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

    return axios
      .get(`/broker/healthcheck`)
      .then((response) => {
        console.log(response.data);

        response = { data: patient };
        return { data: response.data };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },

  // Check-in patient
  // patID: patient ID
  checkIn: (patID) => {
    return axios
      .post(`/broker/check-in`, {}, { params: { patient: patID } })
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
  submitVaccination: () => {
    return healthcheckPromise();
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
