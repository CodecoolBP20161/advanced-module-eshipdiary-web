function multipleSelect () {
    $('.multi-select').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...'
    });
}

function rentalModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            multipleSelect();
        }
    });
}

function validateRentalLog() {
    $.ajax({
        url: '/rentals',
        type: 'POST',
        data: $('#rentalForm').serialize(),
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            $('#rentalPost').click();
        }
    });
    return false;
}

function submitRentalLog() {
    var data = $("#rentalForm").serializeObject();
    data.chosenShip = window.location.origin + '/api/ship/' + data.chosenShip;
    data.cox = window.location.origin + '/api/user/' + data.cox;
    data.captain = window.location.origin + '/api/user/' + data.captain;
    dataCrew = [];
    for (var i = 0; i < data.crew.length; i++) {
        if(data.crew[i] == 0) {
            dataCrew.push(null);
        } else {
            dataCrew.push(window.location.origin + '/api/user/' + data.crew[i]);
        }
    }
    data.crew = dataCrew;

    dataOars = [];
    for (var j = 0; j < data.oars.length; j++) {
        if(data.oars[j] == 0) {
            dataOars.push(null);
        } else {
            dataOars.push(window.location.origin + '/api/oar/' + data.oars[j]);
        }
    }
    data.oars = dataOars;

    $.ajax({
        type: 'POST',
        url: '/api/rental',
        data: JSON.stringify(data),
        success: function (msg) {
            $('#rentalModal').modal('hide');
            // $('#rental-table').DataTable().ajax.reload(null, false);
        },
        dataType: 'json',
        contentType: 'application/json'
    });
}
