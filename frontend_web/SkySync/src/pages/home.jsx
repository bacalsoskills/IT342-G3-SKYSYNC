import React from "react";
import "../styles/home.css";
import "../styles/nicepage.css";
import imgWardrobe from '../assets/alyssa-strohmann-TS--uNw-JqE-unsplash1.png';
import imgActivities from '../assets/alyssa-strohmann-TS--uNw-JqE-unsplash2.png';
import imgWeather from '../assets/alyssa-strohmann-TS--uNw-JqE-unsplash3.png';
import imgAlico from '../assets/Alico.jpg';
import imgBaluntang from '../assets/Baluntang.jpg';
import imgBacalso from '../assets/Bacalso.jpg';
import hero from "../assets/hero.png";
import Header from "../components/header";
import Footer from "../components/footer";

const Home = () => {
  return (
    <div style={{ width: "100%" }}>
      <div className="text-center">
        <Header />
        <div style={{
    boxShadow: `
    inset 0 4px 6px -4px rgba(0, 0, 0, 0.2), 
    inset 0 -4px 6px -4px rgba(0, 0, 0, 0.2)
  `,
    backgroundColor: "white"
  }} 
  className="u-section-image min-h-110vh">
        <section className="container">
          <div className="row align-items-center">
            <div className="col-md-6 text-center">
              <h1 className="u-text u-title">Welcome to SkySync</h1>
              <p className="u-large-text u-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sapien, est felis, sagittis viverra nulla mattis scelerisque. Eget cras integer.
              </p>
              <a
                href="/login"
                className="btn btn-dark btn-rounded"
              >
                Contact Us
              </a>
            </div>
            <div className="col-md-6">
              <img
                className="img-fluid"
                src={hero}
                alt="Hero"
              />
            </div>
          </div>
        </section>
        </div>
        {/* Wardrobe Section */}
        <section className="container py-5">
          <div className="row align-items-center">
            <div className="col-md-6">
              <img
                className="img-fluid"
                src={imgWardrobe}
                alt="Wardrobe"
              />
            </div>
            <div className="col-md-6">
              <h2>Wardrobe</h2>
              <p className="text-start">
              Connects your wardrobe to the weather — so you're never too hot, too cold, or caught in the rain unprepared. Our intelligent outfit suggestions adapt to real-time forecasts, helping you step out in comfort and style, every day
              </p>
            </div>
          </div>
        </section>

        {/* Activities Section */}
        <section className="container py-5">
          <div className="row align-items-center">
            <div className="col-md-6 order-md-2">
              <img
                className="img-fluid"
                src={imgActivities}
                alt="Activities"
              />
            </div>
            <div className="col-md-6 order-md-1">
              <h2>Activities</h2>
              <p className="text-start">
              Get personalized activity suggestions based on the weather, time of day, and your preferences. Whether it’s a jog in the park or a cozy day indoors, your day is curated to match the moment.
              </p>
            </div>
          </div>
        </section>

        {/* Weather Section */}
        <section className="container py-5">
          <div className="row align-items-center">
            <div className="col-md-6">
              <img
                className="img-fluid"
                src={imgWeather}
                alt="Weather"
              />
            </div>
            <div className="col-md-6">
              <h2>Weather</h2>
              <p className="text-start">
              Stay ahead of the elements with real-time weather updates that adapt to your day. From sunshine to storms, know exactly what to expect — and how to dress for it. Weather becomes more than a report; it becomes your daily guide.
              </p>
            </div>
          </div>
        </section>

      <div style={{
      boxShadow: `
        inset 0 4px 6px -4px rgba(0, 0, 0, 0.2), 
        inset 0 -4px 6px -4px rgba(0, 0, 0, 0.2)
      `, }}
      className="u-section-image">
      <section
    className="pb-5 container u-align-center u-clearfix u-container-align-center u-section-3"
    id="block-2"
  >
      <div className="row u-clearfix u-sheet u-valign-middle-lg u-valign-middle-md u-valign-middle-sm u-valign-middle-xs u-sheet-1">
        
        <div className="col-12 u-align-center u-container-align-center u-container-style u-group u-group-1">
          <div className="u-container-layout u-valign-middle u-container-layout-1">
            <h2 className="u-align-center u-text u-text-1">What our customers say</h2>
            <p className="u-align-center u-text u-text-2">
            "Hear directly from our satisfied customers about their experiences with our service."
            </p>
          </div>
        </div>

        <div className="u-expanded-width u-list u-list-1">
          <div className="u-repeater u-repeater-1">

            {/* Testimonial 1 */}
            <div 
              className="u-align-center u-container-align-center u-list-item u-radius u-repeater-item u-shape-round u-video-cover u-white u-list-item-1 p-4 h-100 d-flex flex-column justify-content-between"
              style={{ boxSizing: "border-box" }}
            >
              <div className="u-container-layout u-similar-container u-valign-top-lg u-valign-top-md u-valign-top-sm u-valign-top-xs u-container-layout-2">
                <p className="u-align-center u-text u-text-3">
                  "Aenean pulvinar dui ornare, feugiat lorem non, ultrices justo. Mauris efficitur, mauris in auctor euismod, quam elit ultrices urna, eget eleifend arcu risus id metus."
                </p>
                <h5 className="u-align-center u-text u-text-4">Mattie Smith</h5>
                <h6 className="u-align-center u-text u-text-5">Chief Accountant</h6>
              </div>
            </div>

            {/* Testimonial 2 */}
            <div 
              className="u-align-center u-container-align-center u-list-item u-radius u-repeater-item u-shape-round u-video-cover u-white u-list-item-1 p-4 h-100 d-flex flex-column justify-content-between"
              style={{ boxSizing: "border-box" }}
            >
              <div className="u-container-layout u-similar-container u-valign-top-lg u-valign-top-md u-valign-top-sm u-valign-top-xs u-container-layout-2">
                <p className="u-align-center u-text u-text-3">
                  "Aenean pulvinar dui ornare, feugiat lorem non, ultrices justo. Mauris efficitur, mauris in auctor euismod, quam elit ultrices urna, eget eleifend arcu risus id metus."
                </p>
                <h5 className="u-align-center u-text u-text-4">Mattie Smith</h5>
                <h6 className="u-align-center u-text u-text-5">Chief Accountant</h6>
              </div>
            </div>

            {/* Testimonial 3 */}
            <div 
              className="u-align-center u-container-align-center u-list-item u-radius u-repeater-item u-shape-round u-video-cover u-white u-list-item-1 p-4 h-100 d-flex flex-column justify-content-between"
              style={{ boxSizing: "border-box" }}
            >
              <div className="u-container-layout u-similar-container u-valign-top-lg u-valign-top-md u-valign-top-sm u-valign-top-xs u-container-layout-2">
                <p className="u-align-center u-text u-text-3">
                  "Aenean pulvinar dui ornare, feugiat lorem non, ultrices justo. Mauris efficitur, mauris in auctor euismod, quam elit ultrices urna, eget eleifend arcu risus id metus."
                </p>
                <h5 className="u-align-center u-text u-text-4">Mattie Smith</h5>
                <h6 className="u-align-center u-text u-text-5">Chief Accountant</h6>
              </div>
            </div>

          </div>
        </div>
      </div>
    </section>
    </div>

    
    <section className="container py-5" id="sec-8363">
  <div className="text-center">
    <h2 className="mb-4">Our Team</h2>
    <p className="mb-5">"A team committed to delivering exceptional results."</p>
  </div>

  <div className="row">
    {/* Team Member 1 */}
    <div className="col-md-4 text-center">
      <img
        alt="Alico"
        className="img-fluid  mb-3"
        src={imgAlico}
      />
      <h5>Christian Barry R. Alico</h5>
      <p>Backend Developer</p>
    </div>

    {/* Team Member 2 */}
    <div className="col-md-4 text-center">
      <img
        alt="Baluntang"
        className="img-fluid mb-3"
        src={imgBaluntang}
      />
      <h5>Rendolf E. Baluntang</h5>
      <p>Frontend Web</p>
    </div>

    {/* Team Member 3 */}
    <div className="col-md-4 text-center">
      <img
        alt="Bacalso"
        className="img-fluid mb-3"
        src={imgBacalso}
      />
      <h5>Michael Ferdinand C. Bacalso</h5>
      <p>Frontend Mobile</p>
    </div>
  </div>
</section>

        {/* Footer */}
        <Footer />
      </div>
    </div>
  );
};

export default Home;