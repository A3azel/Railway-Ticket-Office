'use strict';

let moneyButton = $('#topUpTheAccount');
$(moneyButton).click(addInputAndButton);
function addInputAndButton(){
    $('.changedMenu').fadeToggle('slow');
}

/*let moneyButton = document.getElementById("topUpTheAccount");
moneyButton.addEventListener('click',addInputAndButton);*/
/*
function addInputAndButton(){
    $(this).toggleClass("active");
    let div = $("<div></div>")
        .addClass("d-grid gap-2 d-md-flex justify-content-md-start")
        .css("padding-top","20px");

    let form2 = $("<form></form>")
        .attr("action", "/topUpAccount")
        .attr("method", "post");

    let input2 = $("<input/>")
        .attr("type", "number")
        .addClass("form-control")
        .css({"border-radius":"20px","height":"40px", "width":"200px"});

    let but2 = $("<button>Поповнити баланс</button>")
        .attr("type","button")
        .addClass("btn btn-outline-success");

    $(div).append(input2,but2);
    $(form2).append(div);
    $('#placeToAdd').append(form2).fadeToggle( "slow", "linear" );

}*/