import { BoxProps, CircularProgress, Divider, Stack, Typography } from "@mui/material"
import Grid from "@mui/material/Unstable_Grid2/Grid2"
import { FixedSizeList as List } from "react-window"
import AutoSizer from "react-virtualized-auto-sizer"

interface TableData {
  [key: string]: any;
}

type ColSize = number | boolean | 'auto'

interface Props<RowData extends TableData> {
  colSizes?: ColSize[]
  colHeader?: string[]
  tableData: RowData[],
  rowHeight?: number
  loading?: boolean
  sx?: BoxProps
  renderers?: { [K in keyof RowData]?: (data: RowData[K]) => React.ReactNode }
}

export default function Table<RowData extends TableData>({colSizes, tableData, colHeader, rowHeight = 40,  loading, sx, renderers}: Props<RowData>) {

  const colHeaders = tableData.length > 0 ? Object.keys(tableData[0]) : [];

  return(
    <Stack height='100%' overflow='hidden' sx={sx}>
      <TableHeader colSizes={colSizes}  colHeaders={colHeader ? colHeader : colHeaders }/>
      <Stack
        flex='1 1 auto'
        sx={{
          height: 0,
          minHeight: 0,
        }}
      >
        {
          !loading ?
            <AutoSizer>
              {({height, width}) => (
                <List 
                  height={height}
                  width={width}
                  itemCount={tableData.length}
                  itemSize={rowHeight}
                  style={{overflowX: 'hidden'}}
                >
                  {({ index, style }) => (
                    <div style={style}>
                      <TableRow
                        key={index}
                        rowData={tableData[index]}
                        colSizes={colSizes}
                        colHeaders={colHeaders}
                        renderers={renderers}
                      />
                    </div>
                  )}
                </List>
              )}
            </AutoSizer>
            :
            <Stack alignItems='center'>
              <CircularProgress />
            </Stack>
        }
      </Stack>
    </Stack>
  )
}

interface TableHeader {
  colSizes?: ColSize[]
  colHeaders: string[]
  
}

function TableHeader({colSizes, colHeaders}: TableHeader) {

  return(
    <>
      <Grid container spacing={1} px={1} pb={1.5} pr={2.9}>
        {
          colHeaders.map((headerText, idx) => 
            <Grid key={idx} xs={colSizes ? colSizes[idx] : true}>
              <Typography 
                variant="body2" 
                fontWeight={500} 
                color="GrayText"
                textTransform='capitalize'
              >
                {headerText}
              </Typography>
            </Grid>
          )
        }
      </Grid>
      <Divider sx={{mb: 0.5}}/>
    </>
  )
}


interface TableRow<RowData extends TableData> {
  rowData: RowData
  colSizes?: ColSize[]
  colHeaders: string[]
  renderers?: { [K in keyof RowData]?: (data: RowData[K]) => React.ReactNode }
}

function TableRow<RowData extends TableData>({rowData, colSizes, colHeaders, renderers}: TableRow<RowData>) {

  return(
    <Grid container spacing={1} px={1} py={1.5}>
      {
        colHeaders.map((header, idx)=>
          <Grid key={idx} xs={colSizes ? colSizes[idx] : true}>
            {
              renderers && renderers[header as keyof RowData] ?
                renderers[header as keyof RowData]!(rowData[header as keyof RowData])
                :
                <Typography 
                  fontWeight={400}
                >
                  {rowData[header]}
                </Typography>
            }
          </Grid>
        )
      }
    </Grid>
  )
}