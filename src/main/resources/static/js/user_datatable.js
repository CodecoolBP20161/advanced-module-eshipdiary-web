$(document).ready( function () {
    $('#user-table').DataTable ({
        language: {
            'url': 'https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json'
        },
        ajax: {
            url: '/api/user?projection=userOverview',
            dataSrc: '_embedded.user'
        },
        columns: [
            {data: 'name'},
            {data: 'age'},
            {data: 'knowledgeLevel'},
            {data: 'isActive'},
            {
                sortable: false,
                searchable: false,
                render: userActionButtons
            }
        ]
    });
});

function userActionButtons( data, type, row ) {
    var shouldBeActive = row.isActive === "Inaktív";
    var activationLabel = shouldBeActive ? 'Aktiválás' : 'Inaktiválás';
    var buttonType = shouldBeActive ? 'success' : 'warning';
    var detailsButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/users/'+row.id+'\', \''+row.name+'\');">Szerkesztés</a>';
    var deleteButton = ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.name+'\');">Törlés</a>';
    var statusChangeButton = ' <a class="btn btn-'+buttonType+' btn-xs" role="button" onclick="setUserStatus(\''+row._links.self.href+'\', ' + shouldBeActive + ')">' + activationLabel + '</a>';
    return detailsButton + deleteButton + statusChangeButton;
}

function setUserStatus(link, shouldBeActive) {
    $.ajax({
        url: link,
        type: 'PATCH',
        data: JSON.stringify({"active": shouldBeActive}),
        success: function (msg) {$('#user-table').DataTable().ajax.reload( null, false );},
        dataType: 'json',
        contentType : 'application/json'
    })
}

function deleteModal(link, name){
    document.getElementById('deleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('userDelete').addEventListener('click', function(){
        $.ajax({
            type: 'DELETE',
            url: link,
            success: function(msg){
                $('#deleteModal').modal('hide');
                $('#user-table').DataTable().ajax.reload( null, false );
            }
        });
    });
}

function updateModal(link, name){
    if(name !== 'Új tag' || document.getElementById('updateModalLabel').innerHTML !== 'Új tag adatai') {
        document.getElementById('updateModalLabel').innerHTML = name + ' adatai';
        $.ajax({
            url: link,
            success: function (result) {
                document.getElementById('userUpdate').innerHTML = result;
            }
        });
    }
}

function validateForm(id){
    $.ajax({
        url:'/users/' + id,
        type:'post',
        data:$('#userForm').serialize(),
        success:function(result){
            document.getElementById('userUpdate').innerHTML = result;
            $('#userPost').click();
        }
    });
    return false;
}

function submitForm(id){
    var data = $("#userForm").serializeObject();
    $.ajax({
        type: id == 0 ? 'POST' : 'PATCH',
        url: id == 0 ? '/api/user' : 'api/user/' + id,
        data: JSON.stringify(data),
        success: function (msg) {
            document.getElementById('updateModalLabel').innerHTML = "";
            $('#updateModal').modal('hide');
            $('#user-table').DataTable().ajax.reload( null, false );
        },
        dataType: 'json',
        contentType : 'application/json'
    });
}