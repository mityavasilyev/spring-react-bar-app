import React, { useEffect, useState } from "react";
import { Snackbar } from "@mui/material";

interface NotificationInterface {
  message: string;
}

const Notification: React.FC<NotificationInterface> = (props) => {

  const [displayNotification, setDisplayNotification] = useState(false);
  const [notificationMessage, setNotificationMessage] = useState("");

  useEffect(() => {
    if (props.message.length !== 0) {
      setNotificationMessage(props.message);
      setDisplayNotification(true);

      const timer = setTimeout(() => {
        //do stuff
        setDisplayNotification(false);
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [props.message])

  const handleNotificationClose = () => {
    setDisplayNotification(false);
  }

  return (
    <Snackbar
      anchorOrigin={{ vertical: "top", horizontal: "center" }}
      open={displayNotification}
      onClose={handleNotificationClose}
      message={notificationMessage}
    />
  );
};

export default Notification;
