

export const formatterWhole = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'PHP',
  minimumFractionDigits: 0,
  maximumFractionDigits: 0,
})


export const formatterDecimal = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'PHP',
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
})


export const calculateAge = (birthdateString: string) => {
  const birthdate = new Date(birthdateString);

  const today = new Date();

  let age = today.getFullYear() - birthdate.getFullYear();
  const monthDifference = today.getMonth() - birthdate.getMonth();
  const dayDifference = today.getDate() - birthdate.getDate();

  if (monthDifference < 0 || (monthDifference === 0 && dayDifference < 0)) {
      age--;
  }

  return age;
}

type IDType = 'tin' |'sss' | 'philhealth' | 'pagibig'

export const idformatter = (value: string, type: IDType) => {
  const cleanedValue = value.replace(/\D/g, '')

  switch(type){
    case "tin":{
      return `${cleanedValue.substring(0, 3)}-${cleanedValue.substring(3, 6)}-${cleanedValue.substring(6, 9)}-${cleanedValue.substring(9, 12)}`
    }
    case "sss":{
      return `${cleanedValue.substring(0, 2)}-${cleanedValue.substring(2, 7)}-${cleanedValue.substring(7, 10)}`
    }
    case "philhealth":{
      return `${cleanedValue.substring(0, 4)}-${cleanedValue.substring(4, 8)}-${cleanedValue.substring(8, 12)}`
    }
    case "pagibig":{
      return `${cleanedValue.substring(0, 2)}-${cleanedValue.substring(2, 10)}-${cleanedValue.substring(10, 12)}`
    }
  }
}