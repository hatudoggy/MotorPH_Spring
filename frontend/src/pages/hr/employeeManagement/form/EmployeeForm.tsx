import { useMutation, useQueryClient } from "@tanstack/react-query";
import { API, BASE_API } from "../../../../api/Api.ts";
import { SubmitHandler, useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { format } from "date-fns";
import { Button, DialogActions, DialogContent, DialogTitle, Stack } from "@mui/material";
import BasicInfoArea, { ContactNumbersArea, ContributionIdsArea, EmploymentInfoArea, SalaryBenefitsArea } from "./components/EmployeeFormArea.tsx";
import { TextCompleteOption } from "./components/EmployeeFormUtils.tsx";
import axios from "axios";
import {useFetchEmployeeFullById} from "../../../../api/query/UseFetch.ts";

export interface Inputs {
    firstName: string;
    lastName: string;
    dob: Date;
    address: string;
    contacts: ContactInput[];
    benefits: BenefitInput[];
    departmentCode: string;
    positionCode: string;
    statusId: string;
    supervisor: TextCompleteOption | null;
    hireDate: Date;
    sssNo: string;
    philHealthNo: string;
    pagIbigNo: string;
    tinNo: string;
    basicSalary: number;
}

interface ContactInput {
    contactNumber: string;
}

interface BenefitInput {
    amount: number;
    benefitTypeId: number;
}

interface EmployeeFormDialogProps {
    type: 'add' | 'edit';
    selectedId: number;
    onClose: () => void;
}

const EmployeeFormDialog = ({ type, selectedId, onClose }: EmployeeFormDialogProps) => {
    const queryClient = useQueryClient();
    const [isFormInitialized, setIsFormInitialized] = useState(false);

    const { isPending, data } = useFetchEmployeeFullById(selectedId);

    const supervisorData = data ? {
        value: data.supervisor.supervisorId,
        label: `${data.supervisor.firstName} ${data.supervisor.lastName}`
    } : null;

    const mutation = useMutation({
        mutationFn: async (employee: EmployeeReq) => {
            const { EMPLOYEES } = API;

            if (type === 'add') {
                const response = await axios.post(`${BASE_API}${EMPLOYEES.REGISTER}`, employee);
                return response.data;
            } else {
                const response = await axios.put(`${BASE_API}${EMPLOYEES.UPDATE}${selectedId}`, employee);
                return response.data;
            }
        },
        onSuccess: async  (data: EmployeeBasicRes) => {
            onClose();

            const employeeId = data.employeeId;

            await queryClient.refetchQueries({queryKey: ['employees']});
            await queryClient.refetchQueries({queryKey: ['employee', {id: employeeId}]});
        },
    });

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
            dob: undefined,
            address: "",
            contacts: [],
            benefits: [],
            departmentCode: "",
            positionCode: "",
            statusId: "",
            supervisor: null,
            hireDate: undefined,
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
                dob: new Date(data.dob),
                address: data.address,
                contacts: data.contacts.map(contact => ({
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
                hireDate: new Date(data.hireDate),
                sssNo: data.governmentId.sssNo,
                philHealthNo: data.governmentId.philHealthNo,
                pagIbigNo: data.governmentId.pagIbigNo,
                tinNo: data.governmentId.tinNo,
                basicSalary: data.basicSalary
            });
            setIsFormInitialized(true);
        }
    }, [data, isPending, reset, selectedId, supervisorData, isFormInitialized]);

    const onSubmit: SubmitHandler<Inputs> = (formData) => {
        if (selectedId && data) {
            const changedData: Partial<EmployeeReq> = {};
            if (formData.firstName !== data.firstName) changedData.firstName = formData.firstName;
            if (formData.lastName !== data.lastName) changedData.lastName = formData.lastName;
            if (formData.dob && format(formData.dob, 'yyyy-MM-dd') !== data.dob) changedData.dob = format(formData.dob, 'yyyy-MM-dd');
            if (formData.address !== data.address) changedData.address = formData.address;

            const changedContacts = formData.contacts.filter((contact, index) => {
                const originalContact = data.contacts[index];
                return !originalContact || contact.contactNumber !== originalContact.contactNo;
            });

            if (changedContacts.length > 0) {
                changedData.contacts = changedContacts.map(contact => ({ contactNo: contact.contactNumber }));
            }

            const changedBenefits = formData.benefits.filter((benefit, index) => {
                const originalBenefit = data.benefits[index];
                return !originalBenefit || benefit.amount !== originalBenefit.amount || benefit.benefitTypeId !== originalBenefit.benefitType.benefitTypeId;
            });

            if (changedBenefits.length > 0) {
                changedData.benefits = changedBenefits.map(benefit => ({
                    amount: benefit.amount,
                    benefitType: { benefitTypeId: benefit.benefitTypeId }
                }));
            }

            if (formData.departmentCode !== data.department.departmentCode) changedData.department = { departmentCode: formData.departmentCode };
            if (formData.positionCode !== data.position.positionCode) changedData.position = { positionCode: formData.positionCode };

            const governmentIdChanged =
                formData.sssNo !== data.governmentId.sssNo ||
                formData.philHealthNo !== data.governmentId.philHealthNo ||
                formData.pagIbigNo !== data.governmentId.pagIbigNo ||
                formData.tinNo !== data.governmentId.tinNo;

            if (governmentIdChanged) {
                changedData.governmentId = {
                    sssNo: formData.sssNo,
                    philHealthNo: formData.philHealthNo,
                    pagIbigNo: formData.pagIbigNo,
                    tinNo: formData.tinNo
                };
            }

            if (formData.supervisor && formData.supervisor.value !== data.supervisor.supervisorId) {
                changedData.supervisor = { supervisorId: Number(formData.supervisor.value) };
            }

            if (formData.statusId !== data.status.statusId.toString()) {
                changedData.status = { statusId: Number(formData.statusId) };
            }

            if (formData.hireDate && format(formData.hireDate, 'yyyy-MM-dd') !== data.hireDate) {
                changedData.hireDate = format(formData.hireDate, 'yyyy-MM-dd');
            }

            if (formData.basicSalary !== data.basicSalary) changedData.basicSalary = formData.basicSalary;

            if (Object.keys(changedData).length > 0) {
                const formDataWithId = { employeeId: selectedId, ...changedData };
                mutation.mutate(formDataWithId);
                console.log("Updated employee data: " + JSON.stringify(formDataWithId));
            } else {
                console.log("No changes detected");
            }
        } else {
            const newEmployeeData: EmployeeReq = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                dob: format(formData.dob, 'yyyy-MM-dd'),
                address: formData.address,
                contacts: formData.contacts.map(contact => ({ contactNo: contact.contactNumber })),
                benefits: formData.benefits.map(benefit => ({
                    amount: benefit.amount,
                    benefitType: { benefitTypeId: benefit.benefitTypeId }
                })),
                department: { departmentCode: formData.departmentCode },
                position: { positionCode: formData.positionCode },
                governmentId: {
                    sssNo: formData.sssNo,
                    philHealthNo: formData.philHealthNo,
                    pagIbigNo: formData.pagIbigNo,
                    tinNo: formData.tinNo
                },
                supervisor: formData.supervisor ? { supervisorId: Number(formData.supervisor.value) } : undefined,
                status: { statusId: Number(formData.statusId) },
                hireDate: format(formData.hireDate, 'yyyy-MM-dd'),
                basicSalary: formData.basicSalary
            };
            mutation.mutate(newEmployeeData);
            console.log("New employee data: " + JSON.stringify(newEmployeeData));
        }

        onClose();
    };

    // Reset form initialization state on dialog close
    const handleClose = () => {
        setIsFormInitialized(false);
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
                <Button onClick={handleClose}>Cancel</Button>
                <Button variant="contained" onClick={handleSubmit(onSubmit)}>Save</Button>
            </DialogActions>
        </>
    );
};

export default EmployeeFormDialog;
