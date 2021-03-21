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

const required = (v) => !!v || "Field is required";
const dateFormat = (v) =>
  (!!v && v.length === 10) || "Date must be in format, MM/DD/YYYY";
const birthdate = (v) => validBirthdate(v) || "Invalid DOB";
const postalCode = (v) =>
  /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(v) ||
  "Zip code format must be ##### or #####-####";

export default {
  nameRules: [required],
  birthdateRules: [required, dateFormat, birthdate],
  postalCodeRules: [postalCode],
};
