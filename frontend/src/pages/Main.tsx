import { Box } from "@mui/material";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";


export default function Main() {

  const role = 'admin'

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