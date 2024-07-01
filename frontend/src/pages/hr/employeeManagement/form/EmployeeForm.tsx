import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { API, BASE_API } from "../../../../api/Api.ts";
import axios from "axios";
import { SubmitHandler, useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { format } from "date-fns";
import { Button, DialogActions, DialogContent, DialogTitle, Stack } from "@mui/material";
import BasicInfoArea, { ContactNumbersArea, ContributionIdsArea, EmploymentInfoArea, SalaryBenefitsArea } from "./components/EmployeeFormArea.tsx";
import { TextCompleteOption } from "./components/EmployeeFormUtils.tsx";

interface Inputs {
    firstName: string;
    lastName: string;
    dob: Date | null;
    address: string;
    contacts: ContactInput[];
    benefits: BenefitInput[];
    departmentCode: string;
    positionCode: string;
    statusId: string;
    supervisor: TextCompleteOption | null;
    hireDate: Date | null;
    sssNo: string;
    philHealthNo: string;
    pagIbigNo: string;
    tinNo: string;
    basicSalary: number;
}

interface ContactInput {
    contactId: number;
    contactNumber: string;
}

interface BenefitInput {
    amount: number;
    benefitTypeId: number;
}

interface EmployeeFormDialogProps {
    type: 'add' | 'edit';
    selectedId?: number | null;
    onClose: () => void;
}

const EmployeeFormDialog = ({ type, selectedId, onClose }: EmployeeFormDialogProps) => {
    const queryClient = useQueryClient();
    const [isFormInitialized, setIsFormInitialized] = useState(false);

    const fetchEmployee = async () => {
        const { EMPLOYEES } = API;
        if (selectedId) {
            const res = await axios.get(`${BASE_API}${EMPLOYEES.BASE}${selectedId}`);
            return res.data;
        } else {
            return null;
        }
    };

    const { isPending, data } = useQuery<EmployeeFullRes>({
        queryKey: ['employeeEdit', selectedId],
        queryFn: fetchEmployee,
        enabled: !!selectedId
    });

    const supervisorData = data ? {
        value: data.supervisor.supervisorId,
        label: `${data.supervisor.firstName} ${data.supervisor.lastName}`
    } : null;

    const {
        register,
        control,
        watch,
        setValue,
        handleSubmit,
        reset
    } = useForm<Inputs>({
        defaultValues: {
            firstName: "",
            lastName: "",
            dob: null,
            address: "",
            contacts: [],
            benefits: [],
            departmentCode: "",
            positionCode: "",
            statusId: "",
            supervisor: null,
            hireDate: null,
            sssNo: "",
            philHealthNo: "",
            pagIbigNo: "",
            tinNo: "",
            basicSalary: 0
        }
    });

    useEffect(() => {
        if (selectedId && !isPending && data && !isFormInitialized) {
            reset({
                firstName: data.firstName,
                lastName: data.lastName,
                dob: data.dob ? new Date(data.dob) : null,
                address: data.address,
                contacts: data.contacts.map(contact => ({
                    contactId: contact.contactId,
                    contactNumber: contact.contactNo
                })),
                benefits: data.benefits.map(benefit => ({
                    amount: benefit.amount,
                    benefitTypeId: benefit.benefitType.benefitTypeId
                })),
                departmentCode: data.department.departmentCode,
                positionCode: data.position.positionCode,
                statusId: data.status.statusId.toString(),
                supervisor: supervisorData,
                hireDate: data.hireDate ? new Date(data.hireDate) : null,
                sssNo: data.governmentId.sssNo,
                philHealthNo: data.governmentId.philHealthNo,
                pagIbigNo: data.governmentId.pagIbigNo,
                tinNo: data.governmentId.tinNo,
                basicSalary: data.basicSalary
            });
            setIsFormInitialized(true); // Mark form as initialized to prevent further resets
        }
    }, [data, isPending, reset, selectedId, supervisorData, isFormInitialized]);

    const addEmployee = async (employee: EmployeeReq) => {
        const { EMPLOYEES } = API;
        const res = await axios.post(`${BASE_API}${EMPLOYEES.ALL}`, employee);
        return res.data;
    };

    const useAddEmployee = useMutation({
        mutationFn: addEmployee,
        onSettled: async () => {
            await queryClient.invalidateQueries({ queryKey: ['employeesAll'] });
        }
    });

    const editEmployee = async (employee: EmployeeReq) => {
        const { EMPLOYEES } = API;
        const res = await axios.put(`${BASE_API}${EMPLOYEES.UPDATE}${selectedId}`, employee);
        return res.data;
    };

    const useEditEmployee = useMutation({
        mutationFn: editEmployee,
        onSettled: async () => {
            await queryClient.invalidateQueries({ queryKey: ['employeesAll'] });
        }
    });

    const onSubmit: SubmitHandler<Inputs> = (formData) => {
        if (selectedId && data) {
            const originalData = data;
            const changedData: Partial<EmployeeReq> = {};

            // Check and add only changed fields
            if (formData.firstName !== originalData.firstName) changedData.firstName = formData.firstName;
            if (formData.lastName !== originalData.lastName) changedData.lastName = formData.lastName;
            if (formData.dob && format(formData.dob, 'yyyy-MM-dd') !== originalData.dob) {
                changedData.dob = format(formData.dob, 'yyyy-MM-dd');
            }
            if (formData.address !== originalData.address) changedData.address = formData.address;

            // Check for changes in contacts
            if (JSON.stringify(formData.contacts) !== JSON.stringify(originalData.contacts)) {
                changedData.contacts = formData.contacts.map(contact => ({
                    contactNo: contact.contactNumber
                }));
            }

            // Check for changes in benefits
            if (JSON.stringify(formData.benefits) !== JSON.stringify(originalData.benefits)) {
                changedData.benefits = formData.benefits.map(benefit => ({
                    amount: benefit.amount,
                    benefitType: {
                        benefitTypeId: benefit.benefitTypeId
                    }
                }));
            }

            if (formData.departmentCode !== originalData.department.departmentCode) {
                changedData.department = { departmentCode: formData.departmentCode };
            }
            if (formData.positionCode !== originalData.position.positionCode) {
                changedData.position = { positionCode: formData.positionCode };
            }

            // Check for changes in government IDs
            const governmentIdChanged =
                formData.sssNo !== originalData.governmentId.sssNo ||
                formData.philHealthNo !== originalData.governmentId.philHealthNo ||
                formData.pagIbigNo !== originalData.governmentId.pagIbigNo ||
                formData.tinNo !== originalData.governmentId.tinNo;

            if (governmentIdChanged) {
                changedData.governmentId = {
                    sssNo: formData.sssNo,
                    philHealthNo: formData.philHealthNo,
                    pagIbigNo: formData.pagIbigNo,
                    tinNo: formData.tinNo
                };
            }

            if (formData.supervisor && formData.supervisor.value !== originalData.supervisor.supervisorId.toString()) {
                changedData.supervisor = { supervisorId: Number(formData.supervisor.value) };
            }
            if (formData.statusId !== originalData.status.statusId.toString()) {
                changedData.status = { statusId: Number(formData.statusId) };
            }
            if (formData.hireDate && format(formData.hireDate, 'yyyy-MM-dd') !== originalData.hireDate) {
                changedData.hireDate = format(formData.hireDate, 'yyyy-MM-dd');
            }
            if (formData.basicSalary !== originalData.basicSalary) changedData.basicSalary = formData.basicSalary;

            if (Object.keys(changedData).length > 0) {
                const formDataWithId = {
                    employeeId: selectedId,
                    ...changedData
                };
                useEditEmployee.mutate(formDataWithId);
            } else {
                console.log("No changes detected");
            }
        } else {
            // For new employee, send all data
            const newEmployeeData: EmployeeReq = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                dob: format(formData.dob, 'yyyy-MM-dd'),
                address: formData.address,
                contacts: formData.contacts.map(contact => ({
                    contactNo: contact.contactNumber
                })),
                benefits: formData.benefits.map(benefit => ({
                    amount: benefit.amount,
                    benefitType: {
                        benefitTypeId: benefit.benefitTypeId
                    }
                })),
                department:  { departmentCode: formData.departmentCode },
                position: { positionCode: formData.positionCode },
                governmentId: {
                    sssNo: formData.sssNo,
                    philHealthNo: formData.philHealthNo,
                    pagIbigNo: formData.pagIbigNo,
                    tinNo: formData.tinNo
                },
                supervisor: { supervisorId: Number(formData.supervisor.value) } ,
                status:  { statusId: Number(formData.statusId) },
                hireDate: format(formData.hireDate, 'yyyy-MM-dd'),
                basicSalary: formData.basicSalary
            };
            useAddEmployee.mutate(newEmployeeData);
        }

        onClose();
    };

    return (
        <>
            <DialogTitle textTransform='capitalize'>
                {`${type} Employee`}
            </DialogTitle>
            <DialogContent>
                <Stack mb={4} gap={3}>
                    <BasicInfoArea register={register} control={control} selectedId={selectedId} />
                    <ContactNumbersArea register={register} control={control} />
                    <EmploymentInfoArea register={register} control={control} watch={watch} setValue={setValue} />
                    <ContributionIdsArea register={register} control={control} selectedId={selectedId} />
                    <SalaryBenefitsArea register={register} control={control} />
                </Stack>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button variant="contained" onClick={handleSubmit(onSubmit)}>Save</Button>
            </DialogActions>
        </>
    );
};

export default EmployeeFormDialog;
