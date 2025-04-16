import React from 'react';
import '../styles/About-Us.css';
import '../styles/nicepage.css';
import Header from '../components/header';
import Footer from '../components/footer';
import wardrobeImage from '../assets/wardrobe.jpg';
import groupImage from '../assets/Group362.png';

const AboutUs = () => {
    return (
        <>
            <Header />
            <section className="container py-5">
                <div className="row align-items-center">
                    {/* Left Column */}
                    <div className="col-md-6">
                        <img
                            className="img-fluid"
                            src={wardrobeImage}
                            alt="Wardrobe"
                        />
                    </div>

                    {/* Right Column */}
                    <div className="col-md-6">
                        <h4 className="text-dark">
                        At SkySync, we believe that weather should never catch you unprepared. Our mission is to make your daily decisions easier by transforming weather data into practical lifestyle advice.
                        </h4>
                        <blockquote className="blockquote text-dark">
                        SkySync is a smart assistant that not only gives you real-time weather updates but also suggests what to wear and what to do based on current and forecasted conditions. Whether it's a sunny day perfect for a jog or a chilly evening that calls for layers, SkySync helps you plan with confidence.
                        </blockquote>
                        <p className="text-muted">
                        We combine weather intelligence, fashion insights, and activity recommendations to bring you a seamless daily experience. With SkySync, you’ll never have to guess what to wear or wonder what’s best to do—rain or shine, we’ve got you covered.
                        </p>
                    </div>
                </div>

                <div className="row align-items-center mt-5">
                    {/* Left Column */}
                    <div className="col-md-6">
                        <h4 className="text-dark">SkySync</h4>
                        <h5 className="text-muted">Founder and CEO</h5>
                    </div>

                    {/* Right Column */}
                    <div className="col-md-6">
                        <img
                            className="img-fluid"
                            src={groupImage}
                            alt="Group"
                        />
                    </div>
                </div>
            </section>
            <Footer />
        </>
    );
};

export default AboutUs;