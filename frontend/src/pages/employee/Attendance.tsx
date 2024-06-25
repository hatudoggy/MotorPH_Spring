import { AccessTime, Circle, EventBusy, EventBusyOutlined, NorthEast, SouthWest, Today, TodayOutlined } from "@mui/icons-material";
import { Box, Button, CircularProgress, Container, Divider, Paper, PaperProps, Stack, SvgIconTypeMap, Typography, styled } from "@mui/material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { PieChart, useDrawingArea } from "@mui/x-charts";
import { Colors } from "../../constants/Colors";
import Grid from "@mui/material/Unstable_Grid2/Grid2";
import Headertext from "../../components/HeaderText";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { API, BASE_API } from "../../constants/Api";
import axios from "axios";
import { format } from "date-fns";
import { useAuth } from "../../hooks/AuthProvider";
import Table from "../../components/Table";


// Main Attendance component
export default function Attendance() {
    console.log("Rendering Attendance component");

    return(
        <Container
            sx={{
                my: 5,
            }}
        >
            <Stack height='100%'>

                <Headertext>Attendance</Headertext>

                <Stack flex={1} gap={3}>
                    <WidgetBar />

                    <Stack flex={1} gap={1}>
                        <MonthFilter />
                        <AttendanceTable />
                    </Stack>

                </Stack>

            </Stack>
        </Container>
    )
}

// WidgetBar component containing AttendanceToday and AttendanceOverview
function WidgetBar() {
    console.log("Rendering WidgetBar");

    return(
        <Stack direction='row' gap={2}>

            <AttendanceToday />
            <AttendanceOverview />

        </Stack>
    )
}

