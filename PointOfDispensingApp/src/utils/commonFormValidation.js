let minDateStr = "1900-01-01";

let maxDateStr = (() => {
  let d = new Date();
  let date = [
    d.getFullYear(),
    ("0" + (d.getMonth() + 1)).slice(-2),
    ("0" + d.getDate()).slice(-2),
  ].join("-");

  return date;
})();

function validBirthdate(birthdate) {
  var minDate = Date.parse(minDateStr);
  var maxDate = Date.parse(maxDateStr);
  var formattedDate = parseDate(birthdate);
  var date = Date.parse(formattedDate);

  return !Number.isNaN(date) && minDate <= date && date <= maxDate;
}

function parseDate(date) {
  if (!date) return null;
  // Ensure birthdate is fully entered and can be converted into 3 variables before parsing
  if (date.split("/").length !== 3) return null;

  const [month, day, year] = date.split("/");
  return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
}

export default {
  birthdateRules: [
    (v) => !!v || "DOB is required",
    // check if v exists before seeing if the length is 10
    (v) =>
      (!!v && v.length === 10) || "DOB must be in specified format, MM/DD/YYYY",
    (v) => validBirthdate(v) || "Invalid DOB",
  ],
};
