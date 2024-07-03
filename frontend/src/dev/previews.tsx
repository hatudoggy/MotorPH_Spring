import {ComponentPreview, Previews} from "@react-buddy/ide-toolbox";
import {PaletteTree} from "./palette";
import Profile from "../pages/employee/Profile.tsx";
import Main from "../pages/Main.tsx";
import HREmployees from "../pages/hr/HREmployees.tsx";
import Attendance from "../pages/employee/Attendance.tsx";
import HRPayrolls from "../pages/hr/HRPayrolls.tsx";
import EmployeeCard from "../pages/hr/employeeManagement/EmployeeCard.tsx";
import BasicInfoArea from "../pages/hr/employeeManagement/form/components/EmployeeFormArea.tsx";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/Profile">
                <Profile/>
            </ComponentPreview>
            <ComponentPreview path="/Main">
                <Main/>
            </ComponentPreview>
            <ComponentPreview path="/HREmployees">
                <HREmployees/>
            </ComponentPreview>
            <ComponentPreview path="/Attendance">
                <Attendance/>
            </ComponentPreview>
            <ComponentPreview path="/HRPayrolls">
                <HRPayrolls/>
            </ComponentPreview>
            <ComponentPreview path="/EmployeeCard">
                <EmployeeCard/>
            </ComponentPreview>
            <ComponentPreview path="/BasicInfoArea">
                <BasicInfoArea/>
            </ComponentPreview>
        </Previews>
    );
};

export default ComponentPreviews;