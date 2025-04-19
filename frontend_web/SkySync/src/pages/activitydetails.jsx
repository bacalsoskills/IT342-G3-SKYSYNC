import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Card, Form, Button, DatePicker, TimePicker, message, Spin, Row, Col } from "antd";
import { getActivitySchedule, updateSchedule } from "../services/scheduleService";
import moment from "moment";

const ActivityDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { activity } = location.state || {}; // Get the activity passed from the previous page
  const [schedule, setSchedule] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isEditing, setIsEditing] = useState(false); // Toggle for editing mode

  useEffect(() => {
    const fetchSchedule = async () => {
      setLoading(true);
      try {
        const data = await getActivitySchedule(activity.activityId); // Fetch the schedule for the activity
        setSchedule(data);
      } catch (err) {
        console.error("Error fetching schedule:", err);
        message.error("Failed to fetch schedule. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchSchedule();
  }, [activity.activityId]);

  const toggleEdit = () => {
    setIsEditing(!isEditing); // Toggle editing mode
  };

  const onFinish = async (values) => {
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

      // Call the updateSchedule function
      await updateSchedule(schedule.scheduleId, formattedStartTime, formattedEndTime);

      message.success("Schedule updated successfully!");
      setIsEditing(false); // Exit editing mode
      setSchedule({ ...schedule, startTime: formattedStartTime, endTime: formattedEndTime }); // Update local state
    } catch (err) {
      console.error("Error updating schedule:", err);
      message.error("Failed to update schedule. Please try again.");
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/myactivity")} style={{ marginBottom: "20px" }}>
        Back to My Activities
      </Button>
      <h2>Activity Details</h2>
      <Card style={{ marginBottom: "20px" }}>
        <p><strong>Name:</strong> {activity.name}</p>
        <p><strong>Description:</strong> {activity.description}</p>
      </Card>
      <h3>Schedule</h3>
      {loading ? (
        <Spin size="large" />
      ) : schedule ? (
        <>
          {!isEditing ? (
            // Display schedule details
            <Card style={{ marginBottom: "20px" }}>
              <p><strong>Start Time:</strong> {moment(schedule.startTime).format("YYYY-MM-DD HH:mm")}</p>
              <p><strong>End Time:</strong> {moment(schedule.endTime).format("YYYY-MM-DD HH:mm")}</p>
              <Button type="primary" onClick={toggleEdit}>
                Edit Schedule
              </Button>
            </Card>
          ) : (
            // Editable form for updating schedule
            <Form
              layout="vertical"
              onFinish={onFinish}
              initialValues={{
                startDate: moment(schedule.startTime), // Set the initial date for start time
                startTime: moment(schedule.startTime), // Set the initial time for start time
                endDate: moment(schedule.endTime), // Set the initial date for end time
                endTime: moment(schedule.endTime), // Set the initial time for end time
              }}
            >
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
                      onSelect={(value) => console.log("Selected Time:", value)} // Optional: Debugging
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
                      onSelect={(value) => console.log("Selected Time:", value)} // Optional: Debugging
                    />
                  </Form.Item>
                </Col>
              </Row>
              <Form.Item>
                <Button type="primary" htmlType="submit" style={{ marginRight: "10px" }}>
                  Save Changes
                </Button>
                <Button type="default" onClick={toggleEdit}>
                  Cancel
                </Button>
              </Form.Item>
            </Form>
          )}
        </>
      ) : (
        <p>No schedule found for this activity.</p>
      )}
    </div>
  );
};

export default ActivityDetails;