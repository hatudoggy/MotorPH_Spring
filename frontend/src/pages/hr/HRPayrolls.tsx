import { Box, Button, Card, CardActionArea, CardContent, Container, Dialog, DialogActions, DialogContent, DialogTitle, MenuItem, Paper, PaperProps, Select, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Table from "../../components/Table";
import { AttachMoney, ChevronRight, PointOfSale, Widgets, WidgetsRounded, Workspaces } from "@mui/icons-material";
import { API, BASE_API } from "../../constants/Api";
import axios from "axios";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import { LineChart, areaElementClasses } from "@mui/x-charts";
import { DatePicker } from "@mui/x-date-pickers";
import { format, subMonths } from "date-fns";
import { formatterDecimal, formatterWhole } from "../../utils/utils";


export default function HRPayrolls() {

  const [filterYear, setFilterYear] = useState<number | undefined>()
  const [filterMonth, setFilterMonth] = useState<string | undefined>()

  const fetchPayrollYears = async() => {
    const {PAYROLLS} = API
    const res = await axios.get(BASE_API + PAYROLLS.ALL + PAYROLLS.YEARS)
    return res.data;
  }

  const {isPending, data} = useQuery<number[]>({
    queryKey: ['payrollYears'],
    queryFn: fetchPayrollYears
  })

  useEffect(()=>{
    if(data){
      setFilterYear(data[0])
    }
  }, [data])

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>
        <Headertext>HR Payroll</Headertext>
        <PayrollReport />
        <Stack gap={2} flex={1} mt={2}>
          <Stack
            direction='row'
            justifyContent='space-between'
          >
            {
              data && filterYear ? 
                <Select defaultValue={data[0]} value={filterYear} onChange={(e)=>setFilterYear(e.target.value as number)} size="small" sx={{width: 130}}>
                  {
                    data?.map((item)=>
                      <MenuItem key={item} value={item.toString()}>{item.toString()}</MenuItem>
                    )
                  }
                </Select>
                :
                <Box height={40}></Box>
            }


            <GeneratePayroll />
          </Stack>
          <Stack
            direction='row'
            flex={1}
            gap={2}
          >
            <PayrollMonthList filterYear={filterYear} setFilterMonth={setFilterMonth}/>
            <PayrollTable filterMonth={filterMonth} />
          </Stack>
        </Stack>
      </Stack>
      </Container>
  )
}

function PayrollReport() {
  const fetchPayrollReportMonthly = async() => {
    const {PAYROLLS} = API
      const res = await axios.get(BASE_API + PAYROLLS.REPORT_MONTHLY, {
        params: {
          startDate: format(subMonths(new Date(), 5), 'yyyy-MM-dd'),
          endDate: format(subMonths(new Date(), 1), 'yyyy-MM-dd')
        }
      } )
      return res.data;
  }

  const {isPending, data} = useQuery<PayrollReportMonthly[]>({
    queryKey: ['payrollReportMonthly'],
    queryFn: fetchPayrollReportMonthly,
  })

  const deductionsTotal = data ? data.map((item)=> item.totalDeductions) : []
  const earningsTotal = data ? data.map((item)=> item.totalEarnings) : []
  const monthLabels = data ? data.map((item)=> item.month) : []
  return(
    <Stack direction='row' gap={2}>
      <Widget variant="outlined" sx={{width: 'min-content'}}>
        <LineChart 
          width={500}
          height={200}
          series={[
            { data: deductionsTotal, label: 'Deductions', area: true, stack: 'total', showMark: true, color: '#737373'},
            { data: earningsTotal, label: 'Earnings', area: true, stack: 'total', showMark: true, color: '#292929' },
          ]}
          xAxis={[{ scaleType: 'point', data: monthLabels }]}
          sx={{
            [`& .${areaElementClasses.root}`]: {
              opacity: 0.2
            },
          }}
        />
      </Widget>
      <Stack gap={1}>
        <Widget variant="outlined" sx={{flex: 1, width: 200, position: 'relative'}}>
          <Box
            sx={{
              position: 'absolute',
              top: 15,
              right: 15,
              borderRadius: 2,
              display: 'grid',
              placeContent: 'center',
              fontSize: 22
            }}
          >
            <WidgetsRounded fontSize="inherit"/>
          </Box>
          <Stack height='100%' justifyContent='end'>
            <Typography variant="h5" fontSize={28}>
              {formatterWhole.format(earningsTotal[earningsTotal.length - 1])}
            </Typography>
            <Typography variant="body2" color="GrayText">Total Earnings</Typography>
            </Stack>
        </Widget>
        <Widget variant="outlined" sx={{flex: 1, width: 200, position: 'relative'}}>
          <Box
            sx={{
              position: 'absolute',
              top: 15,
              right: 15,
              borderRadius: 2,
              display: 'grid',
              placeContent: 'center',
              fontSize: 22
            }}
          >
            <Workspaces  fontSize="inherit"/>
          </Box>
          <Stack height='100%' justifyContent='end'>
            <Typography variant="h5" fontSize={28}>
              {formatterWhole.format(deductionsTotal[deductionsTotal.length - 1])}
            </Typography>
            <Typography variant="body2" color="GrayText">Total Deductions</Typography>
            </Stack>
        </Widget>
      </Stack>
    </Stack>
  )
}


function GeneratePayroll() {


  const [open, setOpen] = useState(false)


  return(
    <>
      <Button 
        variant="contained"
        sx={{
          borderRadius: 2
        }}
        disableElevation
        startIcon={<PointOfSale />}
        onClick={()=>setOpen(true)}
      >
        Generate
      </Button>
      <Dialog
        maxWidth="sm"
        fullWidth
        open={open}
        onClose={()=>setOpen(false)}
      >
        <GeneratePayrollDialog onClose={()=>setOpen(false)}/>
      </Dialog>
    </>
  )
}

