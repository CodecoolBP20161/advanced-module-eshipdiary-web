$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$('.modal').on('shown.bs.modal', function() {
    $(this).find('[autofocus]').focus();
});

$.ajaxSetup({
    statusCode: { 403: function(XHR, statusCode, msg) { location.reload(); }}
});