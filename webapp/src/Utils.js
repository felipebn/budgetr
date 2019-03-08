export const formatMoney = (value) => {
    return value.toLocaleString(undefined, { style: 'currency', currency: 'EUR' })
};