interface GeneratePayrollDialog {
  onClose: () => void
}

function GeneratePayrollDialog({onClose}: GeneratePayrollDialog) {

  const queryClient = useQueryClient()

  const [startDate, setStartDate] = useState<Date | null>(null)
  const [endDate, setEndDate] = useState<Date | null>(null)

  const generatePayroll = async () => {
    const {PAYROLLS} = API
    if(startDate && endDate){
      const res = await axios.post(BASE_API + PAYROLLS.GENERATE, {
        startDate: format(startDate, 'yyyy-MM-dd'),
        endDate: format(endDate, 'yyyy-MM-dd')
      });
      return res.data;
    }
  }

  const useGeneratePayroll = useMutation({
    mutationFn: generatePayroll,
    onSettled: async () => {
      return await queryClient.invalidateQueries({ queryKey: ['attendanceToday']})
    }
  })

  const handleGeneratePayroll = () => {
    useGeneratePayroll.mutate()
    onClose()
  }

  return(
    <>
      <DialogTitle>Generate Payroll</DialogTitle>
      <DialogContent>
        <Stack direction='row' gap={2} mt={2}>
          <DatePicker
            label="Start Date"
            value={startDate}
            onChange={(e)=>setStartDate(e)}
            slotProps={{
              textField: {
                fullWidth: true
              }
            }}
          />
          <DatePicker
            label="End Date"
            value={endDate}
            onChange={(e)=>setEndDate(e)}
            slotProps={{
              textField: {
                fullWidth: true
              }
            }}
          />
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button variant="contained" onClick={()=>handleGeneratePayroll()}>Generate</Button>
      </DialogActions>
    </>
  )
}


interface PayrollMonthList {
  filterYear: number | undefined
  setFilterMonth: Dispatch<SetStateAction<string | undefined>>
}

function PayrollMonthList({filterYear, setFilterMonth}: PayrollMonthList) {

  const fetchPayrollYears = async() => {
    const {PAYROLLS} = API
    if(filterYear){
      const res = await axios.get(BASE_API + PAYROLLS.ALL + PAYROLLS.YEARS + `/${filterYear}`)
      return res.data;
    }
  }

  const {isPending, data} = useQuery<string[]>({
    queryKey: ['payrollMonths', filterYear],
    queryFn: fetchPayrollYears,
    enabled: !!filterYear
  })

  return(
    <Stack gap={1}>
      {
        data &&
          data.map((item)=>
            <PayrollMonthItem 
              key={item}
              date={item}
              onClick={()=>setFilterMonth(item)}
            />
          )
      }
    </Stack>
  )
}

interface PayrollMonthItem {
  date: string
  onClick: () => void
}

function PayrollMonthItem({date, onClick}: PayrollMonthItem) {
  const month = new Date(date).toLocaleString('default', { month: 'long' })

  return(
    <Card
      elevation={0}
      sx={{
        borderRadius:2,
        border: 'none',
        width: 170,
        position: 'relative'
      }}
    >
      <Box
        sx={{
          height: '100%',
          width: 5,
          bgcolor: 'primary.main',
          position: 'absolute'
        }}
      ></Box>
      <CardActionArea onClick={onClick}>
        <CardContent
          sx={{
            py: 0.5,
            pr: 1
          }}
        >
          <Stack direction='row' alignItems='center' justifyContent='space-between'>
            <Stack>
              <Typography variant="h6">{month}</Typography>
              <Typography variant="body2" color="GrayText">{date}</Typography>
            </Stack>
            <ChevronRight sx={{color: '#a6a6a6'}}/>
          </Stack>
        </CardContent>
      </CardActionArea>
    </Card>
  )
}


interface PayrollTable {
  filterMonth: string | undefined
}

function PayrollTable({filterMonth}: PayrollTable) {

  const fetchPayrollMonths = async() => {
    const {PAYROLLS} = API
    if(filterMonth){
      const res = await axios.get(BASE_API + PAYROLLS.ALL, {
        params: {
          date: filterMonth || ''
        }
      })
      return res.data;
    }
  }

  const {isPending, data} = useQuery<PayrollRes[]>({
    queryKey: ['payrollAll', filterMonth],
    queryFn: fetchPayrollMonths,
    enabled: !!filterMonth
  })

  const tableData = data && data.map(({
    dailyRate, periodStart, periodEnd, deductions, ...rest
  })=> ({
    employee: `${rest.employeeFirstName} ${rest.employeeLastName}`,
    monthlyRate: rest.monthlyRate,
    overtimePay: rest.overtimePay,
    grossIncome: rest.grossIncome,
    netIncome: rest.netIncome
  }))

  return(
    <Widget 
      variant="outlined"
      sx={{
        flex: 1
      }}
    >
      {
        tableData &&
          <Table 
            colSizes={[3.5, true, true, true, true]}
            colHeader={[ 
              "Employee",
              "Monthly Rate",
              "Overtime Pay",
              "Gross Income",
              "Net Income"
            ]}
            tableData={tableData}
            renderers={{
              employee: (item: string) => (
                <Stack direction='row' alignItems='center' gap={1.5}>
                  <Stack>
                    <Typography variant="body2" fontSize={16} >
                      {item}
                    </Typography>
                  </Stack>
                </Stack>
              ),
              monthlyRate: (item) => formatterDecimal.format(item),
              overtimePay: (item) => formatterDecimal.format(item),
              grossIncome: (item) => formatterDecimal.format(item),
              netIncome: (item) => formatterDecimal.format(item),
            }}
          />
      }

    </Widget>
  )
}


const Widget = styled(Paper)<PaperProps>(({}) => ({
  borderRadius: 12,
  padding: 16,
}))