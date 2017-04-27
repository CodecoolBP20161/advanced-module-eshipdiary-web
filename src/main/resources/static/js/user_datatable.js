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
    var detailsButton = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/admin/users/'+row.id+'\', \''+row.name+'\');">Részletek</a>';
    var shipButton = ' <a class="btn btn-default btn-xs" data-toggle="modal" data-target="#shipModal" role="button" onclick="shipModal(\'/admin/ships/user/'+row.id+'\', \'Új hajó\');">Hajó</a>';
    var oarButton = ' <a class="btn btn-default btn-xs" data-toggle="modal" data-target="#oarModal" role="button" onclick="oarModal(\'/admin/oars/user/'+row.id+'\', \'Új evező\');">Evező</a>';
    var current = '';
    if(document.getElementById("current").value !== row.id) {
        current = deleteButton(row);
        current += row.member ? statusButton(row) : '';
    }
    var availableShipsButton = ' <a class="btn btn-default btn-xs" data-toggle="modal" data-target="#shipWhitelistingModal" role="button" ' +
        'onclick="shipWhitelistingModal(\'/admin/users/'+row.id+'/enable_ships\', \''+row.name+'\')">Engedélyezett hajók</a>';
    return detailsButton + current + shipButton + oarButton + availableShipsButton;
}

function statusButton(row){
    var isActive = row.isActive === "Inaktív";
    var activationLabel = isActive ? 'Aktiválás' : 'Inaktiválás';
    var activationClass = isActive ? 'success' : 'warning';
    return ' <a class="btn btn-'+activationClass+' btn-xs" role="button" onclick="setUserStatus(\''+row._links.self.href+'\', ' + isActive + ')">' + activationLabel + '</a>';
}

function deleteButton(row){
    if(row.member === true) return ' <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.name+'\');">Kiléptetés</a>';
    return ' <a class="btn btn-success btn-xs" role="button" data-toggle="modal" data-target="#updateModal" ' +
        'onclick="reActivate(\''+row._links.self.href+'\', \'/admin/users/'+row.id+'\', \''+row.name+'\');">Beiratkozás</a>';
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
    document.getElementById('deleteModalLabel').innerHTML = name + ' kiléptetése';
    $('#userDelete').off('click').on('click', function(){
        $.ajax({
            type: 'PATCH',
            url: link,
            data: JSON.stringify({"active": false, "member": false}),
            success: function(msg){
                $('#deleteModal').modal('hide');
                $('#user-table').DataTable().ajax.reload( null, false );
            },
            dataType: 'json',
            contentType : 'application/json'
        });
    });
}

function shipWhitelistingModal(link, name) {
    document.getElementById('shipWhitelistingModalLabel').innerHTML = name + ' hajókhoz rendelése';
    $.ajax({
        url: link,
        success: function(result){
            document.getElementById('ship-whitelisting-modal-body').innerHTML = result;
            checkAllCorrespondingShips();
            checkAllShips();
        }
    });
}

function checkAllCorrespondingShips() {
    $('input[name=shipTypeCheckbox]').on('click', function () {
        if (this.checked) {
            $(this).parents('tr:first').find('input[type=checkbox]').each(function () {
                this.checked = true;
            });
        } else {
            $(this).parents('tr:first').find('input[type=checkbox]').each(function () {
                this.checked = false;
            });
        }
    });
}

function checkAllShips() {
    $("#select_all").change(function() {
        $(".ship_checkbox").prop('checked', $(this).prop("checked"));
    });

    $('.ship_checkbox').change(function () {
        if($(this).prop("checked") == false) {
            $("#select_all").prop('checked', false);
        }

        if ($('.ship_checkbox:checked').length == $('.ship_checkbox').length ){
            $("#select_all").prop('checked', true);
        }
    });
}

function reActivate(link, reactivateLink, name){
    $.ajax({
        type: 'PATCH',
        url: link,
        data: JSON.stringify({"member": true}),
        success: function(msg){$('#user-table').DataTable().ajax.reload( null, false );},
        dataType: 'json',
        contentType : 'application/json'
    });
    updateModal(reactivateLink, name);
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
        url:'/admin/users/' + id,
        type:'POST',
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
        url: id == 0 ? '/api/user' : '/api/user/' + id,
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


function dataTableUrl(filter) {
    var extra = "";
    switch (filter) {
        case "active":
            extra = '/search/findAllActives';
            break;
        case "inactive":
            extra = '/search/findAllInactives';
            break;
        case "nonMember":
            extra = '/search/findAllNonMembers';
            break;
    }
    var filterUrl = '/api/user'+ extra +'?projection=userOverview';
    $('#user-table').DataTable().ajax.url(filterUrl).load();

}