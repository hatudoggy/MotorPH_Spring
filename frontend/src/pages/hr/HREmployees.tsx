import { Autocomplete, Avatar, Box, Button, Card, CardActionArea, CardContent, Chip, CircularProgress, Container, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, IconButton, InputAdornment, InputLabel, ListItemIcon, ListItemText, Menu, MenuItem, Paper, PaperProps, Select, SelectProps, Stack, StackProps, SvgIconTypeMap, Tab, Tabs, TextField, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Labeled from "../../components/Labeled";
import { Add, ArrowBack, Badge, Call, Edit, FileCopy, LocalPhone, MoreVert, Payments, Person, Phone, Search, Work } from "@mui/icons-material";
import { Shadows } from "../../constants/Shadows";
import { API, BASE_API } from "../../constants/Api";
import axios from "axios";
import { keepPreviousData, useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {ReactNode, useEffect, useMemo, useState} from "react";
import { useDebounce } from "@uidotdev/usehooks";
import { DatePickerProps, TabContext, TabList, TabPanel } from "@mui/lab";
import ReadonlyTextField from "../../components/ReadOnlyTextField";
import { calculateAge, formatterWhole, idformatter } from "../../utils/utils";
import PopupState, { bindMenu, bindTrigger } from "material-ui-popup-state";
import { useForm, SubmitHandler, UseFormRegister, FieldValues, Controller, Control, Path, useFormState, UseFormWatch, UseFormSetValue } from "react-hook-form"
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { DatePicker } from "@mui/x-date-pickers";
import { format } from "date-fns";
import {useFetchEmployees} from "../../hooks/UseFetch.ts";

/**
 * Component for displaying and managing HR employees.
 *
 * @returns {JSX.Element} The HR employees component.
 */
export default function HREmployees() {
  // State for the search input
  const [search, setSearch] = useState('');

  // Debounced search value
  const debouncedSearch = useDebounce(search, 300);

  // Fetch employees data from server
  const { isPending, data } = useFetchEmployees();

  // State for the select employee dialog
  const [openSelectDialog, setOpenSelectDialog] = useState(false);

  // State for the create/update employee dialog
  const [openCUDialog, setOpenCUDialog] = useState<null | 'add' | 'edit'>(null);

  // State for the currently selected employee
  const [selectedEmployee, setSelectedEmployee] = useState<number | null>(null);

  /**
   * Handles selecting an employee.
   *
   * @param {number} employeeId - The ID of the employee to select.
   */
  const handleSelectEmployee = (employeeId: number) => {
    setOpenSelectDialog(true);
    setSelectedEmployee(employeeId);
  };

  /**
   * Handles creating or updating an employee.
   *
   * @param {'add' | 'edit'} type - The type of operation to perform.
   * @param {number} [employeeId] - The ID of the employee to update (optional).
   */
  const handleCUEmployee = (type: 'add' | 'edit', employeeId?: number) => {
    setOpenCUDialog(type);
    if (employeeId) {
      setSelectedEmployee(employeeId);
    }
  };

  /**
   * Filters the employees data based on the search input.
   *
   * @returns {Array<Employee>} The filtered employees.
   */
  const filteredData = useMemo(() => {
    if (!data) return [];
    return data.filter((item) => {
      const fullName = `${item.firstName} ${item.lastName}`.toLowerCase();
      const searchTerm = debouncedSearch.toLowerCase();
      return fullName.includes(searchTerm) ||
          item.position.positionName.toLowerCase().includes(searchTerm) ||
          item.department.departmentName.toLowerCase().includes(searchTerm);
    });
  }, [data, debouncedSearch]);


  return (
      // JSX code for the component
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
                        borderRadius: 3,
                      },
                    }}
                    sx={{
                      bgcolor: 'white',
                      width: 300,
                    }}
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
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
                    onClick={() => handleCUEmployee("add")}
                >
                  New Employee
                </Button>
              </Stack>
            </Stack>
            <Stack direction="row" flexWrap='wrap' gap={2}>
              {!isPending ? (
                  filteredData.map((item) => (
                      <EmployeeCard
                          key={item.employeeId}
                          name={`${item.firstName} ${item.lastName}`}
                          position={item.position.positionName}
                          department={item.department.departmentName}
                          hireDate={item.hireDate}
                          status={item.status.statusName}
                          onClick={() => handleSelectEmployee(item.employeeId)}
                          onEdit={() => handleCUEmployee("edit", item.employeeId)}
                      />
                  ))
              ) : (
                  <Stack width='100%' alignItems='center'>
                    <CircularProgress />
                  </Stack>
              )}
            </Stack>
          </Stack>
        </Container>
        <Dialog
            open={openSelectDialog}
            onClose={() => setOpenSelectDialog(false)}
            TransitionProps={{
              onExit: () => setSelectedEmployee(null),
            }}
            fullWidth
            maxWidth="sm"
            PaperProps={{
              sx: {
                borderRadius: 3,
              },
            }}
        >
          <EmployeeDetailsDialog selectedEmployee={selectedEmployee} onClose={() => setOpenSelectDialog(false)} />
        </Dialog>
        <Dialog
            open={openCUDialog !== null}
            onClose={() => setOpenCUDialog(null)}
            TransitionProps={{
              onExit: () => setSelectedEmployee(null),
            }}
            fullWidth
            maxWidth="xs"
            PaperProps={{
              sx: {
                borderRadius: 3,
              },
            }}
        >
          {openCUDialog && (
              <EmployeeFormDialog type={openCUDialog} selectedId={selectedEmployee} onClose={() => setOpenCUDialog(null)} />
          )}
        </Dialog>
      </>
  );
}


