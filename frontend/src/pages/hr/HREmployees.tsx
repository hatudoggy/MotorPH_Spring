import { Avatar, Box, Button, Card, CardActionArea, CardContent, Chip, CircularProgress, Container, Dialog, DialogContent, IconButton, InputAdornment, Paper, Stack, Tab, Tabs, TextField, Typography } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Labeled from "../../components/Labeled";
import { Add, ArrowBack, Call, FileCopy, LocalPhone, Search } from "@mui/icons-material";
import { Shadows } from "../../constants/Shadows";
import { API, BASE_API } from "../../constants/Api";
import axios from "axios";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { useDebounce } from "@uidotdev/usehooks";
import { TabContext, TabList, TabPanel } from "@mui/lab";
import ReadonlyTextField from "../../components/ReadOnlyTextField";
import { calculateAge, formatterWhole, idformatter } from "../../utils/utils";


export default function HREmployees() {

  const [search, setSearch] = useState('')
  const debouncedSearch = useDebounce(search, 300)

  const fetchAllEmployees = async() => {
    const {EMPLOYEES} = API
    const res = await axios.get(BASE_API + EMPLOYEES.ALL, {
      params: {
        name: debouncedSearch || ''
      }
    })
    return res.data;
  }

  const {isPending, data} = useQuery<EmployeeRes[]>({
    queryKey: ['employeesAll', debouncedSearch],
    queryFn: fetchAllEmployees,
    placeholderData: keepPreviousData
  })

  const [openSelectDialog, setOpenSelectDialog] = useState(false)
  const [selectedEmployee, setSelectedEmployee] = useState<number | null>(null)

  const handleSelectEmployee = (employeeId: number) => {
    setOpenSelectDialog(true)
    setSelectedEmployee(employeeId)
  }

  return(
    <>
      <Container
        sx={{
          my: 5,
        }}
      >
        <Headertext>HR Employees</Headertext>
        <Stack gap={2}>
          <Stack direction="row" justifyContent='space-between'>
            <Box>
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
            </Box>
            <Stack direction='row' gap={1}>
              <Button
                variant="outlined"
                startIcon={<FileCopy />}
              >
                Export Data
              </Button>
              <Button 
                variant="contained"
                startIcon={<Add />}
                disableElevation
              >
                New Employee
              </Button>
            </Stack>
          </Stack>
          <Stack direction="row" flexWrap='wrap' gap={2}>
            {
              !isPending ? 
                data && data.map((item)=>
                  <EmployeeCard 
                    key={item.employeeId}
                    name={`${item.firstName} ${item.lastName}`}
                    position={item.employment.position.positionName}
                    department={item.employment.department.departmentName}
                    hireDate={item.employment.hireDate}
                    contactNo={item.contacts[0].contactNo}
                    status={item.employment.status}
                    onClick={()=>handleSelectEmployee(item.employeeId)}
                  />
                )
                :
                <Stack width='100%' alignItems='center'>
                  <CircularProgress />
                </Stack>
            }
          </Stack>
        </Stack>
      </Container>
      <Dialog
        open={openSelectDialog}
        onClose={()=>setOpenSelectDialog(false)}
        TransitionProps={{
          onExit: ()=>setSelectedEmployee(null)
        }}
        fullWidth
        maxWidth="sm"
        PaperProps={{
          sx: {
            borderRadius: 3,
          }
        }}
      >
        <EmployeeDetailsDialog selectedEmployee={selectedEmployee} onClose={()=>setOpenSelectDialog(false)}/>
      </Dialog>
    </>
  )
}


interface EmployeeCard {
  name: string
  position: string
  department: string
  hireDate: string
  contactNo: string
  status: Status
  onClick: () => void
}


function EmployeeCard({name, position, department, hireDate, contactNo, status, onClick}: EmployeeCard) {

  const statusColor: Record<number, string> = {
    1: "#66bb6a", //Green
    2: "#9f66bb", //Purple
    3: "#bba766", //Yellow
    4: "#667ebb", //Blue
    5: "#bb6666", //Red
    6: "#9e9e9e", //Gray
  }

  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s"

  return(
    <Card
      //variant="outlined"
      sx={{
        borderRadius: 3,
        boxShadow: Shadows.l1
      }}
    >
      <CardActionArea onClick={onClick}>
        <CardContent
          sx={{
            width: 230,
            p: 1.5,
          }}
        >
          <Stack direction='row' justifyContent='end'>
            <Chip 
              size="small" 
              label={status.status}
              sx={{
                color: 'white',
                bgcolor: statusColor[status.statusId],
                opacity: 0.8
              }}
            />
          </Stack>
          <Stack alignItems='center' gap={1}>
            <Avatar 
              sx={{
                width: 100,
                height: 100,
              }}
              src={picURL}
            />
            <Stack alignItems='center'>
              <Typography variant="body2" fontSize={20} fontWeight={500} noWrap>{name}</Typography>
              <Typography variant="body2" fontSize={15} color='GrayText' noWrap>{position}</Typography>
            </Stack>

            <Paper
              variant="outlined"
              sx={{
                border:'none',
                borderRadius: 3,
                width: '100%',
                p: 1.5,
                bgcolor: '#f5f5f5'
              }}
            >
              <Stack width='100%' gap={1.5}>
                <Stack direction='row' justifyContent='space-between'>
                  <Labeled label="Department">
                    <Typography fontWeight={500} noWrap maxWidth={100}>{department}</Typography>
                  </Labeled>
                  <Labeled label="Hire Date">
                    <Typography fontWeight={500} noWrap>{hireDate}</Typography>
                  </Labeled>
                </Stack>
                {/* <Stack direction='row' gap={1}>
                  <Call fontSize="small"/>
                  <Typography  fontWeight={500}>
                    +63{contactNo.substring(1)}
                  </Typography>
                </Stack> */}
              </Stack>
            </Paper>

          </Stack>
        </CardContent>
      </CardActionArea>
    </Card>
  )
}


