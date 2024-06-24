import { Box, Button, Card, CardActionArea, CardContent, CircularProgress, Container, Divider, IconButton, MenuItem, Paper, Select, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import { ArrowBack, ArrowRight, Download, East } from "@mui/icons-material";
import { PieChart, useDrawingArea } from "@mui/x-charts";
import { API, BASE_API } from "../../constants/Api";
import axios from "axios";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { formatterWhole } from "../../utils/utils";
import { useAuth } from "../../hooks/AuthProvider";



export default function Payroll() {

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>

      <Headertext>Payroll</Headertext>
      <PayrollMonthList />

      </Stack>
    </Container>
  )
}


function PayrollMonthList() {

  const {authUser} = useAuth()
  const employeeId = authUser?.employeeId

  const [selectedPayroll, setSelectedPayroll] = useState<number | null>(null)

  const fetchPayroll = async() => {
    const {EMPLOYEES, PAYROLLS} = API
    const res = await axios.get(BASE_API + EMPLOYEES.BASE + employeeId + PAYROLLS.ALL)
    return res.data;
  }

  const {isPending, data} = useQuery<PayrollRes[]>({
    queryKey: ['payrolls'],
    queryFn: fetchPayroll
  })
  
  console.log(data)

  return(
    <Stack flex={1} gap={2}>
      <Select 
        defaultValue='2024' 
        size="small" 
        sx={{
          width: 130,
          borderRadius: 3,
          bgcolor: 'white'
        }}
      >
        <MenuItem value="2024">2024</MenuItem>
        <MenuItem value="2023">2023</MenuItem>
        <MenuItem value="2022">2022</MenuItem>
      </Select>
      <Stack flex={1} direction='row' gap={2}>
        <Stack flex={1} height='min-content' direction='row' flexWrap='wrap' gap={2}>
          {
            !isPending ?
              data && data.map((item)=>
                <PayrollCard 
                  key={item.payrollId}
                  periodEnd={item.periodEnd}
                  grossIncome={item.grossIncome}
                  onClick={()=>setSelectedPayroll(item.payrollId)}
                />
              )
              :
              <Stack alignItems='center'>
                <CircularProgress />
              </Stack>
          }
        </Stack>

        <PayrollSelect selectedPayroll={selectedPayroll} goBack={()=>setSelectedPayroll(null)}/>
      </Stack>

    </Stack>
  )
}


interface PayrollCard {
  periodEnd: string
  grossIncome: number
  onClick?: () => void
}

function PayrollCard({periodEnd, grossIncome, onClick}: PayrollCard) {

  const month = new Date(periodEnd).toLocaleString('default', { month: 'long' })
  const endDate = new Date(periodEnd).toLocaleDateString()

  return(
    <Card
      variant="outlined"
      sx={{
        flex: 'none',
        borderRadius: 3,
        width: 250,
        height: 110,
      }}
    >
      <CardActionArea onClick={onClick}>
        <CardContent
          sx={{
            height: 110,
            position: 'relative'
          }}
        >
          <Stack direction="row" justifyContent='space-between'>
            <Typography variant="h5" fontWeight={500}>{month}</Typography>
            <Typography variant="body1" fontWeight={500}>{formatterWhole.format(grossIncome)}</Typography>
          </Stack>
          <Typography variant="body2" color="GrayText">{endDate}</Typography>
          <East 
            sx={{
              position: 'absolute',
              right: 18,
              bottom: 12,
              color: 'GrayText'
            }}
          />
        </CardContent>
      </CardActionArea>
    </Card>
  )
}


interface PayrollSelect {
  selectedPayroll: number | null
  goBack?: () => void
}

