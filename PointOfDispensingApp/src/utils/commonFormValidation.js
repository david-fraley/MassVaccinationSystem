function dateWithin(minDateStr, maxDateStr, inputDate) {
  var minDate = Date.parse(minDateStr);
  var maxDate = Date.parse(maxDateStr);
  var formattedDate = parseDate(inputDate);
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

function validBirthdate(inputDate) {
  const minDateStr = "1900-01-01";

  const maxDateStr = (() => {
    let d = new Date();
    let date = [
      d.getFullYear(),
      ("0" + (d.getMonth() + 1)).slice(-2),
      ("0" + d.getDate()).slice(-2),
    ].join("-");

    return date;
  })();

  return dateWithin(minDateStr, maxDateStr, inputDate);
}

function validExpiration(inputDate) {
  const minDateStr = (() => {
    let d = new Date();
    let date = [
      d.getFullYear(),
      ("0" + (d.getMonth() + 1)).slice(-2),
      ("0" + d.getDate()).slice(-2),
    ].join("-");

    return date;
  })();

  const maxDateStr = (() => {
    let d = new Date();
    let date = [
      d.getFullYear() + 1,
      ("0" + (d.getMonth() + 1)).slice(-2),
      ("0" + d.getDate()).slice(-2),
    ].join("-");

    return date;
  })();

  return dateWithin(minDateStr, maxDateStr, inputDate);
}

const required = (v) => !!v || "Field is required";
const dateFormat = (v) =>
  (!!v && v.length === 10) || "Date must be in format, MM/DD/YYYY";
const birthdate = (v) => validBirthdate(v) || "Invalid DOB";
const expiration = (v) => validExpiration(v) || "Invalid expiration date";

export default {
  birthdateRules: [required, dateFormat, birthdate],
  expirationRules: [required, dateFormat, expiration],
};
