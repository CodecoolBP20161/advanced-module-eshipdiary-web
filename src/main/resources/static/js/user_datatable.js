$(document).ready( function () {
    var table = $('#user-table').DataTable ({
        ajax: {
            url: '/api/user',
            dataSrc: '_embedded.user',
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


