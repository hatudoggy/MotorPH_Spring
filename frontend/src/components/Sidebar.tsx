import {
    AssignmentInd,
    AssignmentIndOutlined,
    Dashboard,
    DashboardOutlined,
    Group,
    GroupOutlined,
    Logout,
    Paid,
    PaidOutlined,
    Payments,
    PaymentsOutlined,
    Person,
    PersonOutlined,
    RecentActors,
    RecentActorsOutlined,
    UnfoldMore,
    Whatshot,
} from "@mui/icons-material";
import {
    Avatar,
    ListItemIcon,
    ListItemText,
    Menu,
    MenuItem,
    Paper,
    Select,
    Stack,
    SvgIconTypeMap,
    Typography,
} from "@mui/material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Colors } from "../constants/Colors";
import PopupState, { bindMenu, bindTrigger } from "material-ui-popup-state";
import { useEffect, useState } from "react";
import { useAuth } from "../hooks/AuthProvider";
import { useFetchEmployeeById } from "../api/query/UseFetch.ts";

interface Props {
    role: UserRole;
}

export default function Sidebar({ role }: Props) {
    const employeeItems: SidebarItem[] = [
        {
            label: "Dashboard",
            link: "dashboard",
            IconActive: Dashboard,
            IconOutlined: DashboardOutlined,
        },
        {
            label: "Attendance",
            link: "attendance",
            IconActive: AssignmentInd,
            IconOutlined: AssignmentIndOutlined,
        },
        { label: "Payroll", link: "payrollId", IconActive: Paid, IconOutlined: PaidOutlined },
        { label: "Profile", link: "profile", IconActive: Person, IconOutlined: PersonOutlined },
    ];

    const hrItems: SidebarItem[] = [
        {
            label: "Attendance",
            link: "hr/attendance",
            IconActive: RecentActors,
            IconOutlined: RecentActorsOutlined,
        },
        {
            label: "Payroll",
            link: "hr/payrollId",
            IconActive: Payments,
            IconOutlined: PaymentsOutlined,
        },
        { label: "Employees", link: "hr/employee", IconActive: Group, IconOutlined: GroupOutlined },
    ];

    const adminItems: SidebarItem[] = [
        { label: "Users", link: "admin/users", IconActive: Dashboard, IconOutlined: DashboardOutlined },
    ];

    const location = useLocation();
    const navigate = useNavigate();
    const currentRoute = location.pathname.split("/").splice(1, 2).join("/");

    const [currentView, setCurrentView] = useState(() => {
        return localStorage.getItem("currentView") || "employee";
    });

    useEffect(() => {
        localStorage.setItem("currentView", currentView);
    }, [currentView]);

    const viewSwitch = () => {
        const render = (list: SidebarItem[]) => {
            return list.map((item) => (
                <SidebarItem key={item.link} active={item.link === currentRoute} {...item} />
            ));
        };
        switch (currentView) {
            case "employee":
                return render(employeeItems);
            case "hr":
                return render(hrItems);
            case "admin":
                return render(adminItems);
            default:
                return render(employeeItems);
        }
    };

    const handleViewChange = (view: string) => {
        setCurrentView(view);
        switch (view) {
            case "employee":
                navigate("dashboard");
                break;
            case "hr":
                navigate("hr/attendance");
                break;
            case "admin":
                navigate("admin/users");
                break;
        }
    };

    return (
        <Paper
            variant="outlined"
            sx={{
                borderRadius: 0,
                flex: "none",
                width: 230,
                height: "100vh",
                position: "sticky",
                top: 0,
                borderRight: "1px solid lightgray",
            }}
        >
            <Stack width="100%" height="100%" justifyContent="space-between" px={1.5} py={5}>
                <Stack gap={2}>
                    <Typography variant="h5" fontWeight={500} textAlign="center">
                        MotorPH<Whatshot />
                    </Typography>
                    <Select
                        value={currentView}
                        size="small"
                        sx={{
                            borderRadius: 2,
                            mb: 2,
                        }}
                        IconComponent={(props) => <UnfoldMore {...props} />}
                        onChange={(e) => handleViewChange(e.target.value)}
                    >
                        <MenuItem value="employee">Employee</MenuItem>
                        {role === "hr" && <MenuItem value="hr">HR</MenuItem>}
                        {role === "admin" && <MenuItem value="hr">HR</MenuItem>}
                        {role === "admin" && <MenuItem value="admin">Admin</MenuItem>}
                    </Select>
                    <Stack gap={0.5}>{viewSwitch()}</Stack>
                </Stack>

                <AccountButton />
            </Stack>
        </Paper>
    );
}

interface SidebarItem {
    label: string;
    link: string;
    active?: boolean;
    IconActive: OverridableComponent<SvgIconTypeMap<{}, "svg">> & { muiName: string };
    IconOutlined: OverridableComponent<SvgIconTypeMap<{}, "svg">> & { muiName: string };
}

function SidebarItem({ label, link, active, IconActive, IconOutlined }: SidebarItem) {
    return (
        <MenuItem
            component={Link}
            to={link}
            sx={{
                borderRadius: 3,
                py: 1,
                px: 1,
                bgcolor: active ? Colors.primary.main : null,
                color: active ? "white" : null,
                "&:hover": {
                    bgcolor: active ? Colors.primary.light : null,
                },
            }}
        >
            <ListItemIcon>{active ? <IconActive sx={{ color: "white" }} /> : <IconOutlined />}</ListItemIcon>
            <ListItemText>{label}</ListItemText>
        </MenuItem>
    );
}

function AccountButton() {
    const { logout, authUser } = useAuth();
    const employeeId = authUser?.employeeId;

    if (!employeeId) {
        console.error("Employee ID not found");
        return <Typography>Error: Employee ID not found</Typography>;
    }

    const { data } = useFetchEmployeeById(employeeId);

    const handleLogout = () => {
        logout();
    };

    return (
        <PopupState variant="popover">
            {(popstate) => (
                <>
                    <MenuItem
                        sx={{
                            borderRadius: 3,
                            py: 1,
                            px: 1,
                            gap: 1.5,
                        }}
                        {...bindTrigger(popstate)}
                    >
                        <ListItemIcon>
                            <Avatar />
                        </ListItemIcon>
                        <Stack maxWidth="100%">
                            <Typography noWrap maxWidth={150}>
                                {`${data?.firstName.split(" ")[0]} ${data?.lastName}`}
                            </Typography>
                            <Typography variant="caption" noWrap>
                                {`${data?.position.positionName}`}
                            </Typography>
                        </Stack>
                    </MenuItem>
                    <Menu
                        anchorOrigin={{
                            vertical: "top",
                            horizontal: "center",
                        }}
                        transformOrigin={{
                            vertical: "bottom",
                            horizontal: "center",
                        }}
                        {...bindMenu(popstate)}
                    >
                        <MenuItem
                            sx={{
                                minWidth: 180,
                            }}
                            onClick={() => {
                                handleLogout();
                                popstate.close();
                            }}
                        >
                            <ListItemIcon>
                                <Logout />
                            </ListItemIcon>
                            <ListItemText>Logout</ListItemText>
                        </MenuItem>
                    </Menu>
                </>
            )}
        </PopupState>
    );
}
