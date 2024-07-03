import {Control, Controller, FieldValues, Path} from "react-hook-form";
import {
    Autocomplete,
    FormControl,
    InputLabel,
    MenuItem, Paper, PaperProps,
    Select,
    SelectProps, Stack, StackProps, styled,
    SvgIconTypeMap,
    TextField, Typography
} from "@mui/material";
import {DatePickerProps} from "@mui/lab";
import {DatePicker} from "@mui/x-date-pickers";
import {ReactNode} from "react";
import {OverridableComponent} from "@mui/material/OverridableComponent";

interface DropdownOptions {
    value: any
    label: string
}

type Dropdown<T extends FieldValues> = {
    options: DropdownOptions[];
    control: Control<T>;
    name: Path<T>;
    changeTrigger?: () => void
} & SelectProps;


export default function Dropdown<T extends FieldValues>({
                                                            options,
                                                            control,
                                                            name,
                                                            changeTrigger,
                                                            ...props
                                                        }: Dropdown<T>) {

    return (
        <FormControl>
            <InputLabel variant={props.variant} shrink={true}>{props.label}</InputLabel>
            <Controller
                control={control}
                name={name}
                render={({field}) => (
                    <Select
                        {...props}
                        {...field}

                        value={field.value || ''}
                        onChange={(e) => {
                            field.onChange(e)
                            if (changeTrigger) {
                                changeTrigger()
                            }
                        }}
                        sx={{
                            "& .MuiSelect-select": {
                                bgcolor: "transparent",
                            },
                            ...props.sx
                        }}
                    >
                        {
                            options.map((item, idx) =>
                                <MenuItem key={idx} value={item.value}>
                                    {item.label}
                                </MenuItem>
                            )
                        }
                    </Select>
                )}
            />

        </FormControl>
    )
}

export interface TextCompleteOption {
    label: string;
    value: string | number;
}

interface TextCompleteProps<T extends FieldValues> {
    name: Path<T>,
    control: Control<T>,
    label: string,
    options: TextCompleteOption[],
    disabled?: boolean
}

export function TextComplete<T extends FieldValues>({
                                                        name,
                                                        control,
                                                        label,
                                                        options,
                                                        disabled
                                                    }: TextCompleteProps<T>) {
    return (
        <Controller
            name={name}
            control={control}
            render={({field: {onChange, value, ref}}) => (
                <Autocomplete
                    options={options}
                    getOptionLabel={(option) => option.label}
                    onChange={(_, data) => onChange(data)}
                    value={value || null}
                    isOptionEqualToValue={(option, value) => option.value === value?.value}
                    renderInput={(params) => (
                        <TextField
                            {...params}
                            label={label}
                            variant="standard"
                            inputRef={ref}
                            InputLabelProps={{shrink: true}}
                        />
                    )}
                />
            )}
        />
    );
}


interface DateSelectProps<T extends FieldValues> {
    name: Path<T>;
    control: Control<T>;
    label: string;
    datePickerProps?: Partial<DatePickerProps<Date>>;
}

export function DateSelect<T extends FieldValues>({
                                                      name,
                                                      control,
                                                      label,
                                                      datePickerProps,
                                                  }: DateSelectProps<T>) {
    return (
        <Controller
            name={name}
            control={control}
            render={({field: {onChange, value}}) => (
                <DatePicker
                    {...datePickerProps}
                    value={value || null}
                    onChange={(date) => onChange(date)}
                    label={label}
                    slotProps={{
                        textField: {
                            variant: 'standard',
                            InputLabelProps: {shrink: true}
                        },
                    }}
                />
            )}
        />
    );
}

interface HeadIcon {
    children: ReactNode
    Icon: OverridableComponent<SvgIconTypeMap<{}, "svg">> & { muiName: string; }
}

export function HeadIcon({children, Icon}: HeadIcon) {

    return (
        <Stack direction='row' alignItems='center' gap={0.8} mb={0.5}>
            <Icon fontSize="small"/>
            <Typography fontWeight={500}>{children}</Typography>
        </Stack>
    )
}


export const FormWidget = styled(Stack)<StackProps>(({}) => ({
    gap: 12,
    paddingLeft: 8,
    paddingRight: 8,
}))

export const Widget = styled(Paper)<PaperProps>(({}) => ({
    flex: 1,
    borderRadius: 12,
    padding: 15,
}))
