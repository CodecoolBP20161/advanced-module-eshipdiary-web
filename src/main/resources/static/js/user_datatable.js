$(document).ready( function () {
    loadUserTable();
});

function loadUserTable(){
    var table = $('#user-table').DataTable ({
        "language": {
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
            {data: 'active'},
            {
                sortable: false,
                render: function ( data, type, row ) {
                    console.log(row._links.self.href);
                    return '<a class="btn btn-info btn-sm" data-toggle="modal" data-target="#userModal" role="button" onclick="loadUserDetails(\''+row._links.self.href+'\', \''+row.userName+'\');">Részletek</a>' +
                        ' <a class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteModal" role="button" onclick="deleteModal(\''+row._links.self.href+'\', \''+row.userName+'\');">Törlés</a>';
                }
            }
        ]
    });
}

function loadUserDetails(link, name){
    document.getElementById('userModalLabel').innerHTML = name + ' adatai';
    $.getJSON(link, function(data) {
        document.getElementById('first-name').innerHTML = data.firstName;
        document.getElementById('last-name').innerHTML = data.lastName;
        document.getElementById('user-name').innerHTML = data.userName;
        document.getElementById('email-address').innerHTML = data.emailAddress;
        document.getElementById('birth-date').innerHTML = data.birthDate;
        document.getElementById('phone-number').innerHTML = data.phoneNumber;
        document.getElementById('knowledge-level').innerHTML = data.knowledgeLevel;
        document.getElementById('weight').innerHTML = data.weightInKg;
        document.getElementById('active').innerHTML = data.active;
    });
}

function deleteModal(link, name){
    document.getElementById('deleteModalLabel').innerHTML = name + ' törlése';
    document.getElementById('user-delete').setAttribute('onclick', 'deleteUser("' + link + '")');
}

function deleteUser(link){
    $.ajax({
        type: "DELETE",
        url: link,
        success: function(msg){
            location.reload();
        }
    });
}