function PayrollSelect({selectedPayroll, goBack}: PayrollSelect) {

  const palette = ['#000000', '#575757', '#c4c4c4']

  const fetchPayrollById = async() => {
    const { PAYROLLS} = API
    const res = await axios.get(BASE_API + PAYROLLS.BASE + selectedPayroll)
    return res.data;
  }

  const {isPending, data} = useQuery<PayrollFull>({
    queryKey: ['payroll'],
    queryFn: fetchPayrollById,
    enabled: !!selectedPayroll
  })

  if(!data) {
    return
  }

  const totalEarnings = data.employee.employment.basicSalary
  const totalDeductions = data.deductions.reduce(
    (acc, currVal) => acc + currVal.amount,
    0
  )
  const totalGross = (totalEarnings && totalDeductions) && totalEarnings - totalDeductions

  const month = new Date(data.periodEnd).toLocaleString('default', { month: 'long' })

  return(
    selectedPayroll && data &&
      <Paper
        variant="outlined"
        sx={{
          flex: 'none',
          borderRadius: 3,
          width: 330,
          minHeight: 460,
          height: 'min-content'
        }}
      >
          <Stack pt={1.5} pl={1} direction='row' alignItems='center' gap={0.5}>
            <IconButton onClick={goBack}>
              <ArrowBack />
            </IconButton>
            <Stack direction='row' flex={1} pr={2}  alignItems='center' justifyContent='space-between'>
              <Typography variant="h6">Payslip Summary</Typography>
              <Typography color='GrayText'>{month}</Typography>
            </Stack>
          </Stack>
        <Stack px={3} pb={3} gap={1.5}>
          <Stack alignItems='center'>
            <PieChart 
              colors={palette}
              series={[
                {
                  data: [
                    { id: 0, value: totalEarnings, label: 'earnings'},
                    { id: 1, value: totalDeductions, label: 'deductions'},
                  ],
                  innerRadius: 59,
                  outerRadius: 70,
                  paddingAngle: 2,
                  cornerRadius: 3,
                  cx: 85,
                  cy: 85,
                }
              ]}
              width={180}
              height={180}
              slotProps={{
                legend: { hidden: true },
              }}
            >
              <PieCenterLabel>
                {formatterWhole.format(totalGross)}
              </PieCenterLabel>
            </PieChart>
          </Stack>

          <Stack direction="row" justifyContent='space-evenly'>
            <Stack alignItems='center'>
              <Typography fontWeight={500}>{formatterWhole.format(totalEarnings)}</Typography>
              <Typography variant="body2" color="GrayText">Earnings</Typography>
            </Stack>
            <Stack alignItems='center'>
              <Typography fontWeight={500}>{formatterWhole.format(totalDeductions)}</Typography>
              <Typography variant="body2" color="GrayText">Deductions</Typography>
            </Stack>
          </Stack>

          <Stack gap={1}>
            <Typography fontWeight={600}>Details</Typography>
            <Stack px={1}>
              <Stack direction='row' justifyContent='space-between'>
                <Typography color="GrayText">Basic Salary</Typography>
                <Typography>{formatterWhole.format(totalEarnings)}</Typography>
              </Stack>
              {
                data.deductions.map((item)=>
                  <Stack key={item.deductionId} direction='row' justifyContent='space-between'>
                    <Typography color="GrayText" noWrap maxWidth={160}>{item.deductionType.name}</Typography>
                    <Typography>- {formatterWhole.format(item.amount)}</Typography>
                  </Stack>
                )
              }
              <Divider sx={{mt: 1}}/>
              <Stack mt={1} direction='row' justifyContent='space-between'>
                <Typography fontWeight={500}>Gross Pay</Typography>
                <Typography fontWeight={500}>{formatterWhole.format(totalGross)}</Typography>
              </Stack>
            </Stack>
          </Stack>
          
          <Button 
            variant="contained"
            sx={{
              borderRadius: 2,
              mt: 1
            }}
            startIcon={<Download />}
            disableElevation
          >
            Save Receipt
          </Button>
        </Stack>
      </Paper>
  )
}


const StyledText = styled('text')(({ theme }) => ({
  fill: theme.palette.text.primary,
  textAnchor: 'middle',
  dominantBaseline: 'central',
  fontSize: 24,
  fontWeight: 500
}));

function PieCenterLabel({ children }: { children: React.ReactNode }) {
  const { height, top } = useDrawingArea();
  return (
    <StyledText x={180 / 2} y={top + height / 2}>
      {children}
    </StyledText>
  );
}