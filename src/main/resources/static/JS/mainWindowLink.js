(function addPopularDirections(){
    "use strict";
    const clickButton = document.querySelector(".popularDirections");
    clickButton.addEventListener("click",function () {
        const key = event.target.value;
        let coupleOfCities = key.split("-");
        let startCity = document.querySelector("#from");
        startCity.value = coupleOfCities[0];
        let arrivalCity = document.querySelector("#where");
        arrivalCity.value = coupleOfCities[1];
    });
}());