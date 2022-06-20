import { getUnixTime } from "date-fns";

export function getAPIBaseURL(currentLocation) {
  return currentLocation.replace("3000", "8080").split("#")[0];
}

export function createAPIClient(url) {
  return {
    getCharges() {
      return fetch(`${url}charge`).then((r) => r.json());
    },
    delete(chargeId) {
      fetch(`${url}charge/${chargeId}`, {
        method: "DELETE",
      }).catch(console.error);
    },
    create(newCharge) {
      fetch("http://localhost:8080/charge", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newCharge),
      }).catch(console.error);
    },
  };
}
