import {
    AccessTime, Description, EventNote, ExpandLess, ExpandMore,
    FilterList,
} from "@mui/icons-material";
import {
    Box, Button, Container, Paper, Stack, Typography, styled,
    TextField, MenuItem, Collapse, Grid, Chip, Tooltip, IconButton, Card, CardContent, Fade,
    TableRow, TableCell,
    TableHead, TableContainer, TableBody, Table as MuiTable,

} from "@mui/material";
import Headertext from "../../components/HeaderText";
import { useAuth } from "../../hooks/AuthProvider";
import { useEffect, useState } from "react";
import LeaveRequestForm from "./components/LeaveRequestForm.tsx";
import { useFetchLeaveBalancesByEmployeeId, useFetchLeaveRequestsByEmployeeId } from "../../api/query/UseFetch.ts";
import {format, isWithinInterval, subDays} from "date-fns";

let employeeId: number = 0;

// Main LeaveRequest component
export default function LeaveRequest() {
    const { authUser } = useAuth();
    employeeId = authUser?.employeeId || 0;

    const [startDateFilter, setStartDateFilter] = useState<string>("");
    const [endDateFilter, setEndDateFilter] = useState<string>("");
    const [statusFilter, setStatusFilter] = useState<string>("");

    const [filteredData, setFilteredData] = useState<LeaveRequestRes[]>([]);
    const [leaveBalances, setLeaveBalances] = useState<LeaveBalanceRes[]>([]);

    if (!employeeId || employeeId === 0) {
        console.error("Employee ID not found");
        return <Typography>Error: Employee ID not found</Typography>;
    }

    const { data, isPending } = useFetchLeaveRequestsByEmployeeId(employeeId);
    const { data: balances } = useFetchLeaveBalancesByEmployeeId(employeeId);

    useEffect(() => {
        if (data) {
            let filteredData = data;
            if (startDateFilter) {
                filteredData = filteredData.filter(request => new Date(request.startDate) >= new Date(startDateFilter));
            }
            if (endDateFilter) {
                filteredData = filteredData.filter(request => new Date(request.endDate) <= new Date(endDateFilter));
            }
            if (statusFilter) {
                filteredData = filteredData.filter(request => request.status.status.toLowerCase() === statusFilter.toLowerCase());
            }
            setFilteredData(filteredData);
        }

        if (balances) {
            setLeaveBalances(balances);
        }
    }, [data, balances, startDateFilter, endDateFilter, statusFilter]);

    return (
        <Container maxWidth="lg" sx={{ my: 5 }}>
            <Stack spacing={4}>
                <Headertext>Leave Requests</Headertext>
                <Card elevation={3}>
                    <CardContent>
                        <Typography variant="h5" gutterBottom fontWeight={500}>Recent Leave Requests</Typography>
                        <LeaveOverview filteredData={filteredData} />
                    </CardContent>
                </Card>
                <Card elevation={3}>
                    <CardContent>
                        <Typography variant="h5" gutterBottom fontWeight={500}>Leave Request History</Typography>
                        <FilterBar startDate={startDateFilter} endDate={endDateFilter} status={statusFilter}
                                   setStartDate={setStartDateFilter} setEndDate={setEndDateFilter} setStatus={setStatusFilter} />
                        <LeaveRequestTable filteredData={filteredData} isPending={isPending} />
                    </CardContent>
                </Card>
                <Grid container columnGap={3}>
                    <Grid item xs={12} md={8.5}>
                        <Card elevation={3}>
                            <CardContent>
                                <Typography variant="h5" gutterBottom fontWeight={500}>Leave Request Form</Typography>
                                <LeaveRequestForm employeeId={employeeId} />
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid item xs={12} md={3}>
                        <Card elevation={3}>
                            <CardContent>
                                <Typography variant="h5" gutterBottom fontWeight={500}>Leave Balances</Typography>
                                <LeaveBalance leaveBalances={leaveBalances}/>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Stack>
        </Container>
    );
}


// LeaveBalance component for displaying available leave balances
function LeaveBalance({ leaveBalances }: { leaveBalances: LeaveBalanceRes[] }) {
    console.log("Rendering LeaveBalance");

    return (
        <Widget variant="outlined">
            <Stack spacing={1.5} marginTop={1}>
                {leaveBalances.map((balance) => (
                    <Stack direction='row' justifyContent='space-between' key={balance.leaveType.id}>
                        <Typography>{toSentenceCase(balance.leaveType.typeName)}</Typography>
                        <Typography>{balance.balance} days</Typography>
                    </Stack>
                ))}
            </Stack>
        </Widget>
    );
}

