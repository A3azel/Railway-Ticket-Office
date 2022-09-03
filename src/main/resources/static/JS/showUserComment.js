(function showComment(){
    "use strict";
    const userComment =  /*[[${comment}]]*/ {};
    if(userComment != null){
        $('#changeComment').text(userComment.userComments);
    }
}());