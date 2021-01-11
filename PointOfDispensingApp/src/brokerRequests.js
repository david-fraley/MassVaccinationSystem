import axios from "axios";

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
    let query = `?family=${data.lastName}&given=${data.firstName}&birthdate=${data.birthDate}&address-postalcode=${data.postalCode}`;
    return axios
      .get(`/broker/Patient${query}`)
      .then((response) => {
        return { data: response.data };
      })
      .catch((e) => {
        if (e.response) {
          return { error: e.response.data };
        } else if (e.request) {
          return { error: e.request.data };
        } else {
          return { error: e.message ? e.message : e };
        }
      });
  },

  // Get patient from QR code for patient retrival
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
        if (e.response) {
          return { error: e.response.data };
        } else if (e.request) {
          return { error: e.request.data };
        } else {
          return { error: e.message ? e.message : e };
        }
      });
  },
};