interface EmployeeCard {
  name: string
  position: string
  department: string
  hireDate: string
  contactNo: string
  status: EmploymentStatusRes
  onClick: () => void
  onEdit: () => void
}


function EmployeeCard({name, position, department, hireDate, status, onClick, onEdit}: EmployeeCard) {

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
        boxShadow: Shadows.l1,
        position: 'relative'
      }}
    >
      <Stack 
        direction='row' 
        justifyContent='end' 
        gap={0.5}
        position='absolute'
        right={5}
        top={9}
        zIndex={5}
      >
        <PopupState variant="popover">
          {(popstate) => (
            <>
              <IconButton size="small" {...bindTrigger(popstate)}>
                <MoreVert fontSize="small"/>
              </IconButton>
              <Menu 
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'right',
                }}
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                {...bindMenu(popstate)}
              >
                <MenuItem 
                  sx={{
                    minWidth: 130
                  }}
                  onClick={()=>{onEdit(); popstate.close()}}
                >
                  <ListItemIcon>
                    <Edit />
                  </ListItemIcon>
                  <ListItemText>
                    Edit
                  </ListItemText>
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
          }}
        >
          <Stack 
            direction='row' 
            justifyContent='end' 
            mr={3}
          >
            <Chip 
              size="small" 
              label={status.statusName}
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

