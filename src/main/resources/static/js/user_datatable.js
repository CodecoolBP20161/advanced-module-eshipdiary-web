$(document).ready( function () {
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
                render: function ( data, type, full, meta ) {
                    var link = full._links.self.href;
                    return '<a class="btn btn-warning rescindBtn" role="button" onclick="loadUserDetails(link, full.lastName)">Szerkesztés</a>';
                }
            }
        ]
    });
});

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


