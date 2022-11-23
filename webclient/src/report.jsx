import * as React from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import LinearProgress from "@mui/material/LinearProgress";
import { priceFormatter, whFormatter } from "./formatters";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const options = {
  responsive: true,
  plugins: {
    legend: {
      position: "top",
    },
  },
};

export function ReportByMonth({ apiClient }) {
  const [periods, setPeriods] = React.useState(null);

  React.useEffect(() => {
    if (periods !== null) {
      return;
    }
    apiClient
      .reportBreakdown()
      .then((breakdown) => {
        setPeriods(breakdown.periods);
      })
      .catch((e) => {
        console.error(e);
        setPeriods([]);
      });
  }, [periods]);

  if (periods === null) {
    return <LinearProgress />;
  }
  return (
    <>
      <ReportPriceByMonthChart periods={periods} />
      <ReportWhByMonthChart periods={periods} />
      <ReportAvgPriceByMonthChart periods={periods} />
    </>
  );
}

function ReportPriceByMonthChart({ periods }) {
  const periodsByLabel = periods.reduce((byLabel, period) => {
    return {
      ...byLabel,
      [period.key]: period,
    };
  }, {});

  const labels = Object.keys(periodsByLabel);
  const eurs = labels.map((label) => periodsByLabel[label].totalPriceInCents);
  console.log(periodsByLabel, periods);
  const data = {
    labels,
    datasets: [
      {
        label: "Euros",
        data: labels.map(
          (label) => periodsByLabel[label].totalPriceInCents / 100
        ),
        backgroundColor: "rgba(53, 162, 235, 0.5)",
      },
    ],
  };

  return (
    <Bar
      options={{
        ...options,
        title: {
          display: true,
          text: "Euros por mes",
        },
        scales: {
          y: {
            ticks: {
              callback: function (value) {
                return priceFormatter.format(value);
              },
            },
          },
        },
      }}
      data={data}
    />
  );
}

function ReportWhByMonthChart({ periods }) {
  const periodsByLabel = periods.reduce((byLabel, period) => {
    return {
      ...byLabel,
      [period.key]: period,
    };
  }, {});

  const labels = Object.keys(periodsByLabel);
  console.log(periodsByLabel, periods);
  const data = {
    labels,
    datasets: [
      {
        label: "Wh",
        data: labels.map((label) => periodsByLabel[label].totalWh),
        backgroundColor: "rgba(255, 99, 132, 0.5)",
      },
    ],
  };

  return (
    <Bar
      options={{
        ...options,
        title: {
          display: true,
          text: "kWh Por mes",
        },
        scales: {
          y: {
            ticks: {
              callback: function (value) {
                return whFormatter.format(value);
              },
            },
          },
        },
      }}
      data={data}
    />
  );
}

function ReportAvgPriceByMonthChart({ periods }) {
  const periodsByLabel = periods.reduce((byLabel, period) => {
    return {
      ...byLabel,
      [period.key]: period,
    };
  }, {});

  const labels = Object.keys(periodsByLabel);
  const data = {
    labels,
    datasets: [
      {
        label: "Cents per Wh",
        data: labels.map(
          (label) =>
            periodsByLabel[label].totalPriceInCents /
            periodsByLabel[label].totalWh
        ),
        backgroundColor: "rgba(255, 99, 132, 0.5)",
      },
    ],
  };

  return (
    <Bar
      options={{
        ...options,
        title: {
          display: true,
          text: "Precio medio Por mes",
        },
        scales: {
          y: {
            ticks: {
              callback: function (value) {
                return priceFormatter.format(value);
              },
            },
          },
        },
      }}
      data={data}
    />
  );
}
