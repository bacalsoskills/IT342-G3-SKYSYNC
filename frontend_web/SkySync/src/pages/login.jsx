import React, { useState } from "react";
import "../styles/login.css";
import groupImage from "../assets/Group362.png";
import image1 from "../assets/alyssa-strohmann-TS--uNw-JqE-unsplash1.png";
import image2 from "../assets/alyssa-strohmann-TS--uNw-JqE-unsplash2.png";
import image3 from "../assets/alyssa-strohmann-TS--uNw-JqE-unsplash3.png";
import { login } from "../services/authService";
import { Modal } from "antd";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [modalVisible, setModalVisible] = useState(false);
  const [countdown, setCountdown] = useState(3);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const data = await login(email, password);
      localStorage.setItem("authToken", data.token);
      localStorage.setItem("userId", data.userId);
      setModalVisible(true);
      setCountdown(3);
      let counter = 3;
      const interval = setInterval(() => {
        counter -= 1;
        setCountdown(counter);
        if (counter === 0) {
          clearInterval(interval);
          window.location.href = "/dashboard";
        }
      }, 1000);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleRegisterClick = (e) => {
    e.preventDefault();
    window.location.href = "/register";
  };

  return (
    <section className="vh-100 u-clearfix u-valign-top-lg u-valign-top-md u-valign-top-sm u-valign-top-xs u-section-1" id="block-1">
      <Modal
        open={modalVisible}
        footer={null}
        closable={false}
        centered
        bodyStyle={{ textAlign: "center", fontSize: "18px" }}
      >
        Login successful!<br />
        Redirecting in {countdown}
      </Modal>
      <div className="data-layout-selected u-clearfix u-expanded-width u-layout-wrap u-layout-wrap-1">
        <div className="u-layout">
          <div className="u-layout-row">
            <div className="u-container-style u-image u-layout-cell u-size-30 u-image-l1" data-image-width="3668" data-image-height="1976">
              <div className="u-container-layout u-container-layout-1">
                <a href="/">
                  <img className="img-fluid hover-enlarge u-image u-image-contain u-image-default u-image-l2" src={groupImage} alt="Group Image" />
                </a>
                <img className="u-image u-image-default u-image-l3" src={image1} alt="Image 1" />
                <img className="u-image u-image-default u-image-l4" src={image2} alt="Image 2" />
                <img className="u-image u-image-default u-image-l5" src={image3} alt="Image 3" />
              </div>
            </div>
            <div className="u-align-center u-container-align-center u-container-align-center-lg u-container-align-center-xl u-container-align-center-xxl u-container-style u-layout-cell u-size-30 u-layout-cell-2">
              <div className="m-auto u-container-layout u-valign-middle-lg u-valign-middle-md u-valign-middle-sm u-valign-top-xs u-container-layout-2">
                <div style={{ boxShadow: "10px 10px 15px rgba(0, 0, 0, 0.3)" }} className="rounded-3 py-4 u-form u-login-control u-white u-form-1">
                  <h3 className="m-auto u-align-center u-custom-font u-font-montserrat u-text u-text-default u-text-1">Login</h3>
                  <form onSubmit={handleLogin} className="u-clearfix u-form-custom-backend u-form-spacing-20 u-form-vertical u-inner-form" name="form" style={{ padding: "30px" }}>
                    <div className="u-form-group u-form-email">
                      <label htmlFor="email-a30d" className="u-label">Email *</label>
                      <input
                        type="email"
                        placeholder="Enter your Email"
                        id="email-a30d"
                        name="email"
                        className="u-grey-5 u-input u-input-rectangle u-input-1"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                      />
                    </div>
                    <div className="u-form-group u-form-password">
                      <label htmlFor="password-a30d" className="u-label">Password *</label>
                      <input
                        type="password"
                        placeholder="Enter your Password"
                        id="password-a30d"
                        name="password"
                        className="u-grey-5 u-input u-input-rectangle u-input-2"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                      />
                    </div>
                    {error && <p style={{ color: "red" }}>{error}</p>}
                    <div className="u-align-left u-form-group u-form-submit">
                      <button type="submit" className="btn hover-light-blue u-black u-border-none u-btn u-btn-round u-btn-submit u-button-style u-radius u-btn-1">Login</button>
                    </div>
                  </form>
                  <a href="#" className="m-auto u-align-center u-border-none u-btn u-button-style u-login-control u-login-forgot-password u-none u-text-grey-40 u-text-hover-palette-4-base u-btn-2">Forgot password?</a>
                  <a
                    href="/register"
                    onClick={handleRegisterClick}
                    className="m-auto u-align-center u-border-none u-btn u-button-style u-login-control u-login-create-account u-none u-text-grey-40 u-text-hover-palette-4-base u-btn-3"
                  >
                    Don't have an account?
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Login;
