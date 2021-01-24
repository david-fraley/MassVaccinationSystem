const axios = require("../services/axiosInstance.js");
const Encounter = require("../models/Encounter");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

exports.create = (req, res) => {
  let encounter = req.body.Encounter;
  let resource = Encounter.toFHIR(encounter);

  axios
    .post(`/Encounter`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

// Update status and time.
exports.checkIn = async (req) => {
  const status = "arrived";
  let id, patch;
  let config;

  if (req.query.hasOwnProperty("patient")) {
    config = {
      params: {
        subject: req.query.patient,
        status: "planned",
      },
    };
  } else if (req.query.hasOwnProperty("appointment")) {
    config = {
      params: {
        appointment: req.query.appointment,
      },
    };
  } else {
    return {};
  }

  // get id of resource to update
  id = await axios.get("/Encounter", config).then((response) => {
    let bundle = response.data;
    let resource;

    if (!bundle.hasOwnProperty("entry")) {
      console.log("Encounter does not exist");
    }
    resource = bundle.entry[0].resource;
    return resource.id;
  });

  // patch status and start time
  patch = [
    {
      op: "add",
      path: "/status",
      value: status,
    },
    {
      op: "add",
      path: "/period",
      value: {},
    },
    {
      op: "add",
      path: "/period/start",
      value: new Date().toISOString(),
    },
  ];

  // update the database with new encounter
  return axios.patch(`/Encounter/${id}`, patch).then((response) => {
    return Encounter.toModel(response.data);
  });
};

exports.discharge = async (req) => {
  const status = "finished";
  let id, patch;
  let config;

  if (req.query.hasOwnProperty("appointment")) {
    config = {
      params: {
        appointment: req.query.appointment,
      },
    };
  } else return {};

  // get id of resource to update
  id = await axios.get("/Encounter", config).then((response) => {
    let bundle = response.data;
    let resource;

    if (!bundle.hasOwnProperty("entry")) {
      console.log("Encounter does not exist");
    }
    resource = bundle.entry[0].resource;
    return resource.id;
  });

  // patch status and start time
  patch = [
    {
      op: "add",
      path: "/status",
      value: status,
    },
    {
      op: "add",
      path: "/period/end",
      value: new Date().toISOString(),
    },
  ];

  // update the database with new encounter
  return axios.patch(`/Encounter/${id}`, patch).then((response) => {
    return Encounter.toModel(response.data);
  });
};
