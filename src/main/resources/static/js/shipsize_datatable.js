$(document).ready( function () {
    $('#shipsize-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: '/api/shipSize',
            dataSrc: '_embedded.shipSize'
        },
        columns: [
            {data: 'name'},
            {data: 'minKg'},
            {data: 'maxKg'},
            {
                sortable: false,
                searchable: false,
                render: shipSizeActionButtons
            }
        ]
    });
});


function shipSizeActionButtons( data, type, row ) {
    var editButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#shipSizeModal" role="button" onclick="shipSizeModal(\'/shipsizes/'+row.id+'\', \''+row.name+'\');">Szerkesztés</a>';
    var deleteButton = ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#shipSizeDeleteModal" role="button" onclick="shipSizeDeleteModal(\''+row._links.self.href+'\', \''+row.name+'\');">Törlés</a>';
    return editButton + deleteButton;
}

function shipSizeDeleteModal(link, name){
    document.getElementById('shipSizeDeleteModalLabel').innerHTML = name + ' méret törlése';
    document.getElementById('shipSizeDelete').addEventListener('click', function(){
        $.ajax({
            type: 'DELETE',
            url: link,
            success: function(msg){
                $('#shipSizeDeleteModal').modal('hide');
                $('#shipsize-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}

function shipSizeModal(link, name){
    document.getElementById('shipSizeModalLabel').innerHTML = name + ' méret adatai';
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('shipSizeUpdate').innerHTML = result;
        }
    });
}

function validateShipSize(id){
    $.ajax({
        url:'/shipsizes/' + id,
        type:'POST',
        data:$('#shipSizeForm').serialize(),
        success:function(result){
            document.getElementById('shipSizeUpdate').innerHTML = result;
            $('#shipSizePost').click();
        }
    });
    return false;
}

function submitShipSize(id){
    var data = $("#shipSizeForm").serializeObject();
    $.ajax({
        type: id == 0 ? 'POST' : 'PATCH',
        url: id == 0 ? '/api/shipSize' : 'api/shipSize/' + id,
        data: JSON.stringify(data),
        success: function (msg) {
            document.getElementById('shipSizeModalLabel').innerHTML = "";
            $('#shipSizeModal').modal('hide');
            $('#shipsize-table').DataTable().ajax.reload( null, false );
        },
        dataType: 'json',
        contentType : 'application/json'
    });
}