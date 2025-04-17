import React, { useState } from "react";
import "../styles/Register.css";
import "../styles/nicepage.css";
import groupLogo from "../assets/Group362.png";
import heroImage from "../assets/hero.png";
import { registerUser } from "../services/userService";

const Register = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const user = { firstName, lastName, email, password };
      await registerUser(user);
      setSuccess("Registration successful! You can now log in.");
      setError("");
    } catch (err) {
      setError(err.message);
      setSuccess("");
    }
  };

  return (
    <section className="vh-100 u-clearfix u-section-1" id="block-1">
      <div className="m-0 data-layout-selected u-clearfix u-expanded-width u-layout-wrap u-layout-wrap-1">
        <div className="u-layout">
          <div className="vh-100 u-layout-row">
            <div className="u-align-center u-container-align-center u-container-style u-layout-cell u-shape-rectangle u-size-30 u-layout-cell-1">
              <div className="m-auto u-container-layout u-valign-middle-lg u-valign-middle-md u-valign-middle-sm u-valign-middle-xs u-container-layout-1">
                <div
                  style={{ boxShadow: "10px 10px 15px rgba(0, 0, 0, 0.3)" }}
                  className="rounded-3 py-4 u-form u-login-control u-white u-form-1"
                >
                  <h3 className="m-auto u-align-center u-custom-font u-font-montserrat u-text u-text-default u-text-1">
                    Register
                  </h3>
                  <form
                    onSubmit={handleRegister}
                    className="u-clearfix u-form-custom-backend u-form-spacing-20 u-form-vertical u-inner-form"
                    name="form"
                    style={{ padding: "30px" }}
                  >
                    <div className="u-form-group u-form-name">
                      <label htmlFor="first-name">First Name *</label>
                      <input
                        type="text"
                        placeholder="Enter your first name"
                        id="first-name"
                        name="firstName"
                        className="u-grey-5 u-input u-input-rectangle u-input-1"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        required
                      />
                    </div>

                    <div className="u-form-group u-form-name">
                      <label htmlFor="last-name">Last Name *</label>
                      <input
                        type="text"
                        placeholder="Enter your last name"
                        id="last-name"
                        name="lastName"
                        className="u-grey-5 u-input u-input-rectangle u-input-1"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        required
                      />
                    </div>

                    <div className="u-form-group u-form-email">
                      <label htmlFor="email">Email *</label>
                      <input
                        type="email"
                        placeholder="Enter your email"
                        id="email"
                        name="email"
                        className="u-grey-5 u-input u-input-rectangle u-input-2"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                      />
                    </div>

                    <div className="u-form-group u-form-password">
                      <label htmlFor="password">Password *</label>
                      <input
                        type="password"
                        placeholder="Enter your password"
                        id="password"
                        name="password"
                        className="u-grey-5 u-input u-input-rectangle u-input-3"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                      />
                    </div>

                    {error && <p style={{ color: "red" }}>{error}</p>}
                    {success && <p style={{ color: "green" }}>{success}</p>}

                    <div className="u-align-center u-form-group u-form-submit">
                      <button
                        type="submit"
                        className="m-auto u-black u-border-none u-btn u-btn-round u-btn-submit u-button-style u-radius u-btn-1"
                      >
                        Register
                      </button>
                    </div>
                  </form>
                  <h5 className="m-auto u-align-center u-border-none u-btn u-button-style u-login-control u-login-forgot-password u-none u-text-grey-40 u-btn-2">
                    Already have an account?{" "}
                    <a
                      href="/login"
                      className="u-border-none u-button-style u-login-control u-login-create-account u-none u-text-grey-40 u-text-hover-palette-4-base"
                    >
                      Sign In
                    </a>
                  </h5>
                </div>
              </div>
            </div>

            <div className="d-none d-md-flex u-container-style u-image u-layout-cell u-size-30 u-image-r1">
              <div className="u-container-layout u-valign-bottom-lg u-valign-bottom-md u-valign-bottom-sm u-valign-bottom-xs u-container-layout-2">
                <a href="/">
                  <img
                    className="img-fluid hover-enlarge u-image u-image-contain u-image-default u-image-r2"
                    src={groupLogo}
                    alt="Group logo"
                  />
                </a>
                <img
                  className="u-image u-image-default u-image-r3"
                  src={heroImage}
                  alt="Hero image"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Register;
