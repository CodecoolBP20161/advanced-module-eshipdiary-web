$(document).ready( function () {

    $('#user-form').submit(function(event) {
        event.preventDefault();
        validateForm()
        })
});


function validateForm(){
    $.ajax({
        type: "POST",
        url: "/user",
        data: $('form').serialize(),
        success: function(response){
            if(response.status == "SUCCESS") {
                $('#create-new-modal').modal('hide');
                location.href = "/users";
            }
            var errorInfo = "";
            errorInfo += "<br>" + response.result;
            $('#error-message').html("Sikertelen regisztráció: " + errorInfo);
        }
    })
}