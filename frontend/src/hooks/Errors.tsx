// Reusable components
import {CircularProgress, Typography} from "@mui/material";

export const LoadingOrError = ({ isLoading, error, errorMessage }) => {
    if (isLoading) return <CircularProgress />;
    if (error) {
        console.error(errorMessage || "An error occurred");
        return <Typography>{errorMessage || "An error occurred"}</Typography>;
    }
    return null;
};