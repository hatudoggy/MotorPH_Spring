
interface EmployeeBasicRes {
  employeeId: number;
  lastName: string;
  firstName: string;
  position: PositionRes;
  department: DepartmentRes;
  hireDate: string;
  contacts: ContactRes[];
  status: EmploymentStatusRes;

}

interface EmployeeFullRes {
  employeeId: number;
  lastName: string;
  firstName: string;
  dob: string;
  address: string;
  contacts: ContactRes[];
  position: PositionRes;
  department: DepartmentRes;
  governmentId: GovernmentIdRes;
  supervisor: SupervisorRes;
  status: EmploymentStatusRes;
  hireDate: string;
  basicSalary: number;
  semiMonthlyRate: number;
  hourlyRate: number;
  benefits: BenefitRes[];
  leaveBalances: LeaveBalanceRes[];
}

interface BenefitRes {
  benefitId: number;
  employeeId: number;
  benefitType: BenefitTypeRes;
  amount: number;
}

interface BenefitTypeRes {
  benefitTypeId: number;
  benefit: string;
}


interface LeaveBalanceRes {
  id: number;
  employeeId: number;
  leaveTypeId: number;
  balance: number;
}

interface GovernmentIdRes {
  id: number;
  employeeId: number;
  sssNo: string;
  philHealthNo: string;
  pagIbigNo: string;
  tinNo: string;
}

interface EmploymentStatusRes {
  statusId: number;
  statusName: string;
}

interface ContactRes {
  contactId: number;
  contactNo: string;
}

interface DepartmentRes {
  departmentCode: string;
  departmentName: string;
}

interface PositionRes {
  positionCode: string;
  departmentCode: string;
  positionName: string;
}

interface SupervisorRes {
  supervisorId: number;
  lastName: string;
  firstName: string;
  address: string;
  position: PositionRes;
  contacts: ContactRes[];
}


//POST
interface EmployeeReq {
  employeeId?: number;
  lastName?: string;
  firstName?: string;
  dob?: string; // Use string for dates to simplify JSON handling
  address?: string;
  contacts?: ContactReq[];
  position?: PositionReq;
  department?: DepartmentReq;
  governmentId?: GovernmentIdReq;
  supervisor?: SupervisorReq;
  status?: EmploymentStatusReq;
  hireDate?: string; // Use string for dates to simplify JSON handling
  basicSalary?: number;
  benefits?: BenefitReq[];
  // leaveBalances: LeaveBalanceReq[];
}

interface LeaveBalanceReq {
  id?: number;
  employeeId?: number;
  leaveTypeId: number;
  balance: number;
}

interface LeaveRequestReq {
  leaveRequestId?: number;
  employeeId: number;
  requestDate: string; // Use string for dates to simplify JSON handling
  startDate: string; // Use string for dates to simplify JSON handling
  endDate: string; // Use string for dates to simplify JSON handling
  daysRequested: number;
  statusId: number;
  reason?: string;
}

interface EmploymentStatusReq {
  statusId: number;
  statusName?: string;
}

interface ContactReq {
  contactId?: number;
  contactNo: string;
}

interface BenefitReq {
  benefitId?: number;
  benefitType: BenefitTypeReq;
  amount: number;
}

interface BenefitTypeReq {
  benefitTypeId: number;
}

interface DepartmentReq {
  departmentCode: string;
  department?: string;
}


interface PositionReq {
  positionCode: string;
  departmentCode?: string;
  position?: string;
  isLeader?: boolean;
}

interface SupervisorReq {
  supervisorId: number;
}

interface GovernmentIdReq {
  id?: number;
  sssNo: string;
  philHealthNo: string;
  pagIbigNo: string;
  tinNo: string;
}
