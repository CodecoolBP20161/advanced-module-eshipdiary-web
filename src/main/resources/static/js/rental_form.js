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
    $('.multi-select').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Keresés...'
    });
};

