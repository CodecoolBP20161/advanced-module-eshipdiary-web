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
            {data: 'active'}
        ]
    });
});


