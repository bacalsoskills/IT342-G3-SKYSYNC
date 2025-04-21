import React, { useEffect, useState } from "react";
import { Menu, Dropdown, Button, Spin, Badge, Drawer } from "antd";
import { UserOutlined, LogoutOutlined, BellOutlined, MenuOutlined } from "@ant-design/icons";
import Logo from "../assets/logo.png";
import { getUserNotifications } from "../services/notificationService";
import { getUserDetails } from "../services/userService";
import { logout } from "../services/authService";
import { useNavigate } from "react-router-dom";

const UserHeader = () => {
  const [user, setUser] = useState({ firstName: "", lastName: "" });
  const [loading, setLoading] = useState(true);
  const [notifications, setNotifications] = useState([]);
  const [loadingNotifications, setLoadingNotifications] = useState(false);
  const [drawerVisible, setDrawerVisible] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchUser();
    fetchNotifications();
    // eslint-disable-next-line
  }, []);

  const fetchUser = async () => {
    try {
      const userId = localStorage.getItem("userId");
      const authToken = localStorage.getItem("authToken");
      if (!userId || !authToken) {
        navigate("/login");
        return;
      }
      const userDetails = await getUserDetails(userId, authToken);
      setUser({
        firstName: userDetails.firstName,
        lastName: userDetails.lastName,
      });
    } catch (error) {
      setUser({ firstName: "", lastName: "" });
      navigate("/login");
    } finally {
      setLoading(false);
    }
  };

  const fetchNotifications = async () => {
    setLoadingNotifications(true);
    try {
      const userId = localStorage.getItem("userId");
      const data = await getUserNotifications(userId);
      setNotifications(data.slice(0, 5));
    } catch (error) {
      setNotifications([]);
    } finally {
      setLoadingNotifications(false);
    }
  };

  const handleProfile = () => {
    navigate("/userprofile");
  };

  const handleLogout = async () => {
    const authToken = localStorage.getItem("authToken");
    if (!authToken) {
      navigate("/");
      return;
    }
    try {
      await logout(authToken);
      localStorage.removeItem("authToken");
      localStorage.removeItem("userId");
      navigate("/");
    } catch (error) {
      alert("Failed to log out. Please try again.");
    }
  };

  const notificationMenu = (
    <Menu>
      {loadingNotifications ? (
        <Menu.Item key="loading">
          <Spin size="small" /> Loading notifications...
        </Menu.Item>
      ) : (
        <>
          {notifications.length > 0 ? (
            notifications.map((notification, index) => (
              <Menu.Item key={index}>
                <div style={{ whiteSpace: "normal" }}>{notification.message}</div>
              </Menu.Item>
            ))
          ) : (
            <Menu.Item key="no-notifications">No notifications available</Menu.Item>
          )}
          <Menu.Divider />
          <Menu.Item key="view-all">
            <Button type="link" onClick={() => navigate("/notifications")}>
              View All Notifications
            </Button>
          </Menu.Item>
        </>
      )}
    </Menu>
  );

  const userMenu = (
    <Menu>
      <Menu.Item key="profile" onClick={handleProfile}>
        <UserOutlined /> User Profile
      </Menu.Item>
      <Menu.Item key="logout" onClick={handleLogout}>
        <LogoutOutlined /> Logout
      </Menu.Item>
    </Menu>
  );

  return (
    <header className="bg-white shadow-sm">
      <div className="container">
        <nav
          className="navbar navbar-expand-md navbar-light"
          style={{ paddingTop: "0.3rem", paddingBottom: "0.3rem", display: "flex", alignItems: "center" }}
        >
          {/* Logo */}
          <a className="navbar-brand font-weight-bold" href="/dashboard">
            <img className="img-fluid hover-enlarge" src={Logo} alt="Logo" height="36" />
          </a>

          {/* My Activities Link (Centered) */}
          <div className="d-none d-md-block" style={{ flex: 1, textAlign: "center" }}>
            <a
              href="/myactivity"
              style={{
                color: "#1890ff",
                fontWeight: "500",
                textDecoration: "none",
                fontSize: "16px",
              }}
            >
              My Activities
            </a>
          </div>

          {/* Hamburger Menu for Smaller Screens */}
          <Button
            type="text"
            className="d-md-none"
            onClick={() => setDrawerVisible(true)}
            style={{ marginLeft: "auto" }}
          >
            <MenuOutlined style={{ fontSize: "20px" }} />
          </Button>

          {/* Account Menu and Notification */}
          <div className="d-none d-md-flex align-items-center">
            {/* User Dropdown */}
            <Dropdown overlay={userMenu} trigger={["click"]}>
              <Button type="text" className="d-flex align-items-center me-3">
                <UserOutlined className="me-2" />
                {loading ? "Loading..." : `${user.firstName} ${user.lastName}`}
              </Button>
            </Dropdown>

            {/* Notification Dropdown */}
            <Dropdown overlay={notificationMenu} trigger={["click"]}>
              <Badge count={notifications.length} offset={[10, 0]}>
                <Button type="text">
                  <BellOutlined style={{ fontSize: "20px", cursor: "pointer" }} />
                </Button>
              </Badge>
            </Dropdown>
          </div>
        </nav>
      </div>

      {/* Drawer for Smaller Screens */}
      <Drawer
        title="Menu"
        placement="right"
        onClose={() => setDrawerVisible(false)}
        visible={drawerVisible}
      >
        <Menu>
          <Menu.Item key="my-activities" onClick={() => navigate("/myactivity")}>
            My Activities
          </Menu.Item>
          <Menu.Item key="profile" onClick={handleProfile}>
            <UserOutlined /> User Profile
          </Menu.Item>
          <Menu.Item key="logout" onClick={handleLogout}>
            <LogoutOutlined /> Logout
          </Menu.Item>
        </Menu>
      </Drawer>
    </header>
  );
};

export default UserHeader;
