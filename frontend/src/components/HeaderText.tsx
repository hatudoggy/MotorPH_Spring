import { Typography } from "@mui/material";



interface Props {
  children: string
}

export default function Headertext({children}: Props) {

  return(
    <Typography 
      variant="h4" 
      fontWeight={500}
      mb={2}
    >
      {children}
    </Typography>
  )
}