interface LeaveOverviewProps {
    filteredData: LeaveRequestRes[];
}

const LeaveOverview = ({ filteredData } : LeaveOverviewProps) => {
    const [expandedId, setExpandedId] = useState(0);

    const toggleExpand = (id : number) => setExpandedId(expandedId === id ? 0 : id);

    const today = new Date();
    const lastWeek = subDays(today, 7);
    const recentLeaves = filteredData.filter(request =>
        isWithinInterval(new Date(request.requestDate), { start: lastWeek, end: today })
    );

    return (
        <Stack spacing={2}>
            {recentLeaves.length > 0 ? (
                recentLeaves.slice(0, 5).map((request) => (
                    <Fade in={true} key={request.leaveRequestId}>
                        <Paper elevation={1} sx={{ p: 2, transition: 'all 0.3s' }}>
                            <Grid container spacing={2} alignItems="center" direction="row">
                                <Grid item xs={2} marginTop={1} marginLeft={2}>
                                    <Typography variant="h6" fontWeight={500}>
                                        <Chip
                                            label={request.leaveType.typeName}
                                            style={{ backgroundColor: getLeaveTypeColor(request.leaveType.typeName), color: 'white' }}
                                            size="medium"
                                        />
                                    </Typography>
                                </Grid>
                                <Grid item xs={2} marginTop={1}>
                                    <Box display="flex" alignItems="center">
                                        <StatusIndicator color={getStatusColor(request.status.status)} />
                                        <Typography>{request.status.status}</Typography>
                                    </Box>
                                </Grid>
                                <Grid item xs={3} marginTop={1}>
                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <EventNote fontSize="medium" color="action" />
                                        <Typography variant="body1">
                                            {dateFormatter(request.startDate)} - {dateFormatter(request.endDate)}
                                        </Typography>
                                    </Stack>
                                </Grid>
                                <Grid item xs={3} marginTop={1}>
                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <AccessTime fontSize="medium" color="action" />
                                        <Typography variant="body1">
                                            Requested on: {dateFormatter(request.requestDate)}
                                        </Typography>
                                    </Stack>
                                </Grid>
                                <Grid item xs={1}>
                                    <Tooltip title={expandedId === request.leaveRequestId ? "Hide Details" : "Show Details"}>
                                        <IconButton onClick={() => toggleExpand(request.leaveRequestId)} size="small">
                                            {expandedId === request.leaveRequestId ? <ExpandLess /> : <ExpandMore />}
                                        </IconButton>
                                    </Tooltip>
                                </Grid>
                                <Grid item xs={12} marginLeft={2}>
                                    <Collapse in={expandedId === request.leaveRequestId}>
                                        <Stack direction="row" spacing={1} alignItems="flex-start" sx={{ mt: 2 }}>
                                            <Description fontSize="small" color="action" sx={{ mt: 0.5 }} />
                                            <Typography variant="body2">
                                                Reason: {request.reason || 'No reason provided'}
                                            </Typography>
                                        </Stack>
                                    </Collapse>
                                </Grid>
                            </Grid>
                        </Paper>
                    </Fade>
                ))
            ) : (
                <Box display="flex" justifyContent="center" alignItems="center" height="50px">
                    <Typography variant="body1">No recent leave requests</Typography>
                </Box>)}
        </Stack>
    );
};


interface FilterBarProps {
    startDate: string;
    endDate: string;
    status: string;
    setStartDate: (date: string) => void;
    setEndDate: (date: string) => void;
    setStatus: (status: string) => void;
}

