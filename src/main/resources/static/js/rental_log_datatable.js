$(document).ready( function () {
    $('#rental-table').DataTable ({
        language: {
            url: "https://cdn.datatables.net/plug-ins/1.10.13/i18n/Hungarian.json"
        },
        ajax: {
            url: $('#role').val() ==='ADMIN' ? '/api/rental?projection=rentalOverview' : '/api/rental/search/findForPrincipal?projection=rentalOverview',
            dataSrc: '_embedded.rental'
        },
        columns: [
            {   data: 'ship'
            },
            {   data: 'crewNames',
                render: $.fn.dataTable.render.ellipsis(15, true, false)
            },
            {   data: 'cox'
            },
            {
                data: 'rentalStart',
                searchable: false
            },
            {
                data: 'rentalEnd',
                searchable: false
            },
            {
                data: 'itinerary'
            },
            {
                data: 'comment',
                render: $.fn.dataTable.render.ellipsis(15, true, false)
            },
            {
                sortable: false,
                searchable: false,
                render: rentalActionButtons
            }
        ]
    });
    enableNewRental();
});

function getDataSource() {
    if($('#role').val() ==='ADMIN') {
        return '/api/rental?projection=rentalOverview';
    } else {
        return '/api/rental/search/findForPrincipal?projection=rentalOverview';
    }
}

function rentalActionButtons( data, type, row ) {
    var details = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalDetailsModal(\'/rentals/details/'+row.id+'\');">Részletek</a>';
    var final = row.rentalEnd == '' ? ' <a class="btn btn-success btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalFinalModal(\'/rentals/final/'+row.id+'\');">Véglegesítés</a>' : '';
    var isAdmin = document.getElementById("role").value === 'ADMIN';
    var comment = isAdmin ? ' <a class="btn btn-warning btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalCommentModal(\'/rentals/comment/'+row.id+'\');">Megjegyzés</a>' : '';
    var injury = row.injury ? ' <button class="btn btn-danger btn-xs">!</button>' : '';
    var reuse = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalModal(\'/rentals/reuse/'+row.id+'\');">Újra</a>';
    return details + final + comment + injury + reuse;
}

function multipleSelect () {
    $('.multi-select').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...',
        buttonWidth : '100%',
        nonSelectedText: ' ',
        buttonText: function(options, select) {
            if (options.length === 0) {
                return ' ';
            } else if (options.length > 2) {
                return options.length + ' választott';
            } else {
                var labels = [];
                options.each(function() {
                    if ($(this).attr('label') !== undefined) {
                        labels.push($(this).attr('label'));
                    }
                    else {
                        labels.push($(this).html());
                    }
                });
                return labels.join(', ') + '';
            }
        }
    });
}

function rentalModal(link) {
    $.ajax({
        url: link,
        type: 'OPTIONS',
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            document.getElementById('rentalSubmit').style.display = 'inline';
            $('#coxSelect').hide();
            multipleSelect();
            loadValidate();
            selectSubTypesByType();
            selectShipsBySubType();
            selectShipsByName();
        }
    });
}

function rentalFinalModal(link) {
    $.ajax({
        url: link,
        type: 'OPTIONS',
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            $('#injuredOars').val('').multiselect({
                nonSelectedText: ' ',
                buttonWidth: '100%'
            });
            confirmInjuries();
        }
    });
}

function confirmInjuries() {
    $('#injuredOars, #injuredShip').change(function() {
        var oarNum = $("select[id='injuredOars'] option:selected").length;
        var shipBool = $('#injuredShip').is(':checked');
        if (oarNum > 0 || shipBool) {
            $('#validateInjuriesCheck').prop('checked', false);
            $('#validateInjuriesLabel').show();
            $('#rentalSubmit').prop('disabled', true);
        } else {
            $('#validateInjuriesLabel').hide();
            $('#validateInjuriesCheck').prop('checked', true);
            $('#rentalSubmit').removeAttr('disabled');
        }
    });
    $('#validateInjuriesCheck').change(function() {
        if ($('#validateInjuriesCheck').is(':checked')) {
            $('#rentalSubmit').removeAttr('disabled');
        } else {
            $('#rentalSubmit').prop('disabled', true);
        }
    });
}

function rentalCommentModal(link) {
    $.ajax({
        url: link,
        type: 'OPTIONS',
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            multipleSelect();
        }
    });
}

function rentalDetailsModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
        }
    });
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

function loadValidate() {
    $('#crew').on('change', function () {
        this.setCustomValidity(validateCaptainPresence());
        countSeats($('#shipsBySubType').val());
        document.getElementById('oars').setCustomValidity(setMaxNumOfOars());
    });
    $('#cox').on('change', function () {
        document.getElementById('crew').setCustomValidity(validateCaptainPresence());
        removeCox(this.value);
    });
    $('#rentalPeriod').on('input', function () {
        this.setCustomValidity(validateRentalPeriod(this.value));
    });
    $('#distance').on('input', function () {
        this.setCustomValidity(validateDistance(this.value));
    });
    $('#oars').on('change', function () {
        this.setCustomValidity(setMaxNumOfOars());
    });
    $('#itinerary').on('input', function () {
        this.setCustomValidity('');
    });
    $('#itinerary').on('invalid', function () {
        this.setCustomValidity('Útirány kitöltése kötelező');
    });
}

function setMaxNumOfOars() {
    return $('#crew').val().length !== $('#oars').val().length ? 'Nem egyezik a legénység és a lapátok száma.' : '';
}

