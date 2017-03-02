function validate() {
    if ($('#inputUsername').val().length < 3) {
        $('#submit').attr("disabled", "true");
    } else {
        $('#submit').removeAttr("disabled");
    }
}

$(document).ready(function(){
    $('#inputUsername').bind("change keyup",function() {
        validate();
    });
});
