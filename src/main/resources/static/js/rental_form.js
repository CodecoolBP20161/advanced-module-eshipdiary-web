$(document).ready(function() {
    $("#chosenShip").multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Válassz hajót...'
    });
    $('.multi-select').multiselect({
        enableCaseInsensitiveFiltering: true,
        filterPlaceholder: 'Válassz legénységet...'
    });
});