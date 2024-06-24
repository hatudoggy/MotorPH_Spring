import { Stack, Typography } from "@mui/material";
import { ReactNode } from "react";



interface Props {
  label: string
  children?: ReactNode
}

export default function Labeled({label, children}: Props) {

  return(
    <Stack>
      <Typography variant="body2" color="GrayText">{label}</Typography>
      {children}
    </Stack>
  )
}