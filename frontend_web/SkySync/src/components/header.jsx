import React, { useState } from "react";
import Logo from "../assets/logo.png";
import "font-awesome/css/font-awesome.min.css";
const Header = () => {
  const [isOpen, setIsOpen] = useState(false);
  const toggleMenu = () => setIsOpen(!isOpen);

  return (
    <header className="bg-white shadow-sm">
      <div className="container">
      <nav
          className="navbar navbar-expand-md navbar-light"
          style={{ paddingTop: "0.3rem", paddingBottom: "0.3rem" }}
        >
          {/* Logo */}
          <a className="navbar-brand font-weight-bold" href="/">
            <img className="img-fluid hover-enlarge" src={Logo} alt="Logo" height="36" />
          </a>

          {/* Hamburger Toggle */}
          <button
            className="navbar-toggler"
            type="button"
            onClick={toggleMenu}
            aria-controls="main-navbar"
            aria-expanded={isOpen}
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          {/* Nav Links */}
          <div
            className={`collapse navbar-collapse ${isOpen ? "show" : ""}`}
            id="main-navbar"
          >
            <ul className="navbar-nav mx-auto">
              <li className="nav-item">
                <a className="img-fluid hover-enlarge nav-link" href="/">Home</a>
              </li>
              <li className="nav-item">
                <a className="img-fluid hover-enlarge nav-link" href="/wardrobe">Wardrobe</a>
              </li>
              <li className="nav-item">
                <a className="img-fluid hover-enlarge nav-link" href="/weather">Weather</a>
              </li>
              <li className="nav-item">
                <a className="img-fluid hover-enlarge nav-link" href="/activities">Activities</a>
              </li>
              <li className="nav-item">
                <a className="img-fluid hover-enlarge nav-link" href="/about">About Us</a>
              </li>
            </ul>

            {/* Login Icon */}
            <ul className="img-fluid hover-enlarge navbar-nav">
              <li className="nav-item">
                <a className="nav-link d-flex align-items-center justify-content-center" href="/login">
                  <i className="fa fa-user-circle fa-lg mr-1" aria-hidden="true"></i>
                  <span className="px-2">Login</span>
                </a>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </header>
  );
};

export default Header;