// AttendanceToday component for displaying and managing daily attendance
function AttendanceToday() {
    console.log("Rendering AttendanceToday");

    const {authUser} = useAuth()
    const employeeId = authUser?.employeeId

    const queryClient = useQueryClient()

    // Fetch today's attendance data
    const fetchAttendanceToday = async() => {
        const {EMPLOYEES, ATTENDANCES} = API
        const dateToday = format(new Date(), 'yyyy-MM-dd')

        console.log("Fetching attendance for date:", dateToday);

        const { data } = await axios.get(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.BASE}?startDate=${dateToday}`);
        return data
    }

    const {data} = useQuery<AttendanceRes[]>({
        queryKey: ['attendanceToday'],
        queryFn: fetchAttendanceToday,
    })

    // Time in mutation
    const useTimeIn = useMutation({
        mutationFn: async () => {
            const { EMPLOYEES, ATTENDANCES } = API;
            const res = await axios.post(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.TIME_IN}`);
            return res.data;
        },
        onSettled: async () => {
            return await queryClient.invalidateQueries({ queryKey: ['attendanceToday'] });
        }
    });

    // Time out mutation
    const useTimeOut = useMutation({
        mutationFn: async () => {
            const { EMPLOYEES, ATTENDANCES } = API;
            const res = await axios.post(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.TIME_OUT}`);
            return res.data;
        },
        onSettled: async () => {
            return await queryClient.invalidateQueries({ queryKey: ['attendanceToday'] });
        }
    });

    const handleClockIn = () => {
        console.log("Clocking in");
        useTimeIn.mutate();
    };

    const handleClockOut = () => {
        console.log("Clocking out");
        useTimeOut.mutate();
    };

    return(
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
                        <Typography variant="h4">{data && data[0]?.timeIn?.substring(0,5) || "-- : --"}</Typography>
                    </Stack>
                    <Divider flexItem orientation="vertical" />
                    <Stack flex={1} alignItems='center' pb={1}>
                        <Typography variant="body2" color='GrayText'>Time Out</Typography>
                        <Typography variant="h4">{data && data[0]?.timeOut?.substring(0,5) || "-- : --"}</Typography>
                    </Stack>
                </Stack >
                <Stack flex={1} direction='row'>
                    {
                        data && data[0]?.timeIn ?
                            <Button
                                fullWidth
                                variant="contained"
                                disableElevation
                                onClick={()=>handleClockOut()}
                                disabled={data[0].timeOut !== null}
                            >
                                Clock Out
                            </Button>
                            :
                            <Button
                                fullWidth
                                variant="contained"
                                disableElevation
                                onClick={()=>handleClockIn()}
                            >
                                Clock In
                            </Button>
                    }
                </Stack>
            </Stack>
        </Widget>
    )
}

// AttendanceOverview component for displaying monthly attendance summary
function AttendanceOverview() {

    const {authUser} = useAuth()
    const employeeId = authUser?.employeeId

    const palette = ['#000000', '#575757', '#c4c4c4']

    const fetchAttendanceSummary = async() => {
        const {EMPLOYEES, ATTENDANCES} = API

        const { data } = await axios.get(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.SUMMARY}`);
        return data
    }

    // Fetch attendance summary data
    const {data} = useQuery<AttendanceSummaryRes>({
        queryKey: ['attendanceSummary'],
        queryFn: fetchAttendanceSummary,
    })


    return(
        <Widget variant="outlined">
            <Typography variant="body2" fontWeight={600}>This Month</Typography>
            <Stack direction='row' alignItems='center' gap={3}>
                {
                    data &&
                    <>
                        <PieChart
                            colors={palette}
                            series={[
                                {
                                    data: [
                                        { id: 0, value: data.presentCount, label: 'present'},
                                        { id: 1, value: data.lateCount, label: 'late'},
                                        { id: 2, value: data.absentCount, label: 'absent'},
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
                            {/* <PieCenterLabel>
                  Day 12
                </PieCenterLabel> */}
                        </PieChart>

                        <Stack gap={1.5}>
                            <Stack direction='row' alignItems='center' gap={3}>
                                <IconLabel
                                    Icon={TodayOutlined}
                                    value={data.presentCount.toString()}
                                    label="Present"
                                />
                                <IconLabel
                                    Icon={AccessTime}
                                    value={data.lateCount.toString()}
                                    label="Late"
                                />
                                <IconLabel
                                    Icon={EventBusyOutlined}
                                    value={data.absentCount.toString()}
                                    label="Absent"
                                />
                            </Stack>

                            <Stack direction='row' alignItems='center' gap={4}>
                                <IconLabel
                                    Icon={SouthWest}
                                    value={data.averageTimeIn.substring(0, 5)}
                                    label="Avg Check In"
                                />
                                <IconLabel
                                    Icon={NorthEast}
                                    value={data.averageTimeOut.substring(0, 5)}
                                    label="Avg Check Out"
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


// const StyledText = styled('text')(({ theme }) => ({
//   fill: theme.palette.text.primary,
//   textAnchor: 'middle',
//   dominantBaseline: 'central',
//   fontSize: 20,
// }));

// function PieCenterLabel({ children }: { children: React.ReactNode }) {
//   const { height, top } = useDrawingArea();
//   return (
//     <StyledText x={120 / 2} y={top + height / 2}>
//       {children}
//     </StyledText>
//   );
// }

// MonthFilter component for filtering attendance data
function MonthFilter() {
    console.log("Rendering MonthFilter");

    return(
        <Stack direction='row' gap={1} px={1}>
            <MonthFilterButton
                label="This Month"
                active={true}
            />
            <MonthFilterButton
                label="3 Months"
                active={false}
            />
            <MonthFilterButton
                label="6 Months"
                active={false}
            />
            <MonthFilterButton
                label="All Time"
                active={false}
            />
        </Stack>
    )
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

// AttendanceTable component for displaying attendance records
function AttendanceTable() {
    console.log("Rendering AttendanceTable");

    const {authUser} = useAuth()
    const employeeId = authUser?.employeeId

    // Fetch attendance data
    const fetchAttendance = async() => {
        const {EMPLOYEES, ATTENDANCES} = API
        const res = await axios.get(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.BASE}`);
        return res.data;
    }

    const {isPending, data} = useQuery<AttendanceRes[]>({
        queryKey: ['attendances'],
        queryFn: fetchAttendance
    })

    // Helper functions for data processing
    const isSameDate = (dateString: string, dateToCompare: Date) => {
        const date = new Date(dateString)
        return (
            date.getFullYear() === dateToCompare.getFullYear() &&
            date.getMonth() === dateToCompare.getMonth() &&
            date.getDate() === dateToCompare.getDate()
        );
    }

    const filterAttendanceByToday = (attendanceList: AttendanceRes[]) => {
        if (attendanceList.length === 0) {
            return attendanceList
        }

        const firstItemDate = attendanceList[0].date
        const today = new Date()

        if (isSameDate(firstItemDate, today)) {
            return attendanceList.slice(1)
        }

        return attendanceList
    }


    const statusColor: Record<string, string> = {
        "N/A": "#666666",
        "Present": "#67f596",
        "Late": "#e8dd8b",
        "Absent": "#f56767",
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

    // Process table data
    const tableData = data && data
        .map(({attendanceId, employeeId, ...rest})=> ({
            date: rest.date,
            timeIn: rest.timeIn.substring(0,5),
            timeOut: rest.timeOut.substring(0,5),
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
            {/* <Stack height='100%' minHeight='min-content'>
        <TableHeader />
        <Stack
          flex={1}
          sx={{
            overflowY: 'scroll',
            overflowX: 'hidden',
          }}
        >
          {
            !isPending ?
              data && filterAttendanceByToday(data)?.map((row) =>
                <TableRow
                  key={row.attendanceId}
                  {...row}
                />
              )
              :
              <Stack alignItems='center'>
                <CircularProgress />
              </Stack>
          }
        </Stack>
      </Stack> */}
        </Widget>
    )
}

function TableHeader() {

    return(
        <>
            <Grid container spacing={1} px={1} pb={1.5} pr={2.9}>
                <Grid xs={2}>
                    <Typography variant="body2" fontWeight={500} color="GrayText">Date</Typography>
                </Grid>
                <Grid xs={2}>
                    <Typography variant="body2" fontWeight={500} color="GrayText">Time In</Typography>
                </Grid>
                <Grid xs={2}>
                    <Typography variant="body2" fontWeight={500} color="GrayText">Time Out</Typography>
                </Grid>
                <Grid xs={2}>
                    <Typography variant="body2" fontWeight={500} color="GrayText">Status</Typography>
                </Grid>
                <Grid xs={2}>
                    <Typography variant="body2" fontWeight={500} color="GrayText">Overtime</Typography>
                </Grid>
                <Grid xs={2}>
                    <Typography variant="body2" fontWeight={500} color="GrayText">Total Hours</Typography>
                </Grid>
            </Grid>
            <Divider sx={{mb: 0.5}}/>
        </>
    )
}

interface TableRow {
    id?: number
    date: string
    timeIn: string
    timeOut: string
    status: string
    overtime: number
    totalHours: number
}

function TableRow({date, timeIn, timeOut, overtime, totalHours}: AttendanceRes) {

    const statusColor: Record<string, string> = {
        "N/A": "#666666",
        "Present": "#67f596",
        "Late": "#e8dd8b",
        "Absent": "#f56767",
    }

    const statusGenerator = () => {
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

    const status = statusGenerator()

    return(
        <Grid container spacing={1} px={1} py={1.5}>
            <Grid xs={2}>
                <Typography fontWeight={400}>{date}</Typography>
            </Grid>
            <Grid xs={2}>
                <Typography>{timeIn.split(":").slice(0,2).join(":")}</Typography>
            </Grid>
            <Grid xs={2}>
                <Typography>{timeOut.split(":").slice(0,2).join(":")}</Typography>
            </Grid>
            <Grid xs={2}>
                <Stack direction='row' alignItems='center' gap={0.5}>
                    <Circle sx={{pb: 0.4, fontSize: 16, color: statusColor[status]}} />
                    <Typography>
                        {status}
                    </Typography>
                </Stack>
            </Grid>
            <Grid xs={2}>
                <Typography>{overtime} hr</Typography>
            </Grid>
            <Grid xs={2}>
                <Typography>{totalHours} hr</Typography>
            </Grid>
        </Grid>
    )
}

// Styled Widget component
const Widget = styled(Paper)<PaperProps>(({}) => ({
    borderRadius: 12,
    padding: 16,
}))






