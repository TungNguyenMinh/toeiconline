$(document).ready(function () {
    bindEventCheckAllCheckBox('checkAll');
    enableOrDisableDeleteAll();
});
function bindEventCheckAllCheckBox(id) {
    $('#' + id).on('change', function () {
        if ((this).checked) {
            //enable all checkbox
            $(this).closest('table').find('input[type=checkbox]').prop('checked', true);
        } else {
            //disable all checkbox
            $(this).closest('table').find('input[type=checkbox]').prop('checked', false);
        }
    });
}
function enableOrDisableDeleteAll() {
    $('input[type=checkbox]').click(function () {
        if ($(this).attr('id') == 'checkAll' && $(this).prop('checked') == false) {
            $(this).closest('table').find('input[type=checkbox]').prop('checked', false);
        }
        if ($('input[type=checkbox]:checked').length > 0) {
            $('#deleteAll').prop('disabled', false);
        } else {
            $('#deleteAll').prop('disabled', true);
        }
    });
}

