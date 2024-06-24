import { TextField, TextFieldProps } from "@mui/material";


export default function ReadonlyTextField({variant="standard", inputProps, ...props}: TextFieldProps) {

  return(
    <TextField 
      variant={variant}
      inputProps={{ readOnly: true, ...inputProps }}
      {...props}
    />
  )
}