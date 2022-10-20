'use strict';

let moneyButton = $('#topUpTheAccount');
$(moneyButton).click(addInputAndButton);
function addInputAndButton(){
    $('.changedMenu').fadeToggle('slow');
}
