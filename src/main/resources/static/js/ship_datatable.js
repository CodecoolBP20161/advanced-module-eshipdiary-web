$(document).ready( function () {
    $('#ship-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: '/api/ship',
            dataSrc: '_embedded.ship'
        },
        columns: [
            {data: 'name'},
            {data: 'shipType'},
            {data: 'maxSeat'},
            {data: 'category'},

            {
                sortable: false,
                render: shipActionButtons
            }
        ]
    });
});


function shipActionButtons( data, type, row ) {
    var editButton = ' <a class="btn btn-info btn-xs" role="button" href="ships/update/'+row.id+'">Szerkesztés</a>';
    var deleteButton = ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\'/ships/delete/'+row.id+'\', \''+row.name+'\');">Törlés</a>';
    return editButton + deleteButton;
}


function deleteModal(link, name){
    document.getElementById('deleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('shipDelete').addEventListener('click', function(){
        $.ajax({
            type: 'GET',
            url: link,
            success: function(msg){
                $('#deleteModal').modal('hide');
                $('#ship-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}
