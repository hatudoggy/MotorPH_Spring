import { Avatar, Button, Container, Paper, PaperProps, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Labeled from "../../components/Labeled";
import { Badge, Edit, Payments, Person, Work } from "@mui/icons-material";
import Grid from "@mui/material/Unstable_Grid2/Grid2";
import { calculateAge, formatterWhole, idformatter } from "../../utils/utils";
import { Shadows } from "../../constants/Shadows";
import { useAuth } from "../../hooks/AuthProvider";
import {
  useFetchDepartmentById,
  useFetchEmployeeById, useFetchEmploymentStatusById,
  useFetchPositionById,
  useFetchSupervisorById
} from "../../hooks/UseFetch.ts";

// Reusable styled component
const Widget = styled(Paper)<PaperProps>(({}) => ({
  flex: 1,
  borderRadius: 12,
  padding: 20,
  minHeight: 250,
}));

// Reusable component for section headers
const SectionHeader = ({ icon: Icon, title }) => (
    <Stack direction='row' alignItems='center' gap={0.8}>
      <Icon fontSize="small"/>
      <Typography fontWeight={500}>{title}</Typography>
    </Stack>
);

export default function Profile() {
  const {authUser} = useAuth();
  const employeeId = authUser?.employeeId;

  // Error handling for missing employeeId
  if (!employeeId) {
    console.error("Employee ID not found");
    return <Typography>Error: Employee ID not found</Typography>;
  }

  const { data: employeeData, error: employeeError, isLoading: isEmployeeLoading } = useFetchEmployeeById(employeeId);

  // Error handling for missing employee data
  if (!employeeData && !isEmployeeLoading) {
    console.error("Employee data not found");
    return <Typography>Error: Employee data not found</Typography>;
  }

  const supervisorId = employeeData?.supervisorId;
  const positionCode = employeeData?.positionCode;
  const departmentCode = employeeData?.departmentCode;
  const statusId = employeeData?.statusId;

  // Error handling for missing required data
  if (!supervisorId || !positionCode || !departmentCode || !statusId) {
    console.error("Missing required employee data");
    return <Typography>Error: Missing required employee data</Typography>;
  }

  const { data: supervisorData, error: supervisorError, isLoading: isSupervisorLoading } = useFetchSupervisorById(supervisorId);
  const { data: positionData, error: positionsError, isLoading: isPositionsLoading } = useFetchPositionById(positionCode);
  const { data: departmentData, error: departmentsError, isLoading: isDepartmentsLoading } = useFetchDepartmentById(departmentCode);
  const { data: statusData, error: statusError, isLoading: isStatusLoading } = useFetchEmploymentStatusById(statusId);

  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s";

  if (isEmployeeLoading || isSupervisorLoading || isDepartmentsLoading || isPositionsLoading || isStatusLoading) {
    return <Typography>Loading...</Typography>;
  }

  if (employeeError || supervisorError || departmentsError || positionsError || statusError) {
    console.error("Error occurred while fetching data");
    return <Typography>Error occurred while fetching data</Typography>;
  }

  return(
      <Container sx={{ my: 5 }}>
        <Stack height='100%'>
          <Headertext>Profile</Headertext>
          {employeeData && (
              <Stack mt={2} px={5} gap={4}>
                {/* Profile Header */}
                <Stack direction='row' justifyContent='space-between' alignItems='center' px={1}>
                  <Stack direction='row' alignItems='center' gap={2}>
                    <Avatar
                        sx={{
                          width: 80,
                          height: 80,
                          boxShadow: Shadows.l1
                        }}
                        src={picURL}
                    />
                    <Stack>
                      <Typography variant="h5" fontWeight={500}>{`${employeeData.firstName} ${employeeData.lastName}`}</Typography>
                      <Typography>{positionData?.position}</Typography>
                    </Stack>
                  </Stack>
                  <Button
                      variant="contained"
                      sx={{
                        height: 'min-content',
                        borderRadius: 4,
                        py: 1
                      }}
                      disableElevation
                      startIcon={<Edit />}
                  >
                    Edit Profile
                  </Button>
                </Stack>

                {/* Profile Details */}
                <Stack gap={2}>
                  <Stack direction='row' gap={2}>
                    {/* Personal Information */}
                    <Widget variant="outlined">
                      <Stack gap={2}>
                        <SectionHeader icon={Person} title="Personal Information" />
                        <Stack gap={2}>
                          <Grid container>
                            <Grid xs>
                              <Labeled label="Birthdate">
                                <Typography>{new Date(employeeData.dob).toLocaleDateString()}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Age">
                                <Typography>{calculateAge(employeeData.dob)} yrs. old</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs={5}>
                              <Labeled label="Phone No.">
                                <Typography>{employeeData.contacts.contactNumbers[0]}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Labeled label="Address">
                            <Typography>{employeeData.address}</Typography>
                          </Labeled>
                        </Stack>
                      </Stack>
                    </Widget>

                    {/* Employment Details */}
                    <Widget variant="outlined" sx={{flex: 1.3}}>
                      <Stack gap={2}>
                        <SectionHeader icon={Work} title="Employment Details" />
                        <Stack gap={2}>
                          <Grid container gap={3}>
                            <Grid xs={4}>
                              <Labeled label="Position">
                                <Typography>{positionData?.position}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Department">
                                <Typography>{departmentData?.department}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Supervisor">
                                <Typography>{`${supervisorData?.firstName} ${supervisorData?.lastName}`}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Grid container gap={3}>
                            <Grid xs={4}>
                              <Labeled label="Hire Date">
                                <Typography>{new Date(employeeData.hireDate).toLocaleDateString()}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Employment Status">
                                <Typography>{statusData?.statusName}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                        </Stack>
                      </Stack>
                    </Widget>
                  </Stack>

                  <Stack direction='row' gap={2}>
                    {/* Contribution IDs */}
                    <Widget variant="outlined">
                      <Stack gap={2}>
                        <SectionHeader icon={Badge} title="Contribution ID's" />
                        <Stack gap={2}>
                          <Grid container>
                            <Grid xs>
                              <Labeled label="Tin">
                                <Typography>{idformatter(employeeData.governmentId.tinNo,'tin')}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="SSS">
                                <Typography>{idformatter(employeeData.governmentId.sssNo,'sss')}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Grid container>
                            <Grid xs>
                              <Labeled label="Philhealth">
                                <Typography>{idformatter(employeeData.governmentId.pagIbigNo,'philhealth')}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Pagibig">
                                <Typography>{idformatter(employeeData.governmentId.philHealthNo,'pagibig')}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                        </Stack>
                      </Stack>
                    </Widget>

                    {/* Salary & Allowances */}
                    <Widget variant="outlined" sx={{flex: 1.3}}>
                      <Stack gap={2}>
                        <SectionHeader icon={Payments} title="Salary & Allowances" />
                        <Stack gap={2}>
                          <Grid container gap={3}>
                            <Grid xs>
                              <Labeled label="Base Salary">
                                <Typography>{formatterWhole.format(employeeData.basicSalary)}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Grid container>
                            {employeeData.benefits && employeeData.benefits.map((val) => (
                                <Grid key={val.benefitId} xs>
                                  <Labeled label={val.benefitType.benefit}>
                                    <Typography>{formatterWhole.format(val.amount)}</Typography>
                                  </Labeled>
                                </Grid>
                            ))}
                          </Grid>
                        </Stack>
                      </Stack>
                    </Widget>
                  </Stack>
                </Stack>
              </Stack>
          )}
        </Stack>
      </Container>
  );
}