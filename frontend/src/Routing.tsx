import { createBrowserRouter } from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Dashboard from "./pages/employee/Dashboard";
import Attendance from "./pages/employee/Attendance";
import Leave from "./pages/employee/Leave";
import Payroll from "./pages/employee/Payroll";
import Profile from "./pages/employee/Profile";
import RoleRoute from "./components/RoleRoute";
import HRPayrolls from "./pages/hr/HRPayrolls";
import HRAttendances from "./pages/hr/HRAttendances";
import AdminUsers from "./pages/admin/AdminUsers";
import HREmployees from "./pages/hr/HREmployees";


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
      {
        path: "hr",
        element: <RoleRoute roleType="hr" />,
        children: [
          {
            path: "attendance",
            element: <HRAttendances />
          },
          {
            path: "payroll",
            element: <HRPayrolls />
          },
          {
            path: "employee",
            element: <HREmployees />
          },
        ]
      },
      {
        path: "admin",
        element: <RoleRoute roleType="admin" />,
        children: [
          {
            path: "users",
            element: <AdminUsers />
          }
        ]
      },
    ]
  },
])