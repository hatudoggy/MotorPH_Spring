import {
    Avatar,
    Card,
    CardActionArea,
    CardContent, Chip, CircularProgress,
    IconButton,
    ListItemIcon,
    ListItemText,
    Menu,
    MenuItem, Paper,
    Stack, Typography
} from "@mui/material";
import {Shadows} from "../../../constants/Shadows.js";
import PopupState, {bindMenu, bindTrigger} from "material-ui-popup-state";
import {Edit, MoreVert} from "@mui/icons-material";
import Labeled from "../../../components/Labeled.js";
import {format} from "date-fns";


interface EmployeeCard {
    name: string
    position: string
    department: string
    hireDate: string
    status: EmploymentStatusRes
    onClick: () => void
    onEdit: () => void
    isLoading: boolean;
    isSelected: boolean;
}


export default function EmployeeCard({
                                         name,
                                         position,
                                         department,
                                         hireDate,
                                         status,
                                         onClick,
                                         onEdit,
                                         isLoading,
                                         isSelected
                                     }: EmployeeCard) {

    const statusColor: Record<number, string> = {
        1: "#66bb6a", //Green
        2: "#9f66bb", //Purple
        3: "#bba766", //Yellow
        4: "#667ebb", //Blue
        5: "#bb6666", //Red
        6: "#9e9e9e", //Gray
    }

    const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s"

    // console.log(`Rendering EmployeeCard for ${name}, isLoading: ${isLoading}, isSelected: ${isSelected}`);

    return (
        <Card
            sx={{
                borderRadius: 3,
                boxShadow: Shadows.l1,
                position: "relative",
            }}
        >
            <Stack
                direction="row"
                justifyContent="end"
                gap={0.5}
                position="absolute"
                right={5}
                top={9}
                zIndex={5}
            >
                <PopupState variant="popover">
                    {(popstate) => (
                        <>
                            <IconButton size="small" {...bindTrigger(popstate)}>
                                <MoreVert fontSize="small" />
                            </IconButton>
                            <Menu
                                anchorOrigin={{
                                    vertical: "bottom",
                                    horizontal: "right",
                                }}
                                transformOrigin={{
                                    vertical: "top",
                                    horizontal: "right",
                                }}
                                {...bindMenu(popstate)}
                            >
                                <MenuItem
                                    sx={{
                                        minWidth: 130,
                                    }}
                                    onClick={() => {
                                        onEdit();
                                        popstate.close();
                                    }}
                                >
                                    <ListItemIcon>
                                        <Edit />
                                    </ListItemIcon>
                                    <ListItemText>Edit</ListItemText>
                                </MenuItem>
                            </Menu>
                        </>
                    )}
                </PopupState>
            </Stack>
            <CardActionArea onClick={onClick}>
                <CardContent
                    sx={{
                        width: 230,
                        p: 1.5,
                        position: "relative", // Ensure content area is relative for absolute loading indicator
                    }}
                >
                    {isLoading && isSelected && (
                        <Stack
                            alignItems="center"
                            justifyContent="center"
                            sx={{
                                position: "absolute",
                                top: 0,
                                left: 0,
                                width: "100%",
                                height: "100%",
                                bgcolor: "rgba(255, 255, 255, 0.8)",
                                zIndex: 10,
                            }}
                        >
                            <CircularProgress />
                        </Stack>
                    )}

                    {/* Content when not loading */}
                    {!isLoading && (
                        <>
                            <Stack
                                direction="row"
                                justifyContent="end"
                                mr={3}
                            >
                                <Chip
                                    size="small"
                                    label={status.statusName}
                                    sx={{
                                        color: "white",
                                        bgcolor: statusColor[status.statusId],
                                        opacity: 0.8,
                                    }}
                                />
                            </Stack>
                            <Stack alignItems="center" gap={1}>
                                <Avatar
                                    sx={{
                                        width: 100,
                                        height: 100,
                                    }}
                                    src={picURL}
                                />
                                <Stack alignItems="center">
                                    <Typography
                                        variant="body2"
                                        fontSize={20}
                                        fontWeight={500}
                                        noWrap
                                    >
                                        {name}
                                    </Typography>
                                    <Typography
                                        variant="body2"
                                        fontSize={15}
                                        color="GrayText"
                                        noWrap
                                    >
                                        {position}
                                    </Typography>
                                </Stack>

                                <Paper
                                    variant="outlined"
                                    sx={{
                                        border: "none",
                                        borderRadius: 3,
                                        width: "100%",
                                        p: 1.5,
                                        bgcolor: "#f5f5f5",
                                    }}
                                >
                                    <Stack width="100%" gap={1.5}>
                                        <Stack
                                            direction="row"
                                            justifyContent="space-between"
                                        >
                                            <Labeled label="Department">
                                                <Typography
                                                    fontWeight={500}
                                                    noWrap
                                                    maxWidth={100}
                                                >
                                                    {department}
                                                </Typography>
                                            </Labeled>
                                            <Labeled label="Hire Date">
                                                <Typography
                                                    fontWeight={500}
                                                    noWrap
                                                >
                                                    {format(
                                                        hireDate,
                                                        "MMM dd, yyyy"
                                                    )}
                                                </Typography>
                                            </Labeled>
                                        </Stack>
                                    </Stack>
                                </Paper>
                            </Stack>
                        </>
                    )}
                </CardContent>
            </CardActionArea>
        </Card>
    );
}
