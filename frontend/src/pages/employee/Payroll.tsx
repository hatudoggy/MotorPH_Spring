import { useState, useEffect } from 'react';
import {
    Box, Button, Card, CardActionArea, CardContent, Container, Divider, IconButton, MenuItem, Paper, Select, Stack,
    Typography, styled
} from "@mui/material";
import { ArrowBack, East, Download } from "@mui/icons-material";
import { PieChart, useDrawingArea } from "@mui/x-charts";
import Headertext from "../../components/HeaderText";
import { useAuth } from "../../hooks/AuthProvider";
import { formatterWhole } from "../../utils/utils";
import {useEmployeePayrollData, useFetchPayrollById} from "../../hooks/UseFetch.ts";
import {LoadingOrError} from "../../hooks/Errors.tsx";

// Constants
const CHART_PALETTE = ['#000000', '#575757', '#c4c4c4'];
const CHART_DIMENSIONS = { width: 180, height: 180 };
const YEARS = ["2024", "2023", "2022"];

// Utility functions
const formatMonth = (date) => new Date(date).toLocaleString('default', { month: 'long' });
const formatDate = (date) => new Date(date).toLocaleDateString();

// Styled components
const StyledText = styled('text')(({ theme }) => ({
    fill: theme.palette.text.primary,
    textAnchor: 'middle',
    dominantBaseline: 'central',
    fontSize: 24,
    fontWeight: 500
}));

const PieCenterLabel = ({ children }) => {
    const { height, top } = useDrawingArea();
    return (
        <StyledText x={CHART_DIMENSIONS.width / 2} y={top + height / 2}>
            {children}
        </StyledText>
    );
};

// Main Payroll component
export default function Payroll() {
    return (
        <Container sx={{ my: 5 }}>
            <Stack height='100%'>
                <Headertext>Payroll</Headertext>
                <PayrollMonthList />
            </Stack>
        </Container>
    );
}

// PayrollMonthList component
function PayrollMonthList() {
    const { authUser } = useAuth();
    const employeeId = authUser?.employeeId;
    const [selectedPayroll, setSelectedPayroll] = useState(null);
    const [selectedYear, setSelectedYear] = useState(YEARS[0]);

    if (!employeeId) {
        console.error("Employee ID not found");
        return <Typography>Error: Employee ID not found</Typography>;
    }

    const { employee, payrolls, isLoading, error } = useEmployeePayrollData(employeeId);

    useEffect(() => {
        console.log('Employee Payroll Data:', {
            employee,
            payrolls,
            isLoading,
            error,
            payrollsIsArray: Array.isArray(payrolls),
            payrollsLength: payrolls ? payrolls.length : 'N/A'
        });
    }, [employee, payrolls, isLoading, error]);

    const handlePayrollSelect = (payrollId) => {
        setSelectedPayroll(payrollId);
        console.log('Selected Payroll ID:', payrollId);
    };

    return (
        <Stack flex={1} gap={2}>
            <LoadingOrError
                isLoading={isLoading}
                error={error}
                errorMessage="Error occurred while fetching data"
            />

            {!isLoading && !error && (
                <>
                    <Select
                        value={selectedYear}
                        onChange={(e) => setSelectedYear(e.target.value)}
                        size="small"
                        sx={{
                            width: 130,
                            borderRadius: 3,
                            bgcolor: 'white'
                        }}
                    >
                        {YEARS.map(year => (
                            <MenuItem key={year} value={year}>{year}</MenuItem>
                        ))}
                    </Select>
                    <Stack flex={1} direction='row' gap={2}>
                        <Stack flex={1} height='min-content' direction='row' flexWrap='wrap' gap={2}>

                            {Array.isArray(payrolls) && payrolls.length > 0 ? (
                                payrolls.map((item) => (
                                    <PayrollCard
                                        key={item.payrollId}
                                        periodEnd={item.periodEnd}
                                        grossIncome={item.grossIncome}
                                        onClick={() => handlePayrollSelect(item.payrollId)}
                                    />
                                ))
                            ) : (
                                <Typography>No payroll data available.</Typography>
                            )}

                        </Stack>
                        <PayrollSelect
                            selectedPayroll={selectedPayroll}
                            goBack={() => setSelectedPayroll(null)}
                        />
                    </Stack>
                </>
            )}



        </Stack>
    );
}

// PayrollCard component
function PayrollCard({ periodEnd, grossIncome, onClick }) {
    const month = formatMonth(periodEnd);
    const endDate = formatDate(periodEnd);

    return (
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
    );
}

