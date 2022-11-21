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


/*
<script th:inline="javascript">
    //let countOfFreeSeats = /!*[[${selectedRoute.numberOfFreeSeats}]]*!/ {};
    $('#plusCount').click(plus);
    $('#minusCount').click(minus);
    let count = $('#countOfTickets');
    count.val(0);

    let numberOfFreeSeats =  /!*[[${countSeats}]]*!/ {};

    function plus() {
    if(count.val() < numberOfFreeSeats){
    count.val(Number($(count).val())+1);
    //count.attr('readonly',false);
    //return;
}
    //$(count).attr('readonly',true);
}

    function minus() {
    if(count.val() > 0){
    count.val(Number($(count).val())-1);
    //count.attr('readonly',false);
    //return;
}
    //$(count).attr('readonly',true);
}
</script>
*/





/*
<script th:inline="javascript">
    let compartmentFreeSeats = /!*[[${selectedRoute.numberOfCompartmentFreeSeats}]]*!/ {};
let compartmentSeatsPrise = /!*[[${selectedRoute.priseOfCompartmentTicket}]]*!/ {};
let suiteFreeSeats = /!*[[${selectedRoute.numberOfSuiteFreeSeats}]]*!/ {};
let suiteSeatsPrise = /!*[[${selectedRoute.priseOfSuiteTicket}]]*!/ {};

$('#plusCount').click(plus);
$('#minusCount').click(minus);

let count = document.querySelector('#countOfTickets');
let res = document.querySelector("#prise");
let place = document.getElementsByName("place");

let ticketPrice = 0;
let numberOfFreeSeats;


count.value = 1;
checkPlace();

count.oninput = () =>{
    console.log("here")
    if(numberOfFreeSeats<count){
        count.val(numberOfFreeSeats);
    }
    res.textContent = Number(count.val())*ticketPrice + " ₴";
};

for (let i = 0; i < place.length; i++) {
    place[i].onclick = checkPlace;
    res.textContent = Number(count.val())*ticketPrice + " ₴";
}

function plus() {
    if(Number(count.val()) < numberOfFreeSeats){
        count.val(Number(count.val())+1);
        console.log(Number(count.val())*ticketPrice);
        res.textContent = Number(count.val())*ticketPrice + " ₴";
    }
}

function minus() {
    if(count.val() > 1){
        count.val(Number(count.val())-1);
        res.textContent = Number(count.val())*ticketPrice + " ₴";
    }
}

function checkPlace(){
    for (var i = 0, length = place.length; i < length; i++) {
        if (place[i].checked) {
            if(place[i].value === "Купе"){
                ticketPrice = Number(compartmentSeatsPrise);
                numberOfFreeSeats = Number(compartmentFreeSeats);
            }else {
                ticketPrice = Number(suiteSeatsPrise);
                numberOfFreeSeats = Number(suiteFreeSeats);
            }
            res.textContent = Number(count.val())*ticketPrice + " ₴";
            break;
        }
    }
}
</script>
*/

