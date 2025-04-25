import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Card, Form, Button, TimePicker, Input, message, Spin, Row, Col } from "antd";
import { getActivitySchedule, updateSchedule } from "../services/scheduleService";
import { deleteActivity } from "../services/activityService"; // Import the deleteActivity function
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
      const { startTime, endTime } = values;

      // Automatically set today's date and combine it with the selected times
      const today = moment().format("YYYY-MM-DD");
      const formattedStartTime = `${today}T${startTime.format("HH:mm")}`;
      const formattedEndTime = `${today}T${endTime.format("HH:mm")}`;

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

  const handleDelete = async () => {
    try {
      await deleteActivity(activity.activityId); // Call the delete endpoint
      message.success("Activity deleted successfully!");
      navigate("/myactivity"); // Redirect to the My Activities page
    } catch (err) {
      console.error("Error deleting activity:", err);
      message.error("Failed to delete activity. Please try again.");
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
        <Button type="primary" danger onClick={handleDelete}>
          Delete Activity
        </Button>
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
            // Reuse the form structure from ScheduleActivity
            <Form
              layout="vertical"
              onFinish={onFinish}
              initialValues={{
                startTime: moment(schedule.startTime),
                endTime: moment(schedule.endTime),
              }}
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
                      hideDisabledOptions
                      renderExtraFooter={() => null}
                    />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    label="End Time"
                    name="endTime"
                    rules={[{ required: true, message: "Please select an end time!" }]}
                  >
                    <TimePicker
                      format="HH:mm"
                      hideDisabledOptions
                      renderExtraFooter={() => null}
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