import {
    AccessTime,
    Circle,
    MoreTime,
    NorthEast,
    SouthWest,
    TodayOutlined, WorkHistory
} from "@mui/icons-material";
import { Box, Button, Container, Divider, Paper, PaperProps, Stack, SvgIconTypeMap, Typography, styled } from "@mui/material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { PieChart } from "@mui/x-charts";
import Headertext from "../../components/HeaderText";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import {format, subMonths} from "date-fns";
import { useAuth } from "../../hooks/AuthProvider";
import Table from "../../components/Table";
import {useFetchAttendanceByEmployeeIdAndDate, useFetchAttendancesByEmployeeId} from "../../api/query/UseFetch.ts";
import {useEffect, useState} from "react";
import {postTimeIn, putTimeOut} from "../../api/post/PostService.ts";

let employeeId: number = 0;

// Main Attendance component
export default function Attendance() {
    const { authUser } = useAuth();
    employeeId = authUser?.employeeId || 0;

    const [selectedFilter, setSelectedFilter] = useState<string>("This Month");
    const [filteredData, setFilteredData] = useState<AttendanceRes[]>([]);

    if (!employeeId || employeeId === 0) {
        console.error("Employee ID not found");
        return <Typography>Error: Employee ID not found</Typography>;
    }

    const { data, isPending } = useFetchAttendancesByEmployeeId(employeeId);

    useEffect(() => {
        if (data) {
            const filterData = (data: AttendanceRes[], filter: string) => {
                const now = new Date();
                let filteredData = data;

                if (filter === "This Month") {
                    filteredData = data.filter((attendance) =>
                        new Date(attendance.date) >= subMonths(now, 1)
                    );
                } else if (filter === "3 Months") {
                    filteredData = data.filter((attendance) =>
                        new Date(attendance.date) >= subMonths(now, 3)
                    );
                } else if (filter === "6 Months") {
                    filteredData = data.filter((attendance) =>
                        new Date(attendance.date) >= subMonths(now, 6)
                    );
                } // No need for "All Time" filter, as it will display all data

                return filteredData;
            };

            setFilteredData(filterData(data, selectedFilter));
        }
    }, [data, selectedFilter]);

    return (
        <Container sx={{ my: 5 }}>
            <Stack height='100%'>
                <Headertext>Attendance</Headertext>
                <Stack flex={1} gap={3}>
                    <WidgetBar filteredData={filteredData} selectedFilter={selectedFilter} />
                    <Stack flex={1} gap={1}>
                        <MonthFilter selectedFilter={selectedFilter} setSelectedFilter={setSelectedFilter} />
                        <AttendanceTable filteredData={filteredData} isPending={isPending} />
                    </Stack>
                </Stack>
            </Stack>
        </Container>
    );
}

// WidgetBar component containing AttendanceToday and AttendanceOverview
function WidgetBar({filteredData, selectedFilter}: AttendanceOverviewProps) {
    console.log("Rendering WidgetBar");

    return(
        <Stack direction='row' gap={2}>

            <AttendanceToday />
            <AttendanceOverview filteredData={filteredData} selectedFilter={selectedFilter}/>
        </Stack>
    )
}

// AttendanceToday component for displaying and managing daily attendance
function AttendanceToday() {
    console.log("Rendering AttendanceToday");

    const queryClient = useQueryClient();
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);
    const [loadingTimeIn, setLoadingTimeIn] = useState(false);
    const [loadingTimeOut, setLoadingTimeOut] = useState(false);
    const [clockedIn, setClockedIn] = useState(false);

    const todayDate = format(new Date().setDate(29), 'yyyy-MM-dd');

    const { data: attendanceData, refetch: refetchAttendanceData } = useFetchAttendanceByEmployeeIdAndDate(employeeId, todayDate);

    const useTimeIn = useMutation({
        mutationFn: async () => {
            setLoadingTimeIn(true);
            await postTimeIn(employeeId);
        },
        onError: () => {
            setClockedIn(false);
            setLoadingTimeIn(false);
            setIsButtonDisabled(false);
        },
        onSettled: async () => {
            setLoadingTimeIn(false);
            setIsButtonDisabled(false);
            await refetchAttendanceData();
            await queryClient.invalidateQueries(['attendancesByEmployeeId']);
        }
    });

    const useTimeOut = useMutation({
        mutationFn: async () => {
            setLoadingTimeOut(true);
            await putTimeOut(employeeId);
        },
        onError: () => {
            setLoadingTimeOut(false);
            setIsButtonDisabled(false);
        },
        onSettled: async () => {
            setLoadingTimeOut(false);
            await refetchAttendanceData();
            await queryClient.invalidateQueries(['attendancesByEmployeeId']);
        }
    });

    const handleClockIn = () => {
        console.log("Clocking in");
        setClockedIn(true);
        setIsButtonDisabled(true);
        useTimeIn.mutate();
    };

    const handleClockOut = () => {
        console.log("Clocking out");
        setIsButtonDisabled(true);
        useTimeOut.mutate();
    };

    return (
        <Widget
            variant="outlined"
            sx={{
                minWidth: 250
            }}
        >
            <Typography variant="body2" fontWeight={600}>Attendance Today</Typography>
            <Stack width='100%' mt={0.5}>
                <Stack flex={1} direction='row' py={0.5} pb={1.5}>
                    <Stack flex={1} alignItems='center' pb={1}>
                        <Typography variant="body2" color='GrayText'>Time In</Typography>
                        <Typography variant="h4">
                        {loadingTimeIn ? "..." : attendanceData?.timeIn?.substring(0, 5) || "-- : --"}
                    </Typography>
                </Stack>
                <Divider flexItem orientation="vertical" />
                <Stack flex={1} alignItems='center' pb={1}>
                    <Typography variant="body2" color='GrayText'>Time Out</Typography>
                    <Typography variant="h4">
                        {loadingTimeOut ? "..." : attendanceData?.timeOut?.substring(0, 5) || "-- : --"}
                    </Typography>
                </Stack>
            </Stack>
            <Stack flex={1} direction='row'>
                {attendanceData?.timeIn ? (
                    <Button
                        fullWidth
                        variant="contained"
                        disableElevation
                        disabled={isButtonDisabled || attendanceData.timeOut !== null}
                        onClick={handleClockOut}
                    >
                        Clock Out
                    </Button>
                ) : (
                    <Button
                        fullWidth
                        variant="contained"
                        disableElevation
                        disabled={isButtonDisabled || clockedIn}
                        onClick={handleClockIn}
                    >
                        Clock In
                    </Button>
                )}
            </Stack>
            </Stack>
        </Widget>
    );
}


