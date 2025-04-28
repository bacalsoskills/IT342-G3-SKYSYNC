import React, { useEffect, useState } from "react";
import { Menu, Dropdown, Button, Spin, Badge, Drawer } from "antd";
import { UserOutlined, LogoutOutlined, BellOutlined, MenuOutlined, CheckOutlined } from "@ant-design/icons";
import Logo from "../assets/logo.png";
import { getUnreadNotifications } from "../services/notificationService"; // Ensure this is imported
import { getUserDetails } from "../services/userService";
import { logout } from "../services/authService";
import { markNotificationAsRead } from "../services/notificationService";
import { useNavigate } from "react-router-dom";

const capitalize = (str) => {
  if (!str) return "";
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};

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
      if (!userId) {
        console.error("User ID not found in localStorage.");
        return;
      }
      const data = await getUnreadNotifications(userId); // Fetch only unread notifications
      setNotifications(data); // Set the state with unread notifications
    } catch (error) {
      console.error("Failed to fetch notifications:", error);
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

  const handleMarkAsRead = async (notificationId) => {
    try {
      await markNotificationAsRead(notificationId);
      // Remove the notification from the dropdown after marking it as read
      setNotifications((prevNotifications) =>
        prevNotifications.filter((notification) => notification.id !== notificationId)
      );
    } catch (error) {
      console.error("Failed to mark notification as read:", error);
    }
  };

  const notificationMenuItems = loadingNotifications
    ? [
        {
          key: "loading",
          label: (
            <div>
              <Spin size="small" /> Loading notifications...
            </div>
          ),
        },
      ]
    : notifications.length > 0
    ? [
        ...notifications.map((notification, index) => ({
          key: index,
          label: (
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <span style={{ whiteSpace: "normal" }}>{notification.message}</span>
              <Button
                type="text"
                icon={<CheckOutlined />}
                onClick={(e) => {
                  e.stopPropagation(); // Prevent dropdown from closing
                  handleMarkAsRead(notification.id);
                }}
              />
            </div>
          ),
        })),
        { type: "divider" },
        {
          key: "view-all",
          label: (
            <Button type="link" onClick={() => navigate("/notifications")}>
              View All Notifications
            </Button>
          ),
        },
      ]
    : [
        {
          key: "no-notifications",
          label: (
            <Button type="link" onClick={() => navigate("/notifications")} style={{ padding: 0 }}>
              No unread notifications
            </Button>
          ),
        },
      ];

  const userMenuItems = [
    {
      key: "profile",
      label: (
        <div onClick={handleProfile}>
          <UserOutlined /> User Profile
        </div>
      ),
    },
    {
      key: "logout",
      label: (
        <div onClick={handleLogout}>
          <LogoutOutlined /> Logout
        </div>
      ),
    },
  ];

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

          {/* Navigation List (Centered) */}
          <div className="d-none d-md-block" style={{ flex: 1, textAlign: "center" }}>
            <ul className="nav justify-content-center userheader-nav-list">
              <li className="nav-item">
                <a
                  href="/dashboard"
                  className={`nav-link userheader-nav-link${window.location.pathname === "/dashboard" ? " active" : ""}`}
                >
                  Home
                </a>
              </li>
              <li className="nav-item">
                <a
                  href="/weeklyforecast"
                  className={`nav-link userheader-nav-link${window.location.pathname === "/weeklyforecast" ? " active" : ""}`}
                >
                  Weather
                </a>
              </li>
              <li className="nav-item">
                <a
                  href="/recommendedwardrobe"
                  className={`nav-link userheader-nav-link${window.location.pathname === "/recommendedwardrobe" ? " active" : ""}`}
                >
                  Wardrobe
                </a>
              </li>
              <li className="nav-item">
                <a
                  href="/recommendedactivity"
                  className={`nav-link userheader-nav-link${window.location.pathname === "/recommendedactivity" ? " active" : ""}`}
                >
                  Activity
                </a>
              </li>
            </ul>
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
            {/* Notification Dropdown */}
            <Dropdown menu={{ items: notificationMenuItems }} trigger={["click"]}>
              <Badge count={notifications.length} offset={[-15, 0]}>
                <Button type="text">
                  <BellOutlined style={{ fontSize: "20px", cursor: "pointer" }} />
                </Button>
              </Badge>
            </Dropdown>

            {/* User Dropdown */}
            <Dropdown menu={{ items: userMenuItems }} trigger={["click"]}>
              <Button type="text" className="d-flex align-items-center ms-3">
                <UserOutlined className="" style={{ marginRight: -4 }} />
                {loading ? "Loading..." : `${capitalize(user.firstName)} ${capitalize(user.lastName)}`}
              </Button>
            </Dropdown>
          </div>
        </nav>
      </div>

      {/* Drawer for Smaller Screens */}
      <Drawer
        title="Menu"
        placement="right"
        onClose={() => setDrawerVisible(false)}
        open={drawerVisible}
      >
        <Menu
          items={[
            {
              key: "home",
              label: (
                <div
                  className={`userheader-nav-link${window.location.pathname === "/dashboard" ? " active" : ""}`}
                  onClick={() => {
                    setDrawerVisible(false);
                    navigate("/dashboard");
                  }}
                >
                  Home
                </div>
              ),
            },
            {
              key: "weather",
              label: (
                <div
                  className={`userheader-nav-link${window.location.pathname === "/weeklyforecast" ? " active" : ""}`}
                  onClick={() => {
                    setDrawerVisible(false);
                    navigate("/weeklyforecast");
                  }}
                >
                  Weather
                </div>
              ),
            },
            {
              key: "wardrobe",
              label: (
                <div
                  className={`userheader-nav-link${window.location.pathname === "/recommendedwardrobe" ? " active" : ""}`}
                  onClick={() => {
                    setDrawerVisible(false);
                    navigate("/recommendedwardrobe");
                  }}
                >
                  Wardrobe
                </div>
              ),
            },
            {
              key: "activity",
              label: (
                <div
                  className={`userheader-nav-link${window.location.pathname === "/recommendedactivity" ? " active" : ""}`}
                  onClick={() => {
                    setDrawerVisible(false);
                    navigate("/recommendedactivity");
                  }}
                >
                  Activity
                </div>
              ),
            },
            {
              type: "divider",
            },
            {
              key: "profile",
              label: (
                <div onClick={handleProfile}>
                  <UserOutlined /> User Profile
                </div>
              ),
            },
            {
              key: "logout",
              label: (
                <div onClick={handleLogout}>
                  <LogoutOutlined /> Logout
                </div>
              ),
            },
          ]}
        />
      </Drawer>
    </header>
  );
};

export default UserHeader;
