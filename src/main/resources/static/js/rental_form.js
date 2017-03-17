$(document).ready(function() {
    simpleSelectWithFilter();
    multipleSelect();
});


var simpleSelectWithFilter = function () {
    $("#chosenShip").multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...',
        buttonWidth: '368px'
    })
};

var multipleSelect = function () {
    var crewList = [];
    $('#crew').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...',
        buttonText: function(options, select) {
            return 'Válassz legénységet!';
        },
        buttonWidth: '368px',
        onChange: function(element, checked) {
            if (checked === true) {
                crewList.push(element.html());
                document.getElementById("crewDisplay").innerHTML = displayCrew(crewList);
            }
        }
    });
    $('#oars').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...',
        buttonText: function (options, select) {
            return 'Válassz lapátokat!';
        },
        buttonWidth: '368px'
    });
};

var displayCrew = function (crewList) {
    var text = "<ul>";
    for (var i = 0; i < crewList.length; i++) {
        text += "<li>" + crewList[i] + "</li>";
    }
    return text;
};
