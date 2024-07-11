import {useMutation, useQueryClient} from "@tanstack/react-query";
import {postLeaveRequest} from "../../../api/post/PostService.ts";
import React, {useState} from "react";
import {Button, MenuItem, Stack, TextField, Grid} from "@mui/material";
import {format} from "date-fns";
import {useFetchLeaveTypes} from "../../../api/query/UseFetch.ts";

export default function LeaveRequestForm({ employeeId }: { employeeId: number }) {
    const [leaveTypeId, setLeaveTypeId] = useState<number>(1);
    const [startDate, setStartDate] = useState<string>("");
    const [endDate, setEndDate] = useState<string>("");
    const [reason, setReason] = useState<string>("");
    const [isLoading, setIsLoading] = useState(false);
    const { data: leaveTypes } = useFetchLeaveTypes();

    const queryClient = useQueryClient();

    const mutation = useMutation({
        mutationFn: async (leaveRequest: LeaveRequestReq) => {
            return postLeaveRequest(leaveRequest)
        },
        onSuccess: async () => {

            console.log('Mutation successful, fetching leave request and balance data...');
            await queryClient.refetchQueries({queryKey: ['leaveRequestsByEmployeeId', { id: employeeId }], type: 'active'});
            await queryClient.refetchQueries({queryKey: ['leaveBalancesByEmployeeId', { id: employeeId }], type: 'active'});

            console.log('Refetching complete');
            setIsLoading(false);
        },

        onError: () => {
            setIsLoading(false);
        }
    });


    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const leaveRequest: LeaveRequestReq = {
            employee: {
                employeeId : employeeId
            },
            leaveType: {
                id: leaveTypeId
            },
            startDate,
            endDate,
            reason,
            requestDate: format(new Date(), "yyyy-MM-dd"),
            status: {
                id: 1
            }, // Default status to 1 as "Pending"
        };

        setIsLoading(true);

        mutation.mutate(leaveRequest);
    };

    return (
            <form onSubmit={handleSubmit}>
                <Grid container spacing={3} marginTop={1}>
                    <Grid item xs={3}>
                        <Stack gap={3}>
                            <TextField
                                select
                                label="Type"
                                value={leaveTypeId}
                                onChange={(e) => setLeaveTypeId(Number(e.target.value))}
                                fullWidth
                            >
                                <MenuItem value="">Select Leave Type</MenuItem>
                                {leaveTypes?.map((leaveType) => (
                                    <MenuItem key={leaveType.id} value={leaveType.id}>
                                        {toSentenceCase(leaveType.typeName)}
                                    </MenuItem>
                                ))}
                            </TextField>
                            <TextField
                                label="Start Date"
                                type="date"
                                InputLabelProps={{ shrink: true }}
                                value={startDate}
                                onChange={(e) => setStartDate(format(new Date(e.target.value), "yyyy-MM-dd"))}
                                fullWidth
                            />
                            <TextField
                                label="End Date"
                                type="date"
                                InputLabelProps={{ shrink: true }}
                                value={endDate}
                                onChange={(e) => setEndDate(format(new Date(e.target.value), "yyyy-MM-dd"))}
                                fullWidth
                            />
                            <Button
                                variant="contained"
                                type="submit"
                                disabled={isLoading}
                                fullWidth
                            >
                                {isLoading ? "Submitting..." : "Submit"}
                            </Button>
                        </Stack>
                    </Grid>
                    <Grid item xs={9} style={{ marginBottom: '12px' }}>
                        <TextField
                            label="Reason"
                            multiline
                            rows={12}
                            value={reason}
                            onChange={(e) => setReason(e.target.value)}
                            fullWidth
                        />
                    </Grid>
                </Grid>
            </form>
    );
}

const toSentenceCase = (value: string): string => {
    return value
        .toLowerCase()
        .split(" ")
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(" ");
}