interface AttendanceOverviewProps {
    filteredData: AttendanceRes[];
    selectedFilter: string
}

// AttendanceOverview component for displaying monthly attendance summary
function AttendanceOverview({ filteredData, selectedFilter}: AttendanceOverviewProps) {

    const palette = ['#000000', '#575757', '#c4c4c4']

    const calculateSummary = (data: AttendanceRes[]) => {
        const summary = {
            totalCount: data.length,
            presentCount: 0,
            lateCount: 0,
            absentCount: 0,
            averageTimeIn: "",
            averageTimeOut: "",
            totalHours: 0,
            totalOvertime: 0
        };

        let totalTimeIn = 0;
        let totalTimeOut = 0;
        let timeInCount = 0;
        let timeOutCount = 0;

        data.forEach((attendance) => {
            const status = statusGenerator(attendance.timeIn, attendance.timeOut);
            if (status === "Present") summary.presentCount++;
            if (status === "Late") summary.lateCount++;
            if (status === "Absent") summary.absentCount++;

            if (attendance.timeIn) {
                const [hours, minutes] = attendance.timeIn.split(':').map(Number);
                totalTimeIn += hours * 60 + minutes;
                timeInCount++;
            }
            if (attendance.timeOut) {
                const [hours, minutes] = attendance.timeOut.split(':').map(Number);
                totalTimeOut += hours * 60 + minutes;
                timeOutCount++;
            }

            summary.totalHours += attendance.totalHours;
            summary.totalOvertime += attendance.overtimeHours;
        });

        if (timeInCount > 0) {
            const averageTimeInMinutes = totalTimeIn / timeInCount;
            const averageHours = Math.floor(averageTimeInMinutes / 60);
            const averageMinutes = Math.floor(averageTimeInMinutes % 60);
            summary.averageTimeIn = `${averageHours.toString().padStart(2, '0')}:${averageMinutes.toString().padStart(2, '0')}`;
        }

        if (timeOutCount > 0) {
            const averageTimeOutMinutes = totalTimeOut / timeOutCount;
            const averageHours = Math.floor(averageTimeOutMinutes / 60);
            const averageMinutes = Math.floor(averageTimeOutMinutes % 60);
            summary.averageTimeOut = `${averageHours.toString().padStart(2, '0')}:${averageMinutes.toString().padStart(2, '0')}`;
        }

        return summary;
    };

    const summary = calculateSummary(filteredData);

    return(
        <Widget variant="outlined">
            <Typography variant="body2" fontWeight={600}>{selectedFilter}</Typography>
            <Stack direction='row' alignItems='center' gap={3}>
                {
                    <>
                        <PieChart
                            colors={palette}
                            series={[
                                {
                                    data: [
                                        { id: 0, value: summary.presentCount, label: 'present'},
                                        { id: 1, value: summary.lateCount, label: 'late'},
                                        { id: 2, value: summary.absentCount, label: 'absent'},
                                    ],
                                    innerRadius: 39,
                                    outerRadius: 50,
                                    paddingAngle: 2,
                                    cornerRadius: 3,
                                    cx: 55,
                                    cy: 55,
                                }
                            ]}
                            width={120}
                            height={120}
                            slotProps={{
                                legend: { hidden: true },
                            }}
                        >

                        </PieChart>

                        <Stack gap={1.5}>
                            <Stack direction='row' alignItems={'center'} gap={8}>
                                <IconLabel
                                    Icon={TodayOutlined}
                                    value={summary.presentCount.toString()}
                                    label="Present"
                                />
                                <IconLabel
                                    Icon={AccessTime}
                                    value={summary.lateCount.toString()}
                                    label="Late"
                                />
                                <IconLabel
                                    Icon={WorkHistory}
                                    value={(Math.round(summary.totalHours * 100) / 100).toString()}
                                    label={"Rendered Hours"}
                                />
                            </Stack>
                            <Stack direction='row' alignItems={'center'} gap={3}>
                                <IconLabel
                                    Icon={SouthWest}
                                    value={summary.averageTimeIn.substring(0, 5)}
                                    label="Avg Check In"
                                />
                                <IconLabel
                                    Icon={NorthEast}
                                    value={summary.averageTimeOut.substring(0, 5)}
                                    label="Avg Check Out"
                                />
                                <IconLabel
                                    Icon={MoreTime}
                                    value={(Math.round(summary.totalOvertime * 100) / 100).toString()}
                                    label={"Total Overtime"}
                                />
                            </Stack>
                        </Stack>
                    </>
                }


            </ Stack>
        </Widget>
    )
}

