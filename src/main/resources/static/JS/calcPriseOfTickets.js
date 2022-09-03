'use strict';

$('#plusCount').click(plus);
$('#minusCount').click(minus);
//let asdf =  /*[[${selectedTrain}]]*/ {};

console.log(countOfFreeSeats)
function plus() {
    let count = $('#countOfTickets');
    if(count < countOfFreeSeats){
        $(count).val($(count)+1);
        $(count).attr('readonly',false);
        return;
    }
    $(count).attr('readonly',true);
}

function minus() {
    let count = $('#countOfTickets');
    if(count > 0){
        $(count).val($(count)-1);
        $(count).attr('readonly',false);
        return;
    }
    $(count).attr('readonly',true);
}