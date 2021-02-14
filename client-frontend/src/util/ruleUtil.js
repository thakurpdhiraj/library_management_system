import isFinite from "lodash/isFinite";
export const rules = {
  required: v => !!v || "Required",
  number: v => (!!v && isFinite(Number(v))) || "Should be numeric",
  year: v => {
    const data = Number(v);
    const currYear = new Date().getFullYear();
    return (
      (!!data && isFinite(data) && data >= 1000 && data <= currYear) ||
      "Invalid publication year. Valid years: 1000 - " + currYear
    );
  },
  email: v => {
    const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return pattern.test(v) || "Invalid e-mail.";
  },
  minLength(min, v, text) {
    return (
      (!!v && v.length >= min) || "Atleast " + min + " " + text + " required"
    );
  }
};
