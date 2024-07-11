import { useState, useEffect } from 'react';
import {
    Container, Stack, Typography, Card, CardContent, TextField, MenuItem,
    Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Paper, Chip, Box, Dialog, DialogTitle, DialogContent, DialogActions,
    Grid, IconButton, Tooltip
} from '@mui/material';
import { Check, Close, Info } from '@mui/icons-material';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { format, startOfMonth, endOfMonth } from 'date-fns';
import { useFetchLeaveRequestsByRequestDateRange } from "../../api/query/UseFetch";
import { putUpdateLeaveRequest } from "../../api/post/PostService";
import Headertext from "../../components/HeaderText";

function formatDate(date: Date): string {
    return format(date, "yyyy-MM-dd");
}

export default function HRLeaveRequestManagement() {
    const [filter, setFilter] = useState({
        status: '',
        leaveType: '',
    });

    const [selectedRequest, setSelectedRequest] = useState<LeaveRequestRes | null>(null);
    const [isDetailDialogOpen, setIsDetailDialogOpen] = useState(false);
    const [requestDateStart, setRequestDateStart] = useState<string>('');
    const [requestDateEnd, setRequestDateEnd] = useState<string>('');

    const queryClient = useQueryClient();

    useEffect(() => {
        const currentDate = new Date();
        const startDate = startOfMonth(currentDate);
        const endDate = endOfMonth(currentDate);
        const formattedStartDate = formatDate(startDate);
        const formattedEndDate = formatDate(endDate);
        setRequestDateStart(formattedStartDate);
        setRequestDateEnd(formattedEndDate);
        refetch();
    }, []);

    const { data: leaveRequests, isLoading, refetch } = useFetchLeaveRequestsByRequestDateRange(requestDateStart, requestDateEnd);

    const updateStatusMutation = useMutation({
        mutationFn: async (leaveRequest: LeaveRequestReq) => {
            return putUpdateLeaveRequest(leaveRequest);
        },
        onSuccess: async (data: LeaveRequestRes) => {
            await queryClient.invalidateQueries({queryKey: ['leaveRequestsByRequestDateRange', {afterDate: requestDateStart, beforeDate: requestDateEnd}]});
            await queryClient.invalidateQueries({queryKey: ['leaveRequestsByEmployeeId', {id: data.employee.employeeId}]});
            await queryClient.invalidateQueries({queryKey: ['leaveBalancesByEmployeeId', {id: data.employee.employeeId}]});
        },
    });

    const handleStatusUpdate = (requestId: number, newStatusId: number) => {
        if (requestId && leaveRequests) {
            const request: LeaveRequestRes | undefined = leaveRequests.find(request => request.leaveRequestId === requestId);

            if (!request) {
                return;
            }

            const updatedRequest: LeaveRequestReq = {
                leaveRequestId: request.leaveRequestId,
                employee: {
                    employeeId: request.employee.employeeId,
                },
                leaveType: {
                    id: request.leaveType.id,
                },
                status: {
                    id: newStatusId,
                },
                requestDate: request.requestDate,
                startDate: request.startDate,
                endDate: request.endDate,
                reason: request.reason,
            };

            updateStatusMutation.mutate(updatedRequest);
        }
    };

    const filteredRequests = leaveRequests?.filter(request =>
        (!filter.status || request.status.status.toLowerCase() === filter.status.toLowerCase()) &&
        (!filter.leaveType || request.leaveType.typeName.toLowerCase() === filter.leaveType.toLowerCase())
    );

    const handleDateChange = (startOrEnd: 'start' | 'end', value: string) => {
        if (startOrEnd === 'start') {
            setRequestDateStart(value);
        } else {
            setRequestDateEnd(value);
        }

        refetch();
    };

    const handleStatusChange = (requestId: number, newStatus: string) => {
        const newStatusId = newStatus === 'Approved' ? 2 : newStatus === 'Rejected' ? 3 : 1;
        handleStatusUpdate(requestId, newStatusId);
    };

    return (
        <Container maxWidth="lg" sx={{ my: 5 }}>
            <Stack spacing={4}>
                <Headertext>Leave Request Management</Headertext>

                <Card>
                    <CardContent>
                        <Typography variant="h6" gutterBottom>Filters</Typography>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={3}>
                                <TextField
                                    select
                                    fullWidth
                                    label="Status"
                                    value={filter.status}
                                    onChange={(e) => setFilter({ ...filter, status: e.target.value })}
                                >
                                    <MenuItem value="">All</MenuItem>
                                    <MenuItem value="Pending">Pending</MenuItem>
                                    <MenuItem value="Approved">Approved</MenuItem>
                                    <MenuItem value="Rejected">Rejected</MenuItem>
                                </TextField>
                            </Grid>
                            <Grid item xs={12} sm={3}>
                                <TextField
                                    fullWidth
                                    label="Request Date From"
                                    type="date"
                                    value={requestDateStart}
                                    onChange={(e) => handleDateChange('start', e.target.value)}
                                    InputLabelProps={{ shrink: true }}
                                />
                            </Grid>
                            <Grid item xs={12} sm={3}>
                                <TextField
                                    fullWidth
                                    label="Request Date To"
                                    type="date"
                                    value={requestDateEnd}
                                    onChange={(e) => handleDateChange('end', e.target.value)}
                                    InputLabelProps={{ shrink: true }}
                                />
                            </Grid>
                            <Grid item xs={12} sm={3}>
                                <TextField
                                    select
                                    fullWidth
                                    label="Leave Type"
                                    value={filter.leaveType}
                                    onChange={(e) => setFilter({ ...filter, leaveType: e.target.value })}
                                >
                                    <MenuItem value="">All</MenuItem>
                                    <MenuItem value="Vacation">Vacation</MenuItem>
                                    <MenuItem value="Sick">Sick</MenuItem>
                                    <MenuItem value="Personal">Personal</MenuItem>
                                </TextField>
                            </Grid>
                        </Grid>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent>
                        <Typography variant="h6" gutterBottom>Leave Requests</Typography>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Employee</TableCell>
                                        <TableCell>Type</TableCell>
                                        <TableCell>Start Date</TableCell>
                                        <TableCell>End Date</TableCell>
                                        <TableCell>Status</TableCell>
                                        <TableCell>Actions</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {isLoading ? (
                                        <TableRow>
                                            <TableCell colSpan={6} align="center">Loading...</TableCell>
                                        </TableRow>
                                    ) : filteredRequests?.map((request) => (
                                        <TableRow key={request.leaveRequestId}>
                                            <TableCell>{request.employee.firstName + ' ' + request.employee.lastName}</TableCell>
                                            <TableCell>{request.leaveType.typeName}</TableCell>
                                            <TableCell>{format(new Date(request.startDate), 'MMM dd, yyyy')}</TableCell>
                                            <TableCell>{format(new Date(request.endDate), 'MMM dd, yyyy')}</TableCell>
                                            <TableCell>
                                                <Chip
                                                    label={request.status.status}
                                                    color={
                                                        request.status.status.toLowerCase() === 'approved' ? 'success' :
                                                            request.status.status.toLowerCase() === 'rejected' ? 'error' : 'warning'
                                                    }
                                                />
                                            </TableCell>
                                            <TableCell>
                                                {request.status.status.toLowerCase() === 'pending' && (
                                                    <>
                                                        <Tooltip title="Approve">
                                                            <IconButton onClick={() => handleStatusChange(request.leaveRequestId, 'Approved')}>
                                                                <Check color="success" />
                                                            </IconButton>
                                                        </Tooltip>
                                                        <Tooltip title="Reject">
                                                            <IconButton onClick={() => handleStatusChange(request.leaveRequestId, 'Rejected')}>
                                                                <Close color="error" />
                                                            </IconButton>
                                                        </Tooltip>
                                                    </>
                                                )}
                                                <Tooltip title="View Details">
                                                    <IconButton onClick={() => {
                                                        setSelectedRequest(request);
                                                        setIsDetailDialogOpen(true);
                                                    }}>
                                                        <Info color="primary" />
                                                    </IconButton>
                                                </Tooltip>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </CardContent>
                </Card>
            </Stack>

            <Dialog open={isDetailDialogOpen} onClose={() => setIsDetailDialogOpen(false)}>
                <DialogTitle>Leave Request Details</DialogTitle>
                <DialogContent>
                    {selectedRequest && (
                        <Stack spacing={2}>
                            <Typography><strong>Employee:</strong> {selectedRequest.employee.firstName + ' ' + selectedRequest.employee.lastName}</Typography>
                            <Typography><strong>Leave Type:</strong> {selectedRequest.leaveType.typeName}</Typography>
                            <Typography><strong>Start Date:</strong> {format(new Date(selectedRequest.startDate), 'MMM dd, yyyy')}</Typography>
                            <Typography><strong>End Date:</strong> {format(new Date(selectedRequest.endDate), 'MMM dd, yyyy')}</Typography>
                            <Typography><strong>Status:</strong> {selectedRequest.status.status}</Typography>
                            <Typography><strong>Reason:</strong> {selectedRequest.reason}</Typography>
                            {selectedRequest.status.status.toLowerCase() === 'pending' && (
                                <Box mt={2}>
                                    <Button variant="contained" color="success" onClick={() => handleStatusChange(selectedRequest.leaveRequestId, 'Approved')} sx={{ mr: 1 }}>
                                        Approve
                                    </Button>
                                    <Button variant="contained" color="error" onClick={() => handleStatusChange(selectedRequest.leaveRequestId, 'Rejected')}>
                                        Reject
                                    </Button>
                                </Box>
                            )}
                        </Stack>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsDetailDialogOpen(false)}>Close</Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}
