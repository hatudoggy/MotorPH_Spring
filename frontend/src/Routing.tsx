import { createBrowserRouter } from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Dashboard from "./pages/Dashboard";
import Attendance from "./pages/Attendance";
import Leave from "./pages/Leave";
import Payroll from "./pages/Payroll";
import Profile from "./pages/Profile";


export const router = createBrowserRouter([
  {
    path: "login",
    element: <Login />
  },
  {
    path: "/",
    element: <Main />,
    children: [
      {
        path: "dashboard",
        element: <Dashboard />
      },
      {
        path: "attendance",
        element: <Attendance />
      },
      {
        path: "leave",
        element: <Leave />
      },
      {
        path: "payroll",
        element: <Payroll />
      },
      {
        path: "profile",
        element: <Profile />
      },
    ]
  },
])