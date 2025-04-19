import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Form, Input, Button, DatePicker, TimePicker, message, Row, Col } from "antd";
import { scheduleActivity } from "../services/scheduleService";
import moment from "moment"; // Ensure moment is installed

const ScheduleActivity = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { activity } = location.state || {}; // Get the activity passed from the previous page
  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage

  const [loading, setLoading] = useState(false);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const { startDate, startTime, endDate, endTime } = values;

      // Combine date and time into a single datetime string
      const formattedStartTime = moment(startDate)
        .set({
          hour: startTime.hour(),
          minute: startTime.minute(),
        })
        .format("YYYY-MM-DDTHH:mm");

      const formattedEndTime = moment(endDate)
        .set({
          hour: endTime.hour(),
          minute: endTime.minute(),
        })
        .format("YYYY-MM-DDTHH:mm");

      console.log("Formatted Start Time:", formattedStartTime);
      console.log("Formatted End Time:", formattedEndTime);

      await scheduleActivity(userId, activity.activityId, formattedStartTime, formattedEndTime); // Send formatted times to the backend
      message.success("Activity scheduled successfully!");
      navigate("/dashboard"); // Navigate back to the dashboard
    } catch (err) {
      console.error("Error scheduling activity:", err);
      message.error("Failed to schedule activity. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/recommendedactivity")} style={{ marginBottom: "20px" }}>
        Back to Recommended Activities
      </Button>
      <h2>Schedule Activity</h2>
      {activity ? (
        <Form
          layout="vertical"
          onFinish={onFinish}
          initialValues={{
            startDate: null,
            startTime: null,
            endDate: null,
            endTime: null,
          }}
        >
          <Form.Item label="Activity Name">
            <Input value={activity.name} disabled />
          </Form.Item>
          <Row gutter={8}>
            <Col span={10}>
              <Form.Item
                label="Start Date"
                name="startDate"
                rules={[{ required: true, message: "Please select a start date!" }]}
              >
                <DatePicker format="YYYY-MM-DD" />
              </Form.Item>
            </Col>
            <Col span={6}>
              <Form.Item
                label="Start Time"
                name="startTime"
                rules={[{ required: true, message: "Please select a start time!" }]}
              >
                <TimePicker
                  format="HH:mm"
                  hideDisabledOptions // Hides disabled options for cleaner UI
                  renderExtraFooter={() => null} // Ensures the footer is completely removed
                />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={8}>
            <Col span={10}>
              <Form.Item
                label="End Date"
                name="endDate"
                rules={[{ required: true, message: "Please select an end date!" }]}
              >
                <DatePicker format="YYYY-MM-DD" />
              </Form.Item>
            </Col>
            <Col span={6}>
              <Form.Item
                label="End Time"
                name="endTime"   
                rules={[{ required: true, message: "Please select an end time!" }]}
              >
                <TimePicker
                  format="HH:mm"
                  hideDisabledOptions // Hides disabled options for cleaner UI
                  renderExtraFooter={() => null} // Ensures the footer is completely removed
                />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              Schedule Activity
            </Button>
          </Form.Item>
        </Form>
      ) : (
        <p>No activity selected. Please go back and select an activity.</p>
      )}
    </div>
  );
};

export default ScheduleActivity;