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
  submitRegistration: (data) => {
    return axios
      .post(`/broker/Patient`, data)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        return {
          data: response.data,
        };
      })
      .catch((e) => {
        return toResponse(e);
      });
  },
};
