
interface AttendanceRes {
  attendanceId: number
  employeeId: number
  date: string
  timeIn: string
  timeOut: string
  totalHours: number
  overtimeHours: number
}

interface TimeInOut {
  attendanceId: number
  employeeId: number
  timeIn: string
  timeOut: string
}

interface AttendanceFull {
  attendanceId: number
  employee: EmployeeAttendanceInfo
  date: string
  timeIn: string
  timeOut: string
  totalHours: number
  overtimeHours: number
}

interface EmployeeAttendanceInfo {
  employeeId: number
  lastName: string
  firstName: string
  position: PositionRes,
  department: DepartmentRes
}

