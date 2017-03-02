$(document).ready( function () {
    var table = $('#user-table').DataTable ({
        language: {
            'url': 'https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json'
        },
        ajax: {
            url: '/api/user',
            dataSrc: '_embedded.user'
        },
        columns: [
            {data: 'firstName'},
            {data: 'lastName'},
            {data: 'birthDate'},
            {data: 'knowledgeLevel'},
            {
                data: 'active',
                render:
                    function (data, type, row) {
                        if (data === true) {
                            return 'Aktív';
                        }
                        return 'Inaktív'
                    }
            },
            {
                sortable: false,
                render: userActionButtons
            }
        ]
    });
});

function deleteModal(link, name){
    document.getElementById('deleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('user-delete').addEventListener('click', function(){
        $.ajax({
            type: 'DELETE',
            url: link,
            success: function(msg){location.reload()}
        });
    });
}

function updateModal(link, name){
    name = (typeof name !== 'undefined') ?  name : 'Új tag';
    document.getElementById('updateModalLabel').innerHTML = name + ' adatai';
    $.ajax({
        url: link,
        success: function(result){
            document.getElementById('user-update').innerHTML = result;
            if(name !== 'Új tag') {
                disableModal();
            } else {
                enableModal();
            }
        }
    });
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
}

function setUserStatus(link, shouldBeActive) {
    $.ajax({
        headers : {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json'
        },
        url: link,
        type: 'PATCH',
        data: JSON.stringify({"active": shouldBeActive}),
        success: function (msg) {location.reload()}
    })
}

function userActionButtons( data, type, row ) {
    var shouldBeActive;
    var activationLabel;
    var buttonType;
    if (row.active) {
        activationLabel = 'Inaktiválás';
        shouldBeActive = false;
        buttonType = 'warning'
    } else {
        activationLabel = 'Aktiválás';
        shouldBeActive = true;
        buttonType = 'success'
    }

    var detailsButton = ' <a class="btn btn-info btn-sm" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/users/'+row.id+'\', \''+row.userName+'\');">Részletek</a>';
    var deleteButton = ' <a class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.userName+'\');">Törlés</a>';
    var statusChangeButton = ' <a class="btn btn-'+buttonType+' btn-sm" role="button" onclick="setUserStatus(\''+row._links.self.href+'\', ' + shouldBeActive + ')">' + activationLabel + '</a>';
    return detailsButton + deleteButton + statusChangeButton;
}

function submitForm(id){
    var data = $("#userForm").serializeObject();
    $.ajax({
        type: id == 0 ? 'POST' : 'PATCH',
        url: id == 0 ? '/api/user' : 'api/user/' + id,
        data: JSON.stringify(data),
        success: function (msg) {location.reload()},
        dataType: 'json',
        contentType : 'application/json'
    });
    return false;
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