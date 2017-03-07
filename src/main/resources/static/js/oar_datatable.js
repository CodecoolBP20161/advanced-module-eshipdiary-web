$(document).ready( function () {
    $('#oar-table').DataTable ({
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
                searchable: false,
                render: oarActionButtons
            }
        ]
    });
});


function oarActionButtons( data, type, row ) {
    var editButton = ' <a class="btn btn-info btn-xs" role="button" href="oars/update/'+row.id+'">Szerkesztés</a>';
    var deleteButton = ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\'/oars/delete/'+row.id+'\', \''+row.name+'\');">Törlés</a>';
    return editButton + deleteButton;
}

function deleteModal(link, name){
    document.getElementById('deleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('oarDelete').addEventListener('click', function(){
        $.ajax({
            type: 'GET',
            url: link,
            success: function(msg){
                $('#deleteModal').modal('hide');
                $('#oar-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}