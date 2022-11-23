const fs = require('fs')

const minsTextRegexp = /([0-9]+)\smins/
const kwhTextRegexp = /([0-9]+)\.([0-9]+)\skwh/i

function newCharge(epoch, wh, priceInCent, durationInSeconds) {
    return {
        epoch,
        wh,
        priceInCent,
        durationInSeconds
    }
}

// Translate "NN mins" to numner of sencods
function parseTime(textMins) {
    const matches = minsTextRegexp.exec(textMins.replace(/"/g, ''))
    return Number(matches[1]) * 60
}

// Transte 31/07/2022 into an epoch
function parseDate(textDate) {
    const parts = textDate.replace(/"/g, '').split("/")
    const reorder = `${parts[2]}-${parts[1]}-${parts[0]}`
    const date = new Date(reorder)
    return date.setUTCHours(0, 0, 0) / 1000
}

// Translate 4.77 into 477
function parsePrice(textPrice) {
    return Number(textPrice.replace(".", "").replace(/"/g, ''))
}


// Translate 33.42 kWh into 33420
function parseKwh(textKwh) {
    const matches = kwhTextRegexp.exec(textKwh)
    const first = Number(matches[1]) * 1000
    const second = Number(matches[2]) * 100
    return first + second
}

// "31/07/2022","01/08/2022","106 mins","33.42 kWh","3.34"
// returns newCharge
function parseLine(line) {
    const columns = line.split(",")
    const epoch = parseDate(columns[0])
    const duration = parseTime(columns[2])
    const kwh = parseKwh(columns[3])
    const priceInCent = parsePrice(columns[4])

    return newCharge(epoch, kwh, priceInCent, duration)
}

function readLines(path) {
    try {
        // read contents of the file
        const data = fs.readFileSync(path, 'UTF-8');

        // split the contents by new line
        const lines = data.split(/\r?\n/);

        // print all lines
        return lines.map((line) =>
            parseLine(line)
        );
    } catch (err) {
        console.error(err);
    }
}

function sendNewCharge(charge) {
    fetch('http://my-ecc-tracker.herokuapp.com/charge', {
        method: 'POST',
        body: JSON.stringify(charge),
        headers: {
            "Content-Type": "application/json",
        }
    }).then(res => console.log(res.status))
        .catch(console.error)
}

const charges = readLines("exported-megc.csv")

const cs = [
    newCharge(1663401268, 27634, 608, 31248),
    newCharge(1659858868, 8978, 198, 11340),
    newCharge(1657007668, 12475, 274, 14145),
    newCharge(1656662068, 12891,284, 15346),
    newCharge(1655884468, 20394, 20.394 * 0.22, 42172)

]

cs.forEach(c => {
    sendNewCharge(c)
})