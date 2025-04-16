import React from "react";
import "../styles/wardrobe.css";
import "../styles/nicepage.css";
import Header from "../components/header";
import Footer from "../components/footer";

const Wardrobe = () => {
  return (
    <>
      <Header />
      <section className="u-section-1 py-5">
        <div className="container">
          <div className="row align-items-center">
            {/* Left Column */}
            <div className="col-md-6">
              <h1 className="u-text u-title u-text-1">Wardrobe</h1>
              <p className="u-large-text u-text u-text-variant u-text-2">
                The SkySync Smart Wardrobe is an intelligent feature that
                automatically selects the most suitable outfit for you based on
                current and forecasted weather conditions. By integrating with
                real-time weather data, it considers factors like temperature,
                precipitation, wind, and UV index to recommend clothing that
                keeps you comfortable and stylish throughout the day. It
                cross-references this data with your virtual wardrobe and
                personal preferences—such as style (casual, formal, sporty),
                favorite colors, and go-to items—ensuring each outfit is both
                weather-appropriate and aligned with your taste. Additionally,
                it can sync with your calendar to suggest outfits tailored to
                specific events or activities, making SkySync a reliable,
                weather-smart wardrobe assistant that helps you dress
                confidently, no matter the forecast.
              </p>
            </div>

            {/* Right Column */}
            <div className="col-md-6">
            <div className="row">
            <div className="col-6">
                <div className="u-image u-image-5"></div>
            </div>
            <div className="col-6">
                <div className="u-image u-image-6"></div>
            </div>
            </div>
            <div className="row mt-3">
            <div className="col-6">
                <div className="u-image u-image-7"></div>
            </div>
            <div className="col-6">
                <div className="u-image u-image-8"></div>
            </div>
            </div>
            </div>
          </div>
        </div>
      </section>

      
      <Footer />
    </>
  );
};

export default Wardrobe;