// PayrollSelect component
function PayrollSelect({ selectedPayroll, goBack }) {
    if(selectedPayroll != null){
        const { data: selectedPayrollData, error, isLoading } = useFetchPayrollById(selectedPayroll);

        useEffect(() => {
            console.log('Fetching data for selected payroll ID:', selectedPayroll);
        }, [selectedPayroll]);

        useEffect(() => {
            console.log('Payroll data for payroll ID:', selectedPayroll, { selectedPayrollData, error, isLoading });
        }, [selectedPayrollData, error, isLoading]);

        if (!selectedPayroll) return null;

        return (
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
                <LoadingOrError
                    isLoading={isLoading}
                    error={error}
                    errorMessage="Error occurred while fetching payroll data"
                />

                {!isLoading && !error && selectedPayrollData && (
                    <>
                        <Stack pt={1.5} pl={1} direction='row' alignItems='center' gap={0.5}>
                            <IconButton onClick={goBack}>
                                <ArrowBack />
                            </IconButton>
                            <Stack direction='row' flex={1} pr={2} alignItems='center' justifyContent='space-between'>
                                <Typography variant="h6">Payslip Summary</Typography>
                                <Typography color='GrayText'>{formatMonth(selectedPayrollData.periodEnd)}</Typography>
                            </Stack>

                        </Stack>
                        <PayrollDetails payrollData={selectedPayrollData} />
                        <Stack px={3} pb={3} gap={1.5}>
                        <Button
                            variant="contained"
                            sx={{
                                borderRadius: 2,
                                mt: 1,
                            }}
                            startIcon={<Download />}
                            disableElevation
                        >
                            Save Receipt
                        </Button>
                        </Stack>
                    </>
                )}
            </Paper>
        );
    }
}

// PayrollDetails component
function PayrollDetails({ payrollData }) {
    return (
        <Stack px={3} pb={3} gap={1.5}>
            <Stack alignItems='center'>
                <PieChart
                    colors={CHART_PALETTE}
                    series={[
                        {
                            data: [
                                { id: 0, value: payrollData.grossIncome, label: 'gross' },
                                { id: 1, value: payrollData.totalBenefits, label: 'benefits' },
                                { id: 2, value: payrollData.totalDeductions, label: 'deductions' },
                            ],
                            innerRadius: 59,
                            outerRadius: 70,
                            paddingAngle: 2,
                            cornerRadius: 3,
                            cx: 85,
                            cy: 85,
                        }
                    ]}
                    width={CHART_DIMENSIONS.width}
                    height={CHART_DIMENSIONS.height}
                    slotProps={{
                        legend: { hidden: true },
                    }}
                >
                    <PieCenterLabel>
                        {formatterWhole.format(payrollData.netPay)}
                    </PieCenterLabel>
                </PieChart>
            </Stack>

            <Stack direction="row" justifyContent='space-evenly'>
                <Stack alignItems='center'>
                    <Typography fontWeight={500}>{formatterWhole.format(payrollData.grossIncome)}</Typography>
                    <Typography variant="body2" color="GrayText">Gross Income</Typography>
                </Stack>
                <Stack alignItems='center'>
                    <Typography fontWeight={500}>{formatterWhole.format(payrollData.totalBenefits)}</Typography>
                    <Typography variant="body2" color="GrayText">Benefits</Typography>
                </Stack>
                <Stack alignItems='center'>
                    <Typography fontWeight={500}>{formatterWhole.format(payrollData.totalDeductions)}</Typography>
                    <Typography variant="body2" color="GrayText">Deductions</Typography>
                </Stack>
            </Stack>

            <Stack gap={1}>
                <Typography fontWeight={600}>Earnings</Typography>
                <Stack px={1}>
                    <Stack direction='row' justifyContent='space-between'>
                        <Typography color="GrayText">Monthly Rate</Typography>
                        <Typography>{formatterWhole.format(payrollData.monthlyRate)}</Typography>
                    </Stack>
                    <Stack direction='row' justifyContent='space-between'>
                        <Typography color="GrayText">Hourly Rate</Typography>
                        <Typography>{formatterWhole.format(payrollData.hourlyRate)}/hr</Typography>
                    </Stack>
                    <Stack direction='row' justifyContent='space-between'>
                        <Typography color="GrayText">Hours Worked</Typography>
                        <Typography>{(payrollData.hoursWorked)} hr</Typography>
                    </Stack>
                    <Stack direction='row' justifyContent='space-between'>
                        <Typography color="GrayText">Overtime Rate</Typography>
                        <Typography>{formatterWhole.format(payrollData.overtimeRate)}/hr</Typography>
                    </Stack>
                    <Stack direction='row' justifyContent='space-between'>
                        <Typography color="GrayText">Overtime Hours</Typography>
                        <Typography>{(payrollData.overtimeHours)} hr</Typography>
                    </Stack>
                </Stack>

                <Typography fontWeight={600}>Benefits</Typography>

                <Stack px={1}>
                {
                        payrollData.benefits.map((item)=>
                            <Stack key={item.benefitId} direction='row' justifyContent='space-between'>
                                <Typography color="GrayText" noWrap maxWidth={160}>{item.benefitType.benefit}</Typography>
                                <Typography> {formatterWhole.format(item.amount)}</Typography>
                            </Stack>
                        )
                    }
                </Stack>

                <Typography fontWeight={600}>Deductions</Typography>

                <Stack px={1}>
                {
                        payrollData.deductions.map((item)=>
                            <Stack key={item.deductionId} direction='row' justifyContent='space-between'>
                                <Typography color="GrayText" noWrap maxWidth={160}>{item.deductionType.deductionCode}</Typography>
                                <Typography>- {formatterWhole.format(item.amount)}</Typography>
                            </Stack>
                        )
                    }
                    <Divider sx={{mt: 1}}/>
                    <Stack mt={1} direction='row' justifyContent='space-between'>
                        <Typography fontWeight={500}>Net Pay</Typography>
                        <Typography fontWeight={500}>{formatterWhole.format(payrollData.netPay)}</Typography>
                    </Stack>
                </Stack>
            </Stack>
        </Stack>
    );
}
