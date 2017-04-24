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
            {data: 'subType'},
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
    var editButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#shipModal" role="button" onclick="shipModal(\'/admin/ships/'+row.id+'\', \''+row.name+'\');">Részletek</a>';
    var deleteButton = ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#shipDeleteModal" role="button" onclick="shipDeleteModal(\''+row._links.self.href+'\', \''+row.name+'\');">Törlés</a>';
    var statusChangeButton = ' <a class="btn btn-'+buttonType+' btn-xs" role="button" onclick="setShipStatus(\''+row._links.self.href+'\', ' + shouldBeActive + ')">' + activationLabel + '</a>';
    var availableUsersButton = ' <a class="btn btn-default btn-xs" data-toggle="modal" data-target="#userWhitelistingModal" role="button" ' +
        'onclick="userWhitelistingModal(\'/admin/ships/'+row.id+'/enable_users\', \''+row.name+'\')">Engedélyezett tagok</a>';
    return editButton + deleteButton + statusChangeButton + availableUsersButton;
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
    $('#shipDelete').off('click').on('click', function(){
        $.ajax({
            type: 'DELETE',
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

function validateShip(id){
    $.ajax({
        url:'/admin/ships/' + id,
        type:'POST',
        data:$('#shipForm').serialize(),
        success:function(result){
            document.getElementById('shipUpdate').innerHTML = result;
            $('#shipPost').click();
        }
    });
    return false;
}

function submitShip(id){
    var data = $("#shipForm").serializeObject();
    data.owner = window.location.origin + "/api/user/" + data.owner;
    data.size = window.location.origin + "/api/shipSize/" + data.size;
    data.subType = window.location.origin + "/api/subType/" + data.subType;
    $.ajax({
        type: id == 0 ? 'POST' : 'PATCH',
        url: id == 0 ? '/api/ship' : '/api/ship/' + id,
        data: JSON.stringify(data),
        success: function (msg) {
            document.getElementById('shipModalLabel').innerHTML = "";
            $('#shipModal').modal('hide');
            $('#ship-table').DataTable().ajax.reload( null, false );
            $('#user-table').DataTable().ajax.reload( null, false );
        },
        dataType: 'json',
        contentType : 'application/json'
    });
}

function userWhitelistingModal(link, name){
    document.getElementById('userWhitelistingModalLabel').innerHTML = name + ' felhasználókhoz rendelése';
    $.ajax({
        url: link,
        success: function(result){
            document.getElementById('user-whitelistig-modal-body').innerHTML = result;
            multipleSelect();
        },
    });
}