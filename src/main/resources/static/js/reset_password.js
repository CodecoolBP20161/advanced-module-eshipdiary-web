function resetPass(){
    var email = $("#email").val();
    $.ajax({
        type: "POST",
        url: "password-reset",
        data: {"email": email},
        dataType: 'json',
        success: function(data) {}
    });
}


