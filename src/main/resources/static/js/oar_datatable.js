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
            {data: 'oarType'}//,

            // {
            //     sortable: false,
            //     render: shipActionButtons
            // }
        ]
    });
});


// function shipActionButtons( data, type, row ) {
//     var detailsButton = ' <a class="btn btn-info btn-sm" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/ships/'+row.id+'\', \''+row.name+'\');">Részletek</a>';
//     // var deleteButton = ' <a class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.userName+'\');">Törlés</a>';
//     var deleteButton = ' <a class="btn btn-danger btn-sm" href="ships/delete/' + row.name + '">Törlés</a>'; //TODO: confirmation before delete
//
//     return detailsButton + deleteButton;
// }

