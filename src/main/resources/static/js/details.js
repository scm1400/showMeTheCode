$(document).ready(function() {
    let id = getParameterByName('id');
    console.log(id)
    $.ajax({
        type: "GET",
        url: "/details",
        data: {
            id: id,
        },
        contentType: "application/json;charset-utf-8;",
        success: function (res) {
            console.log(res);
        }
    })
})

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