function EmployeeDetailsDialog({ selectedEmployee, onClose }: EmployeeDetailsDialog) {
  const [value, setValue] = useState("1");

  const fetchEmployee = async () => {
    const { EMPLOYEES } = API;
    const res = await axios.get(BASE_API + EMPLOYEES.BASE + selectedEmployee);
    return res.data;
  };

  const { isPending, data } = useQuery<EmployeeFullRes>({
    queryKey: ['employee', selectedEmployee],
    queryFn: fetchEmployee,
    placeholderData: keepPreviousData,
  });

  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s";

  return (
      <>
        <DialogContent>
          <Stack direction="row" position='absolute' left={10} top={10}>
            <IconButton onClick={onClose}>
              <ArrowBack />
            </IconButton>
          </Stack>
          {!isPending ? (
              data && (
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
                      <Typography variant="body2" fontSize={17} color='GrayText' noWrap>{data.position.positionName}</Typography>
                    </Stack>

                    <TabContext value={value}>
                      <Box sx={{ width: '100%', borderBottom: 1, borderColor: 'divider' }}>
                        <TabList
                            onChange={(_e, val) => setValue(val)}
                        >
                          <Tab label="Info" value="1" />
                          <Tab label="Employment" value="2" />
                          <Tab label="Contribution" value="3" />
                          <Tab label="Salary" value="4" />
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
                                  defaultValue={data.position.positionName}
                                  fullWidth
                              />
                              <ReadonlyTextField
                                  label="Department"
                                  defaultValue={data.department.departmentName}
                                  fullWidth
                              />
                            </Stack>
                            <ReadonlyTextField
                                label="Supervisor"
                                defaultValue={`${data.supervisor.firstName} ${data.supervisor.lastName}`}
                                fullWidth
                            />
                            <Stack direction='row' gap={3}>
                              <ReadonlyTextField
                                  label="Hire Date"
                                  defaultValue={data.hireDate}
                                  fullWidth
                              />
                              <ReadonlyTextField
                                  label="Employment Status"
                                  defaultValue={data.status.statusName}
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
                                defaultValue={formatterWhole.format(data.basicSalary)}
                                fullWidth
                            />
                            {data.benefits.map((item) =>
                                <ReadonlyTextField
                                    key={item.benefitId}
                                    label={item.benefitType.benefit}
                                    defaultValue={formatterWhole.format(item.amount)}
                                    fullWidth
                                />
                            )}
                          </Stack>
                        </TabPanel>
                      </Box>
                    </TabContext>
                  </Stack>
              )
          ) : (
              <Stack alignItems='center' justifyContent='center' minHeight={300}>
                <CircularProgress />
              </Stack>
          )}
        </DialogContent>
      </>
  );
}


interface EmployeeFormDialog {
  type: 'add' | 'edit'
  selectedId?: number | null
  onClose: () => void
}

interface BenifitInput {
  amount: number
  type: number
}

interface Inputs {
  firstName: string
  lastName: string
  dob: Date | null
  address: string

  contacts: string[]
  benefits: BenifitInput[]

  departmentCode: string
  positionCode: string
  statusId: string
  supervisor: TextCompleteOption | null
  hireDate: Date | null
  
  sssNo: string
  philHealthNo: string
  pagIbigNo: string
  tinNo: string

  basicSalary: number
  semiMonthlyRate: number
  hourlyRate: number
}

