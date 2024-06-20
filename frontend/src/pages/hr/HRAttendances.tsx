import { Avatar, Container, Paper, PaperProps, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Table from "../../components/Table";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { API, BASE_API } from "../../constants/Api";
import { format } from "date-fns";


export default function HRAttendances() {

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>
        <Headertext>HR Attendance</Headertext>
        <AttendanceTable />
      </Stack>
    </Container>
  )
}

function AttendanceTable() {

  const fetchAttendanceAll = async() => {
    const {ATTENDANCES} = API
    const dateToday = format(new Date(), 'yyyy-MM-dd')
    const {data} = await axios.get(BASE_API + ATTENDANCES.BASE, {
      params: {
        date: dateToday || ''
      }
    })
    return data
  }

  const {isPending, data} = useQuery<AttendanceFull[]>({
    queryKey: ['attendanceAll'],
    queryFn: fetchAttendanceAll,
  })

  const tableData = data && data.map(({attendanceId, ...rest})=>rest)

  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s"

  return(
    <Widget
      variant="outlined"
      sx={{
        height: '100%'
      }}
    >
      <Table 
        colSizes={[3.5, true, true, true, 1.2, 1]}
        colHeader={[
          "Employee", 
          "Date",
          "Time In",
          "Time Out",
          "Hours Worked",
          "Overtime",
        ]}
        tableData={tableData || []}
        renderers={{
          employee: (item) => (
            <Stack direction='row' alignItems='center' gap={1.5}>
              <Avatar 
                sx={{
                  height: 46,
                  width: 46,
                }}
                src={picURL}
              />
              <Stack>
                <Typography variant="body2" fontSize={16} fontWeight={500}>
                  {`${item.firstName} ${item.lastName}`}
                </Typography>
                <Typography variant="body2" color='GrayText'>
                  {item.employment.department.departmentName}
                </Typography>
              </Stack>
            </Stack>
          )
        }}
        loading={isPending}     
      />
    </Widget>
  )
}


const Widget = styled(Paper)<PaperProps>(({}) => ({
  borderRadius: 12,
  padding: 16,
}))
