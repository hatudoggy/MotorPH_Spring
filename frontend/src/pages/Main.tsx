import { Box, CircularProgress } from "@mui/material";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import { useAuth } from "../hooks/AuthProvider";


export default function Main() {

  const {authUser, loading} = useAuth()
  const location = useLocation()

  if(loading) {
    return(
      <Box>
        <CircularProgress />
      </Box>
    )
  }
  
  if (authUser == null) {
    return(
      <Navigate to="login" replace state={{ from: location }} />
    )
  }

  const roles: Record<number, UserRole> = {
    1: "employee",
    2: "admin",
    3: "hr",
    4: "payroll"
  }

  const role = roles[authUser.roleId]

  return(
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex'
      }}
    >
      <Sidebar role={role}/>
      <Outlet />
    </Box>
  )
}