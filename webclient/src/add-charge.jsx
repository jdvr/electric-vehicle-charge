import * as React from "react";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import BatteryChargingFullIcon from "@mui/icons-material/BatteryChargingFull";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import TimerIcon from "@mui/icons-material/Timer";
import EuroIcon from "@mui/icons-material/Euro";
import MenuItem from "@mui/material/MenuItem";
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";
import getUnixTime from "date-fns/getUnixTime";
import { DatePicker } from "@mui/x-date-pickers";
import Alert from "@mui/material/Alert";

const EPOCH_FIELD = "epoch";
const WH_FIELD = "wh";
const DURATION_FIELD = "durationInSeconds";
const PRICE_FIELD = "priceInCent";
const AVG_PRICE_FIELD = "avgPriceInCent";

const mandatoryFields = [WH_FIELD, DURATION_FIELD, EPOCH_FIELD];
const atLeastOneOf = [AVG_PRICE_FIELD, PRICE_FIELD];

export default function AddCharge({ apiClient }) {
  const [isAvgPrice, setIsAvgPrice] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState(null);
  const [newCharge, setNewCharge] = React.useState({});

  const onValue = (field, value) => {
    console.log(field, value);
    if (!value) {
      return;
    }
    const updated = {
      ...newCharge,
      [field]: value,
    };
    console.log(updated);
    setNewCharge(updated);
  };

  const navigate = useNavigate();

  const handleSubmit = () => {
    const validationError = validate(newCharge);
    if (validationError) {
      setErrorMessage(validationError);
      return;
    }
    apiClient.create({
      ...newCharge,
      [EPOCH_FIELD]: getUnixTime(newCharge[EPOCH_FIELD]),
    });
    navigate("/");
  };

  return (
    <Box sx={{ "& > :not(style)": { m: 2 } }}>
      {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
      <Box sx={{ display: "flex", alignItems: "flex-end" }}>
        <CalendarMonthIcon sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <DatePicker
          label="Día"
          value={newCharge[EPOCH_FIELD] || null}
          onChange={(newValue) => {
            onValue(EPOCH_FIELD, newValue.getTime());
          }}
          renderInput={(params) => (
            <TextField {...params} variant="standard" required fullWidth />
          )}
        />
      </Box>
      <Box sx={{ display: "flex", alignItems: "flex-end" }}>
        <BatteryChargingFullIcon
          sx={{ color: "action.active", mr: 1, my: 0.5 }}
        />
        <TextField
          id="wh"
          label="Charge Wh"
          variant="standard"
          type="number"
          required
          fullWidth
          onChange={(event) => {
            onValue(WH_FIELD, Number(event.target.value));
          }}
        />
      </Box>
      <Box sx={{ display: "flex", alignItems: "flex-end" }}>
        <TimerIcon sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <TextField
          id="time"
          label="Tiempo"
          variant="standard"
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          required
          fullWidth
          onChange={(event) => {
            const timeInSeconds = parseTimeAsSeconds(event.target.value);
            onValue(DURATION_FIELD, timeInSeconds);
          }}
        />
      </Box>
      <Box sx={{ display: "flex", alignItems: "flex-end" }}>
        <EuroIcon sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <TextField
          select
          variant="standard"
          label="Tipo de precio"
          value={isAvgPrice ? 1 : 0}
          onChange={(event) => {
            setIsAvgPrice(Boolean(event.target.value));
            delete newCharge[PRICE_FIELD];
            delete newCharge[AVG_PRICE_FIELD];
            setNewCharge(newCharge);
          }}
          required
          fullWidth
        >
          <MenuItem value={0}>Precio total</MenuItem>
          <MenuItem value={1}>Precio medio</MenuItem>
        </TextField>
      </Box>

      {isAvgPrice ? (
        <Box sx={{ display: "flex", alignItems: "flex-end" }}>
          <EuroIcon sx={{ color: "action.active", mr: 1, my: 0.5 }} />
          <TextField
            id="avgPrice"
            label="Precio Medio en centimos"
            variant="standard"
            type="number"
            required
            fullWidth
            value={newCharge[AVG_PRICE_FIELD] || 0}
            onChange={(event) => {
              onValue(AVG_PRICE_FIELD, Number(event.target.value));
            }}
          />
        </Box>
      ) : (
        <Box sx={{ display: "flex", alignItems: "flex-end" }}>
          <EuroIcon sx={{ color: "action.active", mr: 1, my: 0.5 }} />
          <TextField
            id="price"
            label="Precio en centimos"
            variant="standard"
            type="number"
            required
            fullWidth
            value={newCharge[PRICE_FIELD] || 0}
            onChange={(event) => {
              onValue(PRICE_FIELD, Number(event.target.value));
            }}
          />
        </Box>
      )}
      <Box display="flex" flexDirection="row-reverse">
        <Button variant="contained" onClick={handleSubmit}>
          Añadir
        </Button>
      </Box>
    </Box>
  );
}

const VALIDATION_ERROR_MANDATORY = "Falta uno de los campos obligatorios.";
const VALIDATION_ERROR_OPTIONAL = "Falta un precio";

function validate(newCharge) {
  const allMandatory = mandatoryFields.every((field) => newCharge[field]);

  if (!allMandatory) {
    return VALIDATION_ERROR_MANDATORY;
  }

  const someOptional = atLeastOneOf.some((field) => newCharge[field]);

  if (!someOptional) {
    return VALIDATION_ERROR_OPTIONAL;
  }

  return null;
}

/*
  @param time 03:36 (hour:minute)
  @return 12960
 */
function parseTimeAsSeconds(time) {
  if (!time.includes(":")) {
    return 0;
  }
  const [hour, minutes] = time.split(":");
  return Number(hour) * 3600 + Number(minutes) * 60;
}
