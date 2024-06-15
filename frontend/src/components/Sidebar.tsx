import { AssignmentInd, AssignmentIndOutlined, AssignmentLate, AssignmentLateOutlined, Dashboard, DashboardOutlined, Paid, PaidOutlined, Person, PersonOutlined, UnfoldMore, Whatshot } from "@mui/icons-material";
import { Avatar, ListItemIcon, ListItemText, MenuItem, Paper, Select, Stack, SvgIconTypeMap, Typography } from "@mui/material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { Link, useLocation } from "react-router-dom";
import { Colors } from "../constants/Colors";


export default function Sidebar() {

  const employeeItems: SidebarItem[] = [
    {label: "Dashboard", link: "dashboard", IconActive: Dashboard, IconOutlined: DashboardOutlined},
    {label: "Attendance", link: "attendance", IconActive: AssignmentInd, IconOutlined: AssignmentIndOutlined},
    {label: "Leave", link: "leave", IconActive: AssignmentLate, IconOutlined: AssignmentLateOutlined},
    {label: "Payroll", link: "payroll", IconActive: Paid, IconOutlined: PaidOutlined},
    {label: "Profile", link: "profile", IconActive: Person, IconOutlined: PersonOutlined},
  ]

  const location = useLocation()
  const currentRoute = location.pathname.split('/')[1]

  return(
    <Paper
      variant="outlined"
      sx={{
        borderRadius: 0,
        flex: 'none',
        width: 230,
        height: '100vh',
        position: 'sticky',
        top: 0,
        borderRight: '1px solid lightgray'
      }}
    >
      <Stack 
        width='100%' 
        height='100%' 
        justifyContent='space-between'
        px={1.5}
        py={5}
      >
        <Stack gap={2}>
          <Typography variant="h5" fontWeight={500} textAlign='center'>MotorPH<Whatshot /></Typography>
          <Select
            defaultValue="employee"
            size="small"
            sx={{
              borderRadius: 2,
              mb: 2
            }}
            IconComponent={(props)=>(<UnfoldMore {...props} />)}
          >
            <MenuItem value="employee">Employee</MenuItem>
            <MenuItem value="hr">HR</MenuItem>
            <MenuItem value="admin">Admin</MenuItem>
          </Select>
          <Stack gap={0.5}>
            {
              employeeItems.map((item)=>
                <SidebarItem 
                  key={item.label}
                  active={item.link == currentRoute}
                  {...item}
                />
              )
            }
          </Stack>
        </Stack>

        <MenuItem
          sx={{
            borderRadius: 3,
            py: 1,
            px: 1,
            gap: 1.5,
          }}
        >
          <ListItemIcon>
            <Avatar />
          </ListItemIcon>
          <Stack maxWidth='100%'>
            <Typography>John Smith</Typography>
            <Typography variant="caption" noWrap>johnsmith@email.com</Typography>
          </Stack>
        </MenuItem>
      </Stack>
    </Paper>
  )
}

interface SidebarItem{
  label: string
  link: string
  active?: boolean
  IconActive: OverridableComponent<SvgIconTypeMap<{}, "svg">> & {muiName: string;}
  IconOutlined: OverridableComponent<SvgIconTypeMap<{}, "svg">> & {muiName: string;}
}

function SidebarItem({label, link, active, IconActive, IconOutlined }: SidebarItem) {

  return(
    <MenuItem 
      component={Link} 
      to={link}
      sx={{
        borderRadius: 3,
        py: 1,
        px: 1,
        bgcolor: active ? Colors.primary.main : null,
        color: active ? 'white' : null,
        '&:hover': {
          bgcolor: active ? Colors.primary.light : null,
        }
      }}
    >
      <ListItemIcon>
        {active ? <IconActive sx={{color: 'white'}} /> : <IconOutlined />}
      </ListItemIcon>
      <ListItemText>
        {label}
      </ListItemText>
    </MenuItem>
  )
}