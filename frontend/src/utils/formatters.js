export const formatDateTime = (value) => {
  if (!value) return '—'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return '—'
  return new Intl.DateTimeFormat('en-GB', { dateStyle: 'medium', timeStyle: 'short' }).format(date)
}
export const formatCurrency = (value) => value === null || value === undefined ? '—' : new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'USD', minimumFractionDigits: 2 }).format(value)
export const apiErrorMessage = (error) => error?.response?.data?.message || error?.response?.data?.error || error?.message || 'Something went wrong. Please try again.'
export const toLocalInputValue = (date) => {
  const value = new Date(date)
  const local = new Date(value.getTime() - value.getTimezoneOffset() * 60000)
  return local.toISOString().slice(0, 16)
}
export const toIso = (value) => new Date(value).toISOString()
