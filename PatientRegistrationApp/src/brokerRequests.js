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

  submitRegistration: () => {
    return healthcheckPromise();
  },
  
};
