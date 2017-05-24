$(function () {
 $.ajax({
        type: 'GET',
        encoding: 'UTF-8',
        url: 'rest/project',
        success: function (result) {
            var obj = jQuery.parseJSON(JSON.stringify(result));
            $.each(obj, function (key, value) {
            var date = moment(value['date']).format("DD.MM.YYYY HH:mm");
                $('#myProject tbody').append("<tr><td><div id='preview"+value['id']+"'></div></td><td>"+value['projectName']+"</td><td>" + date+"</td><td> <span onclick='remove("+value['id']+")' class='glyphicon glyphicon glyphicon-remove'></span></td></tr>");
                callPreview(value['user']['username'], value['projectName'], value['id']);
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
        if(result == null)
             setTimeout(function(){callPreview(username, projectName, id);}, 1000);
         else{
             $('#preview'+id).html('<img height="70px" width="80px" src="data:image/png;base64,' + result + '" />');
         }

        });

}