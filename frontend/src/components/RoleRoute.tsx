import { Outlet } from "react-router-dom";


interface Props {
  roleType: 'employee' | 'hr' | 'admin'
}

export default function RoleRoute({roleType}: Props) {

  return(
    <>
    <Outlet />
    </>
  )
}