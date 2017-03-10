$(document).ready( function () {
    $('#ship-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: '/api/ship?projection=shipOverview',
            dataSrc: '_embedded.ship'
        },
        columns: [
            {data: 'name'},
            {data: 'shipType'},
            {data: 'maxSeat'},
            {data: 'category'},
            {data: 'owner'},
            {
                sortable: false,
                searchable: false,
                render: shipActionButtons
            }
        ]
    });
});


function shipActionButtons( data, type, row ) {
    var shouldBeActive = !(row.active);
    var activationLabel = shouldBeActive ? 'Aktiválás' : 'Inaktiválás';
    var buttonType = shouldBeActive ? 'success' : 'warning';
    var editButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#shipModal" role="button" onclick="shipModal(\'/ships/update/'+row.id+'\', \''+row.name+'\');">Szerkesztés</a>';
    var deleteButton = ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#shipDeleteModal" role="button" onclick="shipDeleteModal(\'/ships/delete/'+row.id+'\', \''+row.name+'\');">Törlés</a>';
    var statusChangeButton = ' <a class="btn btn-'+buttonType+' btn-xs" role="button" onclick="setShipStatus(\''+row._links.self.href+'\', ' + shouldBeActive + ')">' + activationLabel + '</a>';
    return editButton + deleteButton + statusChangeButton;
}

function setShipStatus(link, shouldBeActive) {
    $.ajax({
        url: link,
        type: 'PATCH',
        data: JSON.stringify({"active": shouldBeActive}),
        success: function (msg) {$('#ship-table').DataTable().ajax.reload( null, false );},
        dataType: 'json',
        contentType : 'application/json'
    })
}


function shipDeleteModal(link, name){
    document.getElementById('shipDeleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('shipDelete').addEventListener('click', function(){
        $.ajax({
            type: 'GET',
            url: link,
            success: function(msg){
                $('#shipDeleteModal').modal('hide');
                $('#ship-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}

function shipModal(link, name){
    document.getElementById('shipModalLabel').innerHTML = name + ' adatai';
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('shipUpdate').innerHTML = result;
        }
    });
}

function validateShip(){
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

function submitShip(){
    document.getElementById('shipModalLabel').innerHTML = "";
    $('#shipModal').modal('hide');
    $('#ship-table').DataTable().ajax.reload( null, false );
    $('#user-table').DataTable().ajax.reload( null, false );
}
