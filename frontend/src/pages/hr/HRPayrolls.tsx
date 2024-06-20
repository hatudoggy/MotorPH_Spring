import { Box, Button, Card, CardActionArea, CardContent, Container, MenuItem, Paper, PaperProps, Select, Stack, Typography, styled } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Table from "../../components/Table";
import { ChevronRight } from "@mui/icons-material";


export default function HRPayrolls() {

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Stack height='100%'>
        <Headertext>HR Payroll</Headertext>
        <Stack gap={2} flex={1}>
          <Stack
            direction='row'
            justifyContent='space-between'
          >
            <Select defaultValue='2024' size="small" sx={{width: 130}}>
              <MenuItem value="2024">2024</MenuItem>
              <MenuItem value="2023">2023</MenuItem>
              <MenuItem value="2022">2022</MenuItem>
            </Select>

            <Button 
              variant="contained"
              sx={{
                borderRadius: 2
              }}
            >
              Generate
            </Button>
          </Stack>
          <Stack
            direction='row'
            flex={1}
            gap={2}
          >
            <PayrollMonthList />
            <PayrollTable />
          </Stack>
        </Stack>
      </Stack>
      </Container>
  )
}


function PayrollMonthList() {

  const months = [
    {
      date: '2024-05-01'
    },
  ]

  return(
    <Stack>
      {
        months.map((item)=>
          <PayrollMonthItem 
            key={item.date}
            date={item.date}
          />
        )
      }
    </Stack>
  )
}

interface PayrollMonthItem {
  date: string
}

function PayrollMonthItem({date}: PayrollMonthItem) {
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
      <CardActionArea>
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


function PayrollTable() {

  const payrollData: PayrollRes[] = [
    {
      payrollId: 1,
      employeeId: 1,
      periodStart: '2024-05-01',
      periodEnd: '2024-05-30',
      monthlyRate: 15125,
      dailyRate: 214214,
      overtimePay: 0,
      grossIncome: 1251251,
      netIncome: 125125
    }
  ]

  const tableData = payrollData.map(({monthlyRate, dailyRate, periodStart, periodEnd, ...rest})=>rest)

  return(
    <Widget 
      variant="outlined"
      sx={{
        flex: 1
      }}
    >
      <Table 
        colSizes={[1.5, true, true, true, true]}
        colHeader={[
          "Payroll Id", 
          "Employee",
          "Overtime Pay",
          "Gross Income",
          "Net Income"
        ]}
        tableData={tableData}
      />
    </Widget>
  )
}


const Widget = styled(Paper)<PaperProps>(({}) => ({
  borderRadius: 12,
  padding: 16,
}))