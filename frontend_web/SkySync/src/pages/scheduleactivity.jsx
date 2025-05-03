import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Form, TimePicker, Button, message, Row, Col, Input } from "antd"; // Add Input here
import { scheduleActivity } from "../services/scheduleService";
import moment from "moment"; // Ensure moment is installed
import UserHeader from "../components/userHeader";

const ScheduleActivity = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { activity } = location.state || {}; // Get the activity passed from the previous page
  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage
  const savedDate = localStorage.getItem("selectedScheduleDate") || moment().format("YYYY-MM-DD");

  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm(); // Add this line to use Form instance
  const [formValues, setFormValues] = useState({ startTime: null, endTime: null });

  const getDisabledEndTime = () => {
    const startTime = form.getFieldValue("startTime");
    if (!startTime) return {};

    const startHour = startTime.hour();
    const startMinute = startTime.minute();

    return {
      // Disable all hours less than or equal to the start hour
      disabledHours: () => Array.from({ length: 24 }, (_, i) => i).filter(h => h <= startHour),
      // If the selected hour is the next hour after start, disable minutes less than or equal to startMinute
      disabledMinutes: (selectedHour) => {
        return [];
      },
    };
  };

  const isScheduleDisabled = () => {
    const { startTime, endTime } = formValues;
    if (!startTime || !endTime) return true;
    return !endTime.isAfter(startTime);
  };

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const { startTime, endTime } = values;

      // Use the saved date for scheduling
      const formattedStartTime = `${savedDate}T${startTime.format("HH:mm")}`;
      const formattedEndTime = `${savedDate}T${endTime.format("HH:mm")}`;

      console.log("Formatted Start Time:", formattedStartTime);
      console.log("Formatted End Time:", formattedEndTime);

      await scheduleActivity(userId, activity.activityId, formattedStartTime, formattedEndTime);

      message.success("Activity scheduled successfully! Redirecting to dashboard...");
      setTimeout(() => {
        navigate("/dashboard");
      }, 2000);
    } catch (err) {
      console.error("Error scheduling activity:", err);
      message.error("Failed to schedule activity. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-vh-100 u-fixed-background" >
      <UserHeader />
    <div className="col-12 col-md-10 col-lg-8 mx-auto" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/recommendedactivity")} style={{ marginBottom: "20px" }}>
        Back to Recommended Activities
      </Button>
      <h2>Schedule Activity</h2>
      {activity ? (
        <Form
          form={form} // Attach form instance
          layout="vertical"
          onFinish={onFinish}
          initialValues={{
            startTime: null,
            endTime: null,
          }}
          onValuesChange={(_, allValues) => setFormValues(allValues)}
        >
          <Form.Item label="Activity Name">
            <Input value={activity.name} disabled />
          </Form.Item>
          <Row gutter={8}>
            <Col span={12}>
              <Form.Item
                label="Start Time"
                name="startTime"
                rules={[{ required: true, message: "Please select a start time!" }]}
              >
                <TimePicker
                  format="HH:mm"
                  hideDisabledOptions // Hides disabled options for cleaner UI
                  renderExtraFooter={() => null} // Ensures the footer is completely removed
                  onChange={() => {
                    // Reset endTime if startTime changes
                    form.setFieldsValue({ endTime: null });
                  }}
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="End Time"
                name="endTime"
                dependencies={["startTime"]}
                rules={[
                  { required: true, message: "Please select an end time!" },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      const start = getFieldValue("startTime");
                      if (!start || !value) return Promise.resolve();
                      if (value.isAfter(start)) return Promise.resolve();
                      return Promise.reject(new Error("End time must be after start time!"));
                    },
                  }),
                ]}
              >
                <TimePicker
                  format="HH:mm"
                  hideDisabledOptions // Hides disabled options for cleaner UI
                  renderExtraFooter={() => null} // Ensures the footer is completely removed
                  {...getDisabledEndTime()}
                />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              disabled={isScheduleDisabled()}
            >
              Schedule Activity
            </Button>
          </Form.Item>
        </Form>
      ) : (
        <p>No activity selected. Please go back and select an activity.</p>
      )}
    </div>
    </div>
  );
};

export default ScheduleActivity;