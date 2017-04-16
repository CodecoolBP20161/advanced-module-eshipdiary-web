function subTypeButtons( data, type, row) {
    var buttons = '';
    for(var i in row.subTypes){
        buttons +=
            ' <div class="btn-group">' +
                '<button type="button" class="btn btn-info btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
                    row.subTypes[i].code + ' <span class="caret"></span>' +
                '</button>' +
                '<ul class="dropdown-menu">' +
                    '<li><a data-toggle="modal" data-target="#subTypeModal" role="button" onclick="subTypeModal(\'/admin/subtypes/'+row.subTypes[i].id+'\', \''+row.subTypes[i].code+'\');">Szerkesztés</a></li>' +
                    deleteSubType(row.subTypes[i]) +
                    '<li><a data-toggle="modal" data-target="#shipModal" role="button" onclick="shipModal(\'/admin/ships/subtype/'+row.subTypes[i].id+'\', \'Új hajó\');">Hajó</a></li>' +
                '</ul>' +
            '</div>';
    }
    return buttons;
}

function deleteSubType(subType){
    if(subType.ships === 0) return '<li><a data-toggle="modal" data-target="#subTypeDeleteModal" role="button" onclick="subTypeDeleteModal(\'/api/subType/'+subType.id+'\', \''+subType.code+'\');">Törlés</a></li>';
    return '<li class="disabled"><a data-toggle="tooltip" title="Hozzátartozó hajó miatt nem törölhető!">Törlés</a></li>';
}

function subTypeDeleteModal(link, name){
    document.getElementById('subTypeDeleteModalLabel').innerHTML = name + ' altípus törlése';
    document.getElementById('subTypeDelete').addEventListener('click', function(){
        $.ajax({
            type: 'DELETE',
            url: link,
            success: function(msg){
                $('#subTypeDeleteModal').modal('hide');
                $('#shiptype-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}

function subTypeModal(link, name){
    document.getElementById('subTypeModalLabel').innerHTML = name + ' altípus adatai';
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('subTypeUpdate').innerHTML = result;
        }
    });
}

function validateSubType(id){
    $.ajax({
        url:'/admin/subtypes/' + id,
        type:'POST',
        data:$('#subTypeForm').serialize(),
        success:function(result){
            document.getElementById('subTypeUpdate').innerHTML = result;
            $('#subTypePost').click();
        }
    });
    return false;
}

function submitSubType(id){
    var data = $("#subTypeForm").serializeObject();
    data.type = window.location.origin + '/api/shipType/' + data.type;
    $.ajax({
        type: id == 0 ? 'POST' : 'PATCH',
        url: id == 0 ? '/api/subType' : '/api/subType/' + id,
        data: JSON.stringify(data),
        success: function (msg) {
            document.getElementById('subTypeModalLabel').innerHTML = "";
            $('#subTypeModal').modal('hide');
            $('#shiptype-table').DataTable().ajax.reload( null, false );
        },
        dataType: 'json',
        contentType : 'application/json'
    });
}