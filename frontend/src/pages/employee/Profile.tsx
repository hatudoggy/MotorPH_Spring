
import { Avatar, Button, Container, Paper, PaperProps, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Labeled from "../../components/Labeled";
import { Badge, Edit, Payments, Person, Work } from "@mui/icons-material";
import Grid from "@mui/material/Unstable_Grid2/Grid2";
import { API, BASE_API } from "../../constants/Api";
import { useQuery } from "@tanstack/react-query";
import { calculateAge, formatterWhole, idformatter } from "../../utils/utils";
import { Shadows } from "../../constants/Shadows";


const employeeId = 1

export default function Profile() {

  const fetchEmployee = async() => {
    const {EMPLOYEES} = API
    const res = await fetch(BASE_API + EMPLOYEES.BASE + employeeId)
    return res.json();
  }

  const {isPending, data} = useQuery<EmployeeRes>({
    queryKey: ['employee'],
    queryFn: fetchEmployee
  })


  const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s"

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>

        <Headertext>Profile</Headertext>

        {
          data && 
            <Stack mt={2} px={5} gap={4}>
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
                    <Typography variant="h5" fontWeight={500}>{`${data.firstName} ${data.lastName}`}</Typography>
                    <Typography>{data.employment.position.positionName}</Typography>
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
              
              <Stack gap={2}>

                <Stack direction='row' gap={2}>
                  <Widget variant="outlined">
                    <Stack gap={2}>


                      <Stack direction='row' alignItems='end' gap={0.8}>
                        <Person />
                        <Typography fontWeight={500}>
                          Personal Information
                        </Typography>
                      </Stack>

                      <Stack gap={2}>
                        <Grid container>
                          <Grid xs>
                            <Labeled label="Birthdate">
                              <Typography>{new Date(data.dob).toLocaleDateString()}</Typography>
                            </Labeled>
                          </Grid>
                          <Grid xs>
                            <Labeled label="Age">
                              <Typography>{calculateAge(data.dob)} yrs. old</Typography>
                            </Labeled>
                          </Grid>
                          <Grid xs={5}>
                            <Labeled label="Phone No.">
                              <Typography>{data.contacts[0].contactNo}</Typography>
                            </Labeled>
                          </Grid>
                        </Grid>

                        <Labeled label="Address">
                          <Typography>{data.address}</Typography>
                        </Labeled>
                      </Stack>

                    </Stack>
                  </Widget>

                  <Widget variant="outlined" sx={{flex: 1.3}}>
                    <Stack gap={2}>
                      <Stack direction='row' alignItems='center' gap={0.8}>
                        <Work fontSize="small"/>
                        <Typography fontWeight={500}>
                          Employment Details
                        </Typography>
                      </Stack>

                      <Stack gap={2}>
                        <Grid container gap={3}>
                          <Grid xs={4}>
                            <Labeled label="Position">
                              <Typography>{data.employment.position.positionName}</Typography>
                            </Labeled>
                          </Grid>
                          <Grid xs>
                            <Labeled label="Department">
                              <Typography>{data.employment.department.departmentName}</Typography>
                            </Labeled>
                          </Grid>
                          <Grid xs>
                            <Labeled label="Supervisor">
                              <Typography>{`${data.employment.supervisor.firstName} ${data.employment.supervisor.lastName}`}</Typography>
                            </Labeled>
                          </Grid>
                        </Grid>


                        <Grid container gap={3}>
                          <Grid xs={4}>
                            <Labeled label="Hire Date">
                              <Typography>{new Date(data.employment.hireDate).toLocaleDateString()}</Typography>
                            </Labeled>
                          </Grid>
                          <Grid xs>
                            <Labeled label="Employment Status">
                              <Typography>{data.employment.status.status}</Typography>
                            </Labeled>
                          </Grid>

                        </Grid>

                      </Stack>

                    </Stack>
                  </Widget>
                </Stack>

                <Stack direction='row' gap={2}>
                  <Widget variant="outlined">
                    <Stack gap={2}>


                      <Stack direction='row' alignItems='center' gap={0.8}>
                        <Badge  fontSize="small"/>
                        <Typography fontWeight={500}>
                          Contribution ID's
                        </Typography>
                      </Stack>

                      <Stack gap={2}>
                        <Grid container>
                          <Grid xs>
                            <Labeled label="Tin">
                              <Typography>{idformatter(data.governmentId.tinNo,'tin')}</Typography>
                            </Labeled>
                          </Grid>
                          <Grid xs>
                            <Labeled label="SSS">
                              <Typography>{idformatter(data.governmentId.sssNo,'sss')}</Typography>
                            </Labeled>
                          </Grid>

                        </Grid>

                        <Grid container>

                          <Grid xs>
                            <Labeled label="Philhealth">
                              <Typography>{idformatter(data.governmentId.pagIbigNo,'philhealth')}</Typography>
                            </Labeled>
                          </Grid>

                          <Grid xs>
                            <Labeled label="Pagibig">
                              <Typography>{idformatter(data.governmentId.philHealthNo,'pagibig')}</Typography>
                            </Labeled>
                          </Grid>

                        </Grid>

                      </Stack>

                    </Stack>
                  </Widget>

                  <Widget variant="outlined" sx={{flex: 1.3}}>
                    <Stack gap={2}>
                      <Stack direction='row' alignItems='center' gap={0.8}>
                        <Payments fontSize="small"/>
                        <Typography fontWeight={500}>
                          Salary & Allowances
                        </Typography>
                      </Stack>

                      <Stack gap={2}>
                        <Grid container gap={3}>
                          <Grid xs>
                            <Labeled label="Base Salary">
                              <Typography>{formatterWhole.format(data.employment.basicSalary)}</Typography>
                            </Labeled>
                          </Grid>
  
                        </Grid>


                        <Grid container>
                          {
                            data.benefits.map((val)=>
                              <Grid key={val.benefitId} xs>
                                <Labeled label={val.benefitType.benefit}>
                                  <Typography>{formatterWhole.format(val.amount)}</Typography>
                                </Labeled>
                              </Grid>
                            )
                          }
                        </Grid>

                      </Stack>

                    </Stack>
                  </Widget>
                </Stack>
              </Stack>
              
            </Stack>
        }


      </Stack>
    </Container>
  )
}




const Widget = styled(Paper)<PaperProps>(({}) => ({
  flex: 1,
  borderRadius: 12,
  padding: 20,
  minHeight: 250,
}))


