(function ready(){
    "use strict";
    const firstLink = document.querySelector("#Київ-Львів");
    firstLink.addEventListener("click",function () {
        let startCity = document.querySelector("#from");
        startCity.value = "Київ";
        let arrivalCity = document.querySelector("#where");
        arrivalCity.value = "Львів";
    });
}());