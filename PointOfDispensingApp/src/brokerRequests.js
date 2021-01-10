import axios from "axios";

// define functions for API requests here
export default {
  // Search patient for patient retrieval
  searchPatient: (data) => {
    let query = `?family=${data.lastName}&given=${data.firstName}&birthdate=${data.birthDate}&address-postalcode=${data.postalCode}`;
    return axios.get(`/Patient${query}`);
  },

  // Get patient from QR code for patient retrival
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

    return axios.get(`/healthcheck`).then((response) => {
      console.log(response.data);

      response = { data: patient };
      return response;
    });
  },
};
