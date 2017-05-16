$(function () {
 $.ajax({
        type: 'GET',
        encoding: 'UTF-8',
        url: 'rest/project/full',
        success: function (result) {
            var obj = jQuery.parseJSON(JSON.stringify(result));
            $.each(obj, function (key, value) {
            var date = moment(value[2]).format("DD.MM.YYYY HH:mm");
                $('#myProject tbody').append("<tr><td>"+value[0]+"</td><td>"+value[3]+"</td><td>"+value[1]+"</td><td>" + date+"</td></tr>");
            });
        }
    });

$.ajax({
        type: 'GET',
        encoding: 'UTF-8',
        url: 'rest/user/full',
        success: function (result) {
            var obj = jQuery.parseJSON(JSON.stringify(result));
            $.each(obj, function (key, value) {
                $('#users tbody').append("<tr><td>"+value['id']+"</td><td>"+value['username']+"</td><td>"+value['password']+"</td></tr>");
            });
        }
    });


    $('#addUserBtn').click(function (){
    var user = {
    username: $('#username').val(),
    password: $('#password').val(),
    role: $('input[name="role"]:checked').val()
    };

    $.ajax({
            type: 'POST',
            encoding: 'UTF-8',
            contentType: "application/json",
            dataType: "json",
            url: 'rest/user',
            data: JSON.stringify(user),
            success: function (result) {
                var obj = jQuery.parseJSON(JSON.stringify(result));
                $('#users tbody').append("<tr><td>"+obj['id']+"</td><td>"+obj['username']+"</td><td>"+obj['password']+"</td></tr>");

            }
        });
    });
});