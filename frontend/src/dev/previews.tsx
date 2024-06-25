import {ComponentPreview, Previews} from "@react-buddy/ide-toolbox";
import {PaletteTree} from "./palette";
import Profile from "../pages/employee/Profile.tsx";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/Profile">
                <Profile/>
            </ComponentPreview>
        </Previews>
    );
};

export default ComponentPreviews;