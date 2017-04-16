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
});

function rentalActionButtons( data, type, row ) {
    var details = ' <a class="btn btn-info btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalDetailsModal(\'/rentals/details/'+row.id+'\');">Részletek</a>';
    var final = row.rentalEnd == "" ? ' <a class="btn btn-success btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalFinalModal(\'/rentals/final/'+row.id+'\');">Véglegesítés</a>' : '';
    var isAdmin = document.getElementById("role").value === 'ADMIN';
    var comment = isAdmin ? ' <a class="btn btn-warning btn-xs" data-toggle="modal" data-target="#rentalModal" role="button" onclick="rentalCommentModal(\'/rentals/comment/'+row.id+'\');">Megjegyzés</a>' : '';
    var injury = row.injury ? ' <button class="btn btn-danger btn-xs">!</button>' : '';
    return details + final + comment + injury;
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
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            document.getElementById('rentalSubmit').style.display = "inline";
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
        type: "OPTIONS",
        success: function (result) {
            document.getElementById('rentalUpdate').innerHTML = result;
            $('#injuredOars').val('').multiselect({
                nonSelectedText: ' ',
                buttonWidth: '100%'
            });
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
    });
}

function rentalCommentModal(link) {
    $.ajax({
        url: link,
        type: "OPTIONS",
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
    });
    $('#cox').on('change', function () {
        document.getElementById('crew').setCustomValidity(validateCaptainPresence());
    });
    $('#rentalPeriod').on('input', function () {
        this.setCustomValidity(validateRentalPeriod(this.value));
    });
    $('#distance').on('input', function () {
        this.setCustomValidity(validateDistance(this.value));
    });

    $('#oars').on('change', function (option) {
        setMaxNumOfOars(option, $('#crew option:selected').length);
    });

}

function setMaxNumOfOars(option, crewNumber) {
    var selectedOptions = $('#oars option:selected');

    if (selectedOptions.length >= crewNumber) {
        var nonSelectedOptions = $('#oars option').filter(function () {
            return !$(this).is(':selected');
        });

        nonSelectedOptions.each(function () {
            var input = $('input[value="' + $(this).val() + '"]');
            input.prop('disabled', true);
            input.parent('li').addClass('disabled');
        });
    } else {
        $('#oars option').each(function () {
            var input = $('input[value="' + $(this).val() + '"]');
            input.prop('disabled', false);
            input.parent('li').addClass('disabled');
        });
    }
}

function validateDistance(distance) {
   if(distance < 1) {
        return "A megadott távolság nem lehet kevesebb, mint 1 km";
    } else {
       return "";
   }
}

function validateCaptainPresence() {
    var crew = $('#crew').val();
    crew.push($('#cox').val());
    if($('#role').val()!=='ADMIN' && !crew.includes($('#captain').val())) {
        return "Bejelentkezett felhasználó nincs a legénységben"
    } else {
        return ""
    }
}

function validateRentalPeriod(rentalPeriod) {
    if(rentalPeriod > minutesUntilMidnight()) {
        return "A bérlési idő maximum a nap végéig tart"
    } else if (rentalPeriod < 15) {
        return "A bérlési idő minimum 15 perc"
    } else if (rentalPeriod % 15 != 0) {
        return "Negyedórás időközt használj"
    } else {
        return ""
    }
}

function minutesUntilMidnight() {
    var now = new Date();
    var midnight = new Date();
    midnight.setHours(24, 0, 0, 0);
    return (midnight.getTime() - now.getTime())/ 1000 / 60;
}

function getSubTypesByType(id) {
    $.ajax({
        type: "GET",
        url: "/subtypesbytype",
        data: {"typeId": id},
        dataType: 'json',
        success: function(data) {
            var subTypes = $('#shipsBySubType')
            subTypes.build(data);
            $.isEmptyObject(data) ? $('#shipByName').build({}) : getShipsBySubType(subTypes.val());

        }
    });
}

function getShipsBySubType(id) {
    $.ajax({
        type: "GET",
        url: "/shipsbysubtype",
        data: {"subTypeId": id},
        dataType: 'json',
        success: function(data) {
            $('#shipByName').build(data);
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
};

function selectSubTypesByType() {
    $('#subTypesByType').val(null).multiselect({
        buttonWidth: '100%',
        nonSelectedText: 'Típus',
        onChange: function (option, checked, select) {
            getSubTypesByType(option.val());
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
        onChange: function (option, checked, select) {
            displayCox(option.val());
        }
    })
}

function displayCox(id) {
    if(id === null){
        $('#coxSelect').hide();
    } else {
        $.ajax({
            type: "GET",
            url: "/isShipCoxed",
            data: {"shipId": id},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    $('#coxSelect').show();
                } else {
                    $('#coxSelect').hide();
                }
            }
        });
    }
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
            console.log("wrong type inserted to ellpsis function: " + typeof d);
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

