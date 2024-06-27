import {Avatar, Button, Container, Paper, PaperProps, Stack, Typography, styled, CircularProgress} from "@mui/material";
import Headertext from "../../components/HeaderText";
import Labeled from "../../components/Labeled";
import { Badge, Edit, Payments, Person, Work } from "@mui/icons-material";
import Grid from "@mui/material/Unstable_Grid2/Grid2";
import { calculateAge, formatterWhole, idformatter } from "../../utils/utils";
import { Shadows } from "../../constants/Shadows";
import { useAuth } from "../../hooks/AuthProvider";
import {
  useFetchEmployeeFullById,

} from "../../hooks/UseFetch.ts";
import {LoadingOrError} from "../../hooks/Errors.tsx";
import {useEffect} from "react";

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

  const { data: employee, isLoading: employeeLoading, error: employeeError } = useFetchEmployeeFullById(employeeId);

  useEffect(() => {
    console.log('Employee data:', {
      employee,
      employeeLoading,
      employeeError
       });
  }, [employee, employeeLoading, employeeError]);

  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s";

  return(
      <Container sx={{ my: 5 }}>
        <Stack height='100%'>

          <Headertext>Profile</Headertext>

          <LoadingOrError
              isLoading={employeeLoading}
              error={employeeError}
              errorMessage="Error occurred while fetching personal data"
          />

          {!employeeLoading && employee &&(
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
                      <Typography variant="h5" fontWeight={500}>{`${employee.firstName} ${employee.lastName}`}</Typography>
                      <Typography>{employee?.position.positionName}</Typography>
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
                                <Typography>{new Date(employee.dob).toLocaleDateString()}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Age">
                                <Typography>{calculateAge(employee.dob)} yrs. old</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs={5}>
                              <Labeled label="Phone No.">
                                <Typography>{employee.contacts.contactNumbers[0]}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Labeled label="Address">
                            <Typography>{employee.address}</Typography>
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
                                <Typography>{employee?.position.positionName}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Department">
                                <Typography>{employee?.department.departmentName}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Supervisor">
                                <Typography>{`${employee?.supervisor.firstName} ${employee?.supervisor.lastName}`}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Grid container gap={3}>
                            <Grid xs={4}>
                              <Labeled label="Hire Date">
                                <Typography>{new Date(employee.hireDate).toLocaleDateString()}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Employment Status">
                                <Typography>{employee.status.statusName}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Hire Date">
                                <Typography>{employee.hireDate}</Typography>
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
                                <Typography>{idformatter(employee.governmentId.tinNo,'tin')}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="SSS">
                                <Typography>{idformatter(employee.governmentId.sssNo,'sss')}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Grid container>
                            <Grid xs>
                              <Labeled label="Philhealth">
                                <Typography>{idformatter(employee.governmentId.pagIbigNo,'philhealth')}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Pagibig">
                                <Typography>{idformatter(employee.governmentId.philHealthNo,'pagibig')}</Typography>
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
                          <Grid container>
                            <Grid xs>
                              <Labeled label="Base Salary">
                                <Typography>{formatterWhole.format(employee.basicSalary)}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Semi-Monthly Rate">
                                <Typography>{formatterWhole.format(employee.semiMonthlyRate)}</Typography>
                              </Labeled>
                            </Grid>
                            <Grid xs>
                              <Labeled label="Hourly Rate">
                                <Typography>{formatterWhole.format(employee.hourlyRate)}</Typography>
                              </Labeled>
                            </Grid>
                          </Grid>
                          <Grid container>
                            {employee.benefits && employee.benefits.map((val) => (
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