$(document).ready( function () {
    var table = $('#oar-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: '/api/oar',
            dataSrc: '_embedded.oar'
        },
        columns: [
            {data: 'name'},
            {data: 'type'},

            {
                sortable: false,
                render: oarActionButtons
            }
        ]
    });
});


function oarActionButtons( data, type, row ) {

    var editButton = '<a class="btn btn-info btn-sm" role="button" href="oars/update/'+row.id+'">Szerkesztés</a>';
    var deleteButton = '<a class="btn btn-danger btn-sm" role="button" href="oars/delete/'+row.id+'">Törlés</a>';
    return editButton + deleteButton;
}