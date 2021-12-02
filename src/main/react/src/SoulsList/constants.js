export const SoulType =  {
    unborn: 'Нерожденная',
    lost: 'В астрале',
    dead: 'Мертвая'
};

export const soulColumns = [
    {
        field: 'id',
        headerName: 'ID',
        width: 300
    },
    {
        field: 'type',
        headerName: 'Тип',
        width: 400,
        sortable: true,
    },
    {
        field: 'info',
        headerName: 'Дополнительная информация',
        width: 600
    }
];