function validateDistance(distance) {
    return distance < 1 ? 'A megadott távolság nem lehet kevesebb, mint 1 km' : '';
}

function validateCaptainPresence() {
    var crew = $('#crew').val();
    crew.push($('#cox').val());
    if($('#role').val()!=='ADMIN' && !crew.includes($('#captain').val())) {
        return 'Bejelentkezett felhasználó nincs a legénységben'
    } else return ''
}

function validateRentalPeriod(rentalPeriod) {
    if(rentalPeriod > minutesUntilMidnight()) {
        return 'A bérlési idő maximum a nap végéig tart'
    } else if (rentalPeriod < 15) {
        return 'A bérlési idő minimum 15 perc'
    } else if (rentalPeriod % 15 != 0) {
        return 'Negyedórás időközt használj'
    } else return ''
}

function minutesUntilMidnight() {
    var midnight = new Date();
    midnight.setHours(24, 0, 0, 0);
    return (midnight.getTime() - new Date().getTime())/ 1000 / 60;
}

function removeCox(id) {
    var crew = $('#crew');
    var values = crew.val();
    if(id !== "0") values.splice($.inArray(id, values),1);
    $.ajax({
        type: 'GET',
        url: '/removecox',
        data: {'userId': id},
        dataType: 'json',
        success: function(data) {
            crew.build(data);
            crew.val(values).multiselect('refresh');
        }
    });
}

function getSubTypesByType(id) {
    $.ajax({
        type: 'GET',
        url: '/subtypesbytype',
        data: {'typeId': id},
        dataType: 'json',
        success: function(data) {
            var subTypes = $('#shipsBySubType');
            subTypes.build(data);
            $.isEmptyObject(data) ? $('#shipByName').build({}) : getShipsBySubType(subTypes.val());
        }
    });
}

function getShipsBySubType(id) {
    $.ajax({
        type: 'GET',
        url: '/availableshipsbysubtype',
        data: {'subTypeId': id},
        dataType: 'json',
        success: function(data) {
            $('#shipByName').build(data);
        }
    });
}

function getOarsByType(id) {
    $.ajax({
        type: 'GET',
        url: '/availableoarsbytype',
        data: {'typeId': id},
        dataType: 'json',
        success: function(data) {
            $('#oars').build(data);
        }
    });
}

$.fn.build = function(data){
    this.empty();
    for(var row in data) {
        this.append($('<option></option>').text(data[row]).val(row));
    }
    this.multiselect('setOptions');
    this.multiselect('rebuild');
    $.isEmptyObject(data) ? this.multiselect('disable') : this.multiselect('enable');
    $('#rentalSubmit').prop('disabled', $("#rentalForm :disabled").length !== 0);
    if (this.attr('id') !== 'crew') displayCox();
};

function selectSubTypesByType() {
    $('#subTypesByType').val(null).multiselect({
        buttonWidth: '100%',
        nonSelectedText: 'Típus',
        onChange: function (option, checked, select) {
            getSubTypesByType(option.val());
            getOarsByType(option.val());
        }
    })
}

function selectShipsBySubType() {
    $('#shipsBySubType').val(null).multiselect({
        buttonWidth: '100%',
        nonSelectedText: 'Altípus',
        onChange: function (option, checked, select) {
            getShipsBySubType(option.val());
        }
    })
}

function selectShipsByName() {
    $('#shipByName').val(null).multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...',
        buttonWidth: '100%',
        nonSelectedText: 'Hajó',
    })
}

function hideCox() {
    $('#cox').val("0").multiselect('refresh');
    removeCox("0");
    $('#coxSelect').hide();
}


function displayCox() {
    $.ajax({
        type: 'GET',
        url: '/isshipcoxed',
        data: {'subTypeId': $('#shipsBySubType').val()},
        dataType: 'json',
        success: function (data) {
            data ? $('#coxSelect').show() : hideCox();
        }
    });
}

function enableNewRental() {
    $.ajax({
        type: 'GET',
        url: '/rentalEnabled',
        success: function (data) {
            $('#newRental').prop('disabled', !data);
        }
    });
}

function countSeats(id) {
    $.ajax({
        type: 'GET',
        url: '/getmaxseat',
        data: {'subTypeId': id},
        dataType: 'json',
        success: function (data) {
            document.getElementById('crew').setCustomValidity(data < $('#crew').val().length ? 'Legénység létszáma meghaladja a megengedettet' : '');
        }
    });
}

$.fn.dataTable.render.ellipsis = function ( cutoff, wordbreak, escapeHtml ) {
    var esc = function ( t ) {
        return t
            .replace( /&/g, '&amp;' )
            .replace( /</g, '&lt;' )
            .replace( />/g, '&gt;' )
            .replace( /"/g, '&quot;' );
    };

    return function ( d, type, row ) {
        // Order, search and type get the original data
        if ( type !== 'display' ) {
            return d;
        }

        if ( typeof d !== 'number' && typeof d !== 'string' ) {
            console.log('wrong type inserted to ellpsis function: ' + typeof d);
            return d;
        }

        d = d.toString(); // cast numbers

        if ( d.length < cutoff ) {
            return d;
        }

        var shortened = d.substr(0, cutoff-1);

        // Find the last white space character in the string
        if ( wordbreak ) {
            shortened = shortened.replace(/\s([^\s]*)$/, '');
        }

        // Protect against uncontrolled HTML input
        if ( escapeHtml ) {
            shortened = esc( shortened );
        }

        return '<span class="ellipsis" title="'+esc(d)+'">'+shortened+'&#8230;</span>';
    };
};

