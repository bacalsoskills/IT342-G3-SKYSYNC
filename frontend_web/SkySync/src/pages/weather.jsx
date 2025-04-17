import React from "react";
import "../styles/Weather.modules.css";
import "../styles/nicepage.css";
import Header from "../components/header";
import Footer from "../components/footer";

const Weather = () => {
  return (
    <>
      <Header />
      <section className="u-section-1 py-5 py-5">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <h1 className="u-text u-title u-text-1">Weather</h1>
              <p className="u-large-text u-text u-text-variant u-text-2">
                SkySync Weather provides real-time, hyperlocal weather updates including temperature, rain chances, wind, and more—helping you plan your day and dress smart with accurate, up-to-date forecasts.
              </p>
            </div>
          </div>
          <div className="row mt-4">
            <div className="col-md-4">
              <div className="u-image w-u-image-1 weather-img"></div>
            </div>
            <div className="col-md-4">
              <div className="u-image w-u-image-2 weather-img"></div>
            </div>
            <div className="col-md-4">
              <div className="u-image w-u-image-3 weather-img"></div>
            </div>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default Weather;