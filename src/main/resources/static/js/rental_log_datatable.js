$(document).ready( function () {
    $('#rental-table').DataTable ({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: '/api/rental?projection=rentalOverview',
            dataSrc: '_embedded.rental'
        },
        columns: [
            {data: 'captain'},
            {data: 'ship'},
            {
                data: 'rentalStart',
                searchable: false
            },
            {
                data: 'rentalPeriod',
                searchable: false
            },
            {
                data: 'cox',
                searchable: false
            },
            {
                data: 'distance',
                searchable: false
            },
            {
                sortable: false,
                searchable: false,
                render: rentalActionButtons
            }
        ]
    });
});

function rentalActionButtons( data, type, row ) {
    var details = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalDetailsModal(\'/rentals/details/'+row.id+'\');">Részletek</a>';
    var final = row.finalized ? '' : ' <a class="btn btn-success btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalFinalModal(\'/rentals/final/'+row.id+'\');">Véglegesítés</a>';
    var isAdmin = document.getElementById("role").value === 'ADMIN';
    var comment = isAdmin ? ' <a class="btn btn-warning btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalCommentModal(\'/rentals/comment/'+row.id+'\');">Megjegyzés</a>' : '';
    return details + final + comment;
}

function multipleSelect () {
    $('.multi-select').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...',
        buttonWidth : '100%'
    });
}

function rentalModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalModalLabel').innerHTML = "Hajóbérlés";
            document.getElementById('rentalUpdate').innerHTML = result;
            document.getElementById('rentalSubmit').style.display = "inline";
            multipleSelect();
        }
    });
}

function rentalFinalModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalModalLabel').innerHTML = "Hajóbérlés befejezése";
            document.getElementById('rentalUpdate').innerHTML = result;
            document.getElementById('rentalSubmit').style.display = "inline";
            multipleSelect();
        }
    });
}

function rentalCommentModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalModalLabel').innerHTML = "Megjegyzés hozzáfűzése";
            document.getElementById('rentalUpdate').innerHTML = result;
            document.getElementById('rentalSubmit').style.display = "inline";
            multipleSelect();
        }
    });
}

function rentalDetailsModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalModalLabel').innerHTML = "Bérlés részletei";
            document.getElementById('rentalUpdate').innerHTML = result;
            document.getElementById('rentalSubmit').style.display = "none";
        }
    });
}

function submitRentalLog() {
    $.ajax({
        type: 'POST',
        url: '/api/rental',
        data: JSON.stringify(processData($("#rentalForm").serializeObject())),
        success: function (msg) {
            $('#rentalModal').modal('hide');
            $('#rental-table').DataTable().ajax.reload(null, false);
        },
        dataType: 'json',
        contentType: 'application/json'
    });
    return false;
}

function processData(data) {
    data.chosenShip = window.location.origin + '/api/ship/' + data.chosenShip;
    data.cox = window.location.origin + '/api/user/' + data.cox;
    data.captain = window.location.origin + '/api/user/' + data.captain;
    if(data.crew.constructor === Array) {
        data.crew = data.crew.map(function(member, index){return window.location.origin + '/api/user/' + member});
    } else {
        data.crew = [window.location.origin + '/api/user/' + data.crew];
    }
    if(data.oars.constructor === Array) {
        data.oars = data.oars.map(function(oar, index){return window.location.origin + '/api/oar/' + oar});
    } else {
        data.oars = [window.location.origin + '/api/oar/' + data.oars];
    }
    return data;
}

function submitFinalRentalLog() {
    $.ajax({
        type: 'GET',
        url: '/rentals/final/transaction',
        // data: JSON.stringify($("#rentalForm").serializeObject()),
        success: function (msg) {
            console.log(msg);
            $('#rentalModal').modal('hide');
            $('#rental-table').DataTable().ajax.reload(null, false);
        },
        dataType: 'json',
        contentType: 'application/json'
    });
    return false;
}

function addAdminComment(id) {
    $.ajax({
        type: 'PATCH',
        url: '/api/rental/' + id,
        data: JSON.stringify($("#rentalForm").serializeObject()),
        success: function (msg) {
            $('#rentalModal').modal('hide');
            $('#rental-table').DataTable().ajax.reload(null, false);
        },
        dataType: 'json',
        contentType: 'application/json'
    });
    return false;
}