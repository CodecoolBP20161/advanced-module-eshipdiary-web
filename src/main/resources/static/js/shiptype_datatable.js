$(document).ready( function () {
    $('#shiptype-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: '/api/shipType?projection=shipTypeOverview',
            dataSrc: '_embedded.shipType'
        },
        columns: [
            {data: 'name'},
            {
                sortable: false,
                searchable: false,
                render: subTypeButtons
            },
            {
                sortable: false,
                searchable: false,
                render: shipTypeActionButtons
            }
        ]
    });
});

function shipTypeActionButtons( data, type, row ) {
    var subTypeButton = ' <a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#subTypeModal" role="button" onclick="subTypeModal(\'/admin/subtypes/shiptype/'+row.id+'\', \'Új\');">Altípus</a>';
    var editButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#shipTypeModal" role="button" onclick="shipTypeModal(\'/admin/shiptypes/'+row.id+'\', \''+row.name+'\');">Részletek</a>';
    var oarButton = ' <a class="btn btn-default btn-xs" data-toggle="modal" data-target="#oarModal" role="button" onclick="oarModal(\'/admin/oars/shiptype/'+row.id+'\', \'Új evező\');">Evező</a>';
    return subTypeButton + editButton + deleteButton(row) + oarButton;
}

function deleteButton(row){
    if(row.oars + row.subTypes.length === 0) return ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#shipTypeDeleteModal" role="button" onclick="shipTypeDeleteModal(\''+row._links.self.href+'\', \''+row.name+'\');">Törlés</a>';
    return ' <button disabled class="btn btn-default btn-xs" data-toggle="tooltip" title="Hozzátartozó altípus/evező miatt nem törölhető!">Törlés</button>';
}

function shipTypeDeleteModal(link, name){
    document.getElementById('shipTypeDeleteModalLabel').innerHTML = name + ' típus törlése';
    document.getElementById('shipTypeDelete').addEventListener('click', function(){
        $.ajax({
            type: 'DELETE',
            url: link,
            success: function(msg){
                $('#shipTypeDeleteModal').modal('hide');
                $('#shiptype-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}

function shipTypeModal(link, name){
    document.getElementById('shipTypeModalLabel').innerHTML = name + ' típus adatai';
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('shipTypeUpdate').innerHTML = result;
        }
    });
}

function validateShipType(id){
    $.ajax({
        url:'/admin/shiptypes/' + id,
        type:'POST',
        data:$('#shipTypeForm').serialize(),
        success:function(result){
            document.getElementById('shipTypeUpdate').innerHTML = result;
            $('#shipTypePost').click();
        }
    });
    return false;
}

function submitShipType(id){
    var data = $("#shipTypeForm").serializeObject();
    $.ajax({
        type: id == 0 ? 'POST' : 'PATCH',
        url: id == 0 ? '/api/shipType' : '/api/shipType/' + id,
        data: JSON.stringify(data),
        success: function (msg) {
            document.getElementById('shipTypeModalLabel').innerHTML = "";
            $('#shipTypeModal').modal('hide');
            $('#shiptype-table').DataTable().ajax.reload( null, false );
        },
        dataType: 'json',
        contentType : 'application/json'
    });
}