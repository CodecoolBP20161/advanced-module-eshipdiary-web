$(document).ready( function () {
    var table = $('#user-table').DataTable ({
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
                render: userActionButtons
            }
        ]
    });
});

function userActionButtons( data, type, row ) {
    var shouldBeActive;
    var activationLabel;
    var buttonType;
    if (row.isActive === "Aktív") {
        activationLabel = 'Inaktiválás';
        shouldBeActive = false;
        buttonType = 'warning'
    } else {
        activationLabel = 'Aktiválás';
        shouldBeActive = true;
        buttonType = 'success'
    }

    var detailsButton = ' <a class="btn btn-info btn-sm" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/users/'+row.id+'\', \''+row.name+'\');">Részletek</a>';
    var deleteButton = ' <a class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.name+'\');">Törlés</a>';
    var statusChangeButton = ' <a class="btn btn-'+buttonType+' btn-sm" role="button" onclick="setUserStatus(\''+row._links.self.href+'\', ' + shouldBeActive + ')">' + activationLabel + '</a>';
    return detailsButton + deleteButton + statusChangeButton;
}

function setUserStatus(link, shouldBeActive) {
    $.ajax({
        url: link,
        type: 'PATCH',
        data: JSON.stringify({"active": shouldBeActive}),
        success: function (msg) {$('#user-table').DataTable().ajax.reload( null, false );},
        statusCode: {
            403: function() {
                location.reload();
            }
        },
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
            },
            statusCode: {
                403: function() {
                    location.reload();
                }
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
                name !== 'Új tag' ? disableModal() : enableModal();
            },
            statusCode: {
                403: function () {
                    location.reload();
                }
            }
        });
    }
}

function disableModal(){
    var form = document.getElementById('userForm');
    var elements = form.elements;
    for (var i = 0, len = elements.length; i < len; ++i) {
        elements[i].disabled = true;
    }
    document.getElementById('userEdit').style.display = 'inline';
    document.getElementById('userSubmit').style.display = 'none';
}

function enableModal(){
    var form = document.getElementById('userForm');
    var elements = form.elements;
    for (var i = 0, len = elements.length; i < len; ++i) {
        elements[i].disabled = false;
    }
    document.getElementById('userEdit').style.display = 'none';
    document.getElementById('userSubmit').style.display = 'inline';
    elements[0].focus();
}



function validateForm(id){
    $.ajax({
        url:'/users/' + id,
        type:'post',
        data:$('#userForm').serialize(),
        success:function(result){
            document.getElementById('userUpdate').innerHTML = result;
            $('#userPost').click();
        },
        statusCode: {
            403: function() {
                location.reload();
            }
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

        statusCode: {
            403: function() {
                location.reload();
            }
        },
        dataType: 'json',
        contentType : 'application/json'
    });
}

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$('.modal').on('shown.bs.modal', function() {
    $(this).find('[autofocus]').focus();
});
