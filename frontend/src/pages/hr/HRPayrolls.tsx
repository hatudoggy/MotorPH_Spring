import { Box, Button, Card, CardActionArea, CardContent, Container, MenuItem, Paper, PaperProps, Select, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Table from "../../components/Table";
import { ChevronRight, PointOfSale } from "@mui/icons-material";
import { API, BASE_API } from "../../api/Api.ts";
import axios from "axios";
import { useQuery } from "@tanstack/react-query";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import { LineChart, areaElementClasses } from "@mui/x-charts";

//TODO: implement this

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

  const pData = [2400, 1398, 9800, 3908, 4800, 3800, 4300];
  const amtData = [2400, 2210, 0, 2000, 2181, 2500, 2100];
  const xLabels = [
    'Page A',
    'Page B',
    'Page C',
    'Page D',
    'Page E',
    'Page F',
    'Page G',
  ];

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>
        <Headertext>HR Payroll</Headertext>
        <Stack>
          <Widget variant="outlined" sx={{width: 'min-content'}}>
            <LineChart 
              width={500}
              height={200}
              series={[
                { data: pData, label: 'Deductions', area: true, stack: 'total', showMark: true },
                {
                  data: amtData,
                  label: 'Earnings',
                  area: true,
                  stack: 'total',
                  showMark: true,
                },
              ]}
              xAxis={[{ scaleType: 'point', data: xLabels }]}
              sx={{
                [`& .${areaElementClasses.root}`]: {
                  opacity: 0.2
                },
              }}
            />
          </Widget>
        </Stack>
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


            <Button 
              variant="contained"
              sx={{
                borderRadius: 2
              }}
              disableElevation
              startIcon={<PointOfSale />}
            >
              Generate
            </Button>
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
    <Stack>
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
          end_date: filterMonth || ''
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

  const payrollData = flattenPayrollData(data || []);

  return(
    <Widget 
      variant="outlined"
      sx={{
        flex: 1
      }}
    >
      {
          payrollData &&
          <Table
              colSizes={[1, 1, 2.5, true, true, true, true, true,true, true,true]}
              tableData={payrollData}
              colHeader={[
                "PID",
                "EID",
                "Name",
                "Monthly Rate",
                "Gross Income",
                "Benefits",
                "Deductions",
                "Net Pay"
              ]}
          />
      }

    </Widget>
  )
}


const Widget = styled(Paper)<PaperProps>(({}) => ({
  borderRadius: 12,
  padding: 16,
}))

const flattenPayrollData = (data: PayrollRes[]) => {
  return data.map((item) => {
    const { employeeId, firstName, lastName } = item.employee;
    const { payrollId, monthlyRate, grossIncome, totalBenefits, totalDeductions, netPay } = item;

    return {
      PID: payrollId,
      EID: employeeId,
      Name: `${firstName}, ${lastName}`,
      'Monthly Rate': formatToPeso(monthlyRate),
      Gross: formatToPeso(grossIncome),
      Benefits: formatToPeso(totalBenefits),
      Deductions: formatToPeso(totalDeductions),
      'Net Pay': formatToPeso(netPay),
    };
  });
};


const formatToPeso = (value: number) => {
  return `₱${value.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')}`;
};
