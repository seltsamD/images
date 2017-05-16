$(function () {
 $.ajax({
        type: 'GET',
        encoding: 'UTF-8',
        url: 'rest/project',
        success: function (result) {
            var obj = jQuery.parseJSON(JSON.stringify(result));
            $.each(obj, function (key, value) {
            var date = moment(value['date']).format("DD.MM.YYYY HH:mm");
                $('#myProject tbody').append("<tr><td>"+value['projectName']+"</td><td>" + date+"</td><td> <span onclick='remove("+value['id']+")' class='glyphicon glyphicon glyphicon-remove'></span></td></tr>");
            });
        }
    });


});
function remove(id){
 $.ajax({
    type: 'DELETE',
    encoding: 'UTF-8',
    url: 'rest/project?id='+id,
    success: function (result) {
    location.reload();
    }
 });
}