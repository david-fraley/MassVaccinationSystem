import axios from "axios";

export default {
  searchPatient: (data) => {
    let query = `?family=${data.lastName}&given=${data.firstName}&birthdate=${data.birthDate}&address-postalcode=${data.postalCode}`;
    return axios.get(`http://localhost:3000/Patient${query}`);
  },
};
