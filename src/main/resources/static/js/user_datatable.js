$(document).ready( function () {
    var table = $('#user-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
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
                render: function ( data, type, row ) {
                    return '<a class="btn btn-info btn-sm" data-toggle="modal" data-target="#updateModal" role="button" onclick="updateModal(\'/users/'+row.id+'\', \''+row.userName+'\');">Részletek</a>' +
                        ' <a class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.userName+'\');">Törlés</a>';
                }
            }
        ]
    });
});

function deleteModal(link, name){
    document.getElementById('deleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('user-delete').addEventListener('click', function(){
        $.ajax({
            type: "DELETE",
            url: link,
            success: function(msg){location.reload()}
        });
    });
}

function updateModal(link, name){
    name = (typeof name !== 'undefined') ?  name : "Új tag";
    document.getElementById('updateModalLabel').innerHTML = name + ' adatai';
    $.ajax({
        url: link,
        success: function(result){
            document.getElementById("user-update").innerHTML = result;
        }
    });
}

