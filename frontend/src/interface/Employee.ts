
interface EmployeeBasicRes {
  employeeId: number;
  lastName: string;
  firstName: string;
  position: PositionRes;
  department: DepartmentRes;
}

interface EmployeeFullRes {
  employeeId: number;
  lastName: string;
  firstName: string;
  dob: string;
  address: string;
  contacts: ContactRes;
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
  employeeId: number;
  contactNumbers: string[];
}

interface DepartmentRes {
  departmentCode: string;
  departmentName: string;
}

interface PositionRes {
  positionCode: string;
  positionName: string;
}

interface SupervisorRes {
  supervisorId: number;
  lastName: string;
  firstName: string;
  address: string;
  positionCode: string;
  contacts: ContactRes;
}


//POST
interface EmployeeReq {
  employeeId?: number;
  lastName: string;
  firstName: string;
  dob: string; // Use string for dates to simplify JSON handling
  address: string;
  contacts: ContactReq;
  positionCode: string;
  departmentCode: string;
  governmentId: GovernmentIdReq;
  supervisorId: number;
  statusId: number;
  hireDate: string; // Use string for dates to simplify JSON handling
  basicSalary: number;
  semiMonthlyRate: number;
  hourlyRate: number;
  benefits: BenefitReq[];
  leaveBalances: LeaveBalanceReq[];
}

interface LeaveBalanceReq {
  id?: number;
  employeeId: number;
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
  reason: string;
}

interface EmploymentStatusReq {
  statusId: number;
  statusName: string;
}

interface ContactReq {
  employeeId: number;
  contactNumbers: string[];
}

interface BenefitReq {
  benefitId?: number;
  employeeId: number;
  benefitTypeId: number;
  amount: number;
}

interface BenefitTypeReq {
  benefitTypeId: number;
  benefit: string;
}


interface DepartmentReq {
  departmentCode: string;
  department: string;
}


interface PositionReq {
  positionCode: string;
  departmentCode: string;
  position: string;
}

interface SupervisorReq {
  supervisorId?: number;
  lastName: string;
  firstName: string;
  address: string;
  positionCode: string;
  contacts: ContactReq;
}

interface GovernmentIdReq {
  id?: number;
  employeeId: number;
  sssNo: string;
  philHealthNo: string;
  pagIbigNo: string;
  tinNo: string;
}