function FilterBar({ startDate, endDate, status, setStartDate, setEndDate, setStatus } : FilterBarProps) {
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <Box mb={3}>
            <Button
                startIcon={<FilterList />}
                onClick={() => setIsExpanded(!isExpanded)}
                variant="outlined"
                sx={{ mb: 2 }}
            >
                {isExpanded ? 'Hide Filters' : 'Show Filters'}
            </Button>
            <Collapse in={isExpanded}>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mt: 2 }}>
                    <TextField
                        label="Start Date"
                        type="date"
                        InputLabelProps={{ shrink: true }}
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        fullWidth
                    />
                    <TextField
                        label="End Date"
                        type="date"
                        InputLabelProps={{ shrink: true }}
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        fullWidth
                    />
                    <TextField
                        select
                        label="Status"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        fullWidth
                    >
                        <MenuItem value="">All</MenuItem>
                        <MenuItem value="Pending">Pending</MenuItem>
                        <MenuItem value="Approved">Approved</MenuItem>
                        <MenuItem value="Rejected">Rejected</MenuItem>
                    </TextField>
                </Stack>
            </Collapse>
        </Box>
    );
}

interface LeaveRequestTableProps {
    filteredData: LeaveRequestRes[];
    isPending: boolean;
}


// LeaveRequestTable component for displaying leave request history
function LeaveRequestTable({ filteredData, isPending }: LeaveRequestTableProps) {
    console.log("Rendering LeaveRequestTable");

    // Process table data
    filteredData
        .map(({ leaveRequestId, employee, ...rest }) => ({
            requestDate: rest.requestDate,
            type: rest.leaveType.typeName,
            status: rest.status.status,
            startDate: rest.startDate,
            endDate: rest.endDate
        }));

    return (
        <TableContainer component={Paper} variant="outlined">
            <MuiTable sx={{ minWidth: 650 }} aria-label="leave request table">
                <TableHead>
                    <TableRow>
                        <TableCell>Request Date</TableCell>
                        <TableCell>Type</TableCell>
                        <TableCell>Status</TableCell>
                        <TableCell>Start Date</TableCell>
                        <TableCell>End Date</TableCell>
                        <TableCell>Total Days</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {filteredData.map((request) => (
                        <TableRow key={request.leaveRequestId}>
                            <TableCell>{dateFormatter(request.requestDate)}</TableCell>
                            <TableCell>
                                <Chip
                                label={request.leaveType.typeName}
                                style={{ backgroundColor: getLeaveTypeColor(request.leaveType.typeName), color: 'white' }}
                                size="small"
                                />
                            </TableCell>
                            <TableCell>
                                <Box display="flex" alignItems="center">
                                    <StatusIndicator color={getStatusColor(request.status.status)} />
                                    <Typography>{request.status.status}</Typography>
                                </Box>
                            </TableCell>
                            <TableCell>{dateFormatter(request.startDate)}</TableCell>
                            <TableCell>{dateFormatter(request.endDate)}</TableCell>
                            <TableCell>{request.daysRequested} days</TableCell>
                        </TableRow>
                    ))}
                    {isPending && (
                        <TableRow>
                            <TableCell colSpan={5} align="center">
                                Loading...
                            </TableCell>
                        </TableRow>
                    )}
                    {!filteredData.length && !isPending && (
                        <TableRow>
                            <TableCell colSpan={5} align="center">
                                No leave requests found
                            </TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </MuiTable>
        </TableContainer>
    );
};

// Styled Widget component
const Widget = styled(Card)(({ theme }) => ({
    borderRadius: theme.shape.borderRadius,
    padding: theme.spacing(2),
}));


const getLeaveTypeColor = (leaveType: string) => {
    switch (leaveType.toLowerCase()) {
        case 'vacation': return '#4CAF50';  // Green
        case 'absence': return '#FF9800';   // Orange
        case 'sick': return '#F44336';      // Red
        case 'maternity': return '#E91E63'; // Pink
        case 'emergency': return '#2196F3'; // Teal
        case 'paternal': return '#2196F3';  // Blue
        case 'paternity': return '#9C27B0'; // Purple
        case 'bereavement': return '#9C27B0'; // Purple
        default: return '#757575';          // Grey
    }
};

const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
        case 'approved': return '#4caf50';  // green
        case 'pending': return '#ff9800';   // orange
        case 'rejected': return '#f44336';  // red
        default: return '#9e9e9e';          // grey
    }
};

const StatusIndicator = styled('span')(({ theme, color }) => ({
    display: 'inline-block',
    width: 10,
    height: 10,
    borderRadius: '50%',
    marginRight: theme.spacing(1),
    backgroundColor: color,
}));

const dateFormatter = (date : string): string => {
    const d = new Date(date);
    return format(d, 'MMM dd, yyyy');
}

const toSentenceCase = (value: string): string => {
    return value
        .toLowerCase()
        .split(" ")
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(" ");
}