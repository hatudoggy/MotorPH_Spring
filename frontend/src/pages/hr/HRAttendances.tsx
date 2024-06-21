import { Avatar, Container, InputAdornment, Paper, PaperProps, Stack, TextField, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Table from "../../components/Table";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import axios from "axios";
import { API, BASE_API } from "../../constants/Api";
import { format } from "date-fns";
import { DatePicker } from "@mui/x-date-pickers";
import { useState } from "react";
import { useDebounce } from "@uidotdev/usehooks";
import { Search } from "@mui/icons-material";


export default function HRAttendances() {

  const [dateFilter, setDateFilter] = useState<Date | null>(new Date())
  const [search, setSearch] = useState('')
  const debouncedSearch = useDebounce(search, 500)

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>
        <Headertext>HR Attendance</Headertext>
        <Stack direction='row' mb={2} gap={1}>
          <DatePicker 
            slotProps={{
              textField: {
                size: 'small',
                sx: {
                  bgcolor: 'white',
                  width: 180
                },
                InputProps: {
                  sx: {
                    borderRadius: 2
                  }
                }
              }
            }}
            value={dateFilter}
            onChange={(val)=>setDateFilter(val)}
          />
          <TextField 
            placeholder="Search"
            size="small"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Search />
                </InputAdornment>
              ),
              sx: {
                borderRadius: 3
              }
            }}
            sx={{
              bgcolor: 'white',
              width: 300
            }}
            value={search}
            onChange={(e)=>setSearch(e.target.value)}
          />
        </Stack>
        <AttendanceTable dateFilter={dateFilter} searchFilter={debouncedSearch} />
      </Stack>
    </Container>
  )
}

interface AttendanceTable {
  dateFilter: Date | null
  searchFilter: string
}

function AttendanceTable({dateFilter, searchFilter}: AttendanceTable) {

  const fetchAttendanceAll = async() => {
    const {ATTENDANCES} = API
    const date = dateFilter && format(dateFilter, 'yyyy-MM-dd')
    const {data} = await axios.get(BASE_API + ATTENDANCES.BASE, {
      params: {
        date: date || '',
        name: searchFilter
      }
    })
    return data
  }

  const {isPending, data} = useQuery<AttendanceFull[]>({
    queryKey: ['attendanceAll', dateFilter, searchFilter],
    queryFn: fetchAttendanceAll,
    placeholderData: keepPreviousData
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
        rowHeight={70}
        renderers={{
          employee: (item: EmployeeRes) => (
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
