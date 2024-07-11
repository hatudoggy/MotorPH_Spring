import { createBrowserRouter } from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Dashboard from "./pages/employee/Dashboard";
import Attendance from "./pages/employee/Attendance";
import Payroll from "./pages/employee/Payroll";
import Profile from "./pages/employee/Profile";
import RoleRoute from "./components/RoleRoute";
import HRPayrolls from "./pages/hr/HRPayrolls";
import HRAttendances from "./pages/hr/HRAttendances";
import AdminUsers from "./pages/admin/AdminUsers";
import HREmployees from "./pages/hr/HREmployees";
import LeaveRequest from "./pages/employee/Leave";
import HRLeaveRequestManagement from "./pages/hr/HRLeaves.tsx";


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
        path: "payrollId",
        element: <Payroll />
      },
      {
        path: "profile",
        element: <Profile />
      },
      {
        path: "leave-request",
        element: <LeaveRequest />
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
            path: "leave-request",
            element: <HRLeaveRequestManagement />
          },
          {
            path: "payrollId",
            element: <HRPayrolls />
          },
          {
            path: "employee",
            element: <HREmployees />
          }
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