interface EmployeeDetailsDialog {
  selectedEmployee: number | null
  onClose: () => void
}

function EmployeeDetailsDialog({selectedEmployee, onClose}: EmployeeDetailsDialog) {

  const [value, setValue] = useState("1")

  const fetchEmployee = async() => {
    const {EMPLOYEES} = API
    const res = await axios.get(BASE_API + EMPLOYEES.BASE + selectedEmployee)
    return res.data;
  }

  const {isPending, data} = useQuery<EmployeeRes>({
    queryKey: ['employee'],
    queryFn: fetchEmployee,
    placeholderData: keepPreviousData
  })

  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s"


  return(
    <>
      <DialogContent>
        <Stack direction="row" position='absolute' left={10} top={10}>
          <IconButton onClick={onClose}>
            <ArrowBack />
          </IconButton>
        </Stack>
        {
          !isPending ?
            data && 
              <Stack alignItems='center' gap={1}>
                <Avatar 
                  sx={{
                    width: 130,
                    height: 130,
                  }}
                  src={picURL}
                />
                <Stack alignItems='center'>
                  <Typography variant="body2" fontSize={24} fontWeight={500} noWrap>{`${data.firstName} ${data.lastName}`}</Typography>
                  <Typography variant="body2" fontSize={17} color='GrayText' noWrap>{data.employment.position.positionName}</Typography>
                </Stack>
                
                <TabContext value={value}>
                  <Box sx={{ width: '100%', borderBottom: 1, borderColor: 'divider' }}>
                    <TabList 
                      onChange={(_e, val)=>setValue(val)}

                    >
                      <Tab label="Info" value="1"/>
                      <Tab label="Employment" value="2"/>
                      <Tab label="Contribution" value="3"/>
                      <Tab label="Salary" value="4"/>
                    </TabList>
                  </Box>
                  <Box width='100%' mb={3}>
                    <TabPanel value="1">
                      <Stack gap={3}>
                        <Stack direction='row' gap={3}>
                          <ReadonlyTextField
                            label="First Name"
                            defaultValue={data.firstName}
                            fullWidth
                          />
                          <ReadonlyTextField
                            label="Last Name"
                            defaultValue={data.lastName}
                            fullWidth
                          />
                        </Stack>
                        <Stack direction='row' gap={3}>
                          <ReadonlyTextField
                            label="Age"
                            defaultValue={calculateAge(data.dob)}
                            fullWidth
                          />
                          <ReadonlyTextField
                            label="Birthdate"
                            defaultValue={data.dob}
                            fullWidth
                          />
                        </Stack>
                        <ReadonlyTextField
                          label="Address"
                          defaultValue={data.address}
                          fullWidth
                        />
                      </Stack>
                    </TabPanel>

                    <TabPanel value="2">
                      <Stack gap={3}>
                        <Stack direction='row' gap={3}>
                          <ReadonlyTextField
                            label="Position"
                            defaultValue={data.employment.position.positionName}
                            fullWidth
                          />
                          <ReadonlyTextField
                            label="Department"
                            defaultValue={data.employment.department.departmentName}
                            fullWidth
                          />
                        </Stack>
                        <ReadonlyTextField
                          label="Supervisor"
                          defaultValue={`${data.employment.supervisor.firstName} ${data.employment.supervisor.lastName}`}
                          fullWidth
                        />
                        <Stack direction='row' gap={3}>
                          <ReadonlyTextField
                            label="Hire Date"
                            defaultValue={data.employment.hireDate}
                            fullWidth
                          />
                          <ReadonlyTextField
                            label="Employment Status"
                            defaultValue={data.employment.status.status}
                            fullWidth
                          />
                        </Stack>
                      </Stack>
                    </TabPanel>

                    <TabPanel value="3">
                      <Stack gap={3}>
                        <ReadonlyTextField
                          label="Tin Number"
                          defaultValue={idformatter(data.governmentId.tinNo, 'tin')}
                          fullWidth
                        />
                        <ReadonlyTextField
                          label="SSS Number"
                          defaultValue={idformatter(data.governmentId.sssNo, 'sss')}
                          fullWidth
                        />
                        <ReadonlyTextField
                          label="Philhealth Number"
                          defaultValue={idformatter(data.governmentId.philHealthNo, 'philhealth')}
                          fullWidth
                        />
                        <ReadonlyTextField
                          label="Pagibig Number"
                          defaultValue={idformatter(data.governmentId.pagIbigNo, 'pagibig')}
                          fullWidth
                        />
                      </Stack>
                    </TabPanel>

                    <TabPanel value="4">
                      <Stack gap={3}>
                        <ReadonlyTextField
                          label="Base Salary"
                          defaultValue={formatterWhole.format(data.employment.basicSalary)}
                          fullWidth
                        />
                        {
                          data.benefits.map((item)=>
                            <ReadonlyTextField
                              key={item.benefitId}
                              label={item.benefitType.benefit}
                              defaultValue={formatterWhole.format(item.amount)}
                              fullWidth
                            />
                          )
                        }

                      </Stack>
                    </TabPanel>
                  </Box>

                </TabContext>
              </Stack>
              :
              <Stack alignItems='center' minHeight={300}>
                <CircularProgress />
              </Stack>
        }
      </DialogContent>
    </>
  )
}