function EmployeeFormDialog({type, selectedId, onClose}: EmployeeFormDialog) {

  const queryClient = useQueryClient()

  const fetchEmployee = async() => {
    const {EMPLOYEES} = API
    if(selectedId){
      const res = await axios.get(BASE_API + EMPLOYEES.BASE + selectedId)
      return res.data;
    } else {
      return null
    }
  }

  const {isPending, data} = useQuery<EmployeeFullRes>({
    queryKey: ['employeeEdit', selectedId],
    queryFn: fetchEmployee,
    enabled: !!selectedId
  })

  const supervisorData = data ? {
    value: data?.supervisor.supervisorId,
    label: `${data?.supervisor.firstName} ${data?.supervisor.lastName}`
  } : null

  const {
    register,
    control,
    watch,
    setValue,
    handleSubmit,
    reset
  } = useForm<Inputs>({
    defaultValues: {
      firstName: data?.firstName || "",
      lastName: data?.lastName || "",
      dob: data?.dob ? new Date(data.dob) : null,
      address: data?.address || "",
    
      contacts: [],
      benefits: data?.benefits || [],
    
      departmentCode: data?.department.departmentCode || "",
      positionCode: data?.position.positionCode || "",
      statusId: data?.status.statusId.toString() || "",
      supervisor: supervisorData || null,
      hireDate: data?.dob ? new Date(data.hireDate) : null,
      
      sssNo: data?.governmentId.sssNo || "",
      philHealthNo: data?.governmentId.philHealthNo || "",
      pagIbigNo: data?.governmentId.pagIbigNo || "",
      tinNo: data?.governmentId.tinNo || "",
    
      basicSalary: data?.basicSalary || 0,
      semiMonthlyRate: data?.semiMonthlyRate || 0,
      hourlyRate: data?.hourlyRate || 0,
    }
  })

  useEffect(()=>{
    if(selectedId && !isPending && data) {
      reset({
        firstName: data.firstName || "",
        lastName: data.lastName || "",
        dob: data.dob ? new Date(data.dob) : null,
        address: data.address || "",
      
        contacts: [],
        benefits: data.benefits || [],
      
        departmentCode: data.department.departmentCode || "",
        positionCode: data.position.positionCode || "",
        statusId: data.status.statusId.toString() || "",
        supervisor: supervisorData || null,
        hireDate: data.dob ? new Date(data.hireDate) : null,
        
        sssNo: data.governmentId.sssNo || "",
        philHealthNo: data.governmentId.philHealthNo || "",
        pagIbigNo: data.governmentId.pagIbigNo || "",
        tinNo: data.governmentId.tinNo || "",
      
        basicSalary: data.basicSalary || 0,
        semiMonthlyRate: data.semiMonthlyRate || 0,
        hourlyRate: data.hourlyRate || 0,
      })
    }
  }, [data, isPending])


  const addEmployee = async (employee: EmployeeReq) => {
    const {EMPLOYEES } = API
    const res = await axios.post(BASE_API + EMPLOYEES.ALL, employee);
    return res.data;
  }

  const useAddEmployee = useMutation({
    mutationFn: addEmployee,
    onSettled: async () => {
      return await queryClient.invalidateQueries({ queryKey: ['employeesAll']})
    }
  })

  const editEmployee = async (employee: EmployeeReq) => {
    const {EMPLOYEES } = API
    const res = await axios.patch(BASE_API + EMPLOYEES.BASE + selectedId, employee);
    return res.data;
  }

  const useEditEmployee = useMutation({
    mutationFn: editEmployee,
    onSettled: async () => {
      return await queryClient.invalidateQueries({ queryKey: ['employeesAll']})
    }
  })

  const onSubmit: SubmitHandler<Inputs> = (data) => {
    const formData: EmployeeReq = {
      lastName: data.lastName,
      firstName: data.firstName,
      dob: data.dob ? format(data.dob, 'yyyy-MM-dd') : "",
      address: data.address,
        contacts: [
        {
          contactNumbers: "1234567890"
        }
      ],
      benefits: [
        {
          amount: 1500,
          benefitType: {
            benefitTypeId: 1
          }
        },
        {
          amount: 2000,
          benefitType: {
            benefitTypeId: 2
          }
        }
      ],
      employment: {
        department: {
          departmentCode: data.departmentCode
        },
        position: {
          positionCode: data.positionCode
        },
        status: {
          statusId: Number(data.statusId)
        },
        hireDate: data.hireDate ? format(data.hireDate, 'yyyy-MM-dd') : "",
        basicSalary: data.basicSalary,
        semiMonthlyRate: data.semiMonthlyRate,
        hourlyRate: data.hourlyRate,
        supervisor: {
          employeeId: Number(data.supervisor?.value)
        }
      },
      governmentId: {
        sssNo: data.sssNo,
        philHealthNo: data.philHealthNo,
        pagIbigNo: data.pagIbigNo,
        tinNo: data.tinNo
      }
    }

    if(selectedId){
      const formDataWId: EmployeeReq = {
        employeeId: selectedId,
        ...formData
      }
      useEditEmployee.mutate(formDataWId)
    } else {
      useAddEmployee.mutate(formData)
    }

    onClose()
  }

  return(
    <>
      <DialogTitle textTransform='capitalize'>
        {`${type} Employee`}
      </DialogTitle>
      <DialogContent>
          <Stack mb={4} gap={3}>
            <BasicInfoArea register={register} control={control} selectedId={selectedId}/>
            {/* <ContactNumbersArea register={register} control={control}/> */}
            <EmploymentInfoArea register={register} control={control} watch={watch} setValue={setValue}/>
            <ContributionIdsArea register={register} control={control} selectedId={selectedId}/>
            <SalaryBenefitsArea register={register} control={control} />
          </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button variant="contained" onClick={handleSubmit(onSubmit)}>Save</Button>
      </DialogActions>
    </>
  )
}

interface FormArea {
  register: UseFormRegister<Inputs>
  control: Control<Inputs, any>
  watch?: UseFormWatch<Inputs>
  setValue?: UseFormSetValue<Inputs>
  selectedId?: number | null
}

