import React, { useState, useEffect } from "react";
import { Modal, Card, Form, Button, TimePicker, Input, message, Spin, Row, Col } from "antd";
import { getActivitySchedule, updateSchedule } from "../services/scheduleService";
import { deleteActivity } from "../services/activityService";
import moment from "moment";

const ActivityDetailsModal = ({ visible, onClose, activity }) => {
  const [schedule, setSchedule] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    if (activity && visible) {
      const fetchSchedule = async () => {
        setLoading(true);
        try {
          const data = await getActivitySchedule(activity.activityId);
          setSchedule(data);
        } catch (err) {
          console.error("Error fetching schedule:", err);
          message.error("Failed to fetch schedule. Please try again.");
        } finally {
          setLoading(false);
        }
      };

      fetchSchedule();
    }
  }, [activity, visible]);

  const toggleEdit = () => {
    setIsEditing(!isEditing);
  };

  const onFinish = async (values) => {
    try {
      const { startTime, endTime } = values;
      const today = moment().format("YYYY-MM-DD");
      const formattedStartTime = `${today}T${startTime.format("HH:mm")}`;
      const formattedEndTime = `${today}T${endTime.format("HH:mm")}`;

      await updateSchedule(schedule.scheduleId, formattedStartTime, formattedEndTime);

      message.success("Schedule updated successfully!");
      setIsEditing(false);
      setSchedule({ ...schedule, startTime: formattedStartTime, endTime: formattedEndTime });
    } catch (err) {
      console.error("Error updating schedule:", err);
      message.error("Failed to update schedule. Please try again.");
    }
  };

  const handleDelete = async () => {
    try {
      await deleteActivity(activity.activityId);
      message.success("Activity deleted successfully!");
      onClose(); // Close the modal
    } catch (err) {
      console.error("Error deleting activity:", err);
      message.error("Failed to delete activity. Please try again.");
    }
  };

  return (
    <Modal
      visible={visible}
      onCancel={onClose}
      footer={null}
      title="Activity Details"
      width={800}
    >
      {loading ? (
        <Spin size="large" />
      ) : (
        <>
          <Card style={{ marginBottom: "20px" }}>
            <p><strong>Name:</strong> {activity.name}</p>
            <p><strong>Description:</strong> {activity.description}</p>
            <Button type="primary" danger onClick={handleDelete}>
              Delete Activity
            </Button>
          </Card>
          <h3>Schedule</h3>
          {schedule ? (
            !isEditing ? (
              <Card style={{ marginBottom: "20px" }}>
                <p><strong>Start Time:</strong> {moment(schedule.startTime).format("YYYY-MM-DD HH:mm")}</p>
                <p><strong>End Time:</strong> {moment(schedule.endTime).format("YYYY-MM-DD HH:mm")}</p>
                <Button type="primary" onClick={toggleEdit}>
                  Edit Schedule
                </Button>
              </Card>
            ) : (
              <Form
                layout="vertical"
                onFinish={onFinish}
                initialValues={{
                  startTime: moment(schedule.startTime),
                  endTime: moment(schedule.endTime),
                }}
              >
                <Row gutter={8}>
                  <Col span={12}>
                    <Form.Item
                      label="Start Time"
                      name="startTime"
                      rules={[{ required: true, message: "Please select a start time!" }]}
                    >
                      <TimePicker format="HH:mm" />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      label="End Time"
                      name="endTime"
                      rules={[{ required: true, message: "Please select an end time!" }]}
                    >
                      <TimePicker format="HH:mm" />
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
            )
          ) : (
            <p>No schedule found for this activity.</p>
          )}
        </>
      )}
    </Modal>
  );
};

export default ActivityDetailsModal;