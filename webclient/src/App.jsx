import React, { useEffect, useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListSubheader from "@mui/material/ListSubheader";
import Stack from "@mui/material/Stack";
import AddIcon from "@mui/icons-material/Add";
import TrashIcon from "@mui/icons-material/Delete";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import LinearProgress from "@mui/material/LinearProgress";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardHeader from "@mui/material/CardHeader";
import Popover from "@mui/material/Popover";
import { Box } from "@mui/material";
import { Routes, Route, useNavigate } from "react-router-dom";

import AddCharge from "./add-charge";

const monthFormatter = new Intl.DateTimeFormat(navigator.language, {
  month: "long",
});
const itemDateFormatter = new Intl.DateTimeFormat(navigator.language, {
  weekday: "short",
  day: "2-digit",
  month: "2-digit",
});

const whFormatter = new Intl.NumberFormat(navigator.language);
const priceFormatter = new Intl.NumberFormat(navigator.language, {
  style: "currency",
  currency: "EUR",
});
const durationFormatter = new Intl.NumberFormat(navigator.language, {
  style: "unit",
  unit: "hour",
});

function calculateChargesByMonth(charges) {
  if (charges === null) {
    return {};
  }
  return charges
    .map((charge) => {
      const date = new Date(charge.startedAt * 1000);
      return {
        month: monthFormatter.format(date),
        date,
        ...charge,
      };
    })
    .reduce((acc, charge) => {
      const byMonth = acc[charge.month] ?? [];
      byMonth.push(charge);
      acc[charge.month] = byMonth;
      return acc;
    }, {});
}

function App() {
  const navigate = useNavigate();

  const [charges, setCharges] = useState(null);

  useEffect(() => {
    if (charges !== null) {
      return;
    }
    fetch("http://localhost:8080/charge")
      .then((r) => r.json())
      .then((charges) => {
        setCharges(charges);
      })
      .catch((e) => {
        console.error(e);
        setCharges([]);
      });
  }, [charges]);

  const handleDelete = (chargeId) => {
    fetch(`http://localhost:8080/charge/${chargeId}`, {
      method: "DELETE",
    }).catch(console.error);
    setCharges(charges.filter((c) => c.id !== chargeId));
  };

  const chargesByMonth = calculateChargesByMonth(charges);
  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Cargas
          </Typography>

          <Stack
            direction="row-reverse"
            justifyContent="flex-start"
            alignItems="center"
            spacing={2}
          >
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={() => navigate(`/new`)}
              color="inherit"
            >
              <AddIcon />
            </IconButton>
          </Stack>
        </Toolbar>
      </AppBar>
      <Routes>
        <Route
          path="/"
          element={
            <Charges
              chargesByMonth={chargesByMonth}
              loading={charges === null}
              onDelete={(chargeId) => handleDelete(chargeId)}
            />
          }
        />
        <Route path="new" element={<AddCharge />} />
      </Routes>
    </>
  );
}

function Charges({ chargesByMonth, loading, onDelete }) {
  if (loading) {
    return <LinearProgress />;
  }
  return (
    <List
      sx={{
        width: "100%",
        bgcolor: "background.paper",
        position: "relative",
        overflow: "auto",
        "& ul": { padding: 0 },
      }}
      subheader={<li />}
    >
      {Object.entries(chargesByMonth).map(([month, charges]) => (
        <li key={`section-${month}`}>
          <ul>
            <ListSubheader>{month}</ListSubheader>
            {charges.map((charge) => (
              <ListItem key={`item-${month}-${charge.id}`}>
                <Charge
                  date={charge.date}
                  wh={charge.wh}
                  duration={charge.duration}
                  priceInCent={charge.priceInCent}
                  onDelete={() => onDelete(charge.id)}
                />
              </ListItem>
            ))}
          </ul>
        </li>
      ))}
    </List>
  );
}

function Charge({ date, wh, duration, priceInCent, onDelete }) {
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? "simple-popover" : undefined;
  return (
    <Card sx={{ minWidth: "100%" }}>
      <CardHeader
        action={
          <IconButton aria-label="settings" onClick={handleClick}>
            <MoreVertIcon />
          </IconButton>
        }
        title={`${whFormatter.format(wh)}wh`}
        subheader={itemDateFormatter.format(date)}
      />
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "left",
        }}
      >
        <Box
          display="flex"
          alignItems="center"
          paddingY={2}
          paddingX={1}
          onClick={onDelete}
        >
          <TrashIcon />
          Delete
        </Box>
      </Popover>
      <CardContent>
        <Typography variant="body2">
          {`Time: ${durationFormatter.format(duration / 3600)}`}
          <br />
          {`Total: ${priceFormatter.format(priceInCent / 100)}`}
          <br />
          {`Cents per kWh: ${priceFormatter.format(priceInCent / wh)}`}
        </Typography>
      </CardContent>
    </Card>
  );
}

export default App;
