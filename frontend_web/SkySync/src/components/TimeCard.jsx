import React, { useState, useEffect } from "react";
import { Card } from "antd";
import dayjs from "dayjs";

const TimeCard = ({ city }) => {
  const [time, setTime] = useState(dayjs());

  useEffect(() => {
    const interval = setInterval(() => {
      setTime(dayjs());
    }, 1000); // Update time every second

    return () => clearInterval(interval); // Cleanup interval on component unmount
  }, []);

  return (
    <Card
      style={{
        display: "inline-block",
        wordWrap: "break-word",
        textAlign: "center",
        borderRadius: "12px",
        backgroundColor: "#1E3A8A",
        color: "white",
        boxShadow: "0 4px 10px rgba(0, 0, 0, 0.2)",
      }}
    >
      <h3 style={{ margin: 0 , textTransform: "uppercase" }}>{city}</h3>
      <h1 style={{ margin: "10px 0", fontSize: "36px", fontWeight: "bold" }}>
        {time.format("HH:mm")}
      </h1>
      <p style={{ margin: 0 }}>{time.format("dddd, DD MMM")}</p>
    </Card>
  );
};

export default TimeCard;