
interface AttendanceRes {
  attendanceId: number
  employeeId: number
  date: string
  timeIn: string
  timeOut: string
  hoursWorked: number
  overtime: number
}

interface AttendanceSummaryRes {
  totalCount: number
  presentCount: number
  lateCount: number
  absentCount: number
  averageTimeIn: string
  averageTimeOut: string
}

interface AttendanceFull {
  attendanceId: number
  employee: EmployeeRes
  date: string
  timeIn: string
  timeOut: string
  hoursWorked: number
  overtime: number
}
