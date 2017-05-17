$(function () {
 $.ajax({
        type: 'GET',
        encoding: 'UTF-8',
        url: 'rest/project',
        success: function (result) {
            var obj = jQuery.parseJSON(JSON.stringify(result));
            $.each(obj, function (key, value) {
            callPreview(value['username'], value['projectName'], value['id']);

            var date = moment(value['date']).format("DD.MM.YYYY HH:mm");
                $('#myProject tbody').append("<tr><td><div id='preview"+value['id']+"'></div></td><td>"+value['projectName']+"</td><td>" + date+"</td><td> <span onclick='remove("+value['id']+")' class='glyphicon glyphicon glyphicon-remove'></span></td></tr>");
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

function callPreview(username, projectName, id){
    var feedback = $.ajax({
        type: "GET",
        url: "rest/project/call?username="+username+"&projectname="+projectName,
        async: false
    }).done(function(result){
        if(result.length == 0)
             setTimeout(function(){callPreview(username, projectName, id);}, 100);
         else
             $('#preview'+id).html(result);
        });

}