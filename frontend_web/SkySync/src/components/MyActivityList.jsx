import React from "react";
import { Card, Spin, Alert, List, Button } from "antd";
import { useNavigate } from "react-router-dom";

const MyActivityList = ({ activities, loading, error, onViewDetails }) => {
  const navigate = useNavigate();

  return (
    <Card style={{ padding: "20px", borderRadius: "8px" }}>
      {loading ? (
        <div style={{ textAlign: "center", padding: "20px" }}>
          <Spin size="large" />
        </div>
      ) : error ? (
        <Alert
          message={error}
          type="error"
          showIcon
          style={{ marginBottom: "16px" }}
        />
      ) : activities && activities.length > 0 ? (
        <List
          bordered
          dataSource={activities}
          renderItem={(activity) => (
            <List.Item>
              <div style={{ display: "flex",alignItems:"center", justifyContent: "space-between", width: "100%" }}>
                <div>
                  <strong>{activity.name}</strong>
                  <div style={{ color: "#666", marginTop: "4px" }}>
                    {activity.description}
                  </div>
                </div>
                <Button
                  type="primary"
                  onClick={() => onViewDetails(activity)}
                >
                  View Details
                </Button>
              </div>
            </List.Item>
          )}
        />
      ) : (
        <p>No activities found.</p>
      )}
    </Card>
  );
};

export default MyActivityList;