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