function BasicInfoArea({register, control, selectedId}: FormArea) {

  return(
    <Box>
      <HeadIcon Icon={Person}>Basic Info</HeadIcon>
      <FormWidget>
        <TextField 
          {...register("firstName")}
          label="First Name"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
        <TextField 
          {...register("lastName")}
          label="Last Name"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
        <DateSelect 
          label="Birth Date"
          control={control}
          name="dob"
        />
        <TextField 
          {...register("address")}
          label="Address"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
      </FormWidget>
    </Box>
  )
}

function ContactNumbersArea({register}: FormArea) {

  return(
    <Box>
      <HeadIcon Icon={Phone}>Contact Numbers</HeadIcon>
      <FormWidget>

      </FormWidget>
    </Box>
  )
}

function EmploymentInfoArea({control, watch, setValue}: FormArea) {

  const departmentCode = watch && watch("departmentCode")

  const fetchAllDepartments = async() => {
    const {COMPANY} = API
    const res = await axios.get(BASE_API + COMPANY.DEPARTMENTS)
    return res.data;
  }
  const departments = useQuery<DepartmentRes[]>({
    queryKey: ['departments'],
    queryFn: fetchAllDepartments
  })

  const fetchAllPositions = async() => {
    const {COMPANY} = API
    const res = await axios.get(BASE_API + COMPANY.POSITIONS, {
      params: {
        department: departmentCode || ''
      }
    })
    return res.data;
  }
  const positions = useQuery<(PositionRes & {departmentCode: string})[]>({
    queryKey: ['positions', departmentCode],
    queryFn: fetchAllPositions,
    enabled: !!departmentCode
  })

  const fetchAllStatuses = async() => {
    const {COMPANY} = API
    const res = await axios.get(BASE_API + COMPANY.STATUSES)
    return res.data;
  }
  const statuses = useQuery<EmploymentStatusRes[]>({
    queryKey: ['statuses'],
    queryFn: fetchAllStatuses
  })



  const fetchSupervisors = async() => {
    const {EMPLOYEES} = API
    const res = await axios.get(BASE_API + EMPLOYEES.ALL)
    return res.data;
  }

  const supervisors = useQuery<SupervisorRes[]>({
    queryKey: ['supervisors'],
    queryFn: fetchSupervisors,
    placeholderData: keepPreviousData
  })

  const departmentOptions = departments.data 
    && departments.data.map(({departmentCode, departmentName})=>({value: departmentCode, label: departmentName}))

  const positionOptions = positions.data 
    && positions.data.map(({positionCode, positionName})=>({value: positionCode, label: positionName}))

  const statusOptions = statuses.data 
    && statuses.data.map(({statusId, statusName})=>({value: statusId, label: statusName}))

  const supervisorOptions = supervisors.data
    && supervisors.data.map(({supervisorId, firstName, lastName}) => ({value: supervisorId, label:`${firstName} ${lastName}`}))

  return(
    <Box>
      <HeadIcon Icon={Work}>Employment Info</HeadIcon>
      <FormWidget>
        <Dropdown 
          variant="standard"
          label="Department"
          options={departmentOptions || []}
          changeTrigger={()=>{setValue && setValue('positionCode', '')}}
          control={control}
          name="departmentCode"
        />
        <Dropdown 
          variant="standard"
          label="Position"
          options={positionOptions || []}
          disabled={!departmentCode}
          control={control}
          name="positionCode"
        />
        <TextComplete 
          name="supervisor"
          control={control}
          label="Supervisor"
          options={supervisorOptions || []}
        />
        <DateSelect 
          label="Hire Date"
          control={control}
          name="hireDate"
        />
        <Dropdown 
          variant="standard"
          label="Employment Status"
          options={statusOptions || []}
          control={control}
          name="statusId"
        />
      </FormWidget>
    </Box>
  )
}

