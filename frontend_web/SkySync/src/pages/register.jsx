import React from "react";
import "../styles/Register.css";
import "../styles/nicepage.css";
import groupLogo from "../assets/Group362.png";
import heroImage from "../assets/hero.png";


const Register = () => {
  return (
    <section className="vh-100 u-clearfix u-section-1" id="block-1">
      <div className="m-0 data-layout-selected u-clearfix u-expanded-width u-layout-wrap u-layout-wrap-1">
        <div className="u-layout">
          <div className="vh-100 u-layout-row">
            <div className="u-align-center u-container-align-center u-container-style u-layout-cell u-shape-rectangle u-size-30 u-layout-cell-1">
              <div className="m-auto u-container-layout u-valign-middle-lg u-valign-middle-md u-valign-middle-sm u-valign-middle-xs u-container-layout-1">
                
                <div style={{boxShadow: '10px 10px 15px rgba(0, 0, 0, 0.3)'}}  className="rounded-3 py-4 u-form u-login-control u-white u-form-1">
                <h3 className="m-auto u-align-center u-custom-font u-font-montserrat u-text u-text-default u-text-1">
                  Register
                </h3>
                  <form
                    action="#"
                    className="u-clearfix u-form-custom-backend u-form-spacing-20 u-form-vertical u-inner-form"
                    name="form"
                    style={{ padding: "30px" }}
                    method="post"
                  >
                    <div className="u-form-group u-form-name">
                      <label htmlFor="username">Full name</label>
                      <input
                        type="text"
                        placeholder="Enter your name"
                        id="username"
                        name="username"
                        className="u-grey-5 u-input u-input-rectangle u-input-1"
                        required
                      />
                    </div>

                    <div className="u-form-group u-form-password">
                      <label htmlFor="password">Password *</label>
                      <input
                        type="text"
                        placeholder="Enter your Password"
                        id="password"
                        name="password"
                        className="u-grey-5 u-input u-input-rectangle u-input-2"
                        required
                      />
                    </div>

                    <div className="u-form-group u-form-group-3">
                      <label htmlFor="contact">E-mail or phone number</label>
                      <input
                        type="text"
                        id="contact"
                        name="text"
                        className="u-grey-5 u-input u-input-rectangle u-input-3"
                        placeholder="Type your e-mail or phone number"
                      />
                    </div>

                    <div className="u-form-checkbox u-form-group">
                      <input
                        type="checkbox"
                        id="remember"
                        name="remember"
                        value="On"
                        className="u-field-input"
                      />
                      <label htmlFor="remember" className="u-field-label">
                        Remember me
                      </label>
                    </div>

                    <div className="u-align-center u-form-group u-form-submit">
                      <button
                        type="submit"
                        className="m-auto u-black u-border-none u-btn u-btn-round u-btn-submit u-button-style u-radius u-btn-1"
                      >
                        Login
                      </button>
                    </div>

                    <input type="hidden" name="recaptchaResponse" value="" />
                  </form>
                  <h5
                  className="m-auto u-align-center u-border-none u-btn u-button-style u-login-control u-login-forgot-password u-none u-text-grey-40 u-btn-2"
                >
                  Already have an account?  
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
