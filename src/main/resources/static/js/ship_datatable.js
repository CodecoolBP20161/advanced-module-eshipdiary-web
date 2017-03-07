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
                searchable: false,
                render: shipActionButtons
            }
        ]
    });
});


function shipActionButtons( data, type, row ) {
    var editButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/ships/update/'+row.id+'\', \''+row.name+'\');">Szerkesztés</a>';
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

function updateModal(link, name){
    if(name !== 'Új hajó' || document.getElementById('updateModalLabel').innerHTML !== 'Új hajó adatai') {
        document.getElementById('updateModalLabel').innerHTML = name + ' adatai';
        $.ajax({
            url: link,
            type: "OPTIONS",
            success: function (result) {
                document.getElementById('shipUpdate').innerHTML = result;
            }
        });
    }
}

function validateForm(){
    $.ajax({
        url:'/ships/update',
        type:'POST',
        data:$('#shipForm').serialize(),
        success:function(result){
            document.getElementById('shipUpdate').innerHTML = result;
            $('#shipPost').click();
        }
    });
    return false;
}

function submitForm(){
    $('#updateModal').modal('hide');
    $('#ship-table').DataTable().ajax.reload( null, false );
}
