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

function checkPass() {
    var password = document.getElementById('password');
    var confirm = document.getElementById('confirm');

    if (password.value == confirm.value) {
        confirm.setCustomValidity("");
    } else {
        confirm.setCustomValidity("A jelszó nem egyezik");
    }
}

