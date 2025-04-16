import React from "react";
import "../styles/Activities.css";
import "../styles/nicepage.css";
import Header from "../components/header";
import Footer from "../components/footer";

const Activities = () => {
  return (
    <>
      <Header />
      <section className="d-flex justify-content-center align-items-center u-section-1 py-5">
        <div className="align-items-center container">
          <div className="row align-items-center">
            {/* Left: Image */}
            <div className="col-md-6 mb-4 mb-md-0">
              <div
                className="u-image u-image-1"
                style={{
                  width: "100%",
                  aspectRatio: "1/1",
                  borderRadius: "8px",
                  minHeight: "300px",
                  backgroundSize: "cover",
                  backgroundPosition: "center",
                }}
              ></div>
            </div>
            {/* Right: Text */}
            <div className="col-md-6">
              <h1 className="u-text u-title u-text-1 text-left">Activities</h1>
              <p className="u-large-text u-text u-text-variant u-text-2 text-left">
                SkySync Activities suggests the best activities tailored to the current weather—whether it's a sunny day perfect for a hike, a cozy indoor plan for rainy afternoons, or winter fun when it’s snowing—so you always make the most of your day.
              </p>
            </div>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default Activities;