function ContributionIdsArea({register, selectedId}: FormArea) {

  return(
    <Box>
      <HeadIcon Icon={Badge}>Contribution Ids</HeadIcon>
      <FormWidget>
        <TextField 
          {...register("tinNo")}
          label="Tin Number"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
        <TextField 
          {...register("sssNo")}
          label="SSS Number"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
        <TextField 
          {...register("philHealthNo")}
          label="Philhealth Number"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
        <TextField 
          {...register("pagIbigNo")}
          label="Pagibig Number"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}  
        />
      </FormWidget>
    </Box>
  )
}

function SalaryBenefitsArea({register}: FormArea) {

  return(
    <Box>
      <HeadIcon Icon={Payments}>Salary & Benefits</HeadIcon>
      <FormWidget>
        <TextField 
          {...register("basicSalary")}
          label="Basic Salary"
          variant="standard"
          fullWidth
          InputLabelProps={{ shrink: true }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <Typography>₱</Typography>
              </InputAdornment>
            )
          }}
        />
      </FormWidget>
    </Box> 
  )
}




interface DropdownOptions {
  value: any
  label: string
}

type Dropdown<T extends FieldValues> = {
  options: DropdownOptions[];
  control: Control<T>;
  name: Path<T>;
  changeTrigger?: () => void
} & SelectProps;


function Dropdown<T extends FieldValues>({options, control, name, changeTrigger, ...props}: Dropdown<T>) {

  return(
    <FormControl>
      <InputLabel variant={props.variant} shrink={true}>{props.label}</InputLabel>
      <Controller 
        control={control}
        name={name}
        render={({field}) => (
          <Select
            {...props}
            {...field}
            
            value={field.value || ''}
            onChange={(e)=> {
              field.onChange(e)
              if(changeTrigger){
                changeTrigger()
              }
            }}
            sx={{
              "& .MuiSelect-select": {
                bgcolor: "transparent",
              },
              ...props.sx
            }}
          >
            {
              options.map((item, idx)=>
                <MenuItem key={idx} value={item.value}>
                  {item.label}
                </MenuItem>
              )
            }
          </Select>
        )}
      />

    </FormControl>
  )
}

interface TextCompleteOption {
  label: string;
  value: string | number;
}

interface TextCompleteProps<T extends FieldValues> {
  name: Path<T>;
  control: Control<T>;
  label: string;
  options: TextCompleteOption[];
}

function TextComplete<T extends FieldValues>({
  name,
  control,
  label,
  options,
}: TextCompleteProps<T>) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field: { onChange, value, ref } }) => (
        <Autocomplete
          options={options}
          getOptionLabel={(option) => option.label}
          onChange={(_, data) => onChange(data)}
          value={value || null}
          isOptionEqualToValue={(option, value) => option.value === value?.value}
          renderInput={(params) => (
            <TextField
              {...params}
              label={label}
              variant="standard"
              inputRef={ref}
              InputLabelProps={{ shrink: true }}  
            />
          )}
        />
      )}
    />
  );
}


interface DateSelectProps<T extends FieldValues> {
  name: Path<T>;
  control: Control<T>;
  label: string;
  datePickerProps?: Partial<DatePickerProps<Date>>;
}

function DateSelect<T extends FieldValues>({
  name,
  control,
  label,
  datePickerProps,
}: DateSelectProps<T>) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field: { onChange, value }}) => (
        <DatePicker
          {...datePickerProps}
          value={value || null}
          onChange={(date) => onChange(date)}
          label={label}
          slotProps={{
            textField: {
              variant: 'standard',
              InputLabelProps: { shrink: true }  
            },
          }}
        />
      )}
    />
  );
}




interface HeadIcon {
  children: ReactNode
  Icon: OverridableComponent<SvgIconTypeMap<{}, "svg">> & {muiName: string;}
}

function HeadIcon({children, Icon}: HeadIcon) {

  return(
    <Stack direction='row' alignItems='center' gap={0.8} mb={0.5}>
      <Icon fontSize="small"/>
      <Typography fontWeight={500}>{children}</Typography>
    </Stack>
  )
}


const FormWidget = styled(Stack)<StackProps>(({}) => ({
  gap: 12,
  paddingLeft: 8,
  paddingRight: 8,
}))

const Widget = styled(Paper)<PaperProps>(({}) => ({
  flex: 1,
  borderRadius: 12,
  padding: 15,
}))
