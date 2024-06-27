import {ComponentPreview, Previews} from "@react-buddy/ide-toolbox";
import {PaletteTree} from "./palette";
import Profile from "../pages/employee/Profile.tsx";
import Main from "../pages/Main.tsx";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/Profile">
                <Profile/>
            </ComponentPreview>
            <ComponentPreview path="/Main">
                <Main/>
            </ComponentPreview>
        </Previews>
    );
};

export default ComponentPreviews;