// IconLabel component for displaying icon with label and value
interface IconLabel {
    Icon?: OverridableComponent<SvgIconTypeMap<{}, "svg">> & { muiName: string; };
    value: string;
    label: string;
}


function IconLabel({Icon, value, label}: IconLabel) {

    return(
        <Stack direction='row'>
            <Box
                sx={{
                    borderRadius: 50,
                    bgcolor: '#ebebeb',
                    width: 40,
                    height: 40,
                    display: 'grid',
                    placeContent: 'center'
                }}
            >
                {
                    Icon &&
                    <Icon sx={{color: '#4d4d4d'}}/>
                }
            </Box>
            <Stack alignItems='center' px={1}>
                <Typography variant="h4" fontSize={30} fontWeight={500}>{value}</Typography>
                <Typography variant="body2" color="GrayText">{label}</Typography>
            </Stack>
        </Stack>
    )
}

interface MonthFilterProps {
    selectedFilter: string;
    setSelectedFilter: (filter: string) => void;
}

function MonthFilter({ selectedFilter, setSelectedFilter }: MonthFilterProps) {
    console.log("Rendering MonthFilter");

    const filters = ["This Month", "3 Months", "6 Months", "All Time"];

    return (
        <Stack direction='row' gap={1} px={1}>
            {filters.map((filter) => (
                <MonthFilterButton
                    key={filter}
                    label={filter}
                    active={selectedFilter === filter}
                    onClick={() => setSelectedFilter(filter)}
                />
            ))}
        </Stack>
    );
}


// MonthFilterButton component for individual filter buttons
interface MonthFilterButton {
    label: string
    active: boolean
    onClick?: () => void
}

function MonthFilterButton({label, active, onClick}: MonthFilterButton) {

    return(
        <Button
            variant={active ? "contained" : "text"}
            sx={{
                borderRadius: 4,
                px: 1.5,
                textTransform: 'capitalize'
            }}
            disableElevation
            size="small"
            onClick={onClick}
        >
            {label}
        </Button>
    )
}

interface AttendanceTableProps {
    filteredData: AttendanceRes[]
    isPending: boolean
}

const statusGenerator = (timeIn: string, timeOut: string) => {
    if(timeOut){
        if(timeIn) {
            if(timeIn < "08:15:00") {
                return "Present"
            } else {
                return "Late"
            }
        } else {
            return "Absent"
        }
    } else {
        return "N/A"
    }
}

// AttendanceTable component for displaying attendance records
function AttendanceTable({ filteredData, isPending }: AttendanceTableProps) {
    console.log("Rendering AttendanceTable");

    // Helper functions for data processing
    const statusColor: Record<string, string> = {
        "N/A": "#666666",
        "Present": "#67f596",
        "Late": "#e8dd8b",
        "Absent": "#f56767",
    }

    // Process table data
    const tableData = filteredData
        .map(({attendanceId, employeeId, ...rest})=> ({
            date: rest.date,
            timeIn: rest.timeIn.substring(0,5),
            timeOut: rest.timeOut?.substring(0,5) || "-- : --",
            status: statusGenerator(rest.timeIn, rest.timeOut),
            overtime: Math.round(rest.overtimeHours * 100) / 100,
            totalHours: Math.round(rest.totalHours * 100) / 100
        }))

    return(
        <Widget
            variant="outlined"
            sx={{
                flex: 1,
                maxHeight: 500
            }}
        >
            <Table
                colHeader={[
                    "Date",
                    "Time In",
                    "Time Out",
                    "Status",
                    "Overtime",
                    "Total Hours",
                ]}
                tableData={tableData || []}
                rowHeight={55}
                renderers={{
                    status: (item: string) => (
                        <Stack direction='row' alignItems='center' gap={0.5}>
                            <Circle sx={{pb: 0.4, fontSize: 16, color: statusColor[item]}} />
                            <Typography>
                                {item}
                            </Typography>
                        </Stack>
                    )
                }}
                loading={isPending}
            />

        </Widget>
    )
}

// Styled Widget component
const Widget = styled(Paper)<PaperProps>(({}) => ({
    borderRadius: 12,
    padding: 16,
}))






