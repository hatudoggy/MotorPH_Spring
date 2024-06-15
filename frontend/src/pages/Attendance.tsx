import { AccessTime, Circle, EventBusy, EventBusyOutlined, NorthEast, SouthWest, Today, TodayOutlined } from "@mui/icons-material";
import { Box, Button, Container, Divider, Paper, PaperProps, Stack, SvgIconTypeMap, Typography, styled } from "@mui/material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { PieChart, useDrawingArea } from "@mui/x-charts";
import { Colors } from "../constants/Colors";
import Grid from "@mui/material/Unstable_Grid2/Grid2";


export default function Attendance() {

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>

        <Typography 
          variant="h4" 
          fontWeight={500}
          mb={2}
        >
          Attendance
        </Typography>

        <Stack flex={1} gap={3}>
          <WidgetBar />

          <Stack flex={1} gap={1}>
            <MonthFilter />
            <Table />
          </Stack>

        </Stack>

      </Stack>
    </Container>
  )
}

function WidgetBar() {


  return(
    <Stack direction='row' gap={2}>
      
      <AttendanceToday />
      <AttendanceOverview />

    </Stack>
  )
}

function AttendanceToday() {
  
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
            <Typography variant="h4">08:12</Typography>
          </Stack>
          <Divider flexItem orientation="vertical" />
          <Stack flex={1} alignItems='center' pb={1}>
            <Typography variant="body2" color='GrayText'>Time Out</Typography>
            <Typography variant="h4">-- : --</Typography>
          </Stack>
        </Stack >
        <Stack flex={1} direction='row'>
          <Button fullWidth variant="contained" disableElevation>Clock In</Button>
        </Stack>
      </Stack>
    </Widget>
  )
}

function AttendanceOverview() {

  const palette = ['#000000', '#575757', '#c4c4c4']

  return(
    <Widget variant="outlined">
      <Typography variant="body2" fontWeight={600}>This Month</Typography>
      <Stack direction='row' alignItems='center' gap={3}>
        <PieChart 
          colors={palette}
          series={[
            {
              data: [
                { id: 0, value: 10, label: 'present'},
                { id: 1, value: 15, label: 'late'},
                { id: 2, value: 20, label: 'absent'},
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
              value="28"
              label="Present"
            />
            <IconLabel
              Icon={AccessTime}
              value="12"
              label="Late"
            />
            <IconLabel
              Icon={EventBusyOutlined}
              value="4"
              label="Absent"
            />
          </Stack>

          <Stack direction='row' alignItems='center' gap={4}>
            <IconLabel
              Icon={SouthWest}
              value="07:02"
              label="Avg Check In"
            />
            <IconLabel
              Icon={NorthEast}
              value="17:12"
              label="Avg Check Out"
            />
          </Stack>
        </Stack>

      </ Stack>
    </Widget>
  )
}


interface IconLabel {
  Icon?: OverridableComponent<SvgIconTypeMap<{}, "svg">> & {muiName: string;}
  value: string
  label: string
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


function MonthFilter() {

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


function Table() {

  const attendance: TableRow[] = [
    {id: 1, date: "June 14 2024", timeIn: "07:30", timeOut: "17:00", status: "Present", overtime: 0, totalHours: 9},
    {id: 2, date: "June 13 2024", timeIn: "08:30", timeOut: "17:00", status: "Late", overtime: 0, totalHours: 8},
    {id: 3, date: "June 12 2024", timeIn: "08:50", timeOut: "17:00", status: "Late", overtime: 0, totalHours: 8},
    {id: 4, date: "June 11 2024", timeIn: "-- : --", timeOut: "-- : --", status: "Absent", overtime: 0, totalHours: 0},
    {id: 5, date: "June 10 2024", timeIn: "07:30", timeOut: "17:00", status: "Present", overtime: 0, totalHours: 9},
    {id: 6, date: "June 9 2024", timeIn: "07:30", timeOut: "17:00", status: "Present", overtime: 0, totalHours: 9},
  ]

  return(
    <Widget
      variant="outlined"
      sx={{
        flex: 1,
      }}
    >
      <Stack height='100%'>
        <TableHeader />
        <Stack
          flex={1}
          sx={{
            overflowY: 'scroll',
            overflowX: 'hidden',
          }}
        >
          {
            attendance.map((row) => 
              <TableRow 
                key={row.id}
                {...row}
              />
            )
          }
        </Stack>
      </Stack>
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

function TableRow({date, timeIn, timeOut, status, overtime, totalHours}: TableRow) {

  const statusColor: Record<string, string> = {
    "Present": "#67f596",
    "Late": "#e8dd8b",
    "Absent": "#f56767",
  }

  return(
    <Grid container spacing={1} px={1} py={1.5}>
      <Grid xs={2}>
        <Typography fontWeight={400}>{date}</Typography>
      </Grid>
      <Grid xs={2}>
        <Typography>{timeIn}</Typography>
      </Grid>
      <Grid xs={2}>
        <Typography>{timeOut}</Typography>
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
        <Typography>{overtime}</Typography>
      </Grid>
      <Grid xs={2}>
        <Typography>{totalHours}</Typography>
      </Grid>
    </Grid>
  )
}


const Widget = styled(Paper)<PaperProps>(({}) => ({
  borderRadius: 12,
  padding: 16,
}))